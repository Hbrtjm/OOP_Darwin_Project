package SimulationClasses;

public class GlobeMap extends SimpleGrassMap {
    public GlobeMap() {
        super();
    }
    public GlobeMap(SimulationParameters parameters) {
        super(parameters);
    }
    @Override
    public void moveAll()
    {
        // Try to move the animal, if it goes above the upper/lower boundary, turn it around,
        // If it tries to go through the left or right boundary, put in on the other side of the map
        // TODO
    }
}
