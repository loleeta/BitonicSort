package com.lolita;

import java.util.Random;
import java.util.concurrent.SynchronousQueue;

class RandomArrayGenerator implements Runnable{
    private int length;
    private SynchronousQueue<Double[]> output;
    private Random rand;

    public RandomArrayGenerator(int length, SynchronousQueue<Double[]> output, long randomSeed) {
        this.length = length;
        this.output = output;
        this.rand = new Random(randomSeed);
    }

    private Double[] getGeneratedNumbers(int count) {
        Double [] arr = new Double[count];
        for (int i = 0; i < count; i++) {
            arr[i] = this.rand.nextDouble();
        }
        return arr;
    }

    @Override
    public void run() {
        //System.out.println("RandomArrayGenerator: run()");
        try {
            output.put(getGeneratedNumbers(this.length));
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println("RandomArrayGenerator: an array is in output queue");
    }
}