package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author Matthew Lu
 */

public class ArraysTest {

    @Test
    public void catenateTest() {
        int[] inx = {1, 2, 3};
        int[] iny = {3, 2, 1};
        int[] result = {1, 2, 3, 3, 2, 1};
        int[] inx2 = {};
        int[] iny2 = {};
        int[] result2 = {};
        int[] inx3 = {1};
        int[] iny3 = {0, 2, 4, -9};
        int[] result3 = {1, 0, 2, 4, -9};
        assertArrayEquals(result, Arrays.catenate(inx, iny));
        assertArrayEquals(result2, Arrays.catenate(inx2, iny2));
        assertArrayEquals(result3, Arrays.catenate(inx3, iny3));
    }

    @Test
    public void removeTest() {
        int[] in = {1, 2, 3, 3, 2, 1};
        int[] result = {1, 2, 2, 1};
        int[] in2 = {};
        int[] result2 = {};
        int[] in3 = {1, 2};
        int[] result3 = {};
        int[] in4 = {1, 2, 3, 4};
        int[] result4 = {1, 2};
        int[] in5 = {1, 2, 3, 4};
        int[] result5 = {3, 4};
        assertArrayEquals(result, Arrays.remove(in, 2, 2));
        assertArrayEquals(result2, Arrays.remove(in2, 0, 0));
        assertArrayEquals(result3, Arrays.remove(in3, 0, 2));
        assertArrayEquals(result4, Arrays.remove(in4, 2, 2));
        assertArrayEquals(result5, Arrays.remove(in5, 0, 2));
    }

    @Test
    public void naturalRunsTest() {
        int[][] result = new int[][] {{1, 3, 7}, {5}, {4, 6, 9, 10}, {10, 11}};
        int[] input = new int [] {1, 3, 7, 5, 4, 6, 9, 10, 10, 11};
        int[][] result2 = new int[][] {{1, 2, 3}, {2, 3}, {2, 3}};
        int[] input2 = new int [] {1, 2, 3, 2, 3, 2, 3};
        int[][] result3 = new int[][] {{1}};
        int[] input3 = new int [] {1};
        int[][] result4 = new int[][] {};
        int[] input4 = new int [] {};
        assertTrue(Utils.equals(result, Arrays.naturalRuns(input)));
        assertTrue(Utils.equals(result2, Arrays.naturalRuns(input2)));
        assertTrue(Utils.equals(result3, Arrays.naturalRuns(input3)));
        assertTrue(Utils.equals(result4, Arrays.naturalRuns(input4)));

    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
