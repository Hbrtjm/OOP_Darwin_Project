package ViewClasses;

import BaseClasses.Vector2d;
import Interfaces.MapChangeListener;
import Interfaces.WorldMap;
import SimulationClasses.Animal;
import SimulationClasses.Plant;
import SimulationClasses.Simulation;
import SimulationClasses.SimulationParameters;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

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
        System.out.println(worldMap.toString());
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

    public void stop()
    {

        simulation.stop();
    }

    @FXML
    public void handlePauseSimulation()
    {
        simulation.pause();
    }

    @FXML
    public void handleResumeSimulation()
    {
        simulation.resume();
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

    private void addLabelWithTextAt(String content, Vector2d place)
    {
        Label cellLabel = new Label(content);
        cellLabel.setAlignment(Pos.CENTER);
        GridPane.setHalignment(cellLabel, javafx.geometry.HPos.CENTER);
        GridPane.setValignment(cellLabel, javafx.geometry.VPos.CENTER);
        mapGrid.add(cellLabel, place.getX(), place.getY());
    }

    private void addPlantAt(Vector2d place, Vector2d mapCoords)
    {
        Plant plant = worldMap.plantAt(place);
        Pane cell = new Pane();
        if(plant != null) {
            String cellContent = worldMap.plantAt(place) != null ? worldMap.plantAt(place).toString() : "";
            addLabelWithTextAt(cellContent, mapCoords);
            cell.setStyle("-fx-border-color: white; -fx-background-color: green;");
            mapGrid.add(cell, mapCoords.getX(), mapCoords.getY());
        }
        else
        {
            cell.setStyle("-fx-border-color: black; -fx-background-color: white;");
        }
    }

    private void addAnimal(Vector2d place, Vector2d mapCoords,Vector2d cellSize) {
        Pane cell = new Pane();
        cell.setMaxWidth(cellSize.getX());
        cell.setMaxHeight(cellSize.getY());
        Circle circle = new Circle();
        circle.setRadius(Math.min(cell.getWidth(), cell.getHeight()) / 4);
        circle.setCenterX(cell.getWidth() / 2);
        circle.setCenterY(cell.getHeight() / 2);
        circle.setFill(Paint.valueOf("RED"));
        cell.setStyle("-fx-border-color: black; -fx-background-color: red;");
        cell.getChildren().clear();
        cell.getChildren().add(circle);
        mapGrid.add(cell, mapCoords.getX(), mapCoords.getY());
    }

    private Vector2d nearestBiggestSquare(int n)
    {
        int oddCounter = 1;
        int square = 0;
        while(n >= square*square)
        {
            square++;
            oddCounter+=2;
        }
        square = (int) Math.pow((double) (oddCounter - 1) /2,2);
        return new Vector2d(square,square);
    }

    private void addAnimals(Vector2d place, Vector2d mapCoords, Pane cell) {
        ArrayList<Animal> animals = worldMap.getAnimalsAtPosition(place);
        if (animals != null) {
            int n = animals.size();
            Vector2d layout = nearestBiggestSquare(n);
            int rows = layout.getX();
            int cols = layout.getY();
            
//            double cellWidth = cell.getWidth();
//            double cellHeight = cell.getHeight();
            double cellWidth = 10;
            double cellHeight = 10;
            double circleRadius = Math.min(cellWidth / cols, cellHeight / rows) / 2; // Adjust radius to fit

            for (int i = 0; i < n; i++) {
                int row = i / cols;
                int col = i % cols;
                double xOffset = (col + 0.5) * (cellWidth / cols);
                double yOffset = (row + 0.5) * (cellHeight / rows);
                Circle circle = new Circle(xOffset, yOffset, circleRadius);
                circle.setFill(Paint.valueOf("RED"));
                cell.getChildren().add(circle);
            }
            mapGrid.add(cell, mapCoords.getX(), mapCoords.getY());
        }
    }

    private void drawMap()
    {
        clearGrid();
        int lowerX = worldMap.getCurrentBounds().lower().getX();
        int lowerY = worldMap.getCurrentBounds().lower().getY();
        int upperX = worldMap.getCurrentBounds().upper().getX();
        int upperY = worldMap.getCurrentBounds().upper().getY();
        int width = upperX - lowerX;
        int height = upperY - lowerY;
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
//                System.out.println(worldMap.plantAt(new Vector2d(x,y)));
                addPlantAt(new Vector2d(x,y),new Vector2d(x - lowerX,height - y + lowerY - 1));
                if(worldMap.getAnimalsAtPosition(new Vector2d(x,y)) != null)
                    addAnimal(new Vector2d(x,y),new Vector2d(x - lowerX,height - y + lowerY - 1), new Vector2d(CELL_WIDTH,CELL_HEIGHT));
            }
        }
    }

    @Override
    public void mapChanged(String message)
    {
//        System.out.println("Map Changed");
        Platform.runLater(this::drawMap);
//        try {
//            Thread.sleep(timePauseValue);
//        }
//        catch(Exception e)
//        {
//        }
          Platform.runLater(() -> updateLabel.setText(message));
    }
}
