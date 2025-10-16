package SimulationClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.OptionalDouble;

/**
 * Tracks and calculates statistics for the Darwin simulation.
 * Provides insights into population dynamics, genetic diversity, and survival metrics.
 */
public class SimulationStatistics {
    private final List<Integer> populationHistory;
    private final List<Double> averageEnergyHistory;
    private final List<Double> averageLifespanHistory;
    private final Map<Integer, Integer> geneFrequency;
    private int totalDeaths;
    private int totalBirths;
    private int currentDay;
    
    public SimulationStatistics() {
        this.populationHistory = new ArrayList<>();
        this.averageEnergyHistory = new ArrayList<>();
        this.averageLifespanHistory = new ArrayList<>();
        this.geneFrequency = new HashMap<>();
        this.totalDeaths = 0;
        this.totalBirths = 0;
        this.currentDay = 0;
    }
    
    /**
     * Updates statistics for the current simulation day.
     * @param animals Current list of all animals in the simulation
     * @param newDeaths Number of animals that died this day
     * @param newBirths Number of animals born this day
     */
    public void updateDailyStatistics(List<Animal> animals, int newDeaths, int newBirths) {
        currentDay++;
        totalDeaths += newDeaths;
        totalBirths += newBirths;
        
        // Population statistics
        populationHistory.add(animals.size());
        
        // Energy statistics
        OptionalDouble avgEnergy = animals.stream()
            .mapToInt(Animal::getEnergyLevel)
            .average();
        averageEnergyHistory.add(avgEnergy.orElse(0.0));
        
        // Lifespan statistics (for animals that died)
        OptionalDouble avgLifespan = animals.stream()
            .filter(animal -> animal.getDayOfDeath() > 0)
            .mapToInt(Animal::getAge)
            .average();
        averageLifespanHistory.add(avgLifespan.orElse(0.0));
        
        // Gene frequency analysis
        updateGeneFrequency(animals);
    }
    
    /**
     * Updates the frequency count of genes in the current population.
     */
    private void updateGeneFrequency(List<Animal> animals) {
        geneFrequency.clear();
        for (Animal animal : animals) {
            if (animal.getGenes() != null) {
                for (Integer gene : animal.getGenes().getGenesList()) {
                    geneFrequency.merge(gene, 1, Integer::sum);
                }
            }
        }
    }
    
    /**
     * @return Current population size
     */
    public int getCurrentPopulation() {
        return populationHistory.isEmpty() ? 0 : populationHistory.get(populationHistory.size() - 1);
    }
    
    /**
     * @return Maximum population reached during simulation
     */
    public int getMaxPopulation() {
        return populationHistory.stream().mapToInt(Integer::intValue).max().orElse(0);
    }
    
    /**
     * @return Minimum population reached during simulation
     */
    public int getMinPopulation() {
        return populationHistory.stream().mapToInt(Integer::intValue).min().orElse(0);
    }
    
    /**
     * @return Average population over the entire simulation
     */
    public double getAveragePopulation() {
        return populationHistory.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }
    
    /**
     * @return Current average energy level of all animals
     */
    public double getCurrentAverageEnergy() {
        return averageEnergyHistory.isEmpty() ? 0.0 : averageEnergyHistory.get(averageEnergyHistory.size() - 1);
    }
    
    /**
     * @return Current average lifespan of animals
     */
    public double getCurrentAverageLifespan() {
        return averageLifespanHistory.isEmpty() ? 0.0 : averageLifespanHistory.get(averageLifespanHistory.size() - 1);
    }
    
    /**
     * @return Total number of deaths throughout the simulation
     */
    public int getTotalDeaths() {
        return totalDeaths;
    }
    
    /**
     * @return Total number of births throughout the simulation
     */
    public int getTotalBirths() {
        return totalBirths;
    }
    
    /**
     * @return Current simulation day
     */
    public int getCurrentDay() {
        return currentDay;
    }
    
    /**
     * @return Most common gene in the current population
     */
    public Integer getMostCommonGene() {
        return geneFrequency.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }
    
    /**
     * @return Genetic diversity index (number of unique genes / total genes)
     */
    public double getGeneticDiversity() {
        int totalGenes = geneFrequency.values().stream().mapToInt(Integer::intValue).sum();
        if (totalGenes == 0) return 0.0;
        return (double) geneFrequency.size() / totalGenes;
    }
    
    /**
     * @return Population growth rate (births - deaths) / current population
     */
    public double getPopulationGrowthRate() {
        int currentPop = getCurrentPopulation();
        if (currentPop == 0) return 0.0;
        return (double) (totalBirths - totalDeaths) / currentPop;
    }
    
    /**
     * Generates a comprehensive statistics report.
     * @return Formatted string containing all major statistics
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== Darwin Simulation Statistics ===\n");
        report.append(String.format("Day: %d\n", getCurrentDay()));
        report.append(String.format("Current Population: %d\n", getCurrentPopulation()));
        report.append(String.format("Max Population: %d\n", getMaxPopulation()));
        report.append(String.format("Min Population: %d\n", getMinPopulation()));
        report.append(String.format("Average Population: %.2f\n", getAveragePopulation()));
        report.append(String.format("Current Average Energy: %.2f\n", getCurrentAverageEnergy()));
        report.append(String.format("Current Average Lifespan: %.2f\n", getCurrentAverageLifespan()));
        report.append(String.format("Total Births: %d\n", getTotalBirths()));
        report.append(String.format("Total Deaths: %d\n", getTotalDeaths()));
        report.append(String.format("Population Growth Rate: %.4f\n", getPopulationGrowthRate()));
        report.append(String.format("Genetic Diversity: %.4f\n", getGeneticDiversity()));
        
        Integer mostCommonGene = getMostCommonGene();
        if (mostCommonGene != null) {
            report.append(String.format("Most Common Gene: %d\n", mostCommonGene));
        }
        
        return report.toString();
    }
}
