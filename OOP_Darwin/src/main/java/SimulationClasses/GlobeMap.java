
package SimulationClasses;

import BaseClasses.Boundary;
import BaseClasses.Vector2d;
import Enums.MapDirection;
import Interfaces.MapChangeListener;
import Interfaces.WorldElement;
import Interfaces.WorldMap;

import java.util.*;

public class GlobeMap extends SimpleGrassMap  {
    List<MapChangeListener> listeners;
    private int daysCount;
    public GlobeMap()
    {
        plants = new HashMap<>();
        animals = new HashMap<>();
        daysCount = 0;
    }
    public GlobeMap(Boundary boundary)
    {
        plants = new HashMap<>();
        animals = new HashMap<>();
        daysCount = 0;
        currentBounds = boundary;
    }
    public GlobeMap(Boundary boundary,List<Animal> newAnimals)
    {
        plants = new HashMap<>();
        animals = new HashMap<>();
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
    public GlobeMap(SimulationParameters parameters) {
        super(parameters);
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
    public void move(Animal animal) {
        animal.moveNextOnGlobeMap(currentBounds);
    }
    public void deleteCorpses()
    {
        for(Vector2d key: animals.keySet())
        {
            List<Animal> animalsList = animals.get(key);
            for(Animal animal : animalsList) {
                if (animal.getEnergyLevel() == 0) {
                    animals.remove(key);
                    animalsList.remove(animal); // The garbage collector will handle this
                    animals.putIfAbsent(key,animalsList);
                }
            }
        }
    }
    @Override
    public void moveAll()
    {
        for(Vector2d key: animals.keySet())
        {
            for(Animal animal : animals.get(key))
            {
                animal.moveNextOnGlobeMap(currentBounds);
            }
        }
    }
}
