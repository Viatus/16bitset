package bitset;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class bitsetTest {
    @Test
    public void contains() {
        bitset testBitset = new bitset(10, Arrays.asList(1, 2, 3, 4));

        assertTrue(testBitset.contains(1));
        assertTrue(testBitset.contains(3));
        assertFalse(testBitset.contains(0));
        assertFalse(testBitset.contains(9));
    }

    @Test
    public void union() {
        bitset firstBitset = new bitset(10, Arrays.asList(1, 2, 3, 7));
        bitset secondBitset = new bitset(15, Arrays.asList(0, 1, 2, 4, 8, 9, 12, 13));
        bitset resultBitset = new bitset(15, Arrays.asList(0, 1, 2, 3, 4, 7, 8, 9, 12, 13));
        assertTrue(resultBitset.equals(firstBitset.union(secondBitset)));

        firstBitset = new bitset(1, Collections.<Integer>emptyList());
        secondBitset = new bitset(15, Arrays.asList(1, 2, 3));
        resultBitset = new bitset(15, Arrays.asList(1, 2, 3));
        assertTrue(resultBitset.equals(firstBitset.union(secondBitset)));

        firstBitset = new bitset(10, Arrays.asList(1, 2, 3, 7));
        secondBitset = new bitset(10, Arrays.asList(1, 2, 3, 7));
        resultBitset = new bitset(10, Arrays.asList(1, 2, 3, 7));
        assertTrue(resultBitset.equals(firstBitset.union(secondBitset)));
    }

    @Test
    public void addition() {
        bitset testBitset = new bitset(10, Arrays.asList(0, 1, 2, 3, 4));
        bitset resultBitset = new bitset(10, Arrays.asList(5, 6, 7, 8, 9));
        assertTrue(resultBitset.equals(testBitset.addition()));

        testBitset = new bitset(7, Collections.<Integer>emptyList());
        resultBitset = new bitset(7, Arrays.asList(0, 1, 2, 3, 4, 5, 6));
        assertTrue(resultBitset.equals(testBitset.addition()));

        testBitset = new bitset(10, Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        resultBitset = new bitset(10, Collections.<Integer>emptyList());
        assertTrue(resultBitset.equals(testBitset.addition()));
    }

    @Test
    public void crossing() {
        bitset firstBitset = new bitset(10, Arrays.asList(1, 2, 3, 7));
        bitset secondBitset = new bitset(15, Arrays.asList(0, 1, 2, 4, 8, 9, 12, 13));
        bitset resultBitset = new bitset(10, Arrays.asList(1, 2));
        assertTrue(resultBitset.equals(firstBitset.crossing(secondBitset)));

        firstBitset = new bitset(1, Collections.<Integer>emptyList());
        secondBitset = new bitset(15, Arrays.asList(1, 2, 3));
        resultBitset = new bitset(1, Collections.<Integer>emptyList());
        assertTrue(resultBitset.equals(firstBitset.crossing(secondBitset)));

        firstBitset = new bitset(10, Arrays.asList(1, 2, 3, 7));
        secondBitset = new bitset(10, Arrays.asList(1, 2, 3, 7));
        resultBitset = new bitset(10, Arrays.asList(1, 2, 3, 7));
        assertTrue(resultBitset.equals(firstBitset.crossing(secondBitset)));
    }
    
    @Test
    public void add() {
        bitset testBitset = new bitset(10, Arrays.asList(0, 4));
        testBitset.addElement(5);
        assertTrue(testBitset.contains(5));
        testBitset.addElement(6);
        assertTrue(testBitset.contains(6));
        testBitset.addMultiplicity(Arrays.asList(7, 8, 9));
        assertTrue(testBitset.contains(7) && testBitset.contains(8) && testBitset.contains(9));
    }

    @Test
    public void delete() {
        bitset testBitset = new bitset(10, Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        testBitset.deleteElement(0);
        assertFalse(testBitset.contains(0));
        testBitset.deleteElement(5);
        assertFalse(testBitset.contains(5));
        testBitset.deleteMultiplicity(Arrays.asList(1, 2, 4, 8, 9));
        assertFalse(testBitset.contains(1) || testBitset.contains(2) || testBitset.contains(4) || testBitset.contains(8) || testBitset.contains(9));
    }
}