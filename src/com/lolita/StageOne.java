package com.lolita;

import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.SynchronousQueue;
import java.util.Arrays;

/**
 * StageOne sorts an array of Doubles using a library sort
 */
class StageOne implements Runnable {
    private SortDirection sortDirection;            //Direction to sort array
    private SynchronousQueue<Double[]> input;       //Input queue
    private SynchronousQueue<Double[]> output;      //Output queue

    /**
     * Costructor for StageOne
     * @param sortDirection     //Direction to sort
     * @param input             //Input queue
     * @param output            //Output queue
     */
    public StageOne(SortDirection sortDirection, SynchronousQueue<Double[]> input,
                    SynchronousQueue<Double[]> output) {
        this.sortDirection = sortDirection;
        this.input = input;
        this.output = output;
    }

    /**
     * Given an array and the direction of the thread, sort the array.
     * @param arr   Array of Doubles
     */
    public void sortArrayInDirection(Double[] arr) {
        switch (this.sortDirection) {
            case Ascending:
                sortAsc(arr);
                break;
            case Descending:
                sortDesc(arr);
                break;
        }
    }

    /**
     * Starts the thread by polling array from input, and then sorts array
     * according to direction, and places results in output queue.
     */
    @Override
    public void run() {
        try {
            while (!(Thread.currentThread().isInterrupted())) {
                Double[] arr = input.take();    //read array in
                sortArrayInDirection(arr);
                output.put(arr);                //write to output
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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


}