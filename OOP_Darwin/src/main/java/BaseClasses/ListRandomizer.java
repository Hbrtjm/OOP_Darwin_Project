package BaseClasses;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListRandomizer implements Iterator<Vector2d>, Iterable<Vector2d> {

    private int index = 0;
    private final List<Vector2d> positions;

    public ListRandomizer(ArrayList<Vector2d> givenPositions) {
        positions = givenPositions;
        randomize();
    }

    @Override
    public boolean hasNext() {
        return index < positions.size();
    }

    @Override
    public Vector2d next() {
        if (!hasNext()) {
            return null;
        }
        return positions.get(index++);
    }

    @Override
    public Iterator<Vector2d> iterator() {
        index = 0; // Reset index for new iteration.
        return this;
    }

    private void randomize() {
        for (int i = positions.size() - 1; i > 0; i--) {
            int new_index = (int) (Math.random() * (i + 1));
            Vector2d temp = positions.get(i);
            positions.set(i, positions.get(new_index));
            positions.set(new_index, temp);
        }
    }

}
