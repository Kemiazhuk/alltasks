package com.epam.rd.autocode.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

class Iterators {

    public static Iterator<Integer> intArrayTwoTimesIterator(int[] array) {
        return new Iterator<Integer>() {
            int count = 0;
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public Integer next() {
                if ((array.length == 0) || (index > array.length - 1)) {
                    throw new NoSuchElementException();
                }
                count++;
                if (count < 2) {
                    return array[index];
                } else {
                    count = 0;
                    return array[index++];
                }
            }
        };
    }

    public static Iterator<Integer> intArrayThreeTimesIterator(int[] array) {
        return new Iterator<Integer>() {
            int count = 0;
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public Integer next() {
                if ((array.length == 0) || (index > array.length - 1)) {
                    throw new NoSuchElementException();
                }
                count++;
                if (count < 3) {
                    return array[index];
                } else {
                    count = 0;
                    return array[index++];
                }
            }
        };
    }

    public static Iterator<Integer> intArrayFiveTimesIterator(int[] array) {
        return new Iterator<Integer>() {
            int count = 0;
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public Integer next() {
                if ((array.length == 0) || (index > array.length - 1)) {
                    throw new NoSuchElementException();
                }
                count++;
                if (count < 5) {
                    return array[index];
                } else {
                    count = 0;
                    return array[index++];
                }
            }
        };
    }

    public static Iterable<String> table(String[] columns, int[] rows) {
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    int indexString = 0;
                    int indexInt = 0;

                    @Override
                    public boolean hasNext() {
                        return indexString < columns.length;
                    }

                    @Override
                    public String next() {

                        if ((columns.length == 0) || (indexString > columns.length - 1)) {
                            throw new NoSuchElementException();
                        }

                        if (indexInt == rows.length - 1) {
                            indexString++;
                            return columns[indexString - 1] + rows[indexInt++];
                        }
                        if (indexInt == rows.length) {
                            indexInt = 0;
                        }
                        return columns[indexString] + rows[indexInt++];

                    }
                };
            }
        };
    }
}
