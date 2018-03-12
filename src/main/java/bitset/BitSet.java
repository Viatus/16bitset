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
    private List<Integer> bits = new ArrayList<Integer>();

    BitSet(List<Integer> elements) {
        for (int element : elements) {
            if (element >= 0) {
                if (!contains(element)) {
                    bits.add(element);
                }
            }
        }
    }

    public int getSize() {
        return bits.size();
    }

    public List<Integer> getBits() {
        return bits;
    }

    public void addElement(int element) {
        if (element >= 0) {
            if (!contains(element)) {
                bits.add(element);
            }
        }
    }

    public void addMultiplicity(List<Integer> elements) {
        for (int element : elements) {
            addElement(element);
        }
    }

    public void deleteElement(int element) {
        if (element >= 0) {
            if (contains(element)) {
                bits.remove((Object) element);
            }
        }
    }

    public void deleteMultiplicity(List<Integer> elements) {
        for (int element : elements) {
            deleteElement(element);
        }
    }

    public boolean contains(int element) {
        if (element >= 0) {
            if (bits.contains(element)) {
                return true;
            }
        }
        return false;
    }

    public BitSet crossing(BitSet other) {
        BitSet crossingBitSet = new BitSet(Collections.<Integer>emptyList());
        for (int elemnt : this.getBits()) {
            if (this.contains(elemnt) && other.contains(elemnt)) {
                crossingBitSet.addElement(elemnt);
            }
        }
        return crossingBitSet;
    }

    public BitSet union(BitSet other) {
        BitSet unionBitSet = new BitSet(Collections.<Integer>emptyList());
        unionBitSet.addMultiplicity(this.getBits());
        unionBitSet.addMultiplicity(other.getBits());
        return unionBitSet;
    }

    public BitSet addition() {
        BitSet addtionBitSet = new BitSet(Collections.<Integer>emptyList());
        int max = 0;
        for (int element : bits) {
            if (element > max) {
                max = element;
            }
        }
        for (; max >= 0; max--) {
            if (!contains(max)) {
                addtionBitSet.addElement(max);
            }
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
        if (this.getSize() == other.getSize()) {
            for (int element : this.getBits()) {
                if (!other.contains(element)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
