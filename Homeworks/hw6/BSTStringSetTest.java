import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Matthew Lu
 */
public class BSTStringSetTest  {

    @Test
    public void testPut() {
        BSTStringSet test = new BSTStringSet();
        String testSet = "bcac";
        for (int i = 0; i < testSet.length(); i++) {
            test.put(testSet.substring(i, i + 1));
        }
        assertEquals("b", test.s(test.root()));
        assertEquals("a", test.s(test.left(test.root())));
        assertEquals("c", test.s(test.right(test.root())));
    }

    @Test
    public void testContains() {
        BSTStringSet test = new BSTStringSet();
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
        BSTStringSet test = new BSTStringSet();
        String testSet = "defabgc";
        String correct = "abcdefg";
        for (int i = 0; i < testSet.length(); i++) {
            test.put(testSet.substring(i, i + 1));
        }
        List<String> list = test.asList();
        for (int i = 0; i < list.size(); i++) {
            assertEquals(correct.substring(i, i + 1), list.get(i));
        }
    }
}
