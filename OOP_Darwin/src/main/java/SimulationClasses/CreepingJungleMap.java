package SimulationClasses;

import Interfaces.WorldMap;

public class CreepingJungleMap extends SimpleGrassMap implements WorldMap {
    public CreepingJungleMap() {
        super();
    }
    public CreepingJungleMap(SimulationParameters parameters)
    {
        super(parameters);
    }
    @Override
    public void frame() {
        // The grass grows differently TODO
    }
}
