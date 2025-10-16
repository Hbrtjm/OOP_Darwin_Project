package BaseClasses;

import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Manages configuration settings for the Darwin simulation.
 * Provides centralized configuration loading, saving, and validation.
 */
public class ConfigurationManager {
    private static final Logger LOGGER = Logger.getLogger(ConfigurationManager.class.getName());
    private static final String DEFAULT_CONFIG_FILE = "simulation.properties";
    
    private Properties properties;
    private String configFilePath;
    
    /**
     * Creates a configuration manager with default settings.
     */
    public ConfigurationManager() {
        this(DEFAULT_CONFIG_FILE);
    }
    
    /**
     * Creates a configuration manager with a specific config file.
     * @param configFilePath Path to the configuration file
     */
    public ConfigurationManager(String configFilePath) {
        this.configFilePath = configFilePath;
        this.properties = new Properties();
        loadDefaultConfiguration();
        loadConfiguration();
    }
    
    /**
     * Loads default configuration values.
     */
    private void loadDefaultConfiguration() {
        // Map settings
        properties.setProperty("map.width", String.valueOf(SimulationConstants.DEFAULT_MAP_WIDTH));
        properties.setProperty("map.height", String.valueOf(SimulationConstants.DEFAULT_MAP_HEIGHT));
        
        // Animal settings
        properties.setProperty("animal.initial.energy", String.valueOf(SimulationConstants.DEFAULT_ENERGY_LEVEL));
        properties.setProperty("animal.max.energy", String.valueOf(SimulationConstants.DEFAULT_MAX_ENERGY_LEVEL));
        properties.setProperty("animal.mating.energy.multiplier", String.valueOf(SimulationConstants.MATING_ENERGY_MULTIPLIER));
        
        // Gene settings
        properties.setProperty("genes.min.value", String.valueOf(SimulationConstants.MIN_GENE_VALUE));
        properties.setProperty("genes.max.value", String.valueOf(SimulationConstants.MAX_GENE_VALUE));
        properties.setProperty("genes.min.amount", String.valueOf(SimulationConstants.MIN_GENES_AMOUNT));
        properties.setProperty("genes.max.amount", String.valueOf(SimulationConstants.MAX_GENES_AMOUNT));
        
        // Simulation settings
        properties.setProperty("simulation.pause.time.ms", String.valueOf(SimulationConstants.DEFAULT_PAUSE_TIME_MS));
        properties.setProperty("simulation.logging.level", "INFO");
        properties.setProperty("simulation.statistics.enabled", "true");
        
        LOGGER.info("Default configuration loaded");
    }
    
    /**
     * Loads configuration from file if it exists.
     */
    private void loadConfiguration() {
        File configFile = new File(configFilePath);
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                properties.load(fis);
                LOGGER.info("Configuration loaded from: " + configFilePath);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Failed to load configuration from: " + configFilePath, e);
            }
        } else {
            LOGGER.info("Configuration file not found, using defaults: " + configFilePath);
        }
    }
    
    /**
     * Saves current configuration to file.
     */
    public void saveConfiguration() {
        try (FileOutputStream fos = new FileOutputStream(configFilePath)) {
            properties.store(fos, "Darwin Simulation Configuration");
            LOGGER.info("Configuration saved to: " + configFilePath);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save configuration to: " + configFilePath, e);
        }
    }
    
    /**
     * Gets an integer property value.
     * @param key The property key
     * @param defaultValue Default value if property is not found or invalid
     * @return The property value as integer
     */
    public int getIntProperty(String key, int defaultValue) {
        try {
            String value = properties.getProperty(key);
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid integer value for property: " + key + ", using default: " + defaultValue);
            return defaultValue;
        }
    }
    
    /**
     * Gets a boolean property value.
     * @param key The property key
     * @param defaultValue Default value if property is not found
     * @return The property value as boolean
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }
    
    /**
     * Gets a string property value.
     * @param key The property key
     * @param defaultValue Default value if property is not found
     * @return The property value as string
     */
    public String getStringProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Sets a property value.
     * @param key The property key
     * @param value The property value
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
    
    /**
     * Gets the map width from configuration.
     * @return Map width
     */
    public int getMapWidth() {
        return getIntProperty("map.width", SimulationConstants.DEFAULT_MAP_WIDTH);
    }
    
    /**
     * Gets the map height from configuration.
     * @return Map height
     */
    public int getMapHeight() {
        return getIntProperty("map.height", SimulationConstants.DEFAULT_MAP_HEIGHT);
    }
    
    /**
     * Gets the initial animal energy from configuration.
     * @return Initial animal energy
     */
    public int getInitialAnimalEnergy() {
        return getIntProperty("animal.initial.energy", SimulationConstants.DEFAULT_ENERGY_LEVEL);
    }
    
    /**
     * Gets the simulation pause time from configuration.
     * @return Pause time in milliseconds
     */
    public int getSimulationPauseTime() {
        return getIntProperty("simulation.pause.time.ms", SimulationConstants.DEFAULT_PAUSE_TIME_MS);
    }
    
    /**
     * Checks if statistics are enabled.
     * @return true if statistics are enabled
     */
    public boolean isStatisticsEnabled() {
        return getBooleanProperty("simulation.statistics.enabled", true);
    }
    
    /**
     * Validates all configuration values.
     * @return true if all values are valid
     */
    public boolean validateConfiguration() {
        boolean valid = true;
        
        // Validate map dimensions
        if (getMapWidth() <= 0 || getMapHeight() <= 0) {
            LOGGER.severe("Invalid map dimensions: " + getMapWidth() + "x" + getMapHeight());
            valid = false;
        }
        
        // Validate energy values
        if (getInitialAnimalEnergy() <= 0) {
            LOGGER.severe("Invalid initial animal energy: " + getInitialAnimalEnergy());
            valid = false;
        }
        
        // Validate pause time
        if (getSimulationPauseTime() < 0) {
            LOGGER.severe("Invalid simulation pause time: " + getSimulationPauseTime());
            valid = false;
        }
        
        return valid;
    }
    
    /**
     * Prints current configuration to console.
     */
    public void printConfiguration() {
        System.out.println("=== Darwin Simulation Configuration ===");
        System.out.println("Map Size: " + getMapWidth() + "x" + getMapHeight());
        System.out.println("Initial Animal Energy: " + getInitialAnimalEnergy());
        System.out.println("Simulation Pause Time: " + getSimulationPauseTime() + "ms");
        System.out.println("Statistics Enabled: " + isStatisticsEnabled());
        System.out.println("=====================================");
    }
}
