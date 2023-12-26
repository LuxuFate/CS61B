package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author
 */
public abstract class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */
    abstract Permutation getNewPermutation(String cycles, Alphabet alphabet);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet(String chars);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet();

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** Check that PERM has an ALPHABET whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }

    @Test
    public void checkPermuteChar() {
        Permutation p1 = getNewPermutation("(ABCD) (EFG) (H) (#$%) (789)", getNewAlphabet(UPPER_STRING + "#$%^&*789"));
        assertEquals('B', p1.permute('A'));
        assertEquals('C', p1.permute('B'));
        assertEquals('D', p1.permute('C'));
        assertEquals('A', p1.permute('D'));

        assertEquals('F', p1.permute('E'));
        assertEquals('G', p1.permute('F'));
        assertEquals('E', p1.permute('G'));

        assertEquals('H', p1.permute('H'));

        assertEquals('$', p1.permute('#'));
        assertEquals('%', p1.permute('$'));
        assertEquals('#', p1.permute('%'));

        assertEquals('8', p1.permute('7'));
        assertEquals('9', p1.permute('8'));
        assertEquals('7', p1.permute('9'));

        assertEquals('Z', p1.permute('Z'));
    }

    @Test
    public void checkPermuteInt() {
        Permutation p1 = getNewPermutation("(012) (34) (5)", getNewAlphabet("012345"));
        assertEquals(1, p1.permute(0));
        assertEquals(2, p1.permute(1));
        assertEquals(0, p1.permute(2));

        assertEquals(4, p1.permute(3));
        assertEquals(3, p1.permute(4));

        assertEquals(5, p1.permute(5));
        assertEquals(5, p1.permute(-1));
    }

    @Test
    public void checkInvertInt() {
        Permutation p1 = getNewPermutation("(012) (34) (5)", getNewAlphabet("012345"));
        assertEquals(2, p1.invert(0));
        assertEquals(0, p1.invert(1));
        assertEquals(1, p1.invert(2));

        assertEquals(4, p1.invert(3));
        assertEquals(3, p1.invert(4));

        assertEquals(5, p1.invert(5));
        assertEquals(5, p1.invert(-1));
    }

    @Test
    public void checkInvertChar() {
        Permutation p1 = getNewPermutation("(ABCD) (EFG) (H) (#$%) (789)", getNewAlphabet(UPPER_STRING + "#$%^&*789"));
        assertEquals('D', p1.invert('A'));
        assertEquals('A', p1.invert('B'));
        assertEquals('B', p1.invert('C'));
        assertEquals('C', p1.invert('D'));

        assertEquals('G', p1.invert('E'));
        assertEquals('E', p1.invert('F'));
        assertEquals('F', p1.invert('G'));

        assertEquals('H', p1.invert('H'));

        assertEquals('%', p1.invert('#'));
        assertEquals('#', p1.invert('$'));
        assertEquals('$', p1.invert('%'));

        assertEquals('9', p1.invert('7'));
        assertEquals('7', p1.invert('8'));
        assertEquals('8', p1.invert('9'));

        assertEquals('Z', p1.invert('Z'));
    }

    @Test
    public void checkDerangement() {
        Permutation p1 = getNewPermutation("(ABCD) (EFG)", getNewAlphabet("ABCDEFG"));
        assertEquals(true, p1.derangement());

        Permutation p2 = getNewPermutation("(ABC)", getNewAlphabet());
        assertEquals(false, p2.derangement());

        Permutation p3 = getNewPermutation("", getNewAlphabet());
        assertEquals(false, p3.derangement());
    }

}
