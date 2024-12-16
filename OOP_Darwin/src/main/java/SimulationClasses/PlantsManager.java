package SimulationClasses;

import BaseClasses.Boundary;
import BaseClasses.Vector2d;
import java.util.Map;

public class PlantsManager {
    private Map<Vector2d,Food> plants;
    private int maxChangeOfPlants;
    public void setMaxAddPlant(int maxChange)
    {
        maxChangeOfPlants = maxChange;
    }
    public int getMaxAddPlant()
    {
        return maxChangeOfPlants;
    }
    private int countNeighbors(Vector2d position)
    {

    }
    public void growPlants(Boundary boundary)
    {
        // Sort positions by the biggest amount of neighboring plants
        //
        for(int i = boundary.lower().getX();i < boundary.upper().getX();i++)
        {
            for(int j = boundary.lower().getY();j < boundary.upper().getY();j++)
            {

            }
        }
    }
}
