package SimulationClasses;

import java.util.ArrayList;
import java.util.Collections;

public class Mutation {

    private ArrayList<Integer> genesList;

    public Mutation(ArrayList<Integer> genesList) {
        this.genesList = genesList;
    }

    public void fullRandom() {
        int geneIndexToRandomise = getRandomNumber(0,genesList.size()-1);
        genesList.set(geneIndexToRandomise, getRandomNumber(0,7));
    }

    public void geneSwitch() {
        int geneIndexToSwitch1 = getRandomNumber(0,genesList.size()-1);
        int geneIndexToSwitch2 = getRandomNumber(0,genesList.size()-1);
        Collections.swap(genesList,geneIndexToSwitch1,geneIndexToSwitch2);
    }

    public int getRandomNumber(int min, int max){
        return  (int) ((Math.random() * (max-min)) + min);
    }

}
