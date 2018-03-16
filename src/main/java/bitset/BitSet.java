package bitset;
/*Вариант 16 -- BitSet
Реализовать множество над заданным набором объектов. Количество элементов в наборе задается в конструкторе.
Конкретный элемент набора идентифицируется неотрицательным целым от нуля до количества элементов - 1 (альтернатива -- уникальным именем).
    Операции: пересечение, объединение, дополнение; добавление/удаление заданного элемента (массива элементов), проверка принадлежности элемента множеству.
    Бонус: итератор по множеству.*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BitSet {
    private final static int ADDRESS_BITS_PER_WORD = 6;
    private final static int BITS_PER_WORD = 1;
    private final static int BIT_INDEX_MASK = BITS_PER_WORD - 1;
    private static final long WORD_MASK = 0xffffffffffffffffL;

    private long[] words;
    private transient int wordsInUse = 0;

    private static int wordIndex(int bitIndex) {
        return bitIndex >> ADDRESS_BITS_PER_WORD;
    }

    private void initWords(int nbits) {
        words = new long[wordIndex(nbits - 1) + 1];
    }

    public BitSet() {
        initWords(BITS_PER_WORD);
    }

    public BitSet(int nbits) {
        if (nbits < 0) {
            throw new NegativeArraySizeException();
        }
        initWords(nbits);
    }

    private BitSet(long[] words) {
        this.words = words;
        this.wordsInUse = words.length;
    }

    private void isInRange(int index) {
        if (index < 0 || wordIndex(index) > wordsInUse) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void isInRange(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex < fromIndex || wordIndex(toIndex) > wordsInUse) {
            throw new IndexOutOfBoundsException();
        }
    }

    public void addElement(int bitIndex) {
        isInRange(bitIndex);
        int wordIndex = wordIndex(bitIndex);
        words[wordIndex] |= (1L << bitIndex);
    }

    public void addMultiplicity(int fromIndex, int toIndex) {
        isInRange(fromIndex, toIndex);
        int startWordIndex = wordIndex(fromIndex);
        int endWordIndex = wordIndex(toIndex - 1);
        long firstWordMask = WORD_MASK << fromIndex;
        long lastWordMask = WORD_MASK >>> -toIndex;
        if (startWordIndex == endWordIndex) {
            words[startWordIndex] |= (firstWordMask & lastWordMask);
        } else {
            words[startWordIndex] |= firstWordMask;
            for (int i = startWordIndex + 1; i < lastWordMask; i++) {
                words[i] = WORD_MASK;
            }
            words[endWordIndex] = lastWordMask;
        }
    }

    public void deleteElement(int bitIndex) {
        isInRange(bitIndex);
        int wordIndex = wordIndex(bitIndex);
        words[wordIndex] &= ~(1L << bitIndex);
    }

    public void deleteMultiplicity(int fromIndex, int toIndex) {
        isInRange(fromIndex, toIndex);
        int startWordIndex = wordIndex(fromIndex);
        int endWordIndex = wordIndex(toIndex - 1);
        long firstWordMask = WORD_MASK << fromIndex;
        long lastWordMask = WORD_MASK >>> -toIndex;
        if (startWordIndex == endWordIndex) {
            words[startWordIndex] &= ~(firstWordMask & lastWordMask);
        } else {
            words[startWordIndex] &= ~firstWordMask;
            for (int i = startWordIndex + 1; i < lastWordMask; i++) {
                words[i] = 0;
            }
            words[endWordIndex] &= ~lastWordMask;
        }
    }

    public boolean contains(int bitIndex) {
        isInRange(bitIndex);
        int wordIndex = wordIndex(bitIndex);
        if (words[wordIndex] == (words[wordIndex] | ~(1L << bitIndex))) {
            return true;
        }
        return false;
    }

    public BitSet crossing(BitSet other) {
        if (this == other) {
            return this;
        }
        int wordsInCommon = Math.min(wordsInUse, other.wordsInUse);
        BitSet mainCrossing;
        BitSet assistingCrossing;
        if (wordsInCommon == wordsInUse) {
            mainCrossing = this;
            assistingCrossing = other;
        } else {
            mainCrossing = other;
            assistingCrossing = this;
        }
        for (int i = 0; i < wordsInCommon; i++) {
            mainCrossing.words[i] &= assistingCrossing.words[i];
        }
        return mainCrossing;
    }

    public BitSet union(BitSet other) {
        if (this == other) {
            return this;
        }
        int wordsInCommon = Math.min(wordsInUse, other.wordsInUse);
        BitSet assistingUnion;
        BitSet mainUnion;
        if (wordsInCommon == wordsInUse) {
            assistingUnion = this;
            mainUnion = other;
        } else {
            assistingUnion = other;
            mainUnion = this;
        }
        for (int i = 0; i < wordsInCommon; i++) {
            mainUnion.words[i] |= assistingUnion.words[i];
        }
        return mainUnion;
    }

    public BitSet addition() {
        BitSet addtionBitSet = this;
        for (int i = 0; i < wordsInUse; i++) {
            words[i] = ~words[i];
        }
        return addtionBitSet;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BitSet))
            return false;
        if (this == obj) {
            return true;
        }
        BitSet other = (BitSet) obj;
        if (wordsInUse != other.wordsInUse) {
            return false;
        }
        for (int i = 0; i < wordsInUse; i++) {
            if (words[i] != other.words[i]) {
                return false;
            }
        }
        return true;
    }
}
