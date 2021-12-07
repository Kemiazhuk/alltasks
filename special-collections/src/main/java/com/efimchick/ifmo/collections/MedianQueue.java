package com.efimchick.ifmo.collections;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class MedianQueue implements Queue {
    Queue<Integer> medianQueue = new LinkedList<>();

    @Override
    public int size() {
        return medianQueue.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator iterator() {
        return medianQueue.iterator();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }

    @Override
    public boolean add(Object o) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean addAll(Collection c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection c) {
        return false;
    }

    @Override
    public boolean containsAll(Collection c) {
        return false;
    }

    public int countingNumbers(Integer elem) {
        int min = 0;
        int max = 0;
        for (Integer compareElem : medianQueue) {
            if (elem > compareElem) {
                max++;
            } else if (elem < compareElem) {
                min++;
            }
        }
        return Math.min(max, min);
    }

    @Override
    public boolean offer(Object o) {
        List<Integer> countMedians = new ArrayList<>();
        List<Integer> tempList = new ArrayList<>();
        medianQueue.add((Integer) o);
        tempList.addAll(medianQueue);
        for (Integer elem : medianQueue) {
            countMedians.add(countingNumbers(elem));
        }

        for (int i = 0; i < countMedians.size() - 1; i++) {
            for (int j = i + 1; j < countMedians.size(); j++) {
                if ((countMedians.get(i) < countMedians.get(j)) ||
                        ((countMedians.get(i) == countMedians.get(j)) && (tempList.get(i) > tempList.get(j)))) {
                    int temp = countMedians.get(i);
                    countMedians.set(i, countMedians.get(j));
                    countMedians.set(j, temp);
                    temp = tempList.get(i);
                    tempList.set(i, tempList.get(j));
                    tempList.set(j, temp);
                }
            }
        }
        medianQueue.clear();
        medianQueue.addAll(tempList);
        return true;
    }

    @Override
    public Object remove() {
        return null;
    }

    @Override
    public Object poll() {
        return medianQueue.poll();
    }

    @Override
    public Object element() {
        return null;
    }

    @Override
    public Object peek() {
        return medianQueue.peek();
    }
}
