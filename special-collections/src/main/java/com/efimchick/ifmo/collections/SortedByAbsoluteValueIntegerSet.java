package com.efimchick.ifmo.collections;


import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

class SortedByAbsoluteValueIntegerSet implements SortedSet {
    List<Integer> arrayValues = new LinkedList<>();

    @Override
    public Comparator comparator() {
        return null;
    }

    @Override
    public SortedSet subSet(Object fromElement, Object toElement) {
        return null;
    }

    @Override
    public SortedSet headSet(Object toElement) {
        return null;
    }

    @Override
    public SortedSet tailSet(Object fromElement) {
        return null;
    }

    @Override
    public Object first() {
        return null;
    }

    @Override
    public Object last() {
        return null;
    }

    @Override
    public int size() {
        return arrayValues.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return arrayValues.contains(o);
    }

    @Override
    public Iterator iterator() {
        return arrayValues.iterator();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public boolean add(Object o) {
        for (int i = 0; i < arrayValues.size(); i++) {
            if (Math.abs(arrayValues.get(i)) > Math.abs((Integer) o)) {
                arrayValues.add(i, (Integer) o);
                return true;
            }
            if (i == arrayValues.size() - 1) {
                arrayValues.add((Integer) o);
                return true;
            }
        }
        if (arrayValues.size() == 0) {
            arrayValues.add((Integer) o);
            return true;
        }

        return false;
    }

    @Override
    public boolean remove(Object o) {
        return arrayValues.remove(o);
    }

    @Override
    public boolean addAll(Collection c) {
        for (Object ob : c) {
            add(ob);
        }
        return true;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean removeAll(Collection c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    @Override
    public boolean containsAll(Collection c) {
        return false;
    }

    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }

}
