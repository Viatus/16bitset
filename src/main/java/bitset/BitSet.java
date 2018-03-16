package bitset;


public class BitSet {
    private final static int ADDRESS_BITS_PER_WORD = 6;
    private final static int BITS_PER_WORD = 1 << ADDRESS_BITS_PER_WORD;
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

    public BitSet(long[] words) {
        for (long word : words) {
            if (word < 0 || word > 255) {
                throw new IllegalArgumentException();
            }
        }
        this.words = words;
        this.wordsInUse = words.length;
    }

    private void isInRange(int bitIndex) {
        if (bitIndex < 0 || wordIndex(bitIndex) > words.length) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void isInRange(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex < fromIndex || wordIndex(toIndex) > words.length) {
            throw new IndexOutOfBoundsException();
        }
    }

    public void addElement(int bitIndex) {
        isInRange(bitIndex);
        int wordIndex = wordIndex(bitIndex);
        words[wordIndex] |= (1L << bitIndex);
        if (wordIndex >= wordsInUse) {
            wordsInUse = wordIndex + 1;
        }
    }

    public void addMultiplicity(int fromIndex, int toIndex) {
        isInRange(fromIndex, toIndex);
        for (int i = fromIndex; i <= toIndex; i++) {
            addElement(i);
        }
    }

    public void deleteElement(int bitIndex) {
        isInRange(bitIndex);
        int wordIndex = wordIndex(bitIndex);
        words[wordIndex] &= ~(1L << bitIndex);
        recalculateWordsInUse();
    }

    public void deleteMultiplicity(int fromIndex, int toIndex) {
        isInRange(fromIndex, toIndex);
        for (int i = fromIndex; i <= toIndex; i++) {
            deleteElement(i);
        }
    }

    public boolean contains(int bitIndex) {
        if (bitIndex < 0 || wordIndex(bitIndex) > wordsInUse) {
            return false;
        }
        int wordIndex = wordIndex(bitIndex);
        if ((words[wordIndex] & (1L << bitIndex)) != 0) {
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
        /*BitSet additionBitSet = this;
        int lastIndex = BITS_PER_WORD * words.length - 1;
        long firstWordMask = WORD_MASK << 0;
        long lastWordMask = WORD_MASK >>> -lastIndex;
        additionBitSet.words[0] ^= firstWordMask;
        for (long word : additionBitSet.words) {
            word ^= WORD_MASK;
        }
        additionBitSet.words[words.length - 1] ^= lastWordMask;
        return additionBitSet;*/
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

    private void recalculateWordsInUse() {
        int i;
        for (i = wordsInUse - 1; i >= 0; i--)
            if (words[i] != 0)
                break;
        wordsInUse = i + 1;
    }


}
