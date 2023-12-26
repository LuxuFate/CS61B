package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Matthew Lu
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkPermuteChar() {
        Alphabet a = new Alphabet(UPPER_STRING + "#$%^&*789");
        Permutation p1 = new Permutation("(ABCD) (EFG) (H) (#$%) (789)", a);
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
        Permutation p1 = new Permutation("(012) (34) (5)",
            new Alphabet("012345"));
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
        Permutation p1 = new Permutation("(012) (34) (5)",
            new Alphabet("012345"));
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
        Permutation p1 = new Permutation("(ABCD) (EFG) (H) (#$%) (789)",
            new Alphabet(UPPER_STRING + "#$%^&*789"));
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
        Permutation p1 =
            new Permutation("(ABCD) (EFG)", new Alphabet("ABCDEFG"));
        assertEquals(true, p1.derangement());

        Permutation p2 = new Permutation("(ABC)", new Alphabet());
        assertEquals(false, p2.derangement());

        Permutation p3 = new Permutation("", new Alphabet());
        assertEquals(false, p3.derangement());
    }

}
