package SimulationClasses;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Genes implements Serializable, Iterable<Integer>, Iterator<Integer> {
    private int currentGene = 0;
    private List<Integer> genesList;
    Mutation mutation;
    public void generateGenes(int genesAmount)
    {
        List<Integer> newGenes = new ArrayList<>();
        while(genesAmount-- != 0) {
            newGenes.add(ThreadLocalRandom.current().nextInt(0, 8));
        }
        genesList = newGenes;
    }
    @Override
    public Iterator<Integer> iterator() {
        // TODO - Implement iterator
        return null;
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
        int firstGenesAmount = firstParentEnergyAmount/combinedEnergy*firstParentGenes.getGenesAmount();
        int secondGenesAmount = firstParentEnergyAmount/combinedEnergy*secondParentGenes.getGenesAmount();
        int leftOrRight = ThreadLocalRandom.current().nextInt(0, 2);
        int leftAmount =  0;
        int rightAmount =  0;
        Genes leftGenes =  null;
        Genes rightGenes = null;
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
            genesList.add(rightGenes.getGenesList().get(rightGenes.getGenesList().size()-1-i));
        }
    }
}
