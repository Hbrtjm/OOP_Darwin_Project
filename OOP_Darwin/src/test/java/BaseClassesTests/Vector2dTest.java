package BaseClassesTests;
import static org.junit.jupiter.api.Assertions.*;

import BaseClasses.Vector2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Vector2dTest {

    private Vector2d vector1;
    private Vector2d vector2;
    private Vector2d vector3;

    @BeforeEach
    void setUp() {
        vector1 = new Vector2d(3, 4);
        vector2 = new Vector2d(5, 6);
        vector3 = new Vector2d(3, 4); // Same as vector1 for equality checks
    }

    @Test
    void testConstructor() {
        assertEquals(3, vector1.getX());
        assertEquals(4, vector1.getY());
        assertEquals(2,1+1);
    }

    @Test
    void testToString() {
        assertEquals("(3,4)", vector1.toString());
        assertEquals("(5,6)", vector2.toString());
    }

    @Test
    void testPrecedes() {
        assertTrue(vector1.precedes(vector2));
        assertFalse(vector2.precedes(vector1));
    }

    @Test
    void testFollows() {
        assertTrue(vector2.follows(vector1));
        assertFalse(vector1.follows(vector2));
    }

    @Test
    void testAdd() {
        Vector2d result = vector1.add(vector2);
        assertEquals(new Vector2d(8, 10), result);
    }

    @Test
    void testSubtract() {
        Vector2d result = vector2.subtract(vector1);
        assertEquals(new Vector2d(2, 2), result);
    }

    @Test
    void testUpperRight() {
        Vector2d result = vector1.upperRight(vector2);
        assertEquals(new Vector2d(5, 6), result);
    }

    @Test
    void testLowerLeft() {
        Vector2d result = vector1.lowerLeft(vector2);
        assertEquals(new Vector2d(3, 4), result);
    }

    @Test
    void testOpposite() {
        Vector2d result = vector1.opposite();
        assertEquals(new Vector2d(-3, -4), result);
    }

    @Test
    void testEqualsAndHashCode() {
        // Equality
        assertEquals(vector1, vector3);
        assertNotEquals(vector1, vector2);
        assertEquals(vector1.hashCode(), vector3.hashCode());
        assertNotEquals(vector1.hashCode(), vector2.hashCode());

        assertTrue(vector1.equals(vector3) && vector3.equals(vector1));
        Vector2d vector4 = new Vector2d(3, 4);
        assertTrue(vector1.equals(vector3) && vector3.equals(vector4) && vector1.equals(vector4));

        // Null and different class
        assertNotEquals(vector1, null);
        assertNotEquals(vector1, "Some String");
    }
}
