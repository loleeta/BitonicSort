package com.lolita;

import java.util.Random;
import java.util.concurrent.SynchronousQueue;

class RandomArrayGenerator implements Runnable{
    private static int n;
    private static int [] arr;
    SynchronousQueue<int[]> output;

    public RandomArrayGenerator(int n, SynchronousQueue<int[]> output) {
        this.n = n;
        this.output = output;
    }

    public static void generateNums() {
        arr = new int[n];
        int randNum;
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            randNum = rand.nextInt();
            arr[i] = randNum;
        }
    }

    @Override
    public void run() {
        System.out.println("RandomArrayGenerator: run()");
        try {
            generateNums();
            output.put(arr);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("RandomArrayGenerator: an array is in output queue");
    }
}