package SimulationClasses;

import BaseClasses.Boundary;
import BaseClasses.Vector2d;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class BasicPlantManager extends AbstractPlantManager {

    public BasicPlantManager(Boundary boundary) {
        super();
        setBoundary(boundary);
        getRegions();
    }

    public void growPlantsWithPreference() {
        int bias = (int)(maxChangeOfPlants/2);
        this.growPlantsRandomized(equator, ThreadLocalRandom.current().nextInt(bias,maxChangeOfPlants+1));
        this.growPlantsRandomized(hemispheres, maxChangeOfPlants-bias);
    }

    @Override
    public void growPlants() {
        getRegions();
        growPlantsWithPreference();
    }

    public ArrayList<Vector2d> getHemispheres() {
        return hemispheres;
    }

    public void setHemispheres(ArrayList<Vector2d> hemispheres) {
        this.hemispheres = hemispheres;
    }

    public ArrayList<Vector2d> getEquator() {
        return equator;
    }

    public void setEquator(ArrayList<Vector2d> equator) {
        this.equator = equator;
    }
}
