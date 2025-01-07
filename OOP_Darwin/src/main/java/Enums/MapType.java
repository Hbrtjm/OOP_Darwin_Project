package Enums;

import Interfaces.WorldMap;
import SimulationClasses.CreepingJungleMap;
import SimulationClasses.GlobeMap;
import SimulationClasses.Simulation;
import SimulationClasses.SimulationParameters;

public enum MapType {
    Globe,
    CreepingJungle;
    public static WorldMap matchMap(MapType mapType, SimulationParameters simulationParameters)
    {
        return switch (mapType)
        {
            case Globe -> new GlobeMap(simulationParameters);
            case CreepingJungle -> new CreepingJungleMap(simulationParameters);
        };
    }
}
