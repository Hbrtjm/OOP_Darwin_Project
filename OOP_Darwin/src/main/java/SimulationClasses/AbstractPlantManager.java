package SimulationClasses;

import BaseClasses.Boundary;
import BaseClasses.ListRandomizer;
import BaseClasses.Vector2d;
import Interfaces.PlantsManager;
import Interfaces.WorldElement;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class AbstractPlantManager implements PlantsManager {
    protected Map<Vector2d, Plant> plants = new HashMap<Vector2d, Plant>();
    protected int maxChangeOfPlants;
    protected Boundary boundary;
    protected ArrayList<Vector2d> equator;
    protected ArrayList<Vector2d> hemispheres;
    protected double preferredRegionPercentage = 0.2;
    public AbstractPlantManager()
    {
        equator =  new ArrayList<>();
        hemispheres = new ArrayList<>();
    }
    public AbstractPlantManager(int givenMaxChangeOfPlants, Boundary givenBoundary)
    {
        maxChangeOfPlants = givenMaxChangeOfPlants;
        boundary = givenBoundary;
    }

    @Override
    public void setBoundary(Boundary givenBoundary) {
        boundary = givenBoundary;
    }
    public Boundary getBoundary(){ return boundary; }
    private boolean inPreferredRegion(Vector2d position)
    {
        int top = boundary.upper().getY();
        int bottom = boundary.lower().getY();
        int width = top-bottom;
        int regionSideWidth = (int)(width * preferredRegionPercentage)/2;
        int center = (int)(width/2);
        return position.getY() >= (center-regionSideWidth) && position.getY() <= (center+regionSideWidth);
    }

    protected void getRegions()
    {
        for (int i = boundary.lower().getX(); i <= boundary.upper().getX(); i++) {
            for (int j = boundary.lower().getY(); j <= boundary.upper().getY(); j++) {
                Vector2d currentPos = new Vector2d(i, j);
                if (!plants.containsKey(currentPos)) {
                    if(inPreferredRegion(currentPos))
                    {
                        equator.add(currentPos);
                    }
                    else
                    {
                        hemispheres.add(currentPos);
                    }
                }
            }
        }
    }

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
    public void growPlantsRandomized(ArrayList<Vector2d> availableSpaces, int plantAmount) {
        /*
        Randomly adds *plantAmount* plants from the given list.
         */
        ListRandomizer<Vector2d> randomizer = new ListRandomizer<Vector2d>(availableSpaces);
        for (int i = 0; i < plantAmount && randomizer.hasNext(); i++) {
            plants.putIfAbsent(randomizer.next(), new Grass());
        }
    }
    @Override
    public Plant plantAt(Vector2d position)
    {
        return plants.get(position);
    }
}
