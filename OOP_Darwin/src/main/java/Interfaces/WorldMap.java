package Interfaces;

import BaseClasses.Vector2d;
import SimulationClasses.Animal;

import java.util.Collection;
import java.util.List;

import BaseClasses.Boundary;
import SimulationClasses.Plant;

public interface WorldMap {
    int getID();
    Boundary getCurrentBounds();
    void move(Animal animal);
    List<Animal> getAnimalsAtPosition(Vector2d position);
    Plant plantAt(Vector2d position);
    Collection<WorldElement> objectsAt();
    void initialState();
    void frame();
    void registerListener(MapChangeListener listener);
    void setBoundary(Boundary givenBoundary);
}

