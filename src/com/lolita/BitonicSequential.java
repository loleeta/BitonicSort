package com.lolita;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * BitonicSequential is a sequential sorting net using bitonic sort. This is
 * used as a benchmark for performance when compared with parallelized bitonic
 * sort.
 */
public class BitonicSequential {
    private int size;           //Size of arrays
    public int sortedCount;     //Number of sorted so far

    /**
     * Constructor
     * @param size      Size of arrays to be created and sorted
     */
    public BitonicSequential(int size) {
        this.size = size;
        sortedCount = 0;
    }

    /**
     * Generates and sorts arrays given a duration of time
     * @param durationMillis        Time of activity
     * @return                      Count of arrays sorted
     */
    public int sortArrays(int durationMillis) {
        Long start = System.currentTimeMillis();
        Long endTime = start + durationMillis;
        int sortedCount = 0;
        while (System.currentTimeMillis() < endTime) {
            //Generate 4 quarter arrays
            Double [] arr1 = generateDoubleArrays(System.nanoTime());
            Double [] arr2 = generateDoubleArrays(System.nanoTime());
            Double [] arr3 = generateDoubleArrays(System.nanoTime());
            Double [] arr4 = generateDoubleArrays(System.nanoTime());
            //Call library sort on them depending on direction
            sortAsc(arr1);
            sortDesc(arr2);
            sortAsc(arr3);
            sortDesc(arr4);
            //Combine arrays into two halves and bitonic sort
            Double[] bitonicSeq1 = new Double[this.size/2];
            Double[] bitonicSeq2 = new Double[this.size/2];
            System.arraycopy(arr1, 0, bitonicSeq1, 0, this.size/4);
            System.arraycopy(arr2, 0, bitonicSeq1, this.size/4, this.size/4);
            System.arraycopy(arr3, 0, bitonicSeq2, 0, this.size/4);
            System.arraycopy(arr4, 0, bitonicSeq2, this.size/4, this.size/4);
            bitonic_sort(bitonicSeq1, 0, this.size/2, SortDirection.Ascending);
            bitonic_sort(bitonicSeq2, 0, this.size/2, SortDirection.Descending);
            //Combine arrays into two halves and bitonic sort
            Double[] bitonicSeq3 = new Double[this.size];
            System.arraycopy(bitonicSeq1, 0, bitonicSeq3, 0, this.size/2);
            System.arraycopy(bitonicSeq2, 0, bitonicSeq3, this.size/2, this.size/2);
            bitonic_sort(bitonicSeq3, 0, this.size, SortDirection.Ascending);
            if(isSorted(bitonicSeq3))
                sortedCount++;
        }
        return sortedCount;

    }

    /**
     * Given a count, generate Doubles and add them to array
     * @param seed     Seed to generate unique sequences of numbers
     */
    private Double[] generateDoubleArrays(long seed) {
        Double [] arr = new Double[this.size/4];
        Random rand = new Random(seed);
        for (int i = 0; i < this.size/4; i++) {
            Double randNum = rand.nextDouble();
            arr[i] = randNum;
        }
        return arr;
    }

    /**
     * Given an array, sort in ascending order.
     * @param arr   Array of Doubles
     */
    public void sortAsc(Double[] arr) {
        Arrays.sort(arr);
    }

    /**
     * Given an array, sort in descending order.
     * @param arr   Array of Doubles
     */
    public void sortDesc(Double[] arr) {
        Comparator<Double> cr = Collections.reverseOrder();
        Arrays.sort(arr, cr);
    }

    /**
     * Given a bitonic sequence, recursively sort the left and right halves.
     * @param seq           Array of doubles
     * @param start         Where to start
     * @param size          Size of sequence to start
     * @param direction     Which direction to sort in
     */
    private void bitonic_sort(Double[] seq, int start, int size, SortDirection direction) {
        if (size > 1) {
            bitonic_merge(seq, start, size, direction);
            bitonic_sort(seq, start, size / 2, direction);
            bitonic_sort(seq, start + (size / 2), size / 2, direction);
        }
    }

    /**
     * Create two bitonic sequences out of one by comparing each item to its
     * halfway point in the array.
     * @param seq           Array of doubles
     * @param start         Where to start
     * @param size          Size of sequence to start
     * @param direction     Which direction to sort in
     */
    private void bitonic_merge(Double[] seq, int start, int size, SortDirection direction) {
        if (direction == SortDirection.Ascending) {
            for (int i = start; i < start + size / 2; i++)
                if (seq[i] > seq[i + (size / 2)])
                    swap(seq, i, (i + (size / 2)));
        } else {
            for (int i = start; i < start + size / 2; i++)
                if (seq[i] < seq[i + (size / 2)])
                    swap(seq, i, (i + (size / 2)));
        }
    }

    /**
     * Given an array and two indices, swap the items.
     * @param arr       Array of doubles
     * @param indx1     Index 1
     * @param indx2     Index 2
     */
    private void swap(Double[] arr, int indx1, int indx2) {
        Double temp = arr[indx1];
        arr[indx1] = arr[indx2];
        arr[indx2] = temp;
    }

    /**
     * Given an array, check if it's sorted in ascending order
     * @param arr
     * @return
     */
    private boolean isSorted(Double[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] > arr[i]) {
                System.out.printf("arr[%d] > arr[%d]: %f, %f", i - 1, (i), arr[i - 1], arr[i]);
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("Starting BitonicSequential sort.");
        int N = 1<<22;
        int duration = 10000; //ten seconds
        BitonicSequential bitonicSequential = new BitonicSequential(N);
        int sortedCount = bitonicSequential.sortArrays(duration);
        System.out.println("Processed " + sortedCount + " " + N + "-sized arrays in 10 seconds");
    }
}