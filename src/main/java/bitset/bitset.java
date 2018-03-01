package bitset;
/*Вариант 16 -- bitset
Реализовать множество над заданным набором объектов. Количество элементов в наборе задается в конструкторе.
Конкретный элемент набора идентифицируется неотрицательным целым от нуля до количества элементов - 1 (альтернатива -- уникальным именем).
    Операции: пересечение, объединение, дополнение; добавление/удаление заданного элемента (массива элементов), проверка принадлежности элемента множеству.
    Бонус: итератор по множеству.*/


import java.util.ArrayList;
import java.util.List;

public class bitset {
    private List<Object> multiplicity = new ArrayList<Object>();

    private bitset(Object... objects) {
        for (Object obj : objects) {
            if (!contains(obj)) {
                multiplicity.add(obj);
            }
        }
    }

    public Object getElement(int index) {
        return multiplicity.get(index);
    }

    public List<Object> getMultiplicity() {
        return multiplicity;
    }

    public void addElement(Object element) {
        if (!contains(element)) {
            multiplicity.add(element);
        }
    }

    public void addMultiplicity(Object... elements) {
        for (Object obj : elements) {
            if (!contains(obj)) {
                multiplicity.add(obj);
            }
        }
    }

    public void deleteElement(Object element) {
        List<Object> newMultiplicity = new ArrayList<Object>();
        for (Object obj : multiplicity) {
            if (!element.equals(obj)) {
                newMultiplicity.add(obj);
            }
        }
        multiplicity = newMultiplicity;
    }

    public void deleteMultiplicity(Object... elements) {
        List<Object> newMultiplicity = new ArrayList<Object>();
        for (Object obj : multiplicity) {
            boolean isContain = false;
            for (Object element : elements) {
                if (element.equals(obj)) {
                    isContain = true;
                }
            }
            if (!isContain) {
                newMultiplicity.add(obj);
            }
        }
        multiplicity = newMultiplicity;
    }

    public boolean contains(Object element) {
        for (Object obj : multiplicity) {
            if (element.equals(obj)) {
                return true;
            }
        }
        return false;
    }

    public bitset crossing(bitset other) {
        bitset crossingBitset = new bitset();
        for (Object element : other.getMultiplicity()) {
            if (this.contains(element)) {
                crossingBitset.addElement(element);
            }
        }
        return crossingBitset;
    }

    public bitset union(bitset other) {
        bitset unionBitsets = new bitset(multiplicity);
        unionBitsets.addMultiplicity(other.getMultiplicity());
        return unionBitsets;
    }

    public bitset addition(bitset other) {
        bitset additionBitset = new bitset();
        for (Object obj : other.getMultiplicity()) {
            if (!contains(obj)) {
                additionBitset.addElement(obj);
            }
        }
        return additionBitset;
    }
}
