package SimulationClasses;

import Enums.MapType;
import BaseClasses.Boundary;
import BaseClasses.Vector2d;

import java.util.*;

public class GrassMap extends AbstractMap {
    public GrassMap()
    {
        super();
    }
    public GrassMap(SimulationParameters parameters) {
        super(new Boundary(new Vector2d(0, 0), new Vector2d(parameters.mapWidth(), parameters.mapHeight())));
        this.plantsManager = parameters.mapVariant() == MapType.CreepingJungle
                ? new CreepingJunglePlantsManager(currentBounds)
                : new BasicPlantManager(currentBounds);
    }

    // ============== For test only ==============
    public GrassMap(Boundary boundary) {
        super(boundary);
        this.plantsManager = new CreepingJunglePlantsManager(currentBounds);
    }

    public GrassMap(Boundary boundary, List<Animal> newAnimals) {
        super(boundary, newAnimals);
        this.plantsManager = new CreepingJunglePlantsManager(currentBounds);
    }
    // ===========================================
}
