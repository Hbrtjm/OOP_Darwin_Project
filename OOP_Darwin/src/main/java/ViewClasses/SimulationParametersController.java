package ViewClasses;

import Enums.MapType;
import Enums.MutationType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import SimulationClasses.SimulationParameters;

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
    @FXML private TextField genomeLengthField;
    @FXML private Slider genomeLengthSlider;

    @FXML
    private void initialize() {
        mapVariantComboBox.getItems().setAll(MapType.values());
        mutationVariantComboBox.getItems().setAll(MutationType.values());

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
    private void handleSubmit() {
        try {
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
            int genomeLength = Integer.parseInt(genomeLengthField.getText());

            SimulationParameters parameters = new SimulationParameters(
                    mapWidth, mapHeight, mapVariant, initialPlantCount, plantEnergy,
                    dailyPlantGrowth, initialAnimalCount, initialAnimalEnergy, energyRequiredToBeFed,
                    energyUsedForReproduction, minMutations, maxMutations,
                    mutationVariant, genomeLength
            );

            System.out.println("Parameters submitted: " + parameters);

        } catch (Exception e) {
            // Handle invalid input and show error to the user
            System.err.println("Invalid input: " + e.getMessage());
        }
    }
}