package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *
 *  @author Matthew Lu
 */

public class ListsTest {

    @Test
    public void naturalRunsTest() {
        int[][] result = new int[][] {{1, 3, 7}, {5}, {4, 6, 9, 10}, {10, 11}};
        int[] input = new int [] {1, 3, 7, 5, 4, 6, 9, 10, 10, 11};
        int[][] result2 = new int[][] {{1, 2, 3}, {2, 3}};
        int[] input2 = new int [] {1, 2, 3, 2, 3};
        int[][] result3 = new int[][] {{1}};
        int[] input3 = new int [] {1};
        int[][] result4 = new int[][] {};
        int[] input4 = new int [] {};
        assertEquals(IntListList.list(result),
                Lists.naturalRuns(IntList.list(input)));
        assertEquals(IntListList.list(result2),
                Lists.naturalRuns(IntList.list(input2)));
        assertEquals(IntListList.list(result3),
                Lists.naturalRuns(IntList.list(input3)));
        assertEquals(IntListList.list(result4),
                Lists.naturalRuns(IntList.list(input4)));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
