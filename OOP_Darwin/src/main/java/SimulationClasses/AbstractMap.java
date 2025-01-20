package SimulationClasses;

import BaseClasses.Boundary;
import BaseClasses.Vector2d;
import Interfaces.MapChangeListener;
import Interfaces.PlantsManager;
import Interfaces.WorldElement;
import Interfaces.WorldMap;

import java.io.Console;
import java.util.*;

abstract public class AbstractMap implements WorldMap {
    protected Boundary currentBounds;
    protected PlantsManager plantsManager;
    protected Map<Vector2d, ArrayList<Animal>> animals;
    protected int daysCount;
    protected final List<MapChangeListener> subscribers;

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
    public AbstractMap() {
        currentBounds = new Boundary(new Vector2d(0,0), new Vector2d(10,10));
        plantsManager = new CreepingJunglePlantsManager(currentBounds);
        this.animals = new HashMap<>();
        this.daysCount = 0;
        this.subscribers = new ArrayList<>();
    }

    public AbstractMap(Boundary boundary) {
        this();
        this.currentBounds = boundary;
    }

    public AbstractMap(Boundary boundary, List<Animal> newAnimals) {
        this(boundary);
        addAnimals(newAnimals);
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

    public void placeAnimal(Animal animal)
    {
        if(animal != null)
        {
            if(!animals.containsKey(animal.getPosition()))
            {
                animals.put(animal.getPosition(), new ArrayList<>());
            }
            animals.get(animal.getPosition()).add(animal);
//            System.out.println("Add");
        }
    }

    protected void removeAnimal(Animal animal)
    {
        if(animal != null)
        {
            animals.get(animal.getPosition()).remove(animal);
            if(animals.get(animal.getPosition()).isEmpty())
            {
                animals.remove(animal.getPosition());
            }
//            System.out.println("Remove");
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

