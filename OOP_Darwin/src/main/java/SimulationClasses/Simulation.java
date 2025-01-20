package SimulationClasses;

import Interfaces.WorldMap;
import Enums.MapType;

public class Simulation implements Runnable {
    private final WorldMap map;
    private int pauseTime = 500;
    private boolean running = true;
    private boolean paused = false;


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
        while (running) {
            synchronized (this) {
                while(paused) {
                    try{
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
            map.frame();
            System.out.println("Next frame");
            try {
                Thread.sleep(pauseTime);
            } catch (InterruptedException e) {
            }
        }
    }

    public void pause()
    {
        synchronized (this){
            paused = true;
        }

    }

    public void resume()
    {
        synchronized (this){
            paused = false;
            notify();
        }

    }

    public void stop()
    {
        running = false;
    }
}
