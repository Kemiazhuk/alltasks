package com.efimchick.ifmo.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

class PairStringList extends ArrayList<String> {
    private List<String> listPairs = new ArrayList<>();

    public int size() {
        return listPairs.size();
    }

    public Iterator<String> iterator() {
        return listPairs.iterator();
    }

    public boolean add(String o) {
        listPairs.add(o);
        listPairs.add(o);
        return true;
    }

    public boolean remove(Object o) {
        int index = indexOf((String) o);
        if (index == -1) {
            return false;
        }
        index = indexOf((String) o);
        listPairs.remove(o);
        if (index == -1) {
            return false;
        }
        listPairs.remove(o);
        return true;
    }

    public boolean addAll(Collection<? extends String> c) {
        for (String str : c) {
            add(str);
        }
        return true;
    }

    public boolean addAll(int index, Collection<? extends String> c) {
        if (index >= listPairs.size()) {
            return false;
        }
        for (Object ob : c) {
            add(index, ob.toString());
            index += 2;
        }
        return true;
    }

    public void add(int index, String element) {
        if (index % 2 != 0) {
            index++;
        }
        listPairs.add(index++, element);
        listPairs.add(index, element);
    }

    public void clear() {
        listPairs.clear();
    }

    public String get(int index) {
        return listPairs.get(index);
    }

    public String set(int index, String element) {
        if (index % 2 != 0) {
            index--;
        }
        listPairs.set(index++, element);
        listPairs.set(index, element);
        return element;
    }

    public String remove(int index) {
        try {
            String temp = listPairs.get(index);
            remove(temp);
            return temp;
        } catch (IndexOutOfBoundsException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public int indexOf(String o) {
        return indexOfRange(o, 0, listPairs.size());
    }

    private int indexOfRange(String o, int start, int end) {
        if (o == null) {
            for (int i = start; i < end; i++) {
                if (listPairs.get(i) == null) {
                    return i;
                }
            }
        } else {
            for (int i = start; i < end; i++) {
                if (o.equals(listPairs.get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }
}
