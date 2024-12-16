package SimulationClasses;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import BaseClasses.Boundary;
import BaseClasses.MoveTranslator;
import BaseClasses.Vector2d;
import Enums.MapDirection;
import Interfaces.WorldElement;

import java.util.ArrayList;
import java.util.List;

// F2
public class Animal implements WorldElement {
    public int color;
    private double energyLevel = 150;
    private double maxEnergyLevel;
    private Vector2d currentPosition;
    private Integer currentDirection;
    private int currentGene;
    private int currentMove;
    private List<Integer> genes;
    private Collection<Animal> kids;
    private List<Integer> getGenes()
    {
        return genes;
    }
    public boolean equals()
    {
        return true; // For now
    }
    private List<Integer> generateGenes(int genotypeLength)
    {
        List<Integer> newGenes = new ArrayList<>();
        while(genotypeLength-- != 0) {
            newGenes.add(ThreadLocalRandom.current().nextInt(0, 8));
        }
        return newGenes;
    }
    public Animal()
    {
        color = 0;
        int genesAmount = ThreadLocalRandom.current().nextInt(1, 101);
        genes = generateGenes(genesAmount);
        maxEnergyLevel = Double.POSITIVE_INFINITY;
        currentDirection = 0;
        currentPosition = new Vector2d(2, 2);
    }
    public Animal(Integer direction, Vector2d position,Integer animalColor,double maxEnergy, int geneAmount)
    {
        this();
        if(animalColor != null){
            color = animalColor;
        }
        if(maxEnergy > 0) {
            maxEnergyLevel = maxEnergy;
        }
        if(direction != null)
        {
            currentDirection = direction;
        }
        if(position != null)
        {
            currentPosition = position;
        }
        if(geneAmount > 0) {
            genes = generateGenes(geneAmount);
        }
    }
    public Animal(Integer direction, Vector2d position,Integer animalColor,double maxEnergy,List<Integer> inheritedGenes)
    {
        this(direction, position,animalColor,maxEnergy,0);
        if(inheritedGenes != null)
        {
            genes = inheritedGenes;
        }
    }
    public double getEnergyLevel()
    {
        return energyLevel;
    }
    public void setEnergyLevel(int newEnergyLevel)
    {
        energyLevel = newEnergyLevel;
    }
    public void addEnergy(double energy)
    {
        energyLevel = Math.min(maxEnergyLevel,energy+energyLevel);
    }
    public void subtractEnergy(double energy)
    {
        energyLevel = Math.max(0,energyLevel-energy);
    }
    public MapDirection getCurrentDirection()
    {
        return MoveTranslator.TranslateOne(currentDirection);
    }
    public Integer getCurrentDirectionInteger() { return currentDirection; }
    public boolean isAt(Vector2d position) {
        return currentPosition.equals(position);
    }
    public void setPositionAndDirection(Vector2d newPosition, int directionChange)
    {
        currentPosition = newPosition;
        currentDirection = (currentDirection+directionChange)%8;
    }
    @Override
    public Vector2d getPosition()
    {
        return currentPosition;
    }
    public void moveNext(Boundary boundary)
    {
        // One-liner to be changed, but looks funny for now...
        currentDirection = MapDirection.changeDirection(currentDirection,genes.get(currentGene++));
        currentPosition.add(MoveTranslator.TranslateOne(currentDirection).toUnitVector());
        if(!inBounds(currentPosition,boundary))
        {
            currentPosition.subtract(MoveTranslator.TranslateOne(currentDirection).toUnitVector());
        }
    }
    public void moveNextBoundless()
    {
        // One-liner to be changed, but looks funny for now...
        currentDirection = MapDirection.changeDirection(currentDirection,genes.get(currentGene++));
        currentPosition.add(MoveTranslator.TranslateOne(currentDirection).toUnitVector());
    }
    private boolean inBounds(Vector2d position, Boundary bounds)
    {
        return position.follows(bounds.lower()) && position.precedes(bounds.upper());
    }
    public void actionBounded(Boundary boundary,Animal other, boolean canEat, Plant plant)
    {
        moveNext(boundary);
        if(other != null) {
            mate(other); // I think it should be handled
        }
        // Same approach, sort and give food to the strongest one
        if(canEat)
        {
            eat(plant);
        }
    }
    // Very stupid, but I would sort every animal
    // that is on designated field and mate them in order
    // they both know that they are mating (hopefully),
    // so the kid needs to be registered once, this should happen
    // on the map
    public void mate(Animal other)
    {
        System.out.println("53x0");
    }
    public void eat(Plant plant)
    {
        addEnergy(plant.getEnergy());
    }
}
