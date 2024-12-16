package SimulationClasses;

import BaseClasses.Boundary;
import BaseClasses.Vector2d;
import Enums.MapDirection;
import Interfaces.MapChangeListener;
import Interfaces.WorldElement;
import Interfaces.WorldMap;

import java.util.*;

public class SimpleGrassMap implements WorldMap {
    private Boundary currentBounds;
    private Map<Vector2d, Plant> plants;
    private Map<Vector2d,List<Animal>> animals;
    List<MapChangeListener> listeners;
    private int daysCount;
    public SimpleGrassMap()
    {
        this.animals = animals;
        daysCount = 0;
    }
    public SimpleGrassMap(Boundary boundary)
    {
        daysCount = 0;
        currentBounds = boundary;
    }
    public SimpleGrassMap(Boundary boundary,List<Animal> newAnimals)
    {
        addAnimals(newAnimals);
        daysCount = 0;
        currentBounds = boundary;
    }
    private void addAnimals(List<Animal> newAnimals)
    {
        for(Animal animal : newAnimals)
        {
            if(animals.get(animal.getPosition()) == null)
            {
                List<Animal> newAnimalList = new ArrayList<>();
                newAnimalList.add(animal);
                animals.putIfAbsent(animal.getPosition(),newAnimalList);
                continue;
            }
            List<Animal> currentPositionAnimals = animals.get(animal.getPosition());
            currentPositionAnimals.add(animal);
            animals.remove(animal.getPosition());
            animals.putIfAbsent(animal.getPosition(),currentPositionAnimals);
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
    public void setCurrentBounds(Boundary boundary)
    {
        currentBounds = boundary;
    }
    @Override
    public void place(Animal animal) {
        // Probably not necessary
        List<Animal> animalsAtPosition = animals.get(animal.getPosition());
        animals.remove(animal.getPosition());
        animalsAtPosition.add(animal);
        animals.putIfAbsent(animal.getPosition(),animalsAtPosition);
    }

    public void placePlant(Plant plant, Vector2d position)
    {
        plants.putIfAbsent(position,plant);
    }

    @Override
    public void move(Animal animal, MapDirection direction) {
        animal.moveNext(currentBounds);
    }

    public void deleteCorpses()
    {
        for(Vector2d key: animals.keySet())
        {
            for(Animal animal : animals.get(key)) {
                if (animal.getEnergyLevel() == 0) {
                    animal = null; // The garbage collector will handle this
                }
            }
        }
    }
    public void moveAll()
    {
        for(Vector2d key: animals.keySet())
        {
            for(Animal animal : animals.get(key))
            {
                animal.moveNext(currentBounds);
            }
        }
    }
    public void feedAll()
    {
        for(Vector2d key : animals.keySet())
        {
            List<Animal> animalPlace = animals.get(key);
            if (animalPlace != null && !animalPlace.isEmpty()) {
                animalPlace.sort(Comparator.comparingDouble(Animal::getEnergyLevel));

                // Feed the first animal in the sorted list
                animalPlace.getFirst().eat(plants.get(key));
            }
        }
    }
    public void mateAll()
    {
        for(Vector2d key : animals.keySet())
        {
            List<Animal> animalPlace = animals.get(key);
            if (animalPlace != null && !animalPlace.isEmpty()) {
                animalPlace.sort(Comparator.comparingDouble(Animal::getEnergyLevel));

                for (int i = 0; i < animalPlace.size(); i += 2) {
                    animalPlace.get(i).mate(animalPlace.get(i + 1));
                }
            }
        }
    }
    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.get(position) != null && animals.get(position) instanceof Animal;
    }
    @Override
    public List<Animal> getAnimalsAtPosition(Vector2d position)
    {
        return animals.get(position);
    }
    @Override
    public Collection<WorldElement> objectsAt()
    {
        List<WorldElement> combined = new ArrayList<>();
        combined.addAll((Collection<? extends WorldElement>)plants.values());

        combined.addAll((Collection<? extends WorldElement>)animals.values());

        return combined;
    }
    @Override
    public void initialState()
    {
        mapChanged("Initial state");
    }
    @Override
    public void frame()
    {
        deleteCorpses();
        moveAll();
        feedAll();
        mateAll();

        daysCount++;
        mapChanged("Day " + daysCount);
    }
    @Override
    public boolean canMoveTo(Vector2d position)
    {
        // To do and to be discussed
        return false;
    }
    public void mapChanged(String change)
    {
        for(MapChangeListener listener : listeners)
        {
            listener.update(change);
        }
    }
}
