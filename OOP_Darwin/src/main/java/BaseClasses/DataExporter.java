package BaseClasses;

import SimulationClasses.Animal;
import SimulationClasses.SimulationStatistics;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Utility class for exporting simulation data to various formats.
 * Supports CSV, JSON, and plain text exports for analysis and reporting.
 */
public class DataExporter {
    private static final Logger LOGGER = Logger.getLogger(DataExporter.class.getName());
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    private final String outputDirectory;
    
    /**
     * Creates a data exporter with default output directory.
     */
    public DataExporter() {
        this("exports");
    }
    
    /**
     * Creates a data exporter with specified output directory.
     * @param outputDirectory Directory to save exported files
     */
    public DataExporter(String outputDirectory) {
        this.outputDirectory = outputDirectory;
        createOutputDirectory();
    }
    
    /**
     * Creates the output directory if it doesn't exist.
     */
    private void createOutputDirectory() {
        File dir = new File(outputDirectory);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                LOGGER.info("Created output directory: " + outputDirectory);
            } else {
                LOGGER.warning("Failed to create output directory: " + outputDirectory);
            }
        }
    }
    
    /**
     * Exports simulation statistics to CSV format.
     * @param statistics The statistics to export
     * @param filename Custom filename (optional, null for auto-generated)
     * @return Path to the exported file
     */
    public String exportStatisticsToCSV(SimulationStatistics statistics, String filename) {
        if (filename == null) {
            filename = "statistics_" + LocalDateTime.now().format(TIMESTAMP_FORMAT) + ".csv";
        }
        
        String filePath = outputDirectory + File.separator + filename;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // CSV Header
            writer.println("Day,Population,AvgEnergy,MaxEnergy,MinEnergy,BirthRate,DeathRate,GeneticDiversity");
            
            // CSV Data using actual SimulationStatistics methods
            writer.printf("%d,%d,%.2f,%d,%d,%d,%d,%.2f%n",
                statistics.getCurrentDay(),
                statistics.getCurrentPopulation(),
                statistics.getCurrentAverageEnergy(),
                statistics.getMaxPopulation(),
                statistics.getMinPopulation(),
                statistics.getTotalBirths(),
                statistics.getTotalDeaths(),
                statistics.getGeneticDiversity()
            );
            
            LOGGER.info("Statistics exported to CSV: " + filePath);
            return filePath;
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to export statistics to CSV: " + filePath, e);
            return null;
        }
    }
    
    /**
     * Exports animal data to CSV format.
     * @param animals Map of animals by position
     * @param filename Custom filename (optional, null for auto-generated)
     * @return Path to the exported file
     */
    public String exportAnimalsToCSV(Map<Vector2d, List<Animal>> animals, String filename) {
        if (filename == null) {
            filename = "animals_" + LocalDateTime.now().format(TIMESTAMP_FORMAT) + ".csv";
        }
        
        String filePath = outputDirectory + File.separator + filename;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // CSV Header
            writer.println("ID,X,Y,Energy,Age,Direction,ChildrenCount,DayOfBirth,DayOfDeath,Genes");
            
            // CSV Data
            animals.values().stream()
                .flatMap(List::stream)
                .forEach(animal -> {
                    writer.printf("%d,%d,%d,%d,%d,%d,%d,%d,\"%s\"%n",
                        animal.hashCode(), // Using hashCode as ID
                        animal.getPosition().getX(),
                        animal.getPosition().getY(),
                        animal.getEnergyLevel(),
                        animal.getAge(),
                        animal.getCurrentDirectionInteger(),
                        animal.getChildrenCount(),
                        animal.getDayOfDeath(),
                        animal.getGenes().toString()
                    );
                });
            
            LOGGER.info("Animals exported to CSV: " + filePath);
            return filePath;
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to export animals to CSV: " + filePath, e);
            return null;
        }
    }
    
    /**
     * Exports simulation statistics to JSON format.
     * @param statistics The statistics to export
     * @param filename Custom filename (optional, null for auto-generated)
     * @return Path to the exported file
     */
    public String exportStatisticsToJSON(SimulationStatistics statistics, String filename) {
        if (filename == null) {
            filename = "statistics_" + LocalDateTime.now().format(TIMESTAMP_FORMAT) + ".json";
        }
        
        String filePath = outputDirectory + File.separator + filename;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("{");
            writer.println("  \"timestamp\": \"" + LocalDateTime.now() + "\",");
            writer.println("  \"simulation_statistics\": {");
            writer.println("    \"current_day\": " + statistics.getCurrentDay() + ",");
            writer.println("    \"current_population\": " + statistics.getCurrentPopulation() + ",");
            writer.println("    \"average_energy\": " + statistics.getCurrentAverageEnergy() + ",");
            writer.println("    \"max_population\": " + statistics.getMaxPopulation() + ",");
            writer.println("    \"min_population\": " + statistics.getMinPopulation() + ",");
            writer.println("    \"total_births\": " + statistics.getTotalBirths() + ",");
            writer.println("    \"total_deaths\": " + statistics.getTotalDeaths() + ",");
            writer.println("    \"genetic_diversity\": " + statistics.getGeneticDiversity());
            writer.println("  }");
            writer.println("}");
            
            LOGGER.info("Statistics exported to JSON: " + filePath);
            return filePath;
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to export statistics to JSON: " + filePath, e);
            return null;
        }
    }
    
    /**
     * Exports a simulation summary report to text format.
     * @param statistics The statistics to include in the report
     * @param animals Current animals data
     * @param filename Custom filename (optional, null for auto-generated)
     * @return Path to the exported file
     */
    public String exportSimulationReport(SimulationStatistics statistics, 
                                       Map<Vector2d, List<Animal>> animals, 
                                       String filename) {
        if (filename == null) {
            filename = "simulation_report_" + LocalDateTime.now().format(TIMESTAMP_FORMAT) + ".txt";
        }
        
        String filePath = outputDirectory + File.separator + filename;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("=".repeat(60));
            writer.println("DARWIN EVOLUTION SIMULATION REPORT");
            writer.println("=".repeat(60));
            writer.println("Generated: " + LocalDateTime.now());
            writer.println();
            
            // Statistics Summary
            writer.println("SIMULATION STATISTICS:");
            writer.println("-".repeat(30));
            writer.println("Current Day: " + statistics.getCurrentDay());
            writer.println("Current Population: " + statistics.getCurrentPopulation());
            writer.println("Average Energy: " + String.format("%.2f", statistics.getCurrentAverageEnergy()));
            writer.println("Population Range: " + statistics.getMinPopulation() + " - " + statistics.getMaxPopulation());
            writer.println("Total Births: " + statistics.getTotalBirths());
            writer.println("Total Deaths: " + statistics.getTotalDeaths());
            writer.println("Genetic Diversity: " + String.format("%.2f", statistics.getGeneticDiversity()));
            writer.println();
            
            // Population Distribution
            writer.println("POPULATION DISTRIBUTION:");
            writer.println("-".repeat(30));
            int totalAnimals = animals.values().stream().mapToInt(List::size).sum();
            writer.println("Total Animals: " + totalAnimals);
            writer.println("Occupied Positions: " + animals.size());
            writer.println("Average Animals per Position: " + 
                (animals.isEmpty() ? 0 : String.format("%.2f", (double) totalAnimals / animals.size())));
            writer.println();
            
            // Energy Distribution
            writer.println("ENERGY ANALYSIS:");
            writer.println("-".repeat(30));
            animals.values().stream()
                .flatMap(List::stream)
                .mapToInt(Animal::getEnergyLevel)
                .summaryStatistics();
            
            writer.println("Energy statistics calculated from current population");
            writer.println();
            
            writer.println("=".repeat(60));
            writer.println("END OF REPORT");
            writer.println("=".repeat(60));
            
            LOGGER.info("Simulation report exported: " + filePath);
            return filePath;
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to export simulation report: " + filePath, e);
            return null;
        }
    }
    
    /**
     * Exports configuration settings to a properties file.
     * @param config The configuration manager
     * @param filename Custom filename (optional, null for auto-generated)
     * @return Path to the exported file
     */
    public String exportConfiguration(ConfigurationManager config, String filename) {
        if (filename == null) {
            filename = "config_backup_" + LocalDateTime.now().format(TIMESTAMP_FORMAT) + ".properties";
        }
        
        String filePath = outputDirectory + File.separator + filename;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("# Darwin Simulation Configuration Backup");
            writer.println("# Generated: " + LocalDateTime.now());
            writer.println();
            
            writer.println("# Map Settings");
            writer.println("map.width=" + config.getMapWidth());
            writer.println("map.height=" + config.getMapHeight());
            writer.println();
            
            writer.println("# Animal Settings");
            writer.println("animal.initial.energy=" + config.getInitialAnimalEnergy());
            writer.println();
            
            writer.println("# Simulation Settings");
            writer.println("simulation.pause.time.ms=" + config.getSimulationPauseTime());
            writer.println("simulation.statistics.enabled=" + config.isStatisticsEnabled());
            
            LOGGER.info("Configuration exported: " + filePath);
            return filePath;
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to export configuration: " + filePath, e);
            return null;
        }
    }
    
    /**
     * Gets the output directory path.
     * @return The output directory path
     */
    public String getOutputDirectory() {
        return outputDirectory;
    }
}
