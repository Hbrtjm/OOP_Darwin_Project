package ViewClasses;

import BaseClasses.Vector2d;
import Interfaces.MapChangeListener;
import Interfaces.WorldMap;
import SimulationClasses.Simulation;
import SimulationClasses.SimulationParameters;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.awt.*;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class SimulationWindowPresenter implements MapChangeListener {
    SimulationParameters parameters;
    WorldMap worldMap;
    Simulation simulation;
    long timePauseValue = 300;
    @FXML
    private Label updateLabel;
    @FXML
    private GridPane mapGrid;
    @FXML
    private Label speedValue;
    @FXML
    private Slider pauseTime;
    public void initialize(SimulationParameters sParameters)
    {
        drawMap();
        simulation = new Simulation(worldMap);
        Thread simulationThread = new Thread(simulation);
        simulationThread.start();
    }
    public void setParameters(SimulationParameters sParameters)
    {
        parameters = sParameters;
    }
    public void setWorldMap(WorldMap map)
    {
        worldMap = map;
    }

    @FXML
    public void handleSliderChange()
    {
        drawMap();
        timePauseValue = Math.round(pauseTime.getValue());
        speedValue.setText("Czas pauzy w milisekundach: " + timePauseValue);
        simulation.setPause((int)(timePauseValue));
    }

    public void clearGrid()
    {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    public void addLabelWithTextAt(String content, Vector2d place)
    {
        Label cellLabel = new Label(content);
        cellLabel.setAlignment(Pos.CENTER);
        GridPane.setHalignment(cellLabel, javafx.geometry.HPos.CENTER);
        GridPane.setValignment(cellLabel, javafx.geometry.VPos.CENTER);
        mapGrid.add(cellLabel, place.getX(), place.getY());
    }

    private void drawMap()
    {
        clearGrid();
        int lowerX = worldMap.getCurrentBounds().lower().getX();
        int lowerY = worldMap.getCurrentBounds().lower().getY();
        int upperX = worldMap.getCurrentBounds().upper().getX();
        int upperY = worldMap.getCurrentBounds().upper().getY();
        int width = upperX - lowerX + 2;
        int height = upperY - lowerY + 2;
        int HEIGHT_LIMIT = 400;
        int WIDTH_LIMIT = 500;
        int CELL_HEIGHT = Math.round(HEIGHT_LIMIT/(height));
        int CELL_WIDTH = Math.round(WIDTH_LIMIT/(width));
        for (int i = 0; i < width; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        }
        for (int i = 0; i < height; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        }
        for (int x = lowerX; x < upperX; x++) {
            for (int y = lowerY; y < upperY; y++) {
                String cellContent = worldMap.plantAt(new Vector2d(x,y)) != null ? worldMap.plantAt(new Vector2d(x,y)).toString() : "";
                addLabelWithTextAt(cellContent, new Vector2d(x - lowerX,height - y + lowerY));
            }
        }
    }

    @Override
    public void mapChanged(String message)
    {
        System.out.println("Map Changed");
        Platform.runLater(this::drawMap);
//        try {
//            Thread.sleep(timePauseValue);
//        }
//        catch(Exception e)
//        {
//        }        Platform.runLater(() -> updateLabel.setText(message));
    }
}
