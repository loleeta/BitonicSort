package com.lolita;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//sequential sorthing net used as benchmark for parallelization
//does not use any threads or SynchronousQueues
//reports the number of arrays processed per second for a given output array size N
//uses System.currentTimeMillis
public class BitonicSequential implements Runnable{
    private int size;
    private Double[] arr;
    public int sortedCount;

    public BitonicSequential(int size) {
        this.size = size;
        this.arr = new Double[size];
        sortedCount = 0;
    }

    public void run() {
        Long start = System.currentTimeMillis(); //fetch starting time
        while((System.currentTimeMillis()-start) < 10000) {
            generateNums(System.nanoTime());
            sort(arr, 0, size, "UP");
            checkSort();
        }
    }

    private void sort(Double[] seq, int start, int size, String direction) {
        if (size > 1) {
            bitonic_sequence(seq, start, size);
            bitonic_sort(seq, start, size, direction);
        }
    }

    private void bitonic_sequence(Double[] seq, int start, int size){
        if (size > 1) {
            sort(seq, start, size/2, "UP");
            sort(seq, start+(size/2), size/2, "DOWN");
        }
    }

    private void bitonic_sort(Double[] seq, int start, int size, String direction) {
        if (size > 1) {
            bitonic_merge(seq, start, size, direction);
            bitonic_sort(seq, start, size/2, direction);
            bitonic_sort(seq, start+(size/2), size/2, direction);
        }
    }

    private void bitonic_merge(Double[] seq, int start, int size, String direction) {
        if (direction.equals("UP")) {
            for (int i = start; i < start+size/2; i++)
                if (seq[i] > seq[i + (size/2)])
                    swap(seq, i, (i + (size/2)));
        }
        else {
            for (int i = start; i < start+size/2; i++)
                if (seq[i] < seq[i + (size/2)])
                    swap(seq, i, (i + (size/2)));
        }
    }

    private void swap(Double[] arr, int indx1, int indx2) {
        Double temp = arr[indx1];
        arr[indx1] = arr[indx2];
        arr[indx2] = temp;
    }

    private void generateNums(long seed) {
        Double randNum;
        Random rand = new Random(seed);
        for (int i = 0; i < size; i++) {
            randNum = rand.nextDouble();
            arr[i] = randNum;
        }
    }

    private void checkSort() {
        for (int i = 0; i < size-1; i++) {
            if (arr[i] > arr[i + 1]) {
                System.out.println("Sorting did not work.");
                System.out.printf("arr[%d] > arr[%d]: %f, %f", i, (i + 1), arr[i], arr[i + 1]);
                System.out.println(arr[i] + " at " + i + " > " + arr[i + 1]);
                break;
            }
        }
        sortedCount++;
    }

    public int getCount() {
        return this.sortedCount;
    }

    public static void main(String[] args){
        System.out.println("Starting BitonicSequential sort.");
        int N = 1<<20;
        BitonicSequential bitonicSequential = new BitonicSequential(N);
        bitonicSequential.run(); //maybe use ExecutorService instead of the time loop in run()?
        System.out.println("Processed " + bitonicSequential.sortedCount + " " + N + "-sized arrays in 10 seconds");
    }
}