package BaseClasses;

/**
 * Constants used throughout the Darwin simulation.
 * Centralizes magic numbers and configuration values for better maintainability.
 */
public final class SimulationConstants {
    
    // Private constructor to prevent instantiation
    private SimulationConstants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }
    
    // Animal constants
    public static final int DEFAULT_ENERGY_LEVEL = 150;
    public static final int DEFAULT_MAX_ENERGY_LEVEL = Integer.MAX_VALUE;
    public static final int DEFAULT_INITIAL_DIRECTION = 0;
    public static final int MIN_GENES_AMOUNT = 1;
    public static final int MAX_GENES_AMOUNT = 100;
    
    // Gene constants
    public static final int MIN_GENE_VALUE = 0;
    public static final int MAX_GENE_VALUE = 8;
    public static final int DIRECTION_COUNT = 8;
    public static final int TURN_AROUND_DIRECTION = 4;
    
    // Map constants
    public static final int DEFAULT_MAP_WIDTH = 10;
    public static final int DEFAULT_MAP_HEIGHT = 10;
    public static final int DEFAULT_MAP_MIN_X = 0;
    public static final int DEFAULT_MAP_MIN_Y = 0;
    public static final int DEFAULT_MAP_MAX_X = DEFAULT_MAP_WIDTH;
    public static final int DEFAULT_MAP_MAX_Y = DEFAULT_MAP_HEIGHT;
    public static final Vector2d DEFAULT_MAP_LOWER_BOUND = new Vector2d(0, 0);
    public static final Vector2d DEFAULT_MAP_UPPER_BOUND = new Vector2d(DEFAULT_MAP_WIDTH, DEFAULT_MAP_HEIGHT);
    
    // Simulation constants
    public static final int DEFAULT_PAUSE_TIME_MS = 500;
    public static final int MATING_ENERGY_MULTIPLIER = 2;
    
    // Random generation constants
    public static final int RANDOM_DIRECTION_BOUND = 9;
    public static final int BINARY_CHOICE_BOUND = 2;
    
    // Default position for new animals
    public static final Vector2d DEFAULT_ANIMAL_POSITION = new Vector2d(2, 2);
}
