package Interfaces;

import BaseClasses.Boundary;
import BaseClasses.Vector2d;
import SimulationClasses.Plant;

import java.util.ArrayList;
import java.util.Map;

public interface PlantsManager {
    void setMaxAddPlant(int maxChange);
    void removePlant(Vector2d position);
    Map<Vector2d, Plant> getPlants();
    int getMaxAddPlant();
    void growPlantsRandomized(ArrayList<Vector2d> availableSpaces, int plantsAmount);
    void growPlants();
    public Plant plantAt(Vector2d position);
    public void setBoundary(Boundary givenBoundary);
}
