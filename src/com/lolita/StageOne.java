package com.lolita;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;

enum Direction {
    UP, DOWN;
}

class StageOne implements Runnable {
    private static int size;
    private enum Direction{UP, DOWN};
    private static SynchronousQueue<int[]> input;
    private static SynchronousQueue<int[]> output;
    int [] arr;


    public StageOne (String way, SynchronousQueue<int[]> input, SynchronousQueue<int[]> output) {
        Direction d = Direction.valueOf(way);
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
        System.out.println("StageOne: run()");

        try {
            arr = input.poll(10, TimeUnit.SECONDS);
            System.out.println("Unsorted array: ");
            for (int i: arr)
                System.out.print(i + " ");
            System.out.println();
            Arrays.sort(arr);
            System.out.println("Sorted array: ");
            for (int i: arr)
                System.out.print(i + " ");
            System.out.println();
            output.put(arr);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("StageOne: array is in output queue.");
    }

}