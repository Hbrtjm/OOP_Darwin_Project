package Interfaces;

import SimulationClasses.Genes;

@FunctionalInterface
public interface Behaviour {
    Integer nextGene(Genes genes);
}