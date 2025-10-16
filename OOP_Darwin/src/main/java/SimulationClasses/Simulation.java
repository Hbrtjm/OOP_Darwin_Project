package SimulationClasses;

import BaseClasses.SimulationConstants;
import Interfaces.WorldMap;
import Enums.MapType;

public class Simulation implements Runnable {
    private final WorldMap map;
    private volatile int pauseTime = SimulationConstants.DEFAULT_PAUSE_TIME_MS;
    private volatile boolean running = true;
    private volatile boolean paused = false;


    /**
     * Sets the pause time between simulation frames.
     * @param pause The pause time in milliseconds (must be non-negative)
     * @throws IllegalArgumentException if pause is negative
     */
    public void setPause(int pause)
    {
        if (pause < 0) {
            throw new IllegalArgumentException("Pause time cannot be negative");
        }
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
                Thread.currentThread().interrupt();
                return;
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
