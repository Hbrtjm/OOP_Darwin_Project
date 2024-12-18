package SimulationClasses;

import Enums.MapType;
import Enums.PlantGrowthType;
import Enums.MutationType;

public record SimulationParameters (
        int mapWidth,
        int mapHeight,
        MapType mapVariant,
        int initialPlantCount,
        double plantEnergy,
        int dailyPlantGrowth,
        PlantGrowthType plantGrowthVariant,
        int initialAnimalCount,
        double initialAnimalEnergy,
        double energyRequiredToBeFed,
        double energyUsedForReproduction,
        int minMutations,
        int maxMutations,
        MutationType mutationVariant,
        int genomeLength
) {}