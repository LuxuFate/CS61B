import java.util.ArrayList;
import java.util.Arrays;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 1; i <= k - 1; i++) {
                for (int j = i; j > 0 && array[j] < array[j - 1]; j--) {
                    int temp = array[j - 1];
                    array[j - 1] = array[j];
                    array[j] = temp;
                }
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 0; i < k; i++) {
                int small = i;
                for (int j = i + 1; j <= k - 1; j++) {
                    if (array[j] <= array[small]) {
                        small = j;
                    }
                }
                int temp = array[i];
                array[i] = array[small];
                array[small] = temp;
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if(k > 1) {
                int[] ca = new int[k];
                int[] x = new int[k/2];
                int[] y = new int[k - (k / 2)];
                for (int i = 0; i < x.length; i++) {
                    x[i] = array[i];
                }
                for (int j = 0; j < y.length; j++) {
                    y[j] = array[j + k / 2];
                }
                sort(x, x.length);
                sort(y, y.length);
                ca = merge(x, y);
                for (int i = 0; i < ca.length; i++) {
                    array[i] = ca[i];
                }
            }
        }

        public int[] merge(int[] x, int[] y) {
            int[] array = new int[x.length + y.length];
            int i = 0;
            int j = 0;
            for (int z = 0; z < array.length; z++) {
                if(i >= x.length) {
                    array[z] = y[j++];
                }
                else if (j >= y.length) {
                    array[z] = x[i++];
                }
                else if (x[i] <= y[j]) {
                    array[z] = x[i++];
                }
                else {
                    array[z] = y[j++];
                }
            }
            return array;
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            int length = 1;
            for (int i : a) {
                int digits = (int) (Math.log10(i) + 1);
                if (length < digits) {
                    length = digits;
                }
            }
            ArrayList<Integer>[] count = new ArrayList[10];
            for (int i = 0; i < count.length; i++) {
                count[i] = new ArrayList<Integer>();
            }
            for (int i = 1; i <= length; i++) {
                for (int j = 0; j < k; j++) {
                    int index = (int) ((a[j] % Math.pow(10, i)) /  Math.pow(10, i - 1));
                    count[index].add(a[j]);
                }
                int ct = 0;
                for (ArrayList r: count) {
                    while (!r.isEmpty()) {
                        a[ct] = (int) r.get(0);
                        r.remove(0);
                        ct++;
                    }
                }
            }

        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
