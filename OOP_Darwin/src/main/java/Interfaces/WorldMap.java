package Interfaces;

import BaseClasses.Vector2d;
import SimulationClasses.Animal;

import java.util.Collection;
import java.util.List;

import BaseClasses.Boundary;

public interface WorldMap {
    int getID();
    Boundary getCurrentBounds();

    void place(Animal animal);

    void move(Animal animal);

    List<Animal> getAnimalsAtPosition(Vector2d position);

    Collection<WorldElement> objectsAt();
    void initialState();
    void frame();
}
