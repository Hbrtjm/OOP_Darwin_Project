package ViewClasses;

import Enums.BehaviourType;
import Enums.MapType;
import Enums.MutationType;
import Interfaces.WorldMap;
import SimulationClasses.SimulationParameters;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.scene.Scene;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.naming.NoPermissionException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.List;

public class SimulationParametersController {
    @FXML private TextField mapWidthField;
    @FXML private TextField mapHeightField;
    @FXML private ComboBox<MapType> mapVariantComboBox;
    @FXML private TextField initialPlantCountField;
    @FXML private Slider initialPlantCountSlider;
    @FXML private TextField plantEnergyField;
    @FXML private Slider plantEnergySlider;
    @FXML private TextField dailyPlantGrowthField;
    @FXML private Slider dailyPlantGrowthSlider;
    @FXML private TextField initialAnimalCountField;
    @FXML private Slider initialAnimalCountSlider;
    @FXML private TextField initialAnimalEnergyField;
    @FXML private Slider initialAnimalEnergySlider;
    @FXML private TextField energyRequiredToBeFedField;
    @FXML private Slider energyRequiredToBeFedSlider;
    @FXML private TextField energyUsedForReproductionField;
    @FXML private Slider energyUsedForReproductionSlider;
    @FXML private TextField minMutationsField;
    @FXML private Slider minMutationsSlider;
    @FXML private TextField maxMutationsField;
    @FXML private Slider maxMutationsSlider;
    @FXML private ComboBox<MutationType> mutationVariantComboBox;
    @FXML private ComboBox<BehaviourType> behaviourTypeComboBox;
    @FXML private TextField genomeLengthField;
    @FXML private Slider genomeLengthSlider;
    @FXML private TextField loadFileField;
    @FXML private TextField saveFileField;
    @FXML private ComboBox<String> usedFile;
    ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(4);
    List<File> jsonFiles;
    private Map<String,File> filenameToFile;
    private final String PARAMETERS_DIR = "parameters";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private void configureStage(Stage primaryStage, BorderPane viewRoot)
    {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    @FXML
    private void initialize() {
        mapVariantComboBox.getItems().setAll(MapType.values());
        mutationVariantComboBox.getItems().setAll(MutationType.values());
        behaviourTypeComboBox.getItems().setAll(BehaviourType.values());
        bindSliderToTextField(initialPlantCountSlider, initialPlantCountField);
        bindSliderToTextField(plantEnergySlider, plantEnergyField);
        bindSliderToTextField(dailyPlantGrowthSlider, dailyPlantGrowthField);
        bindSliderToTextField(initialAnimalCountSlider, initialAnimalCountField);
        bindSliderToTextField(initialAnimalEnergySlider, initialAnimalEnergyField);
        bindSliderToTextField(energyRequiredToBeFedSlider, energyRequiredToBeFedField);
        bindSliderToTextField(energyUsedForReproductionSlider, energyUsedForReproductionField);
        bindSliderToTextField(minMutationsSlider, minMutationsField);
        bindSliderToTextField(maxMutationsSlider, maxMutationsField);
        bindSliderToTextField(genomeLengthSlider, genomeLengthField);

        // Handles saving/loading to a file
        filenameToFile = new HashMap<>();
        try {
            parametersDirectoryExists();
        } catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        try {
            jsonFiles = getAllJsonFiles();
        }
        catch (Exception e)
        {
            System.err.println("Loading the parameters failed " + e.getMessage());
        }
        for(File file : jsonFiles)
        {
            filenameToFile.putIfAbsent(file.getName(),file);
            usedFile.getItems().add(file.getName());
        }
    }

    @FXML
    private void deleteSavedFiles()
    {
        for(File file : jsonFiles)
        {
            boolean deleted =  file.delete();
            if(deleted)
            {
                filenameToFile.remove(file.getName());
                usedFile.getItems().remove(file.getName());
                System.out.println("File deleted successfully");
            }
            else
            {
                System.out.println("File could not be deleted");
            }
        }
    }

    private void bindSliderToTextField(Slider slider, TextField textField) {
        slider.valueProperty().addListener((obs, oldValue, newValue) ->
                textField.setText(String.valueOf(newValue.intValue()))
        );
        textField.textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                slider.setValue(Double.parseDouble(newValue));
            } catch (NumberFormatException e) {
                System.out.println("Wrong value");
                System.out.println(e.getMessage());
            }
        });
    }

    @FXML
    private void handleSaveParameters()
    {
        try
        {
            SimulationParameters parameters = getSimulationParameters();
            String saveFileName = saveFileField.getText();
            String fileName;
            if(!saveFileName.isEmpty())
            {
                fileName = PARAMETERS_DIR + "/" + saveFileName + ".json";
            }
            else
            {
                fileName = PARAMETERS_DIR + "/parameters_at_time_" + System.currentTimeMillis() + ".json";
            }
            objectMapper.writeValue(new File(fileName), parameters);
            // Suboptimal, should just consider the newly created file
            jsonFiles = getAllJsonFiles();
            for(File file : jsonFiles)
            {
                filenameToFile.putIfAbsent(file.getName(),file);
                usedFile.getItems().add(file.getName());
            }
        } catch (Exception e) {
            System.err.println("Error saving parameters: " + e.getMessage());
        }
    }



    private List<File> getAllJsonFiles() throws IOException {
        return Files.list(Paths.get(PARAMETERS_DIR))
                .filter(path -> path.toString().endsWith(".json"))
                .map(Path::toFile)
                .collect(Collectors.toList());
    }
    private void loadParametersByFilename(String filename)
    {
        try{
        File fileToLoad = filenameToFile.get(filename);
        if(fileToLoad == null)
        {
            System.out.println("No saved parameters found.");
            return;
        }
        SimulationParameters parameters = objectMapper.readValue(fileToLoad, SimulationParameters.class);
        loadParametersToUI(parameters);
        System.out.println("Parameters loaded from " + fileToLoad.getName());
        } catch (Exception e) {
            System.err.println("Error loading parameters: " + e.getMessage());
        }
    }
    @FXML
    private void handleLoadParameters() {
        String filename = loadFileField.getText();
        if(filename.endsWith(".json"))
        {
            loadParametersByFilename(filename);
        }
        else if(!filename.isEmpty()){
            loadParametersByFilename(filename + ".json");
        }
    }

    @FXML
    private void handlePickParametersFile()
    {
        loadParametersByFilename(usedFile.getValue());
    }

    private void loadParametersToUI(SimulationParameters parameters) {
        mapWidthField.setText(String.valueOf(parameters.mapWidth()));
        mapHeightField.setText(String.valueOf(parameters.mapHeight()));
        mapVariantComboBox.setValue(parameters.mapVariant());
        initialPlantCountField.setText(String.valueOf(parameters.initialPlantCount()));
        plantEnergyField.setText(String.valueOf(parameters.plantEnergy()));
        dailyPlantGrowthField.setText(String.valueOf(parameters.dailyPlantGrowth()));
        initialAnimalCountField.setText(String.valueOf(parameters.initialAnimalCount()));
        initialAnimalEnergyField.setText(String.valueOf(parameters.initialAnimalEnergy()));
        energyRequiredToBeFedField.setText(String.valueOf(parameters.energyRequiredToBeFed()));
        energyUsedForReproductionField.setText(String.valueOf(parameters.energyUsedForReproduction()));
        minMutationsField.setText(String.valueOf(parameters.minMutations()));
        maxMutationsField.setText(String.valueOf(parameters.maxMutations()));
        mutationVariantComboBox.setValue(parameters.mutationVariant());
        behaviourTypeComboBox.setValue(parameters.behaviourType());
        genomeLengthField.setText(String.valueOf(parameters.genomeLength()));
    }

    @FXML
    private void handleSubmit() {
        try {
            SimulationParameters parameters = getSimulationParameters();

            System.out.println("Parameters submitted: " + parameters);
            threadPoolExecutor.submit(() -> Platform.runLater(() -> {
                try {
                    Stage newStage = new Stage();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/LaunchWindow.fxml"));
                    BorderPane viewRoot = loader.load();
                    configureStage(newStage, viewRoot);
                    SimulationWindowPresenter presenter = loader.getController();
                    presenter.setParameters(parameters);
                    WorldMap map = MapType.matchMap(parameters.mapVariant(),parameters);
                    map.registerListener(presenter);
                    presenter.setWorldMap(map);
                    presenter.initialize(parameters);
                    map.registerListener(presenter);
                    newStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
        } catch (Exception e) {
            // Handle invalid input and show error to the user
            System.err.println("Invalid input: " + e.getMessage());
        }
    }

    private SimulationParameters getSimulationParameters() {
        int mapWidth = Integer.parseInt(mapWidthField.getText());
        int mapHeight = Integer.parseInt(mapHeightField.getText());
        MapType mapVariant = mapVariantComboBox.getValue();
        int initialPlantCount = Integer.parseInt(initialPlantCountField.getText());
        int plantEnergy = Integer.parseInt(plantEnergyField.getText());
        int dailyPlantGrowth = Integer.parseInt(dailyPlantGrowthField.getText());
        int initialAnimalCount = Integer.parseInt(initialAnimalCountField.getText());
        int initialAnimalEnergy = Integer.parseInt(initialAnimalEnergyField.getText());
        int energyRequiredToBeFed = Integer.parseInt(energyRequiredToBeFedField.getText());
        int energyUsedForReproduction = Integer.parseInt(energyUsedForReproductionField.getText());
        int minMutations = Integer.parseInt(minMutationsField.getText());
        int maxMutations = Integer.parseInt(maxMutationsField.getText());
        MutationType mutationVariant = mutationVariantComboBox.getValue();
        BehaviourType behaviourType = behaviourTypeComboBox.getValue();
        int genomeLength = Integer.parseInt(genomeLengthField.getText());
        // TODO - Handle wrong values
        SimulationParameters parameters = new SimulationParameters(
                mapWidth, mapHeight, mapVariant, initialPlantCount, plantEnergy,
                dailyPlantGrowth, initialAnimalCount, initialAnimalEnergy, energyRequiredToBeFed,
                energyUsedForReproduction, minMutations, maxMutations,
                mutationVariant, genomeLength, behaviourType
        );
        return parameters;
    }

    private void parametersDirectoryExists() throws NoPermissionException {
        Path path = Paths.get(PARAMETERS_DIR);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
                System.out.println("Created parameters directory at: " + PARAMETERS_DIR);
            } catch (IOException e) {
                boolean created = new File(PARAMETERS_DIR).mkdirs();
                if(!created)
                {
                    throw new NoPermissionException("Could not create a directory");
                }
            }
        }
    }

}