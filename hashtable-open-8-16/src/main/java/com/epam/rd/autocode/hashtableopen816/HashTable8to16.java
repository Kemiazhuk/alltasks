package com.epam.rd.autocode.hashtableopen816;


public class HashTable8to16 implements HashtableOpen8to16 {
    private static final int START_SIZE = 8;
    private static final int FINAL_SIZE = 16;
    private HashNode[] buckets;
    private int numberOfAllBuckets;
    private int numberOfFilledBuckets;

    public HashTable8to16() {
        buckets = new HashNode[START_SIZE];
        this.numberOfAllBuckets = START_SIZE;
        this.numberOfFilledBuckets = 0;
    }

    @Override
    public void insert(int key, Object value) {
        if ((numberOfFilledBuckets == numberOfAllBuckets)
                && (numberOfAllBuckets != FINAL_SIZE)
                && (getFinalBucketIndex(key) == numberOfAllBuckets)) {
            doubleSizeBucket(buckets);
        }
        if ((numberOfFilledBuckets == FINAL_SIZE) && (getFinalBucketIndex(key) == numberOfAllBuckets)) {
            throw new IllegalStateException();
        }
        int tempIndex = getStartBucketIndex(key);
        while (true) {
            HashNode tempNode = buckets[tempIndex];
            if (tempNode == null) {
                buckets[tempIndex] = new HashNode(key, value);
                numberOfFilledBuckets++;
                break;
            }
            if (tempNode.key.equals(key)) {
                tempNode.value = value;
                break;
            }
            tempIndex = (tempIndex + 1) % numberOfAllBuckets;
        }
    }

    @Override
    public void remove(int key) {
        if (numberOfFilledBuckets == 0) {
            return;
        }
        int tempIndex = getStartBucketIndex(key);
        int count = 0;
        while (count != numberOfAllBuckets) {
            if ((buckets[tempIndex] != null) && (buckets[tempIndex].key.equals(key))) {
                buckets[tempIndex] = null;
                numberOfFilledBuckets--;
                break;
            } else {
                tempIndex = (tempIndex + 1) % numberOfAllBuckets;
                count++;
            }
        }
        if ((numberOfAllBuckets >= 2) && ((1.0 * numberOfFilledBuckets) / numberOfAllBuckets == 0.25)) {
            halveSizeBucket(buckets);
        }
    }

    private void doubleSizeBucket(HashNode[] tempBucket) {
        numberOfAllBuckets = numberOfAllBuckets * 2;
        buckets = new HashNode[numberOfAllBuckets];
        numberOfFilledBuckets = 0;
        for (HashNode hashNode : tempBucket) {
            if (hashNode != null) {
                insert(hashNode.key, hashNode.value);
            }
        }
    }

    private void halveSizeBucket(HashNode[] tempBucket) {
        numberOfAllBuckets = numberOfAllBuckets / 2;
        buckets = new HashNode[numberOfAllBuckets];
        numberOfFilledBuckets = 0;
        for (HashNode hashNode : tempBucket) {
            if (hashNode != null) {
                insert(hashNode.key, hashNode.value);
            }
        }
    }

    @Override
    public Object search(int key) {
        int index = getFinalBucketIndex(key);
        if (index == numberOfAllBuckets) {
            return null;
        }
        return buckets[index].value;
    }

    private int getFinalBucketIndex(int key) {
        int index = getStartBucketIndex(key);
        int count = 0;
        while (count != numberOfAllBuckets) {
            if ((buckets[index] != null) && (buckets[index].key == key)) {
                return index;
            }
            index = (index + 1) % numberOfAllBuckets;
            count++;
        }
        return count;
    }

    private int getStartBucketIndex(int key) {
        return Math.abs(key % numberOfAllBuckets);
    }

    @Override
    public int size() {
        return numberOfFilledBuckets;
    }

    @Override
    public int[] keys() {
        int[] allKeys = new int[buckets.length];
        int i = 0;
        for (HashNode node : buckets) {
            if (node == null) {
                allKeys[i++] = 0;
            } else {
                allKeys[i++] = node.key;
            }
        }
        return allKeys;
    }
}
