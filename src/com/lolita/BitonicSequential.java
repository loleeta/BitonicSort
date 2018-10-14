package com.lolita;

import java.util.Arrays;
import java.util.Random;

//sequential sorthing net used as benchmark for parallelization
//does not use any threads or SynchronousQueues
//reports the number of arrays processed per second for a given output array size N
//uses System.currentTimeMillis
public class BitonicSequential {
    public static int n = 1<<26;
    private static Double[] arr;


    public static void main(String[] args){
        System.out.println("Starting BitonicSequential");
        generateNums();
        Long start = System.currentTimeMillis();
        Arrays.sort(arr);
        Long end = System.currentTimeMillis();
        System.out.println("Processed " + n + "-sized array in " + ((end-start)/1000) + " seconds.");
    }

    public static void generateNums() {
        arr = new Double[n];
        Double randNum;
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            randNum = rand.nextDouble();
            arr[i] = randNum;
        }
    }
}