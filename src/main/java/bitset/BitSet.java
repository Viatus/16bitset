package bitset;


public class BitSet {
    private final static int ADDRESS_BITS_PER_WORD = 6;
    private final static int BITS_PER_WORD = 1 << ADDRESS_BITS_PER_WORD;

    /**
     * Поле количсевто битов
     */
    private int nbits;
    /**
     * Поле массив последовательностей битов, представленных в виде чисел типа long
     */
    private long[] words;

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
     * Функция создания массива последовательностей битов по их количеству
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
        if (nbits <= 0) {
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
        this.words = words.clone();
        nbits = BITS_PER_WORD * (words.length - 1) + (BITS_PER_WORD - Long.numberOfLeadingZeros(words[words.length - 1]));
    }

    /**
     * Функция проверки принадлежности номера бита к последовательности
     *
     * @param bitIndex - номер бита
     * @throws IndexOutOfBoundsException - исключения, оповещающее о том, что данный номер бита не принадлежит последовательности
     */
    private boolean isInRange(int bitIndex) {
        return bitIndex >= 0 && wordIndex(bitIndex) < words.length;
    }

    /**
     * Функция проверки принадлежности номеров последовательности битов к общей последовательности
     *
     * @param fromIndex - начальный комер последовательности
     * @param toIndex   - конечный номер последовательности
     * @throws IndexOutOfBoundsException - исключения, оповещающее о том, что хотя бы один из данных номеров бита не принадлежит последовательности
     */
    private boolean isInRange(int fromIndex, int toIndex) {
        return fromIndex >= 0 && toIndex > fromIndex && wordIndex(toIndex) < words.length;
    }

    /**
     * Функция добавления бита к BitSet
     *
     * @param bitIndex - номер бита
     * @return - возвращает true, если бит установлен и false, если бит находится вне массива или уже имеет значение 1
     */
    public boolean addElement(int bitIndex) {
        if (!isInRange(bitIndex) || contains(bitIndex)) {
            return false;
        }
        int wordIndex = wordIndex(bitIndex);
        words[wordIndex] |= (1L << bitIndex);
        return true;
    }

    /**
     * Функция добавления последовательности битов к BitSet
     *
     * @param fromIndex - начальный номер бита
     * @param toIndex   - конечный номер бита
     */
    public boolean addMultiplicity(int fromIndex, int toIndex) {
        if (!isInRange(fromIndex, toIndex)) {
            return false;
        }
        for (int i = fromIndex; i <= toIndex; i++) {
            addElement(i);
        }
        return true;
    }

    /**
     * Функция удаления бита из BitSet
     *
     * @param bitIndex - номер бита
     * @return - возвращает true, если бит удале и false, если бит находится вне массива или уже имеет значение 0
     */
    public boolean deleteElement(int bitIndex) {
        if (!isInRange(bitIndex) || !contains(bitIndex)) {
            return false;
        }
        words[wordIndex(bitIndex)] &= ~(1L << bitIndex);
        return true;
    }

    /**
     * Функция удаления последовательности битов из BitSet
     *
     * @param fromIndex - начальный номер бита
     * @param toIndex   - конечный номер бита
     */
    public boolean deleteMultiplicity(int fromIndex, int toIndex) {
        if (!isInRange(fromIndex, toIndex)) {
            return false;
        }
        for (int i = fromIndex; i <= toIndex; i++) {
            deleteElement(i);
        }
        return true;
    }

    /**
     * Функция проверки принадлежности бита к BitSet
     *
     * @param bitIndex - номер бита
     */
    public boolean contains(int bitIndex) {
        if (!isInRange(bitIndex)) {
            return false;
        }
        return (words[wordIndex(bitIndex)] & (1L << bitIndex)) != 0;
    }

    /**
     * Функция создания пересечения или объединения двух BitSet'ов
     *
     * @param first      - первый BitSet
     * @param second     - второй BitSet
     * @param isCrossing - переменная, отвечающая за выбор действий над BitSet
     * @return - BitSet, являющимся пересечением или объединением данного и переданного в виде параметра BitSet'ов
     */
    private BitSet unionAndCrossingAssist(BitSet first, BitSet second, boolean isCrossing) {
        if (first == second) {
            return first;
        }
        int wordsInCommon = Math.min(first.words.length, second.words.length);
        BitSet main;
        BitSet assist;
        if (wordsInCommon == words.length) {
            if (isCrossing) {
                main = new BitSet(first.words);
                assist = new BitSet(second.words);
            } else {
                main = new BitSet(second.words);
                assist = new BitSet(first.words);
            }
        } else {
            if (isCrossing) {
                main = new BitSet(second.words);
                assist = new BitSet(first.words);
            } else {
                main = new BitSet(first.words);
                assist = new BitSet(second.words);
            }
        }
        for (int i = 0; i < wordsInCommon; i++) {
            if (isCrossing) {
                main.words[i] &= assist.words[i];
            } else {
                main.words[i] |= assist.words[i];
            }
        }
        return main;
    }

    /**
     * Функция создания пересечения двух BitSet'ов
     *
     * @param other - переданный в виде параметра BitSet
     * @return - BitSet, являющимся пересечением данного и переданного в виде параметра BitSet'ов
     */
    public BitSet crossing(BitSet other) {
        return unionAndCrossingAssist(this, other, true);
    }

    /**
     * Функция создания объединения двух BitSet'ов
     *
     * @param other - переданный в виде параметра BitSet
     * @return - BitSet, являющимся объединением данного и переданного в виде параметра BitSet'ов
     */
    public BitSet union(BitSet other) {
        return unionAndCrossingAssist(this, other, false);
    }

    /**
     * Функция создания дополнения BitSet'а
     *
     * @return - BitSet, являющийся дополнением данного BitSet'а
     */
    public BitSet addition() {
        BitSet addition = new BitSet(words.clone());
        for (int bitIndex = 0; bitIndex < nbits; bitIndex++) {
            int wordIndex = wordIndex(bitIndex);
            addition.words[wordIndex] ^= (1L << bitIndex);
        }
        return addition;
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
        if (this.words.length != other.words.length) {
            return false;
        }
        for (int i = 0; i < words.length; i++) {
            if (words[i] != other.words[i]) {
                return false;
            }
        }
        return true;
    }

}
