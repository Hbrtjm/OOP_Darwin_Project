package SimulationClassesTests;

import SimulationClasses.Grass;
import org.junit.jupiter.api.Test;

import BaseClasses.Boundary;
import BaseClasses.Vector2d;
import SimulationClasses.BasicPlantManager;
import SimulationClasses.Plant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BasicPlantManagerTest {

    private BasicPlantManager plantManager;
    private Map<Vector2d, Plant> plants;
    private Boundary boundary;

    @Test
    public void testGrowPlantsWithPreference_DistributesPlantsCorrectly() {
        plants = new HashMap<>();
        boundary = new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));

        plantManager = new BasicPlantManager(boundary);
        plantManager.setMaxAddPlant(10);
        int initialPlantCount = plantManager.getPlants().size();
        plantManager.growPlants();
        int finalPlantCount = plantManager.getPlants().size();
        assertNotEquals(0, finalPlantCount);
        assertTrue(finalPlantCount > initialPlantCount, "Plants should grow.");

        // Check if plants grew preferentially in the equator and hemispheres.
        long equatorPlants = plantManager.getEquator().stream()
                .filter(pos -> plantManager.plantAt(pos) != null)
                .count();
        long hemispherePlants = plantManager.getHemispheres().stream()
                .filter(pos -> plantManager.plantAt(pos) != null)
                .count();

        assertTrue(equatorPlants > 0, "Plants should grow in the equator.");
        assertTrue(hemispherePlants > 0, "Plants should grow in the hemispheres.");
    }

    @Test
    public void growPlantsRandomizedTest() {
        plants = new HashMap<>();
        boundary = new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));

        plantManager = new BasicPlantManager(boundary);
        plantManager.setMaxAddPlant(22);
        ArrayList<Vector2d> availableSpaces = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            availableSpaces.add(new Vector2d(i, i));
        }

        plantManager.growPlantsRandomized(availableSpaces, 3);

        int plantCount = (int) plantManager.getPlants().size();

        assertEquals(3, plantCount, "Exactly 3 plants should have been added.");
        plantManager.growPlants();
        plantCount = (int) plantManager.getPlants().size();
        assertEquals(25, plantCount, "The field should be fully populatet");
    }

    @Test
    public void testSetAndGetMaxAddPlant() {
        plants = new HashMap<>();
        boundary = new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));

        plantManager = new BasicPlantManager(boundary);
        plantManager.setMaxAddPlant(10);
        plantManager.setMaxAddPlant(5);
        assertEquals(5, plantManager.getMaxAddPlant(), "MaxAddPlant should be set correctly.");
    }

    @Test
    public void testRemovePlant() {
        plants = new HashMap<>();
        boundary = new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));

        plantManager = new BasicPlantManager(boundary);
        plantManager.setMaxAddPlant(10);
        Vector2d plantPosition = new Vector2d(2, 2);
        plants.put(plantPosition, new Grass());

        plantManager.removePlant(plantPosition);

        assertNull(plantManager.plantAt(plantPosition), "Plant should be removed.");
    }

    @Test
    public void testGetRegions() {
        plants = new HashMap<>();
        boundary = new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));

        plantManager = new BasicPlantManager(boundary);
        plantManager.setBoundary(boundary);
        plantManager.setMaxAddPlant(10);
        plantManager.growPlants();

        assertFalse(plantManager.getEquator().isEmpty(), "Equator should be populated.");
        assertFalse(plantManager.getHemispheres().isEmpty(), "Hemispheres should be populated.");
    }

    @Test
    public void testSetAndGetEquator() {
        plants = new HashMap<>();
        boundary = new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));

        plantManager = new BasicPlantManager(boundary);
        plantManager.setBoundary(boundary);
        plantManager.setMaxAddPlant(10);
        ArrayList<Vector2d> equator = new ArrayList<>();
        equator.add(new Vector2d(5, 5));
        plantManager.setEquator(equator);

        assertEquals(equator, plantManager.getEquator(), "Equator should be set and retrieved correctly.");
    }

    @Test
    public void testSetAndGetHemispheres() {
        plants = new HashMap<>();
        boundary = new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));

        plantManager = new BasicPlantManager(boundary);
        plantManager.setBoundary(boundary);
        plantManager.setMaxAddPlant(10);
        ArrayList<Vector2d> hemispheres = new ArrayList<>();
        hemispheres.add(new Vector2d(2, 2));
        plantManager.setHemispheres(hemispheres);

        assertEquals(hemispheres, plantManager.getHemispheres(), "Hemispheres should be set and retrieved correctly.");
    }
}