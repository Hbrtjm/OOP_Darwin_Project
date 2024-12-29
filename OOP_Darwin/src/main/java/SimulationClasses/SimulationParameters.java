package SimulationClasses;

import Enums.MapType;
import Enums.MutationType;

public record SimulationParameters (
        int mapWidth,
        int mapHeight,
        MapType mapVariant,
        int initialPlantCount,
        int plantEnergy,
        int dailyPlantGrowth,
        int initialAnimalCount,
        int initialAnimalEnergy,
        int energyRequiredToBeFed,
        int energyUsedForReproduction,
        int minMutations,
        int maxMutations,
        MutationType mutationVariant,
        int genomeLength
) {}
