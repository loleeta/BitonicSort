package com.lolita;

import java.util.Arrays;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

class BitonicStage implements Runnable {
    private int length;
    private String direction;
    private SynchronousQueue<Double[]> input;
    private SynchronousQueue<Double[]> output;

    public BitonicStage(int length, String w, SynchronousQueue<Double[]> input1, SynchronousQueue<Double[]> output) {
        this.length = length;
        this.direction = w;
        this.input = input1;
        this.output = output;
    }

    @Override
    public void run() {
        System.out.println("BitonicStage: run() ");
        try {
            Double[] arr1 = input.poll(1000, TimeUnit.SECONDS);
            Double[] arr2 = input.poll(1000, TimeUnit.SECONDS);
            Double[] arr = new Double[arr1.length + arr2.length];

            for (int i = 0; i < arr1.length; i++)
                arr[i] = arr1[i];
            for (int i = 0; i < arr2.length; i++)
                arr[i+arr1.length] = arr2[i];
            System.out.println("The array is now " + Arrays.toString(arr));
            sort(arr, 0, arr.length, direction);
            output.put(arr);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sort(Double[] seq, int start, int n, String direction) {
        if (n > 1) {
            bitonic_sequence(seq, start, n);
            bitonic_sort(seq, start, n, direction);
        }
        System.out.println("sort: " + Arrays.toString(seq));
    }

    private void bitonic_sequence(Double[] seq, int start, int n){
        if (n > 1) {
            sort(seq, start, n/2, "UP");
            sort(seq, start+n/2, n/2, "DOWN");
        }
    }

    private void bitonic_sort(Double[] seq, int start, int n, String direction) {
        if (n > 1) {
            bitonic_merge(seq, start, n, direction);
            bitonic_sort(seq, start, n/2, direction);
            bitonic_sort(seq, start+n/2, n/2, direction);
        }
    }

    private void bitonic_merge(Double[] seq, int start, int n, String direction) {
        if (direction.equals("UP")) {
            for (int i = start; i < start+n/2; i++)
                if (seq[i] > seq[i+n/2])
                    swap(seq, i, (i+n/2));
        }
        if (direction.equals("DOWN")){
            for (int i = start; i < start+n/2; i++)
                if (seq[i] < seq[i+n/2])
                    swap(seq, i, (i+n/2));
        }
    }

    public void swap(Double[] arr, int indx1, int indx2) {
        Double temp = arr[indx1];
        arr[indx1] = arr[indx2];
        arr[indx2] = temp;
    }
}