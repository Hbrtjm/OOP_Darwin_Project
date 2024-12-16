package SimulationClasses;

import BaseClasses.Boundary;
import BaseClasses.ListRandomizer;
import BaseClasses.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class PlantsManager {
    private Map<Vector2d, Plant> plants;
    private Map<Integer, List<Vector2d>> neighborCounts;
    private int maxChangeOfPlants;
    public void setMaxAddPlant(int maxChange)
    {
        maxChangeOfPlants = maxChange;
    }
    public void removePlant(Vector2d position)
    {
        plants.remove(position);
    }
    public Map<Vector2d,Plant> getPlants()
    {
        return plants;
    }
    public int getMaxAddPlant()
    {
        return maxChangeOfPlants;
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
                    continue;
                }
                equalCounts.add(currentPosition);
                neighborCounts.remove(currentCount);
                neighborCounts.putIfAbsent(currentCount,equalCounts);
            }
        }
    }
    public void growPlants(Boundary boundary)
    {
        sortPositions(boundary);
        int grown = 0;
        for(Integer i = 8;i >= 0;i--)
        {
            ArrayList<Plant> list = neighborCounts.get(i);
            ListRandomizer(list);
            
            if(list != null)
            {
                if(grown >= maxChangeOfPlants)
                    continue;

            }
        }
    }
}
