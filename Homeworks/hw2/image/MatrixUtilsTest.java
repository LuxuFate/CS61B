package image;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/** FIXME
 *  @author Matthew Lu
 */

public class MatrixUtilsTest {

    @Test
    public void accumulateVerticalTest() {
        double[][] input = new double[][]
            {{1000000, 1000000, 1000000, 1000000},
                    {1000000, 75990, 30003, 1000000},
                    {1000000, 30002, 103046, 1000000},
                    {1000000, 29515, 38273, 1000000},
                    {1000000, 73403, 35399, 1000000},
                    {1000000, 1000000, 1000000, 1000000}};
        double[][] result = new double[][]
            {{1000000, 1000000, 1000000, 1000000},
                    {2000000, 1075990, 1030003, 2000000},
                    {2075990, 1060005, 1133049, 2030003},
                    {2060005, 1089520, 1098278, 2133049},
                    {2089520, 1162923, 1124919, 2098278},
                    {2162923, 2124919, 2124919, 2124919}};
        double[][] input2 = new double[][]
            {{10, 10, 10, 10},
                    {1, 2, 3, 4},
                    {5, 5, 5, 5},
                    {4, 3, 2, 1},
                    {10, 10, 10, 10},
                    {20, 20, 20, 20}};
        double[][] result2 = new double[][]
            {{10, 10, 10, 10},
                    {11, 12, 13, 14},
                    {16, 16, 17, 18},
                    {20, 19, 18, 18},
                    {29, 28, 28, 28},
                    {48, 48, 48, 48}};
        assertTrue(Arrays.deepEquals(result,
                MatrixUtils.accumulateVertical(input)));
        assertTrue(Arrays.deepEquals(result2,
                MatrixUtils.accumulateVertical(input2)));
    }

    @Test
    public void accumulateTest() {
        double[][] input = new double[][]
            {{1000000, 1000000, 1000000, 1000000},
                    {1000000, 75990, 30003, 1000000},
                    {1000000, 30002, 103046, 1000000},
                    {1000000, 29515, 38273, 1000000},
                    {1000000, 73403, 35399, 1000000},
                    {1000000, 1000000, 1000000, 1000000}};
        double[][] resultV = new double[][]
            {{1000000, 1000000, 1000000, 1000000},
                    {2000000, 1075990, 1030003, 2000000},
                    {2075990, 1060005, 1133049, 2030003},
                    {2060005, 1089520, 1098278, 2133049},
                    {2089520, 1162923, 1124919, 2098278},
                    {2162923, 2124919, 2124919, 2124919}};
        double[][] resultH = new double[][]
            {{1000000,   2000000,   2075990,   2060005},
                    {1000000,   1075990,   1060005,   2060005},
                    {1000000,   1030002,   1132561,   2060005},
                    {1000000,   1029515,   1067788,   2064914},
                    {1000000,   1073403,   1064914,   2064914},
                    {1000000,   2000000,   2073403,   2064914}};
        double[][] input2 = new double[][]
            {{10, 10, 10, 10},
                    {1, 2, 3, 4},
                    {5, 5, 5, 5},
                    {4, 3, 2, 1},
                    {10, 10, 10, 10},
                    {20, 20, 20, 20}};
        double[][] result2H = new double[][]
            {{10, 11, 13, 16},
                    {1, 3, 6, 10},
                    {5, 6, 8, 11},
                    {4, 7, 8, 9},
                    {10, 14, 17, 18},
                    {20, 30, 34, 37}};
        assertTrue(Arrays.deepEquals(resultV,
                MatrixUtils.accumulate(input,
                        MatrixUtils.Orientation.VERTICAL)));
        assertTrue(Arrays.deepEquals(resultH,
                MatrixUtils.accumulate(input,
                        MatrixUtils.Orientation.HORIZONTAL)));
        assertTrue(Arrays.deepEquals(result2H,
                MatrixUtils.accumulate(input2,
                        MatrixUtils.Orientation.HORIZONTAL)));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}
