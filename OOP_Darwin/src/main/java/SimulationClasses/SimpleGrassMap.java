package SimulationClasses;

import Enums.MapType;
import BaseClasses.Boundary;
import BaseClasses.Vector2d;
import Interfaces.MapChangeListener;
import Interfaces.WorldElement;
import Interfaces.WorldMap;
import java.util.*;

public class SimpleGrassMap extends AbstractMapClass {
    public SimpleGrassMap(SimulationParameters parameters) {
        super(new Boundary(new Vector2d(0, 0), new Vector2d(parameters.mapWidth(), parameters.mapHeight())));
        this.plantsManager = parameters.mapVariant() == MapType.CreepingJungle
                ? new CreepingJunglePlantsManager()
                : new BasicPlantManager();
    }

    public SimpleGrassMap() {
        super();
        this.plantsManager = new CreepingJunglePlantsManager();
    }

    public SimpleGrassMap(Boundary boundary) {
        super(boundary);
        this.plantsManager = new CreepingJunglePlantsManager();
    }

    public SimpleGrassMap(Boundary boundary, List<Animal> newAnimals) {
        super(boundary, newAnimals);
        this.plantsManager = new CreepingJunglePlantsManager();
    }

    @Override
    public void move(Animal animal) {
        animal.moveNext(currentBounds);
    }

    @Override
    public void moveAll() {
        animals.values().forEach(animalList -> animalList.forEach(animal -> animal.moveNext(currentBounds)));
    }
}
