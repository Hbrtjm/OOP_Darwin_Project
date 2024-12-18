package SimulationClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Genes implements Serializable, Iterable<Integer>, Iterator<Integer> {
    private int currentGene;
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
        currentGene = (currentGene + 1)%genesList.size();
        return genesList.get(currentGene++);
    }

    public Genes combineGenes(int myGenesAmount, int otherGenesAmount, Genes otherGenes)
    {
        // TODO - Combination of genes
        return new Genes();
    }

}
