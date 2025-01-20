package SimulationClasses;

abstract public class Plant {
    private int energy;
    public int getEnergy()
    {
        return energy;
    }
    public Plant()
    {
        energy = 10;
    }
    public Plant(int initialEnergy)
    {
        this();
        energy = initialEnergy;
    }
}
