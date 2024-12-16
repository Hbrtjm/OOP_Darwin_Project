package SimulationClasses;

abstract public class Food {
    private double energy;
    public double getEnergy()
    {
        return energy;
    }
    public Food()
    {
        energy = 10;
    }
    public Food(double initialEnergy)
    {
        this();
        energy = initialEnergy;
    }
}
