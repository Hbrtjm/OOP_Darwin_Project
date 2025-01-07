package SimulationClasses;

import BaseClasses.Boundary;
import BaseClasses.ListRandomizer;
import BaseClasses.Vector2d;

import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreepingJunglePlantsManager extends BasicPlantManager {
    public CreepingJunglePlantsManager() {
        super();
    }
    private Integer countNeighbors(Vector2d position, Boundary boundaries)
    {
        Integer count = 0;
        for(int i = 0;i < 3;i++)
        {
            for(int j = 0;j < 3;j++)
            {
                if(i == 1 && j == 1)
                    continue;
                Vector2d currentVector = position.add(new Vector2d(i,j));
                if
                (
                        boundaries.lower().precedes(currentVector) &&
                                boundaries.upper().follows(currentVector) &&
                                plants.get(new Vector2d(i,j)) != null
                )
                {
                    count++;
                }
            }
        }
        return count;
    }
    public void sortPositions(Boundary boundary)
    {
        for(int i = boundary.lower().getX();i < boundary.upper().getX();i++)
        {
            for(int j = boundary.lower().getY();j < boundary.upper().getY();j++)
            {
                Vector2d currentPosition = new Vector2d(i,j);
                Integer currentCount = countNeighbors(currentPosition, boundary);
                List<Vector2d> equalCounts = neighborCounts.get(currentCount);
                if(equalCounts == null)
                {
                    equalCounts = new ArrayList<>();
                    equalCounts.add(currentPosition);
                    neighborCounts.putIfAbsent(currentCount,equalCounts);
                    continue;
                }
                equalCounts.add(currentPosition);
                neighborCounts.remove(currentCount);
                neighborCounts.putIfAbsent(currentCount,equalCounts);
            }
        }
    }
    @Override
    public void growPlants(Boundary boundary)
    {
        sortPositions(boundary);
        int grown = 0;
        int grow;
        // This should be used as a parameter
        double maxBias = 0.5;
        int bias = (int) (maxChangeOfPlants * maxBias);
        for(int i = 8; i >= 0; i--)
        {
            List<Vector2d> list = neighborCounts.get(i);
            if(list != null)
            {
                ListRandomizer randomizer = new ListRandomizer((ArrayList<Vector2d>) list);
                grow = ThreadLocalRandom.current().nextInt(bias,maxChangeOfPlants);
                for(int j = 0;j < grow && grown < maxChangeOfPlants;j++)
                {
                    plants.putIfAbsent(randomizer.next(),new Grass());
                    grown++;
                }
                if (grown == maxChangeOfPlants)
                {
                    break;
                }
                int change = (int) (maxChangeOfPlants * (maxBias /8));
                bias = Math.max(0,bias - change);
            }
        }
    }
}
