package enigma;

import org.junit.Test;
import ucb.junit.textui;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.TestUtils.*;
import static org.junit.Assert.assertEquals;

/** The suite of all JUnit tests for the enigma package.
 *  @author Matthew Lu
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(PermutationTest.class,
                                      MovingRotorTest.class));
    }
    /** Sets up the Machine.
     * @return returns a Machine */
    private Machine setMachine() {
        Alphabet testAl = new Alphabet();
        Collection<Rotor> allRotors = new ArrayList<Rotor>();
        Machine testMachine = new Machine(testAl,
                5, 3, allRotors);
        return testMachine;
    }

    /* ***** TESTS ***** */

    @Test
    public void checkMachine() {
        Machine M = setMachine();
        assertEquals(5, M.numRotors());
        assertEquals(3, M.numPawls());
        assertEquals(new Alphabet(), M.alphabet());
    }


}


