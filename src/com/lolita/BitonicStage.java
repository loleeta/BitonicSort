package com.lolita;

import java.util.Arrays;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * BitonicStage sorts an array using bitonic sort.
 */
public class BitonicStage implements Runnable {
    private SortDirection direction;                        //direction of sort
    private SynchronousQueue<Double[]> inputAsc;            //input queue
    private SynchronousQueue<Double[]> inputDesc;           //input queue
    private SynchronousQueue<Double[]> output;              //output queue

    /**
     * Constructor for BitonicStage.
     * @param direction     the direction of the sort
     * @param inputAsc      SynchronousQueue for incoming ascending arrays
     * @param inputDesc     SynchronousQueue for incoming descending arrays
     * @param output        SynchronousQueue for sorted output
     */
    public BitonicStage(SortDirection direction, SynchronousQueue<Double[]> inputAsc,
                        SynchronousQueue<Double[]> inputDesc, SynchronousQueue<Double[]> output) {
        this.direction = direction;
        this.inputAsc = inputAsc;
        this.inputDesc = inputDesc;
        this.output = output;
    }

    /**
     * Starts the thread by polling from input queues and sorting the array
     * and putting the array into the output queue until interrupted.
     */
    @Override
    public void run() {
        try {
            while (!(Thread.currentThread().isInterrupted())) {

                Double[] arrAsc = inputAsc.take();
                Double[] arrDesc = inputDesc.take();

                int inputLen = arrAsc.length;
                Double[] outArr = new Double[2 * inputLen];

                //copy the arrays into a larger array
                System.arraycopy(arrAsc, 0, outArr, 0, inputLen);
                System.arraycopy(arrDesc, 0, outArr, inputLen, inputLen);

                bitonic_sort(outArr, 0, outArr.length, direction);
                output.put(outArr);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Given a bitonic sequence, recursively sort the left and right halves.
     * @param seq           Array of doubles
     * @param start         Where to start
     * @param n             Size of sequence to start
     * @param direction     Which direction to sort in
     */
    private void bitonic_sort(Double[] seq, int start, int n, SortDirection direction) {
        if (n > 1) {
            bitonic_merge(seq, start, n, direction);
            bitonic_sort(seq, start, n / 2, direction);
            bitonic_sort(seq, start + n / 2, n / 2, direction);
        }
    }

    /**
     * Create two bitonic sequences out of one by comparing each item to its
     * halfway point in the array.
     * @param seq           Array of doubles
     * @param start         Where to start
     * @param n             Size of sequence to start
     * @param direction     Which direction to sort in
     */
    private void bitonic_merge(Double[] seq, int start, int n, SortDirection direction) {
        if (direction == SortDirection.Ascending) {
            for (int i = start; i < start + n / 2; i++)
                if (seq[i] > seq[i + n / 2])
                    swap(seq, i, (i + n / 2));
        } else {
            for (int i = start; i < start + n / 2; i++)
                if (seq[i] < seq[i + n / 2])
                    swap(seq, i, (i + n / 2));
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
}