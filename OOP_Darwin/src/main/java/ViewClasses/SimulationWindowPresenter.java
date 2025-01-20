package ViewClasses;

import BaseClasses.Vector2d;
import Interfaces.MapChangeListener;
import Interfaces.WorldMap;
import SimulationClasses.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

public class SimulationWindowPresenter implements MapChangeListener {
    private SimulationParameters parameters;
    private WorldMap worldMap;
    private Simulation simulation;
    private Animal selectedAnimal;
    private int animalsCountValue = 0;
    private double averageAnimalsChildrenCountValue = 0;
    private int plantsCountValue =  0;
    private final Paint selectedAnimalPaint = Paint.valueOf("VIOLET");
    private int emptySpaceCountValue = 0;

    private double averageAnimalEnergyValue = 0;

    private double averageDeadAnimalAgeValue = 0;
    private int sumOfDeadAnimalsAgeValue = 0;
    private int deadAnimalsCountValue = 0;
    private String mostFrequentGenome;

    long timePauseValue = 300;

    @FXML
    private Label selectedAnimalEnergyField;
    @FXML
    private Label selectedAnimalAgeField;
    @FXML
    private Label selectedAnimalChildrenCountField;
    @FXML
    private Label selectedAnimalGenesField;
    @FXML
    private Label selectedAnimalDateOfDeath;
    @FXML
    private Label updateLabel;
    @FXML
    private GridPane mapGrid;
    @FXML
    private Label speedValue;
    @FXML
    private Label animalsCount;
    @FXML
    private Label plantsCount;
    @FXML
    private Label emptySpacesCount;
    @FXML
    private Label averageAnimalEnergy;
    @FXML
    private Label averageDeadAnimalAge;
    @FXML
    private Label averageAnimalsChildrenCount;
    @FXML
    private Label mostCommonGenome;
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
        speedValue.setText("Pause in milliseconds: " + timePauseValue);
        simulation.setPause((int)(timePauseValue));
    }

    public void updateStatistics()
    {

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


    public static Paint getAnimalColor(int currentEnergy, int maxEnergy) {
        if (currentEnergy == maxEnergy) {
            return Paint.valueOf("BLUE");
        } else if (currentEnergy > maxEnergy * 2 / 3) {
            return Paint.valueOf("YELLOW");
        } else if (currentEnergy > maxEnergy / 3) {
            return Paint.valueOf("BLACK");
        } else {
            return Paint.valueOf("RED");
        }
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
//        square = (int) Math.pow((double) (oddCounter - 1) / 2,2);
        return new Vector2d(square,square);
    }

    private void addAnimals(Vector2d place, Vector2d mapCoords, Vector2d cellSize) {
        ArrayList<Animal> animals = worldMap.getAnimalsAtPosition(place);
        StackPane cell = new StackPane();
        if (animals != null) {
            int n = animals.size();

            Vector2d layout = nearestBiggestSquare(n);
            int rows = layout.getX();
            int cols = layout.getY();
            double cellWidth = cellSize.getX();
            double cellHeight = cellSize.getY();
            double circleRadius = Math.min(cellWidth / cols, cellHeight / rows) / 2; // Adjust radius to fit
            double centerX;
            double centerY;
            if(n == 1)
            {
                 centerX = 0;
                 centerY = 0;
            }
            else
            {
                centerX = -circleRadius*(layout.getX()-1)-cellWidth*0.06;
                centerY = -circleRadius*(layout.getX()-1);
            }
            for (int i = 0; i < n; i++) {
                int row = i / rows;
                int col = i % cols;
                double xOffset = centerX + (col) * (cellWidth / cols);
                double yOffset = centerY + (row) * (cellHeight / rows);
                Circle circle = new Circle(circleRadius);
                if(animals.get(i).equals(selectedAnimal))
                {
                    circle.setFill(selectedAnimalPaint);
                }
                else
                {
                    circle.setFill(getAnimalColor(animals.get(i).getEnergyLevel(), animals.get(i).getMaxEnergyLevel()));
                }
                circle.setTranslateX(xOffset);
                circle.setTranslateY(yOffset);
                int finalI = i;
                circle.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                    selectedAnimal = animals.get(finalI);
                });
                cell.getChildren().add(circle);
            }
            mapGrid.add(cell, mapCoords.getX(), mapCoords.getY());
        }
    }

    private int calculateTotalAnimals() {
        animalsCountValue = 0;
        int lowerX = worldMap.getCurrentBounds().lower().getX();
        int lowerY = worldMap.getCurrentBounds().lower().getY();
        int upperX = worldMap.getCurrentBounds().upper().getX();
        int upperY = worldMap.getCurrentBounds().upper().getY();

        for (int x = lowerX; x <= upperX; x++) {
            for (int y = lowerY; y <= upperY; y++) {
                ArrayList<Animal> animalsAtPosition = worldMap.getAnimalsAtPosition(new Vector2d(x, y));
                if (animalsAtPosition != null) {
                    animalsCountValue += animalsAtPosition.size();
                }
            }
        }

        return animalsCountValue;
    }

    private int calculateTotalPlants() {
        plantsCountValue = 0;
        int lowerX = worldMap.getCurrentBounds().lower().getX();
        int lowerY = worldMap.getCurrentBounds().lower().getY();
        int upperX = worldMap.getCurrentBounds().upper().getX();
        int upperY = worldMap.getCurrentBounds().upper().getY();

        for (int x = lowerX; x <= upperX; x++) {
            for (int y = lowerY; y <= upperY; y++) {
                Plant plantAtPosition = worldMap.plantAt(new Vector2d(x, y));
                if (plantAtPosition != null) {
                    plantsCountValue++;
                }
            }
        }

        return plantsCountValue;
    }

    private int calculateTotalEmptySpaces()
    {
        int lowerX = worldMap.getCurrentBounds().lower().getX();
        int lowerY = worldMap.getCurrentBounds().lower().getY();
        int upperX = worldMap.getCurrentBounds().upper().getX();
        int upperY = worldMap.getCurrentBounds().upper().getY();
        int width = upperX - lowerX;
        int height = upperY - lowerY;

        emptySpaceCountValue = width * height;

        for (int x = lowerX; x <= upperX; x++) {
            for (int y = lowerY; y <= upperY; y++) {
                Plant plantAtPosition = worldMap.plantAt(new Vector2d(x, y));
                ArrayList<Animal> animalsAtPosition = worldMap.getAnimalsAtPosition(new Vector2d(x, y));
                if (plantAtPosition != null || animalsAtPosition != null ) {
                    emptySpaceCountValue--;
                }
            }
        }

        return emptySpaceCountValue;
    }

    private double calculateAverageAgeForDeadAnimals()
    {

        int lowerX = worldMap.getCurrentBounds().lower().getX();
        int lowerY = worldMap.getCurrentBounds().lower().getY();
        int upperX = worldMap.getCurrentBounds().upper().getX();
        int upperY = worldMap.getCurrentBounds().upper().getY();

        for (int x = lowerX; x <= upperX; x++) {
            for (int y = lowerY; y <= upperY; y++) {
                ArrayList<Animal> animalsAtPosition = worldMap.getAnimalsAtPosition(new Vector2d(x, y));
                if (animalsAtPosition != null) {
                    for (Animal animal : animalsAtPosition) {
                        if(animal.getEnergyLevel() == 0){
                            deadAnimalsCountValue++;
                            sumOfDeadAnimalsAgeValue += animal.getAge();
                        }
                    }
                }
            }
        }
        return (double) sumOfDeadAnimalsAgeValue / deadAnimalsCountValue;
    }

    private double calculateAverageAnimalEnergy() {
        double totalEnergy = 0;
        int totalAnimals = 0;

        int lowerX = worldMap.getCurrentBounds().lower().getX();
        int lowerY = worldMap.getCurrentBounds().lower().getY();
        int upperX = worldMap.getCurrentBounds().upper().getX();
        int upperY = worldMap.getCurrentBounds().upper().getY();

        for (int x = lowerX; x <= upperX; x++) {
            for (int y = lowerY; y <= upperY; y++) {
                ArrayList<Animal> animalsAtPosition = worldMap.getAnimalsAtPosition(new Vector2d(x, y));
                if (animalsAtPosition != null) {
                    totalAnimals += animalsAtPosition.size();
                    for (Animal animal : animalsAtPosition) {
                        totalEnergy += animal.getEnergyLevel();
                    }
                }
            }
        }

        if (totalAnimals > 0) {
            return totalEnergy / totalAnimals;
        } else {
            return 0;
        }
    }

    private double calculateAverageChildrenCount() {
        animalsCountValue = 0;
        averageAnimalsChildrenCountValue = 0;
        int lowerX = worldMap.getCurrentBounds().lower().getX();
        int lowerY = worldMap.getCurrentBounds().lower().getY();
        int upperX = worldMap.getCurrentBounds().upper().getX();
        int upperY = worldMap.getCurrentBounds().upper().getY();

        for (int x = lowerX; x <= upperX; x++) {
            for (int y = lowerY; y <= upperY; y++) {
                ArrayList<Animal> animalsAtPosition = worldMap.getAnimalsAtPosition(new Vector2d(x, y));
                if (animalsAtPosition != null) {
                    for (Animal animal : animalsAtPosition) {
                        animalsCountValue++;
                        averageAnimalsChildrenCountValue += animal.getChildrenCount();
                    }
                }
            }
        }
        return averageAnimalsChildrenCountValue / animalsCountValue;
    }


private String findMostFrequentGenome() {
    Map<List<Integer>, Integer> genomeFrequency = new HashMap<>();

    int lowerX = worldMap.getCurrentBounds().lower().getX();
    int lowerY = worldMap.getCurrentBounds().lower().getY();
    int upperX = worldMap.getCurrentBounds().upper().getX();
    int upperY = worldMap.getCurrentBounds().upper().getY();

    for (int x = lowerX; x <= upperX; x++) {
        for (int y = lowerY; y <= upperY; y++) {
            ArrayList<Animal> animalsAtPosition = worldMap.getAnimalsAtPosition(new Vector2d(x, y));
            if (animalsAtPosition != null) {
                for (Animal animal : animalsAtPosition) {
                    List<Integer> genome = animal.getGenes().getGenesList();
                    genomeFrequency.put(genome, genomeFrequency.getOrDefault(genome, 0) + 1);
                }
                }
            }
        }


    List<Map.Entry<List<Integer>, Integer>> sortedGenomes = genomeFrequency.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .collect(Collectors.toList());

    return sortedGenomes.isEmpty() ? Collections.emptyList().toString() : sortedGenomes.get(0).getKey().toString();
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
                    addAnimals(new Vector2d(x,y),new Vector2d(x - lowerX,height - y + lowerY - 1), new Vector2d(CELL_WIDTH,CELL_HEIGHT));
            }
        }

        //updating statistics

        animalsCountValue = calculateTotalAnimals();
        plantsCountValue = calculateTotalPlants();
        emptySpaceCountValue = calculateTotalEmptySpaces();
        averageAnimalEnergyValue = calculateAverageAnimalEnergy();
        averageDeadAnimalAgeValue = calculateAverageAgeForDeadAnimals();
        averageAnimalsChildrenCountValue = calculateAverageChildrenCount();
        mostFrequentGenome = findMostFrequentGenome();

        Platform.runLater(() -> {
            animalsCount.setText("Animals Count: " + animalsCountValue);
            plantsCount.setText("Plants Count: " + plantsCountValue);
            emptySpacesCount.setText("Empty fields Count: " + emptySpaceCountValue);
            averageAnimalEnergy.setText("Average Animal Energy: " + String.format("%.2f",averageAnimalEnergyValue));
            averageDeadAnimalAge.setText("Average Dead Animal Age: " + String.format("%.2f",averageDeadAnimalAgeValue));
            averageAnimalsChildrenCount.setText("Average Children Count of Animal: "+  String.format("%.2f",averageAnimalsChildrenCountValue));
            mostCommonGenome.setText("Most frequent genome: " + mostFrequentGenome);
        });
        int selectedAnimalEnergy;
        int selectedAnimalMaxEnergy;
        int selectedAnimalAge;
        int selectedAnimalChildrenCount;
        String selectedAnimalGenes;
        String selectedAnimalChildren;
        String dateOfDeath;
        // Animal statistics
        if(selectedAnimal != null)
        {
            selectedAnimalEnergy = "" + selectedAnimal.getEnergyLevel();
            selectedAnimalMaxEnergy = "" + selectedAnimal.getMaxEnergyLevel();
            selectedAnimalAge = "" + selectedAnimal.getAge();
            selectedAnimalChildrenCount = "" + selectedAnimal.getChildrenCount();
            selectedAnimalGenes = selectedAnimal.getGenes().toString();
            dateOfDeath = selectedAnimal.getDayOfDeath() == 0 ? "Still alive" : "Day of death:" + selectedAnimal.getDayOfDeath();
            descendantsCount = "Descendants count: " + selectedAnimal.getDescendantsCount();
        }
        else
        {
            selectedAnimalEnergy = "-";
            selectedAnimalMaxEnergy = "-";
            selectedAnimalAge = "-";
            selectedAnimalChildrenCount = "-";
            selectedAnimalGenes = "-";
            dateOfDeath = "-";
            descendantsCount = "-";
        }
        selectedAnimalEnergyField.setText("Selected Animal Energy: " + selectedAnimalEnergy + " / " + selectedAnimalMaxEnergy);
        selectedAnimalAgeField.setText("Selected Animal Age: " + selectedAnimalAge);
        selectedAnimalChildrenCountField.setText("Children Count: " + selectedAnimalChildrenCount);
        selectedAnimalGenesField.setText("Genes: " + selectedAnimalGenes);
        selectedAnimalDateOfDeath.setText(dateOfDeath);
        selectedAnimalDescendants.setText(descendantsCount);
        
        synchronized (this) {
            if (parameters.saveStatistics()) {
                saveStatisticsToCSV();
            }

        }

    }


    public void saveStatisticsToCSV() {
        String csvFile = "statistics.csv";
        boolean append = new File(csvFile).exists();

        try (FileWriter writer = new FileWriter(csvFile, true)) {

            if (!append) {
                writer.append("Animals Count").append(";")
                        .append("Plants Count").append(";")
                        .append("Empty Spaces").append(";")
                        .append("Average Animal Energy").append(";")
                        .append("Average Dead Animal Age").append(";")
                        .append("Average Animal Children Count").append(";")
                        .append("Most Frequent Genome").append("\n");

            }

            writer.append(String.valueOf(animalsCountValue)).append(";")
                    .append(String.valueOf(plantsCountValue)).append(";")
                    .append(String.valueOf(emptySpaceCountValue)).append(";")
                    .append(String.format("%.2f", averageAnimalEnergyValue)).append(";")
                    .append(String.format("%.2f", averageDeadAnimalAgeValue)).append(";")
                    .append(String.format("%.2f", averageAnimalsChildrenCountValue)).append(";")
                    .append(mostFrequentGenome).append("\n");

        } catch (IOException e) {
            e.printStackTrace();
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
