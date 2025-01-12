
package SimulationClasses;

import BaseClasses.Boundary;
import BaseClasses.Vector2d;
import Interfaces.MapChangeListener;

import java.util.*;

public class GlobeMap extends GrassMap {
    List<MapChangeListener> listeners;
    private int daysCount;
    public GlobeMap()
    {
        super();
        animals = new HashMap<>();
        daysCount = 0;
    }
    public GlobeMap(Boundary boundary)
    {
        super();
        animals = new HashMap<>();
        daysCount = 0;
        currentBounds = boundary;
    }
    public GlobeMap(Boundary boundary,List<Animal> newAnimals)
    {
        super();
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
                ArrayList<Animal> newAnimalList = new ArrayList<>();
                newAnimalList.add(animal);
                animals.putIfAbsent(animal.getPosition(),newAnimalList);
                continue;
            }
            ArrayList<Animal> currentPositionAnimals = animals.get(animal.getPosition());
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

    public void setCurrentBounds(Boundary boundary)
    {
        currentBounds = boundary;
    }

    @Override
    public void move(Animal animal) {
        animal.moveNextOnGlobeMap(currentBounds);
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
