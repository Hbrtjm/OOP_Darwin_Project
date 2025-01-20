package SimulationClassesTests;


import static org.junit.jupiter.api.Assertions.*;

import BaseClasses.Boundary;
import BaseClasses.Vector2d;
import SimulationClasses.Animal;
import SimulationClasses.Genes;
import SimulationClasses.Grass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class AnimalTest {

    private Animal defaultAnimal;
    private Animal customAnimal;
    private Genes mockGenes;
    private Vector2d initialPosition;
    private Boundary boundary;

    @BeforeEach
    void setUp() {

        initialPosition = new Vector2d(5, 5);
        boundary = new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));

        defaultAnimal = new Animal();
//        customAnimal = new Animal(3, initialPosition, 100, 200, mockGenes);
    }

    @Test
    void testDefaultConstructor() {
        assertEquals(150, defaultAnimal.getEnergyLevel());
        assertEquals(0, defaultAnimal.color);
        assertNotNull(defaultAnimal.getPosition());
        assertEquals(new Vector2d(2, 2), defaultAnimal.getPosition());
        assertNotNull(defaultAnimal.getCurrentDirectionInteger());
        assertEquals(0, defaultAnimal.getCurrentDirectionInteger());
    }

    @Test
    void testCustomConstructor() {
        assertEquals(3, customAnimal.getCurrentDirectionInteger());
        assertEquals(initialPosition, customAnimal.getPosition());
        assertEquals(100, customAnimal.color);
        assertEquals(Integer.MAX_VALUE, customAnimal.getEnergyLevel()); // Default max energy;
    }

    @Test
    void testSetEnergyLevel() {
        defaultAnimal.setEnergyLevel(200);
        assertEquals(200, defaultAnimal.getEnergyLevel());
    }

    @Test
    void testAddEnergy() {
        defaultAnimal.addEnergy(50);
        assertEquals(200, defaultAnimal.getEnergyLevel()); // 150 + 50 = 200

        defaultAnimal.addEnergy(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, defaultAnimal.getEnergyLevel());
    }

    @Test
    void testSubtractEnergy() {
        defaultAnimal.subtractEnergy(50);
        assertEquals(100, defaultAnimal.getEnergyLevel()); // 150 - 50 = 100

        defaultAnimal.subtractEnergy(200);
        assertEquals(0, defaultAnimal.getEnergyLevel()); // Cannot go below 0
    }

    @Test
    void testSetPositionAndDirection() {
        defaultAnimal.setPositionAndDirection(new Vector2d(10, 10), 2);
        assertEquals(new Vector2d(10, 10), defaultAnimal.getPosition());
        assertEquals(2, defaultAnimal.getCurrentDirectionInteger());
    }

    @Test
    void testIsAt() {
        assertTrue(defaultAnimal.isAt(new Vector2d(2, 2)));
        assertFalse(defaultAnimal.isAt(new Vector2d(3, 3)));
    }

    @Test
    void testMoveNextBoundless() {
        // TODO
    }

    @Test
    void testMoveNext() {
        // TODO
    }

    @Test
    void testActionBounded_Mate() {
//        Animal mateAnimal = new Animal(3, , 100, 200)
//        Animal child = defaultAnimal.mate(mateAnimal);
//        assertNotNull(child);
    }
}
