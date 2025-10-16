package SimulationClasses;

import BaseClasses.SimulationConstants;
import Interfaces.Mutation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Genes implements Serializable, Iterable<Integer>, Iterator<Integer> {
    private int currentGene = 0;
    private List<Integer> genesList;
    private final Mutation mutation;

    @Override
    public String toString()
    {
        return genesList.toString();
    }

    public Genes(Mutation mutationType) {
        genesList = new ArrayList<>();
        this.mutation = mutationType;

    }

    public void generateGenes(int genesAmount)
    {
        List<Integer> newGenes = new ArrayList<>();
        while(genesAmount-- != 0) {
            newGenes.add(ThreadLocalRandom.current().nextInt(SimulationConstants.MIN_GENE_VALUE, SimulationConstants.MAX_GENE_VALUE));
        }
        genesList = newGenes;
    }

    public void mutateGenes() {
        mutation.mutate(new ArrayList<>(genesList));
    }

    public void setCurrentGene(int geneIndex) {
        currentGene = geneIndex;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new GenesIterator();
    }

    private class GenesIterator implements Iterator<Integer> {
        private int index = 0;

        @Override
        public boolean hasNext() {
            return !genesList.isEmpty();
        }

        @Override
        public Integer next() {
            if (genesList.isEmpty()) {
                throw new IllegalStateException("No genes available");
            }
            Integer gene = genesList.get(index % genesList.size());
            index++;
            return gene;
        }
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Integer next() {
        currentGene = currentGene%genesList.size();
        return genesList.get(currentGene++);
    }

    public int getGenesAmount()
    {
        return genesList.size();
    }

    public List<Integer> getGenesList()
    {
        return genesList;
    }

    public Integer getIthGene(int index)
    {
        return genesList.get(index);
    }

    public void combineGenes(int firstParentEnergyAmount, int secondParentEnergyAmount, Genes firstParentGenes, Genes secondParentGenes)
    {
        // Failsafe, considering that the animals will be sorted by energy, this should not be used
        if(firstParentEnergyAmount < secondParentEnergyAmount)
        {
            Genes tempGenes = firstParentGenes;
            firstParentGenes = secondParentGenes;
            secondParentGenes = tempGenes;
            int tempAmount = firstParentEnergyAmount;
            firstParentEnergyAmount = secondParentEnergyAmount;
            secondParentEnergyAmount = tempAmount;
        }
        int combinedEnergy = firstParentEnergyAmount + secondParentEnergyAmount;
        double dominantFraction = (double) firstParentEnergyAmount / combinedEnergy;

        int firstGenesAmount = (int) (dominantFraction * firstParentGenes.getGenesAmount());

        int secondGenesAmount = (int) ((1-dominantFraction) * secondParentGenes.getGenesAmount());

        while(firstGenesAmount + secondGenesAmount < firstParentGenes.getGenesAmount())
        {
            firstGenesAmount++;
        }

        int leftOrRight = ThreadLocalRandom.current().nextInt(0, SimulationConstants.BINARY_CHOICE_BOUND);
        int leftAmount;
        int rightAmount;
        Genes leftGenes;
        Genes rightGenes;
        if(leftOrRight == 0)
        {
            // Left side of genes of the stronger animal, right side of the weaker
            leftAmount = firstGenesAmount;
            rightAmount = secondGenesAmount;
            leftGenes = firstParentGenes;
            rightGenes = secondParentGenes;
        }
        else {
            // Right side of genes of the stronger animal, left side of the weaker
            leftAmount = secondGenesAmount;
            rightAmount = firstGenesAmount;
            leftGenes = secondParentGenes;
            rightGenes = firstParentGenes;
        }
        for(int i = 0;i < leftAmount;i++)
        {
            genesList.add(leftGenes.getGenesList().get(i));
        }
        for(int i = rightAmount;i > 0;i--)
        {
            genesList.add(rightGenes.getGenesList().get(rightGenes.getGenesList().size()-i));
        }
        mutateGenes();
    }
}
