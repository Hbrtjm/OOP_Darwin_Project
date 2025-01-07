package SimulationClasses;

import BaseClasses.Boundary;
import BaseClasses.ListRandomizer;
import BaseClasses.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class CreepingJunglePlantsManager extends AbstractPlantManager {
    public CreepingJunglePlantsManager() {
        super();
    }

    private Integer countNeighbors(Vector2d position, Boundary boundaries) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                Vector2d neighbor = position.add(new Vector2d(i, j));
                if (boundaries.lower().precedes(neighbor) &&
                        boundaries.upper().follows(neighbor) &&
                        plants.containsKey(neighbor)) {
                    count++;
                }
            }
        }
        return count;
    }

    private void sortPositions(Boundary boundary) {
        neighborCounts.clear();
        for (int i = boundary.lower().getX(); i <= boundary.upper().getX(); i++) {
            for (int j = boundary.lower().getY(); j <= boundary.upper().getY(); j++) {
                Vector2d position = new Vector2d(i, j);
                int neighborCount = countNeighbors(position, boundary);
                neighborCounts.computeIfAbsent(neighborCount, k -> new ArrayList<>()).add(position);
            }
        }
    }

    @Override
    public void growPlants(Boundary boundary) {
        sortPositions(boundary);
        int plantsGrown = 0;
        double maxBias = 0.5;
        int bias = (int) (maxChangeOfPlants * maxBias);

        for (int i = 8; i >= 0; i--) {
            List<Vector2d> positions = neighborCounts.get(i);
            if (positions != null) {
                ListRandomizer randomizer = new ListRandomizer((ArrayList<Vector2d>) positions);
                int growLimit = ThreadLocalRandom.current().nextInt(bias, maxChangeOfPlants + 1);
                for (int j = 0; j < growLimit && plantsGrown < maxChangeOfPlants && randomizer.hasNext(); j++) {
                    plants.putIfAbsent(randomizer.next(), new Grass());
                    plantsGrown++;
                }
                if (plantsGrown >= maxChangeOfPlants) break;

                int biasReduction = (int) (maxChangeOfPlants * (maxBias / 8));
                bias = Math.max(0, bias - biasReduction);
            }
        }
    }
}
