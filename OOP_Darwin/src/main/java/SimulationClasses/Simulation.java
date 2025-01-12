package SimulationClasses;

import Interfaces.WorldMap;
import Enums.MapType;

public class Simulation implements Runnable {
    private final WorldMap map;
    private int pauseTime = 500;
    public void setPause(int pause)
    {
        pauseTime = pause;
    }
    public Simulation(WorldMap wMap) {
        map = wMap;
    }
    public Simulation(SimulationParameters parameters)
    {
        map = MapType.matchMap(parameters.mapVariant(),parameters);
    }
    public void run()
    {
        int i = 0;
        while (true) {
            map.frame();
            System.out.println("Next frame");
            try {
                Thread.sleep(pauseTime);
            } catch (InterruptedException e) {
            }
        }
    }
}
