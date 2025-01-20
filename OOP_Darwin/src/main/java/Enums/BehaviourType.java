package Enums;

import Interfaces.Behaviour;
import SimulationClasses.Genes;

import java.util.concurrent.ThreadLocalRandom;

public enum BehaviourType {
    FULLY_DETERMINISTIC(genes -> genes.next()),

    CRAZY_MOVES(genes -> {
        if (ThreadLocalRandom.current().nextDouble() < 0.8) {
            genes.next();
        }
        else
        {
            genes.setCurrentGene(ThreadLocalRandom.current().nextInt(0,genes.getGenesAmount()));
        }
        return genes.next(); // Perform first `next` call (discard result)
    });

    private final Behaviour behaviourLogic;

    BehaviourType(Behaviour behaviourLogic) {
        this.behaviourLogic = behaviourLogic;
    }

    public Integer nextGene(Genes genes) {
        return behaviourLogic.nextGene(genes);
    }
}
