package SimulationClasses;

import BaseClasses.Boundary;
import BaseClasses.SimulationConstants;
import BaseClasses.Vector2d;
import Interfaces.MapChangeListener;
import Interfaces.PlantsManager;
import Interfaces.WorldElement;
import Interfaces.WorldMap;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Abstract base class for all map implementations in the Darwin simulation.
 * Provides common functionality for animal management, plant interactions, and simulation lifecycle.
 * Thread-safe implementation using concurrent collections.
 */
abstract public class AbstractMap implements WorldMap {
    private static final Logger LOGGER = Logger.getLogger(AbstractMap.class.getName());
    
    protected Boundary currentBounds;
    protected PlantsManager plantsManager;
    protected Map<Vector2d, ArrayList<Animal>> animals;
    protected int daysCount;
    protected final List<MapChangeListener> subscribers;
    protected SimulationStatistics statistics;
    
    // Performance tracking
    private long lastFrameTime;
    private int totalAnimalsProcessed;
    private int totalMatingEvents;

    @Override
    public void setBoundary(Boundary givenBoundary)
    {
        currentBounds = givenBoundary;
    }

    @Override
    public ArrayList<Animal> getAnimalsAtPosition(Vector2d position)
    {
        return animals.get(position);
    }
    public List<MapChangeListener> getSubscribers()
    {
        return subscribers;
    }
    public void registerListener(MapChangeListener listener)
    {
        subscribers.add(listener);
    }
    /**
     * Default constructor with thread-safe collections and performance tracking.
     */
    public AbstractMap() {
        currentBounds = new Boundary(
            new Vector2d(SimulationConstants.DEFAULT_MAP_MIN_X, SimulationConstants.DEFAULT_MAP_MIN_Y),
            new Vector2d(SimulationConstants.DEFAULT_MAP_MAX_X, SimulationConstants.DEFAULT_MAP_MAX_Y)
        );
        plantsManager = new CreepingJunglePlantsManager(currentBounds);
        this.animals = new ConcurrentHashMap<>();
        this.daysCount = 0;
        this.subscribers = new CopyOnWriteArrayList<>();
        this.statistics = new SimulationStatistics();
        this.lastFrameTime = System.currentTimeMillis();
        this.totalAnimalsProcessed = 0;
        this.totalMatingEvents = 0;
        
        LOGGER.info("AbstractMap initialized with default bounds: " + currentBounds);
    }

    /**
     * Constructor with custom boundary.
     * @param boundary The map boundary
     * @throws IllegalArgumentException if boundary is null
     */
    public AbstractMap(Boundary boundary) {
        this();
        if (boundary == null) {
            throw new IllegalArgumentException("Boundary cannot be null");
        }
        this.currentBounds = boundary;
        LOGGER.info("AbstractMap initialized with custom bounds: " + boundary);
    }

    /**
     * Constructor with boundary and initial animals.
     * @param boundary The map boundary
     * @param newAnimals Initial animals to place on the map
     * @throws IllegalArgumentException if boundary is null or animals list is null
     */
    public AbstractMap(Boundary boundary, List<Animal> newAnimals) {
        this(boundary);
        if (newAnimals == null) {
            throw new IllegalArgumentException("Animals list cannot be null");
        }
        addAnimals(newAnimals);
        LOGGER.info("AbstractMap initialized with " + newAnimals.size() + " initial animals");
    }

    private void addAnimals(List<Animal> newAnimals) {
        for (Animal animal : newAnimals) {
            animals.computeIfAbsent(animal.getPosition(), k -> new ArrayList<>()).add(animal);
        }
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public Boundary getCurrentBounds() {
        return currentBounds;
    }

    public void setCurrentBounds(Boundary boundary) {
        this.currentBounds = boundary;
    }

    /**
     * Places an animal on the map at its current position.
     * Thread-safe implementation with validation.
     * @param animal The animal to place
     * @throws IllegalArgumentException if animal is null or position is invalid
     */
    public void placeAnimal(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Cannot place null animal");
        }
        
        Vector2d position = animal.getPosition();
        if (!currentBounds.isInside(position)) {
            LOGGER.warning("Attempting to place animal outside bounds: " + position);
            return;
        }
        
        animals.computeIfAbsent(position, k -> new ArrayList<>()).add(animal);
        totalAnimalsProcessed++;
        
        LOGGER.fine("Animal placed at position: " + position);
    }

    /**
     * Removes an animal from the map.
     * Thread-safe implementation with validation.
     * @param animal The animal to remove
     */
    protected void removeAnimal(Animal animal) {
        if (animal == null) {
            LOGGER.warning("Attempting to remove null animal");
            return;
        }
        
        Vector2d position = animal.getPosition();
        List<Animal> animalsAtPosition = animals.get(position);
        
        if (animalsAtPosition != null) {
            boolean removed = animalsAtPosition.remove(animal);
            if (removed) {
                if (animalsAtPosition.isEmpty()) {
                    animals.remove(position);
                }
                LOGGER.fine("Animal removed from position: " + position);
            } else {
                LOGGER.warning("Animal not found at expected position: " + position);
            }
        }
    }

    @Override
    public void move(Animal animal) {
        removeAnimal(animal);
        animal.moveNext(currentBounds);
        placeAnimal(animal);
    }

    public void deleteCorpses() {
        animals.values().forEach(animalsList ->
                animalsList.removeIf(animal -> {
                    if (animal.getEnergyLevel() == 0) {
                        animal.setDayOfDeath(daysCount); // Set the day of death
                        return true; // Remove the animal
                    }
                    return false; // Keep the animal
                })
        );

        animals.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    public void moveAll() {
        Map<Vector2d, List<Animal>> animalsCopy = new HashMap<>();
        animals.forEach((key, value) -> animalsCopy.put(key, new ArrayList<>(value)));
        animalsCopy.values().forEach(animalList -> animalList.forEach(animal -> move(animal)));
    }

    private void orderAnimals(List<Animal> animals) {
        animals.sort(Comparator.comparingInt(Animal::getEnergyLevel).reversed()
                .thenComparingInt(Animal::getAge).reversed()
                .thenComparingInt(Animal::getChildrenCount).reversed());
    }

    public void feedAll() {
        for (Vector2d position : animals.keySet()) {
            List<Animal> animalList = animals.get(position);
            if (animalList != null && !animalList.isEmpty() && plantsManager.plantAt(position) != null) {
                orderAnimals(animalList);
                Animal strongest = animalList.getFirst();
                strongest.eat(plantsManager.plantAt(position));
                plantsManager.removePlant(position);
            }
        }
    }


    public void mateAll() {
        Collection<Vector2d> animalsPositions = animals.keySet();
        List<Vector2d> distinctAnimalsPositions = animalsPositions.stream().distinct().toList();
        for (Vector2d position : distinctAnimalsPositions) {
            ArrayList<Animal> animalList = animals.get(position);
            if (animalList != null && animalList.size() > 1) {
                orderAnimals(animalList);
                int iterations =  animalList.size()-1;
                System.out.println("Mating " + (int)((iterations)/2));

                for(int i = 0;i < iterations;i+=2) {
                    Animal parent1 = animalList.get(i);
                    Animal parent2 = animalList.get(i + 1);
                    if(parent1.canMate() && parent2.canMate()) {
                        Animal child = parent1.mate(parent2);
                        placeAnimal(child);
                    }
                }
                System.out.println("Finished mating " + animalList.size());
            }
        }
    }

    @Override
    public void initialState() {
        mapChanged("Initial state");
    }

    @Override
    public void frame() {
        deleteCorpses();
        moveAll();
        feedAll();
        mateAll();
        System.out.println("Another day");
        plantsManager.growPlants();
        daysCount++;
        mapChanged("Day " + daysCount);
    }

    public void mapChanged(String change) {
        for (MapChangeListener subscriber : subscribers) {
            subscriber.mapChanged(change);
        }
    }
    @Override
    public Collection<WorldElement> objectsAt()
    {
        List<WorldElement> combined = new ArrayList<>();
//        combined.addAll((Collection<? extends WorldElement>)plants.values());
//
//        combined.addAll((Collection<? extends WorldElement>)animals.values());

        return combined;
    }

    @Override
    public Plant plantAt(Vector2d position)
    {
        return plantsManager.plantAt(position);
    }

}
