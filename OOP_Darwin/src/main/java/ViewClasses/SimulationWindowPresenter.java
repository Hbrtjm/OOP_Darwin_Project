package ViewClasses;

import Enums.MapType;
import SimulationClasses.Simulation;
import SimulationClasses.SimulationParameters;

public class SimulationWindowPresenter {
    SimulationParameters parameters;
    public void setParameters(SimulationParameters sParameters)
    {
        parameters = sParameters;
    }
    public void setWorldMap(MapType mapVariant)
    {

    }

    public void initializeWithParameters(SimulationParameters sParametes)
    {
        Simulation simulation = new Simulation(sParametes);
        Thread simulationThread = new Thread(simulation);
        simulationThread.start();

    }
}
