package SimulationClasses;

import BaseClasses.Boundary;
import BaseClasses.ListRandomizer;
import BaseClasses.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class CreepingJunglePlantsManager extends AbstractPlantManager {
    private final Map<Integer, List<Vector2d>> neighborCounts;

    public CreepingJunglePlantsManager(Boundary boundary) {
        super();
        this.setBoundary(boundary);
        this.getRegions();
        neighborCounts = new HashMap<Integer, List<Vector2d>>();
    }

    private Integer countNeighbors(Vector2d position) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                Vector2d neighbor = position.add(new Vector2d(i, j));
                if (boundary.lower().precedes(neighbor) &&
                        boundary.upper().follows(neighbor) &&
                        plants.containsKey(neighbor)) {
                    count++;
                }
            }
        }
        return count;
    }

    private void sortPositions(ArrayList<Vector2d> freePositions) {
        neighborCounts.clear();
        for(Vector2d position : freePositions) {
            int neighborCount = countNeighbors(position);
            neighborCounts.computeIfAbsent(neighborCount, k -> new ArrayList<>()).add(position);
        }
    }

    public void growPlantsJungle(ArrayList<Vector2d> freePositions) {
        sortPositions(freePositions);
        int plantsGrown = 0;
        int maxPlantsLeft = maxChangeOfPlants;
        double maxBias = 1;
        int bias = (int) (maxChangeOfPlants * maxBias);
        int grown;
        for (int i = 8; i >= 0; i--) {
            ArrayList<Vector2d> positions = (ArrayList<Vector2d>) neighborCounts.get(i);
            if (positions != null) {
                int growLimit = Math.min(ThreadLocalRandom.current().nextInt(bias, maxChangeOfPlants + 1),maxPlantsLeft);
                grown = super.growPlantsRandomized(positions,growLimit);
                maxPlantsLeft -= grown;
                int biasReduction = (int) (maxChangeOfPlants * (maxBias / 8));
                bias = Math.max(0, bias - biasReduction);
            }
        }
    }

    @Override
    public void growPlants()
    {
        growPlantsJungle(equator);
        growPlantsJungle(hemispheres);
    }
}
