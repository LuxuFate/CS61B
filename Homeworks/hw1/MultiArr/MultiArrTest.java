import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        assertEquals(625, MultiArr.maxValue( new int[][]
                {{2, 3, 4, 5}, {4, 9, 16, 25}, {16, 81, 256, 625}}));
        assertEquals(625, MultiArr.maxValue( new int[][]
                {{625, 3, 4, 5}, {4, 9, 16, 25}, {16, 81, 256, 0}}));
        assertEquals(625, MultiArr.maxValue( new int[][]
                {{0, 3, 4, 5}, {4, 9, 16, 625}, {16, 81, 256, 0}}));
        assertEquals(625, MultiArr.maxValue( new int[][]
                {{0, 3, 4, 625}, {4, 9, 16, 25}, {16, 81, 256, 0}}));
    }

    @Test
    public void testAllRowSums() {
        assertArrayEquals(new int[] {4, 8, 12}, MultiArr.allRowSums( new int[][]
                {{1, 1, 1, 1}, {2, 2, 2, 2}, {3, 3, 3, 3}}));
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
