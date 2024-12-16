package Interfaces;

import BaseClasses.Vector2d;
import SimulationClasses.Animal;
import Enums.MapDirection;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import BaseClasses.Boundary;

public interface WorldMap extends MoveValidator {
    int getID();
    Boundary getCurrentBounds();

    void place(Animal animal);

    void move(Animal animal, MapDirection direction);

    boolean isOccupied(Vector2d position);

    List<Animal> getAnimalsAtPosition(Vector2d position);

    Collection<WorldElement> objectsAt();
    void initialState();
    void frame();
}
