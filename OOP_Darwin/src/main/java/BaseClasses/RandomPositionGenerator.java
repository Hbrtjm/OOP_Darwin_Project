package BaseClasses;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class RandomPositionGenerator implements Iterable<Vector2d>, Iterator<Vector2d> {

        private int index = 0;
        private final int gCount;
        private final List<Vector2d> X = new ArrayList<>();

        public RandomPositionGenerator(int maxWidth, int maxHeight, int grassCount) {
            gCount = grassCount;
            for (int i = 0; i < maxWidth; i++) {
                for (int j = 0; j < maxHeight; j++) {
                    X.add(new Vector2d(i, j));
                }
            }
            randomize();
        }

        @Override
        public boolean hasNext() {
            return index < gCount && index < X.size();
        }

        @Override
        public Vector2d next() {
            if (!hasNext()) {
                return null;
            }
            return X.get(index++);
        }

        @Override
        public Iterator<Vector2d> iterator() {
            index = 0; // Reset index for new iteration.
            return this;
        }

        private void randomize() {
            for (int i = X.size() - 1; i > 0; i--) {
                int new_index = (int) (Math.random() * (i + 1));
                Vector2d temp = X.get(i);
                X.set(i, X.get(new_index));
                X.set(new_index, temp);
            }
        }

}
