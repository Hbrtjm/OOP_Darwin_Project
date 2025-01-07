package Interfaces;

import BaseClasses.Boundary;
import BaseClasses.Vector2d;
import SimulationClasses.Plant;

import java.util.Map;

public interface PlantsManager {
    void setMaxAddPlant(int maxChange);
    void removePlant(Vector2d position);
    Map<Vector2d, Plant> getPlants();
    int getMaxAddPlant();
    void growPlants(Boundary boundary);
}
