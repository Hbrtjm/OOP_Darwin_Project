package SimulationClasses;

import BaseClasses.ListRandomizer;
import Enums.MapType;
import BaseClasses.Boundary;
import BaseClasses.Vector2d;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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
        this.plantsManager.setMaxAddPlant(parameters.dailyPlantGrowth());
        ArrayList<Vector2d> positions = new ArrayList<>();
        for (int i = currentBounds.lower().getX(); i < currentBounds.upper().getX(); i++) {
            for (int j = currentBounds.lower().getY(); j < currentBounds.upper().getY(); j++) {
                positions.add(new Vector2d(i, j));
            }
        }
        ListRandomizer<Vector2d> randomPositions = new ListRandomizer<>(positions);
        for(int i = 0;i < parameters.initialAnimalCount() && randomPositions.hasNext();i++)
        {
            Vector2d position = randomPositions.next();
            placeAnimal(new Animal(ThreadLocalRandom.current().nextInt(0,8),position,parameters.initialAnimalEnergy(),parameters.genomeLength(),parameters.energyUsedForReproduction(),parameters.movementEnergyCost(),parameters.mutationVariant(),parameters.behaviourType()));
        }
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
