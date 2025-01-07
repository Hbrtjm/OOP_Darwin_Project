package SimulationClasses;

import BaseClasses.Boundary;
import BaseClasses.Vector2d;
import Interfaces.MapChangeListener;
import Interfaces.PlantsManager;
import Interfaces.WorldElement;
import Interfaces.WorldMap;
import java.util.*;

abstract public class AbstractMapClass implements WorldMap {
    protected Boundary currentBounds;
    protected PlantsManager plantsManager;
    protected Map<Vector2d, List<Animal>> animals;
    protected Map<Vector2d, Plant> plants;
    protected List<MapChangeListener> listeners;
    private int daysCount;

    @Override
    public List<Animal> getAnimalsAtPosition(Vector2d position)
    {
        return animals.get(position);
    }

    public AbstractMapClass() {
        this.plants = new HashMap<>();
        this.animals = new HashMap<>();
        this.listeners = new ArrayList<>();
        this.daysCount = 0;
    }

    public AbstractMapClass(Boundary boundary) {
        this();
        this.currentBounds = boundary;
    }

    public AbstractMapClass(Boundary boundary, List<Animal> newAnimals) {
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

    @Override
    public void place(Animal animal) {
        animals.computeIfAbsent(animal.getPosition(), k -> new ArrayList<>()).add(animal);
    }

    public void placePlant(Plant plant, Vector2d position) {
        plants.putIfAbsent(position, plant);
    }

    @Override
    public void move(Animal animal) {
        animal.moveNext(currentBounds);
    }

    public void deleteCorpses() {
        animals.values().forEach(animalsList -> animalsList.removeIf(animal -> animal.getEnergyLevel() == 0));
        animals.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    public void moveAll() {
        animals.values().forEach(animalList -> animalList.forEach(animal -> animal.moveNext(currentBounds)));
    }

    private void orderAnimals(List<Animal> animals) {
        animals.sort(Comparator.comparingInt(Animal::getEnergyLevel).reversed()
                .thenComparingInt(Animal::getAge).reversed()
                .thenComparingInt(Animal::getChildrenCount).reversed());
    }

    public void feedAll() {
        for (Vector2d position : animals.keySet()) {
            List<Animal> animalList = animals.get(position);
            if (animalList != null && !animalList.isEmpty()) {
                orderAnimals(animalList);
                Animal strongest = animalList.get(0);
                strongest.eat(plants.get(position));
                plants.remove(position);
            }
        }
    }

    public void mateAll() {
        for (Vector2d position : animals.keySet()) {
            List<Animal> animalList = animals.get(position);
            if (animalList != null && animalList.size() > 1) {
                orderAnimals(animalList);
                Animal parent1 = animalList.get(0);
                Animal parent2 = animalList.get(1);
                Animal child = parent1.mate(parent2);
                animals.computeIfAbsent(position, k -> new ArrayList<>()).add(child);
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
        daysCount++;
        mapChanged("Day " + daysCount);
    }

    public void mapChanged(String change) {
        for (MapChangeListener listener : listeners) {
            listener.update(change);
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
}

