package bitset;


public class BitSet {
    private final static int ADDRESS_BITS_PER_WORD = 6;
    private final static int BITS_PER_WORD = 1 << ADDRESS_BITS_PER_WORD;
    private static final long WORD_MASK = 0xffffffffffffffffL;
    /**
     * Поле количсевто битов
     */
    private int nbits;
    /**
     * Поле массив последовательностей битов, представленных в виде чисел типа long
     */
    private long[] words;
    /**
     * Поле количство последовательностей битов, имеющих хотя бы один ненулевой бит
     */
    private transient int wordsInUse = 0;

    /**
     * Функция получения номера последовательности по номеру бита
     *
     * @param bitIndex - номер бита в общей последовательности битов
     * @return - номер последовательности
     */
    private static int wordIndex(int bitIndex) {
        return bitIndex >> ADDRESS_BITS_PER_WORD;
    }

    /**
     * Функция создания массива последжовательностей битов по их количеству
     *
     * @param nbits - количсетво битов
     */
    private void initWords(int nbits) {
        words = new long[wordIndex(nbits - 1) + 1];
    }

    /**
     * Конструктор - создает BitSet с одной последовательностью битов, все биты которой равны нулю
     */
    public BitSet() {
        initWords(BITS_PER_WORD);
        nbits = BITS_PER_WORD;
    }

    /**
     * Конструктор - создает BitSet с заданным количеством битов, где каждый бит равен нулю
     *
     * @param nbits - количество битов
     */
    public BitSet(int nbits) {
        if (nbits < 0) {
            throw new NegativeArraySizeException();
        }
        this.nbits = nbits;
        initWords(nbits);
    }

    /**
     * Конструктор - создает BitSet из заданных последовательностей битов
     *
     * @param words - последовательности битов
     */
    public BitSet(long[] words) {
        this.words = words;
        this.wordsInUse = words.length;
        nbits = BITS_PER_WORD * (wordsInUse - 1) + (BITS_PER_WORD - Long.numberOfLeadingZeros(words[wordsInUse - 1]));
    }

    /**
     * Функция проверки принадлежности номера бита к последовательности
     *
     * @param bitIndex - номер бита
     * @throws IndexOutOfBoundsException - исключения, оповещающее о том, что данный номер бита не принадлежит последовательности
     */
    private void isInRange(int bitIndex) {
        if (bitIndex < 0 || wordIndex(bitIndex) > words.length) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Функция проверки принадлежности номеров последовательности битов к общей последовательности
     *
     * @param fromIndex - начальный комер последовательности
     * @param toIndex   - конечный номер последовательности
     * @throws IndexOutOfBoundsException - исключения, оповещающее о том, что хотя бы один из данных номеров бита не принадлежит последовательности
     */
    private void isInRange(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex < fromIndex || wordIndex(toIndex) > words.length) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Функция добавления бита к BitSet
     *
     * @param bitIndex - номер бита
     */
    public void addElement(int bitIndex) {
        isInRange(bitIndex);
        int wordIndex = wordIndex(bitIndex);
        words[wordIndex] |= (1L << bitIndex);
        recalculateWordsInUse();
    }

    /**
     * Функция добавления последовательности битов к BitSet
     *
     * @param fromIndex - начальный номер бита
     * @param toIndex   - конечный номер бита
     */
    public void addMultiplicity(int fromIndex, int toIndex) {
        isInRange(fromIndex, toIndex);
        for (int i = fromIndex; i <= toIndex; i++) {
            addElement(i);
        }
    }

    /**
     * Функция удаления бита из BitSet
     *
     * @param bitIndex - номер бита
     */
    public void deleteElement(int bitIndex) {
        isInRange(bitIndex);
        int wordIndex = wordIndex(bitIndex);
        words[wordIndex] &= ~(1L << bitIndex);
        recalculateWordsInUse();
    }

    /**
     * Функция удаления последовательности битов из BitSet
     *
     * @param fromIndex - начальный номер бита
     * @param toIndex   - конечный номер бита
     */
    public void deleteMultiplicity(int fromIndex, int toIndex) {
        isInRange(fromIndex, toIndex);
        for (int i = fromIndex; i <= toIndex; i++) {
            deleteElement(i);
        }
    }

    /**
     * Функция проверки принадлежности бита к BitSet
     *
     * @param bitIndex - номер бита
     */
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

    /**
     * Функция создания пересечения двух BitSet'ов
     *
     * @param other - переданный в виде параметра BitSet
     * @return - BitSet, являющимся пересечением данного и переданного в виде параметра BitSet'ов
     */
    public BitSet crossing(BitSet other) {
        if (this == other) {
            return this;
        }
        int wordsInCommon = Math.min(wordsInUse, other.wordsInUse);
        BitSet mainCrossing;
        BitSet assistingCrossing;
        if (wordsInCommon == wordsInUse) {
            mainCrossing = new BitSet(this.words.clone());
            assistingCrossing = new BitSet(other.words.clone());
        } else {
            mainCrossing = new BitSet(other.words.clone());
            assistingCrossing = new BitSet(this.words.clone());
        }
        for (int i = 0; i < wordsInCommon; i++) {
            mainCrossing.words[i] &= assistingCrossing.words[i];
        }
        return mainCrossing;
    }

    /**
     * Функция создания объединения двух BitSet'ов
     *
     * @param other - переданный в виде параметра BitSet
     * @return - BitSet, являющимся объединением данного и переданного в виде параметра BitSet'ов
     */
    public BitSet union(BitSet other) {
        if (this == other) {
            return this;
        }
        int wordsInCommon = Math.min(wordsInUse, other.wordsInUse);
        BitSet assistingUnion;
        BitSet mainUnion;
        if (wordsInCommon == wordsInUse) {
            assistingUnion = new BitSet(this.words.clone());
            mainUnion = new BitSet(other.words.clone());
        } else {
            assistingUnion = new BitSet(other.words.clone());
            mainUnion = new BitSet(this.words.clone());
        }
        for (int i = 0; i < wordsInCommon; i++) {
            mainUnion.words[i] |= assistingUnion.words[i];
        }
        return mainUnion;
    }

    /**
     * Функция создания дополнения BitSet'а
     *
     * @return - BitSet, являющийся дополнением данного BitSet'а
     */
    public BitSet addition() {
        BitSet additionBitSet = new BitSet(words.clone());
        for (int bitIndex = 0; bitIndex < nbits; bitIndex++) {
            int wordIndex = wordIndex(bitIndex);
            additionBitSet.words[wordIndex] ^= (1L << bitIndex);
        }
        return additionBitSet;
    }

    /**
     * Функция проверки на равенство
     *
     * @param obj - объект, с которым сравнивается данный BitSet
     */
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

    /**
     * Функция перерассчета последовательностей битов, имеющих хотя бы один ненулевой бит
     */
    private void recalculateWordsInUse() {
        int i;
        for (i = words.length - 1; i >= 0; i--)
            if (words[i] != 0)
                break;
        wordsInUse = i + 1;
    }


}
