package BaseClasses;

import SimulationClasses.Animal;

import java.util.Comparator;

public class AnimalComparator implements Comparator<Animal> {
        @Override
        public int compare(Animal o1, Animal o2) {
            if(o1.equals(o2))
                return 0;
            else if (o1.getEnergyLevel() < o2.getEnergyLevel())
                return -1;
            else
                return 1;
        }
}
