package SimulationClasses;

import Interfaces.WorldMap;
import Enums.MapType;

public class Simulation {
    private WorldMap map;

    public Simulation(SimulationParameters parameters)
    {
        map = MapType.matchMap(parameters.mapVariant(),parameters);
    }
    public void run()
    {
        map.frame();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
    }
}
