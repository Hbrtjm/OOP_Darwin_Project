package BaseClasses;

import BaseClasses.Vector2d;
import Enums.MapDirection;

public class MoveTranslator {
    public static MapDirection TranslateOne(int arg) {
        return switch (arg) {
            case 0 -> MapDirection.NORTH;
            case 1 -> MapDirection.NORTH_EAST;
            case 2 -> MapDirection.EAST;
            case 3 -> MapDirection.SOUTH_EAST;
            case 4 -> MapDirection.SOUTH;
            case 5 -> MapDirection.SOUTH_WEST;
            case 6 -> MapDirection.WEST;
            case 7 -> MapDirection.NORTH_WEST;
            default -> throw new IllegalArgumentException("Invalid direction value: " + arg);
        };
    }
}

