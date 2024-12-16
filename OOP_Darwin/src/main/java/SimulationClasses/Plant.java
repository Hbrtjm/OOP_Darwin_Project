package SimulationClasses;

abstract public class Plant {
    private double energy;
    public double getEnergy()
    {
        return energy;
    }
    public Plant()
    {
        energy = 10;
    }
    public Plant(double initialEnergy)
    {
        this();
        energy = initialEnergy;
    }
}
