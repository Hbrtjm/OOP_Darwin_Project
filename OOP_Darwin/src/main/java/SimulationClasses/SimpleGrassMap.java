package SimulationClasses;

import BaseClasses.AnimalComparator;
import BaseClasses.Boundary;
import BaseClasses.Vector2d;
import Enums.MapDirection;
import Interfaces.MapChangeListener;
import Interfaces.WorldElement;
import Interfaces.WorldMap;

import java.util.*;

public class SimpleGrassMap implements WorldMap {
    private Boundary currentBounds;
    private Map<Vector2d,Food> plants;
    private Map<Vector2d,List<Animal>> animals; // The above will be deprecated
    List<MapChangeListener> listeners;
    private int daysCount;
    @Override
    public int getID() {
        return 0;
    }

    @Override
    public Boundary getCurrentBounds() {
        return currentBounds;
    }

    @Override
    public void place(Animal animal) {
        // Probably not necessary
        List<Animal> animalsAtPosition = animals.get(animal.getPosition());
        animals.remove(animal.getPosition());
        animalsAtPosition.add(animal);
        animals.putIfAbsent(animal.getPosition(),animalsAtPosition);
    }

    public void placePlant(Food plant,Vector2d position)
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
            for(Animal animal : animals.get(key)) {
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
        mapChanged("Day " + daysCount);
        daysCount++;
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
