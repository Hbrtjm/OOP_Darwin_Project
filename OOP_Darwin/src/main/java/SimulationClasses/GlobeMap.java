
package SimulationClasses;

import BaseClasses.Boundary;
import BaseClasses.Vector2d;
import Enums.MapDirection;
import Interfaces.MapChangeListener;
import Interfaces.WorldElement;
import Interfaces.WorldMap;

import java.util.*;

public class GlobeMap implements WorldMap  {
    private Boundary currentBounds;
    private final Map<Vector2d, Plant> plants;
    private final Map<Vector2d,List<Animal>> animals;
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
    public void move(Animal animal, MapDirection direction) {
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
    private void orderAnimals(List<Animal> animals)
    {
        // Practically a radix sort - start from the least significant property
        // The least significant is the kids count
        animals.sort(Comparator.comparingInt(Animal::getChildrenCount));
        // The second factor is the age
        animals.sort(Comparator.comparingInt(Animal::getAge));
        // The primary factor is energy level
        animals.sort(Comparator.comparingInt(Animal::getEnergyLevel));
    }
    public void feedAll()
    {
        for(Vector2d key : animals.keySet())
        {
            List<Animal> animalPlace = animals.get(key);
            if (animalPlace != null && !animalPlace.isEmpty()) {
                orderAnimals(animalPlace);
                // Feed the first animal in the sorted list
                animalPlace.getFirst().eat(plants.get(key));
                plants.remove(key);
            }
        }
    }
    public void mateAll()
    {
        for(Vector2d key : animals.keySet())
        {
            List<Animal> animalPlace = animals.get(key);
            List<Animal> kids = new ArrayList<>();
            if (animalPlace != null && !animalPlace.isEmpty()) {
                orderAnimals(animalPlace);
                for (int i = 0; i < animalPlace.size(); i += 2) {
                    Animal newKid = animalPlace.get(i).mate(animalPlace.get(i + 1));
                    kids.add(newKid);
                }
                animals.remove(key);
                animalPlace.addAll(kids);
                animals.putIfAbsent(key,animalPlace);
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
        // TODO and to be discussed
        return true;
    }
    public void mapChanged(String change)
    {
        for(MapChangeListener listener : listeners)
        {
            listener.update(change);
        }
    }
}
