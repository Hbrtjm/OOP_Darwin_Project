package SimulationClasses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import BaseClasses.Boundary;
import BaseClasses.MoveTranslator;
import BaseClasses.Vector2d;
import Enums.BehaviourType;
import Enums.MapDirection;
import Enums.MutationType;
import Interfaces.WorldElement;
import Interfaces.WorldMap;

// F2
public class Animal implements WorldElement {
    private SimulationParameters parameters;
    private WorldMap map;
    private int age;
    private List<Animal> children = new ArrayList<>();
    public int color;
    private int energyLevel = 150;
    private MutationType mutation;
    // If the maxEnergyLevel value can be universal, then we can make it a static
    private static int maxEnergyLevel;
    private Vector2d currentPosition;
    private Integer currentDirection;
    private int matingEnergy;
    private int currentMove;
    private Genes genes;
    private Collection<Animal> kids;
    private Genes getGenes()
    {
        return genes;
    }
    private BehaviourType behaviour;
    public Animal()
    {
//        genes = new Genes();
        color = 0;
        int genesAmount = ThreadLocalRandom.current().nextInt(1, 101);
        genes.generateGenes(genesAmount);
        maxEnergyLevel = Integer.MAX_VALUE;
        currentDirection = 0;
        currentPosition = new Vector2d(2, 2);
    }

    public Animal(Integer direction, Vector2d position, int maxEnergy, int genesAmount, int matingEnergy, MutationType mutationType, BehaviourType behaviourType)
    {
        super();
        genes = new Genes(mutationType.getMutationBehavior());
        if(maxEnergy > 0) {
            maxEnergyLevel = maxEnergy;
        }
        setEnergyLevel(maxEnergyLevel);
        if(direction != null)
        {
            currentDirection = direction;
        }
        if(position != null)
        {
            currentPosition = position;
        }
        if(genesAmount > 0) {
//            genes = new Genes();
            genes.generateGenes(genesAmount);
        }
        if(matingEnergy > 0) {
            setMatingEnergy(matingEnergy);
        }
        behaviour = behaviourType;
        mutation = mutationType;
    }

    public Animal(Integer direction, Vector2d position, int maxEnergy, Genes inheritedGenes, int matingEnergy, MutationType mutationType, BehaviourType behaviourType)
    {
        if(inheritedGenes != null)
        {
//            genes = inheritedGenes;
        }
//        this(direction,position,maxEnergy,inheritedGenes.getGenesAmount(),matingEnergy,mutationType);
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

    public void setMatingEnergy(int mEnergy)
    {
        matingEnergy = mEnergy;
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
        currentDirection = MapDirection.changeDirection(currentDirection,behaviour.nextGene(genes));
        currentPosition = currentPosition.add(MoveTranslator.TranslateOne(currentDirection).toUnitVector());
        if(!inBounds(currentPosition,boundary))
        {
            // Gets pushed back if it doesn't comply with the boundary, then turns around
            currentPosition = currentPosition.subtract(MoveTranslator.TranslateOne(currentDirection).toUnitVector());
            currentDirection = MapDirection.changeDirection(currentDirection,4);
        }

    }
    
    public void moveNextOnGlobeMap(Boundary boundary)
    {
        currentDirection = MapDirection.changeDirection(currentDirection,behaviour.nextGene(genes));

        //newPosition is for checking if animal does not go over boundaries
        Vector2d newPosition = new Vector2d(currentPosition.getX(),currentPosition.getY());
        newPosition = newPosition.add(MoveTranslator.TranslateOne(currentDirection).toUnitVector());

        //going over upper/lower boundary
        if (newPosition.getY() > boundary.upper().getY() || newPosition.getY() < boundary.lower().getY() )
        {

            currentDirection = MapDirection.changeDirection(currentDirection,4);
        }
        else
        {
            //going over left boundary
            if (newPosition.getX() < boundary.lower().getX()) {
                currentPosition = currentPosition.add(MoveTranslator.TranslateOne(currentDirection).toUnitVector()); //ruch odbywa się jak bez granic
                currentPosition = currentPosition.add(new Vector2d(boundary.upper().getX() - boundary.lower().getX() + 1, 0)); // następuje przeniesienie na drugą stronę mapy
            }

            //going over right boundary
            else if (newPosition.getX() > boundary.upper().getX()) {
                currentPosition = currentPosition.add(MoveTranslator.TranslateOne(currentDirection).toUnitVector());
                currentPosition = currentPosition.add(new Vector2d(boundary.lower().getX() - boundary.upper().getX() - 1, 0));
            }
            // normal movement
            else {
                currentPosition = currentPosition.add(MoveTranslator.TranslateOne(currentDirection).toUnitVector());
            }
        }
    }
    
    public void moveNextBoundless()
    {
        // One-liner to be changed, but looks funny for now...
        currentDirection = MapDirection.changeDirection(currentDirection,behaviour.nextGene(genes));
        currentPosition.add(MoveTranslator.TranslateOne(currentDirection).toUnitVector());
    }

    private boolean inBounds(Vector2d position, Boundary bounds)
    {
        return position.follows(bounds.lower()) && position.precedes(bounds.upper());
    }

//    PROBASBLY DEPRECATED, MAP HANDLES THE SORTING LOGIC
//    public void actionBounded(Boundary boundary,Animal other, boolean canEat, Plant plant)
//    {
//        moveNext(boundary);
//        if(other != null) {
//            mate(other); // I think it should be handled
//        }
//        // Same approach, sort and give food to the strongest one
//        if(canEat)
//        {
//            eat(plant);
//        }
//    }

    public boolean canMate()
    {
        return energyLevel >= matingEnergy;
    }
    // Very stupid, but I would sort every animal
    // that is on designated field and mate them in order
    // they both know that they are mating (hopefully),
    // so the kid needs to be registered once, this should happen
    // on the map
    public Animal mate(Animal other)
    {
        Genes childGenes = new Genes(mutation.getMutationBehavior());

        // Genes of self and other energy
        subtractEnergy(matingEnergy);
        other.subtractEnergy(matingEnergy);

        childGenes.combineGenes(energyLevel,other.getEnergyLevel(),genes,other.getGenes());
        int randomChildDirection = ThreadLocalRandom.current().nextInt(0,9);
//        Animal child = new Animal(randomChildDirection,currentPosition,maxEnergyLevel,childGenes,parameters.mutationVariant(),parameters.behaviourType());
//        child.setEnergyLevel(2*matingEnergy);
//
//        return child;
        return new Animal();
    }

    public void eat(Plant plant)
    {
        addEnergy(plant.getEnergy());
    }
}
