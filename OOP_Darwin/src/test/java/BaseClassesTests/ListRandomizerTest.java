package BaseClassesTests;

import BaseClasses.ListRandomizer;
import BaseClasses.Vector2d;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ListRandomizerTest {
    public static ArrayList<Vector2d> positions;
    public static ListRandomizer randomizer;
    public static int N;
    public static int M;
    @BeforeAll
    public static void testConstructor()
    {
        N = 10;
        M = 20;
        ArrayList<Vector2d> positions = new ArrayList<>();
        for(int i = 0;i < N;i++)
        {
            for(int j = 0;j < M;j++)
            {
                positions.add(new Vector2d(j,i));
            }
        }
        ListRandomizer randomizer = new ListRandomizer(positions);
    }

    @Test
    public void testHasNext()
    {

    }

}
