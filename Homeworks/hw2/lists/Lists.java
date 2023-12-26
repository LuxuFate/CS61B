package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

/** List problem.
 *  @author Matthew Lu
 */
public class Lists {

    /* B. */
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */
    static IntListList naturalRuns(IntList L) {
        IntList first = L;
        IntList next = L;
        IntList rest = L;
        if (L == null) {
            return null;
        }
        while (next.tail != null) {
            if (next.head >= next.tail.head) {
                break;
            }
            next = next.tail;
        }
        rest = next.tail;

        IntListList result =  new IntListList();
        result.tail = naturalRuns(rest);
        next.tail = null;
        result.head = first;

        return result;
    }
}
