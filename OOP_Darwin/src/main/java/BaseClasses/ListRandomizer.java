package BaseClasses;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListRandomizer<T> implements Iterator<T>, Iterable<T> {

    private int index = 0;
    private final List<T> positions;

    public ListRandomizer(ArrayList<T> givenPositions) {
        positions = givenPositions;
        randomize();
    }

    @Override
    public boolean hasNext() {
        return index < positions.size();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            return null;
        }
        return positions.get(index++);
    }

    @Override
    public Iterator<T> iterator() {
        index = 0; // Reset index for new iteration.
        return this;
    }

    private void randomize() {
        for (int i = positions.size() - 1; i > 0; i--) {
            int new_index = (int) (Math.random() * (i + 1));
            T temp = positions.get(i);
            positions.set(i, positions.get(new_index));
            positions.set(new_index, temp);
        }
    }

}
