package Enums;

import BaseClasses.ListRandomizer;
import Interfaces.Mutation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public enum MutationType {
    RANDOM(genes -> {
        ArrayList<Integer> genesRange = new ArrayList<>(IntStream.range(0, genes.size()).boxed().toList());
        ListRandomizer<Integer> indexListRandomizer = new ListRandomizer<>(genesRange);
        int amountToRandomize = ThreadLocalRandom.current().nextInt(0, genes.size());
        for (int i = 0; i < amountToRandomize && indexListRandomizer.hasNext(); i++) {
            int randomIndex = indexListRandomizer.next();
            int randomGene = ThreadLocalRandom.current().nextInt(0, 8);
            genes.set(randomIndex, randomGene);
        }
    }),
    GENE_SWITCH(genes -> {
        int geneIndexToSwitch1 = ThreadLocalRandom.current().nextInt(genes.size());
        int geneIndexToSwitch2 = ThreadLocalRandom.current().nextInt(genes.size());
        Collections.swap(genes, geneIndexToSwitch1, geneIndexToSwitch2);
    });

    private final Mutation mutationBehavior;

    MutationType(Mutation mutationBehavior) {
        this.mutationBehavior = mutationBehavior;
    }

    public Mutation getMutationBehavior() {
        return mutationBehavior;
    }
}