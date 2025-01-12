package Interfaces;

import java.util.ArrayList;

@FunctionalInterface
public interface Mutation {
    void mutate(ArrayList<Integer> genes);
}
