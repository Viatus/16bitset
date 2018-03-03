package bitset;
/*Вариант 16 -- bitset
Реализовать множество над заданным набором объектов. Количество элементов в наборе задается в конструкторе.
Конкретный элемент набора идентифицируется неотрицательным целым от нуля до количества элементов - 1 (альтернатива -- уникальным именем).
    Операции: пересечение, объединение, дополнение; добавление/удаление заданного элемента (массива элементов), проверка принадлежности элемента множеству.
    Бонус: итератор по множеству.*/


import java.util.ArrayList;
import java.util.List;

public class bitset {
    private List<Boolean> bits = new ArrayList<Boolean>();
    private final int bitsetSize;

    private bitset(int size, List<Integer> elements) {
        bitsetSize = size;
        for (int i = 0; i < bitsetSize; i++) {
            boolean isInNumbers = false;
            for (int number : elements) {
                if (i == number) {
                    bits.add(true);
                    isInNumbers = true;
                }
            }
            if (!isInNumbers) {
                bits.add(false);
            }
        }
    }

    public int getSize() {
        return bitsetSize;
    }

    public void addElement(int element) {
        if (element < bitsetSize) {
            bits.set(element, true);
        }
    }

    public void addMultiplicity(List<Integer> elements) {
        for (int element : elements) {
            addElement(element);
        }
    }

    public void deleteElement(int element) {
        if (element < bitsetSize) {
            bits.set(element, false);
        }
    }

    public void deleteMultiplicity(List<Integer> elements) {
        for (int element : elements) {
            deleteElement(element);
        }
    }

    public boolean contains(int element) {
        if (element < bitsetSize) {
            if (bits.get(element)) {
                return true;
            }
        }
        return false;
    }

    public bitset crossing(bitset other) {
        List<Integer> crossingNumbers = new ArrayList<Integer>();
        for (int i = 0; i < bitsetSize; i++) {
            if (this.contains(i) && other.contains(i)) {
                crossingNumbers.add(i);
            }
        }
        return new bitset(crossingNumbers.get(crossingNumbers.size() - 1), crossingNumbers);
    }

    public bitset union(bitset other) {
        List<Integer> crossingNumbers = new ArrayList<Integer>();
        int maxBitsetSize;
        if (bitsetSize > other.getSize()) {
            maxBitsetSize = bitsetSize;
        } else {
            maxBitsetSize = other.getSize();
        }
        for (int i = 0; i < maxBitsetSize; i++) {
            if (this.contains(i) || other.contains(i)) {
                crossingNumbers.add(i);
            }
        }
        return new bitset(crossingNumbers.get(crossingNumbers.size() - 1), crossingNumbers);
    }

    public bitset addition() {
        List<Integer> crossingNumbers = new ArrayList<Integer>();
        for (int i = 0; i < bitsetSize; i++) {
            if (!bits.get(i)) {
                crossingNumbers.add(i);
            }
        }
        return new bitset(crossingNumbers.get(crossingNumbers.size() - 1), crossingNumbers);

    }
}
