package bitset;

import org.junit.Test;

import java.util.Collections;
import java.util.Arrays;

import static org.junit.Assert.*;

public class bitSetTest {
    @Test
    public void contains() {
        BitSet testBitset = new BitSet();
        assertFalse(testBitset.contains(1));
        long[] words = {3, 7};
        testBitset = new BitSet(words);
        assertTrue(testBitset.contains(1));
        assertFalse(testBitset.contains(3));
    }

    @Test
    public void union() {
        BitSet firstBitset = new BitSet(10);
        firstBitset.addMultiplicity(3, 7);
        BitSet secondBitset = new BitSet(5);
        secondBitset.addMultiplicity(0, 2);
        BitSet resultBitset = new BitSet(10);
        resultBitset.addMultiplicity(0, 7);
        assertTrue(resultBitset.equals(firstBitset.union(secondBitset)));

        firstBitset = new BitSet(100);
        firstBitset.addMultiplicity(44, 99);
        secondBitset = new BitSet(120);
        secondBitset.addMultiplicity(100, 119);
        resultBitset = new BitSet(120);
        resultBitset.addMultiplicity(44, 119);
        assertTrue(resultBitset.equals(firstBitset.union(secondBitset)));
        assertTrue(firstBitset.equals(firstBitset.union(firstBitset)));
    }

    @Test
    public void addition() {
        BitSet testBitset = new BitSet(5);
        testBitset.addMultiplicity(0, 3);
        BitSet resultBitset = new BitSet(5);
        resultBitset.addElement(4);
        assertTrue(resultBitset.equals(testBitset.addition()));

        testBitset = new BitSet(66);
        testBitset.addMultiplicity(0, 23);
        testBitset.addMultiplicity(36, 55);
        resultBitset = new BitSet(40);
        resultBitset.addMultiplicity(24, 35);
        resultBitset.addMultiplicity(56, 65);
        assertTrue(resultBitset.equals(testBitset.addition()));

    }


    @Test
    public void crossing() {
        BitSet firstBitset = new BitSet(10);
        firstBitset.addMultiplicity(0, 5);
        BitSet secondBitset = new BitSet(10);
        secondBitset.addMultiplicity(3, 9);
        BitSet resultBitset = new BitSet(10);
        resultBitset.addMultiplicity(3, 5);
        assertTrue(resultBitset.equals(firstBitset.crossing(secondBitset)));

        firstBitset = new BitSet(23);
        firstBitset.addMultiplicity(0, 4);
        firstBitset.addMultiplicity(7, 18);
        secondBitset = new BitSet(8);
        secondBitset.addMultiplicity(2, 7);
        resultBitset = new BitSet(8);
        resultBitset.addMultiplicity(2, 4);
        resultBitset.addElement(7);
        assertTrue(resultBitset.equals(firstBitset.crossing(secondBitset)));
        assertTrue(firstBitset.equals(firstBitset.crossing(firstBitset)));
    }

    @Test
    public void add() {
        BitSet testBitset = new BitSet(7);
        assertFalse(testBitset.contains(5));
        testBitset.addElement(5);
        testBitset = new BitSet(10);
        assertFalse(testBitset.contains(7) || testBitset.contains(4) || testBitset.contains(5));
        testBitset.addMultiplicity(3, 9);
        assertTrue(testBitset.contains(7) && testBitset.contains(4) && testBitset.contains(5));
        testBitset = new BitSet(10);
        assertFalse(testBitset.contains(5) || testBitset.contains(4));
        testBitset.addMultiplicity(4, 5);
        assertTrue(testBitset.contains(5) && testBitset.contains(4));
    }

    @Test
    public void delete() {
        BitSet testBitset = new BitSet(7);
        testBitset.addElement(3);
        assertTrue(testBitset.contains(3));
        testBitset.deleteElement(3);
        assertFalse(testBitset.contains(3));
        //long[] words = {255, 255, 255};
        testBitset = new BitSet(25);
        testBitset.addMultiplicity(3, 12);
        assertTrue(testBitset.contains(4) && testBitset.contains(5));
        testBitset.deleteMultiplicity(4, 5);
        assertFalse(testBitset.contains(4) || testBitset.contains(5));
        assertTrue(testBitset.contains(7) && testBitset.contains(9));
        testBitset.deleteMultiplicity(6, 11);
        assertFalse(testBitset.contains(7) || testBitset.contains(9));
    }
}