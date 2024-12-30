package SimulationClasses;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import BaseClasses.Boundary;
import BaseClasses.MoveTranslator;
import BaseClasses.Vector2d;
import Enums.MapDirection;
import Interfaces.WorldElement;

// F2
public class Animal implements WorldElement {
    private int age;
    private List<Animal> children;
    public int color;
    private int energyLevel = 150;
    // If the maxEnergyLevel value can be universal, then we can make it a static
    private static int maxEnergyLevel;
    private Vector2d currentPosition;
    private Integer currentDirection;
    private int currentMove;
    private Genes genes;
    private Collection<Animal> kids;
    private Genes getGenes()
    {
        return genes;
    }
    public boolean equals()
    {
        return true; // For now
    }
    public Animal()
    {
        color = 0;
        int genesAmount = ThreadLocalRandom.current().nextInt(1, 101);
        genes = new Genes();
        genes.generateGenes(genesAmount);
        maxEnergyLevel = Integer.MAX_VALUE;
        currentDirection = 0;
        currentPosition = new Vector2d(2, 2);
    }
    public Animal(Integer direction, Vector2d position,Integer animalColor,int maxEnergy, int genesAmount)
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
        if(genesAmount > 0) {
            genes = new Genes();
            genes.generateGenes(genesAmount);
        }
    }
    public Animal(Integer direction, Vector2d position,Integer animalColor,int maxEnergy,Genes inheritedGenes)
    {
        this(direction, position,animalColor,maxEnergy,0);
        if(inheritedGenes != null)
        {
            genes = inheritedGenes;
        }
    }
    public int getEnergyLevel()
    {
        return energyLevel;
    }
    public int getAge() { return age; }
    public List<Animal> getChildren() { return children; }
    public int getChildrenCount() { return children.size(); }
    public void setMaxEnergyLevel(int newMaxEnergyLevel) { maxEnergyLevel = newMaxEnergyLevel; }
    public void setEnergyLevel(int newEnergyLevel)
    {
        energyLevel = newEnergyLevel;
    }
    public void addEnergy(int energy)
    {
        energyLevel = Math.min(maxEnergyLevel,energy+energyLevel);
    }
    public void subtractEnergy(int energy)
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
        currentDirection = MapDirection.changeDirection(currentDirection,genes.next());
        currentPosition.add(MoveTranslator.TranslateOne(currentDirection).toUnitVector());
        if(currentPosition.getY() > boundary.upper().getY() && currentPosition.getY() < boundary.lower().getY())
        {
            currentDirection = MapDirection.changeDirection(currentDirection,4);
        }
        if(!inBounds(currentPosition,boundary))
        {
            currentPosition.subtract(MoveTranslator.TranslateOne(currentDirection).toUnitVector());
        }
    }
    
    public void moveNextOnGlobeMap(Boundary boundary)
    {
        currentDirection = MapDirection.changeDirection(currentDirection,genes.next());

        //newPosition is for testing if animal does not go over boundaries
        Vector2d newPosition=new Vector2d(currentPosition.getX(),currentPosition.getY());
        newPosition.add(MoveTranslator.TranslateOne(currentDirection).toUnitVector());

        //going over upper/lower boundary
        if (newPosition.getY() > boundary.upper().getY() || newPosition.getY() < boundary.lower().getY() )
        {
            currentDirection = MapDirection.changeDirection(currentDirection,4);
        }
        else
        {
            //going over left boundary
            if (newPosition.getX() < boundary.lower().getX()) {
                currentPosition.add(MoveTranslator.TranslateOne(currentDirection).toUnitVector()); //ruch odbywa się jak bez granic
                currentPosition.add(new Vector2d(boundary.upper().getX() - boundary.lower().getX() + 1, 0)); // następuje przeniesienie na drugą stronę mapy
            }

            //going over right boundary
            if (newPosition.getX() > boundary.upper().getX()) {
                currentPosition.add(MoveTranslator.TranslateOne(currentDirection).toUnitVector());
                currentPosition.add(new Vector2d(boundary.lower().getX() - boundary.upper().getX() - 1, 0));
            }
        }
    }
    
    public void moveNextBoundless()
    {
        // One-liner to be changed, but looks funny for now...
        currentDirection = MapDirection.changeDirection(currentDirection,genes.next());
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
    public Animal mate(Animal other)
    {
        Genes childGenes = new Genes();
        // Geny z energii w self i other
        childGenes.combineGenes(energyLevel,other.getEnergyLevel(),genes,other.getGenes());
        // TODO - Random position for now, to be discussed
        return new Animal(ThreadLocalRandom.current().nextInt(0,9),currentPosition,color,maxEnergyLevel,childGenes);
    }
    public void eat(Plant plant)
    {
        addEnergy(plant.getEnergy());
    }
}
