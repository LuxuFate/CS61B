import java.util.Arrays;

/** HW #7, Two-sum problem.
 * @author Matthew Lu
 */
public class Sum {

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        Arrays.sort(A);
        Arrays.sort(B);
        for (int i = 0; i < A.length; i++) {
            int left = m - A[i];
            int found = Arrays.binarySearch(B, left);
            if (found >= 0) {
                return true;
            }
        }
        return false;
    }

}
