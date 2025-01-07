package SimulationClasses;

import BaseClasses.Boundary;
import BaseClasses.ListRandomizer;
import BaseClasses.Vector2d;
import Interfaces.PlantsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

abstract public class AbstractPlantManager implements PlantsManager {
    protected Map<Vector2d, Plant> plants;
    protected Map<Integer, List<Vector2d>> neighborCounts;
    protected int maxChangeOfPlants;

    @Override
    public void setMaxAddPlant(int maxChange) {
        maxChangeOfPlants = maxChange;
    }

    @Override
    public void removePlant(Vector2d position) {
        plants.remove(position);
    }

    @Override
    public Map<Vector2d, Plant> getPlants() {
        return plants;
    }

    @Override
    public int getMaxAddPlant() {
        return maxChangeOfPlants;
    }

    @Override
    public void growPlants(Boundary boundary) {
        ArrayList<Vector2d> availableSpaces = new ArrayList<>();
        for (int i = boundary.lower().getX(); i <= boundary.upper().getX(); i++) {
            for (int j = boundary.lower().getY(); j <= boundary.upper().getY(); j++) {
                Vector2d currentPos = new Vector2d(i, j);
                if (!plants.containsKey(currentPos)) {
                    availableSpaces.add(currentPos);
                }
            }
        }
        ListRandomizer randomizer = new ListRandomizer(availableSpaces);
        for (int i = 0; i < maxChangeOfPlants && randomizer.hasNext(); i++) {
            plants.putIfAbsent(randomizer.next(), new Grass());
        }
    }
}
