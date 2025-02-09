
package SimulationClasses;

import BaseClasses.Boundary;
import BaseClasses.Vector2d;
import Interfaces.MapChangeListener;

import java.util.*;

public class GlobeMap extends GrassMap {
    List<MapChangeListener> listeners;
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
    public void move(Animal animal) {
        removeAnimal(animal);
        animal.moveNextOnGlobeMap(currentBounds);
        placeAnimal(animal);
    }

//TODO - przemieszczanie się za granicę

    @Override
    public void moveAll()
    {
        Map<Vector2d, List<Animal>> animalsCopy = new HashMap<>();
        animals.forEach((key, value) -> animalsCopy.put(key, new ArrayList<>(value)));

        animalsCopy.values().forEach(animalList -> animalList.forEach(animal -> move(animal)));
    }
}
