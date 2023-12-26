package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author Matthew Lu
 */
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        int [] result = new int[A.length + B.length];
        System.arraycopy(A, 0, result, 0, A.length);
        System.arraycopy(B, 0, result, A.length, B.length);
        return result;
    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        int[] result = new int[A.length - len];
        int[] front = Utils.subarray(A, 0, start);
        int[] back =
                Utils.subarray(A, start + len, A.length - len - front.length);
        System.arraycopy(front, 0, result, 0, front.length);
        System.arraycopy(back, 0, result, front.length, back.length);
        return result;
    }

    /* C3. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        int counter = 0;
        for (int i = 0; i < A.length - 1; i++) {
            if (A[i] >= A[i + 1]) {
                counter++;
            }
        }
        int[][] result = new int[counter + 1][];
        if (A.length <= 1) {
            if (A.length == 1) {
                result[0] = A;
            } else {
                return new int[][]{};
            }
            return result;
        }
        int list = 0;
        int start = 0;
        for (int i = 0; i < A.length - 1; i++) {
            if (A[i] >= A[i + 1]) {
                result[list] = Utils.subarray(A, start, i - start + 1);
                list++;
                start = i + 1;
            }
            if (i + 1 == A.length - 1) {
                result[list] = Utils.subarray(A, start, A.length - start);
                break;
            }
        }
        return result;
    }
}
