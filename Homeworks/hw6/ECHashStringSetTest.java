import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Matthew Lu
 */
public class ECHashStringSetTest  {

    @Test
    public void testPut() {
        ECHashStringSet test = new ECHashStringSet();
        String testSet = "bcac";
        for (int i = 0; i < testSet.length(); i++) {
            test.put(testSet.substring(i, i + 1));
        }
        assertEquals((int)(1/0.2), test.length());
        assertEquals(4, test.elements());
    }

    @Test
    public void testContains() {
        ECHashStringSet test = new ECHashStringSet();
        String testSet = "defabgc";
        for (int i = 0; i < testSet.length(); i++) {
            test.put(testSet.substring(i, i + 1));
        }
        assertTrue(test.contains("a"));
        assertTrue(test.contains("b"));
        assertTrue(test.contains("c"));
        assertTrue(test.contains("d"));
        assertTrue(test.contains("e"));
        assertTrue(test.contains("f"));
        assertTrue(test.contains("g"));
        assertFalse(test.contains("z"));
    }

    @Test
    public void testAtList() {
        ECHashStringSet test = new ECHashStringSet();
        String testSet = "defabgc";
        for (int i = 0; i < testSet.length(); i++) {
            test.put(testSet.substring(i, i + 1));
        }
        List<String> list = test.asList();
        for (int i = 0; i < list.size(); i++) {
            assertTrue(list.contains(testSet.substring(i, i + 1)));
        }

        ECHashStringSet test2 = new ECHashStringSet();
        String testSet2 = "";
        for (int i = 0; i < testSet2.length(); i++) {
            test2.put(testSet.substring(i, i + 1));
        }
        List<String> list2 = test2.asList();
        for (int i = 0; i < list2.size(); i++) {
            assertTrue(list2.contains(testSet));
        }
    }

    @Test
    public void testResize() {
        ECHashStringSet test = new ECHashStringSet();
        String testSet = "abcdefghijklmnopqrstuvwxyzA";
        for (int i = 0; i < testSet.length(); i++) {
            test.put(testSet.substring(i, i + 1));
        }
        assertEquals(10, test.length());
        assertEquals(27, test.elements());
    }
}
