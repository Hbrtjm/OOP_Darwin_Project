package SimulationClasses;

public class Grass extends Plant {
    public Grass()
    {
        super(20);
    }
    @Override
    public String toString() {
        return "*"; // Will be replaced by a colorful field
    }
}
