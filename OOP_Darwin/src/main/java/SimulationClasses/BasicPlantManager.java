package SimulationClasses;

import BaseClasses.Boundary;
import BaseClasses.ListRandomizer;
import BaseClasses.Vector2d;

import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class BasicPlantManager {
    protected Map<Vector2d, Plant> plants;
    protected Map<Integer, List<Vector2d>> neighborCounts;
    protected int maxChangeOfPlants;

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
    public void growPlants(Boundary boundary)
    {
        ArrayList<Vector2d> availableSpaces = new ArrayList<>();
        for(int i = boundary.lower().getX();i < boundary.upper().getX()-boundary.lower().getX();i++)
        {
            for(int j = boundary.lower().getY();j < boundary.upper().getY()-boundary.lower().getY();j++)
            {
                if(plants.containsKey(new Vector2d(i,j)))
                {
                    availableSpaces.add(new Vector2d(i,j));
                }
            }
        }
        ListRandomizer randomizer = new ListRandomizer(availableSpaces);
        for(int i = 0; i < maxChangeOfPlants && randomizer.hasNext(); i++)
        {
            plants.putIfAbsent(randomizer.next(), new Grass());
        }
    }
}
