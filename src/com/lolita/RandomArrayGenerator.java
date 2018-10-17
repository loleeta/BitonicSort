package com.lolita;

import java.util.Random;
import java.util.concurrent.SynchronousQueue;

/**
 * Given a size, RandomArrayGenerator will generate random Doubles and place
 * them into an array for consumption in the SynchronousQueue.
 */
class RandomArrayGenerator implements Runnable{
    private int length;                         //Number of Doubles to make
    private SynchronousQueue<Double[]> output;  //Output queue
    private Random rand;                        //Instance of Random

    /**
     * Constructor for RandomArrayGenerator
     * @param length        //Number of doubles to make
     * @param output        //Output queue
     * @param randomSeed    //Seed to generate unique sequences of numbers
     */
    public RandomArrayGenerator(int length, SynchronousQueue<Double[]> output, long randomSeed) {
        this.length = length;
        this.output = output;
        this.rand = new Random(randomSeed);
    }

    /**
     * Given a count, generate Doubles and add them to array
     * @param count     Number of Doubles to generate
     * @return          Array of doubles
     */
    private Double[] getGeneratedNumbers(int count) {
        Double [] arr = new Double[count];
        for (int i = 0; i < count; i++) {
            arr[i] = this.rand.nextDouble();
        }
        return arr;
    }

    /**
     * Starts thread by creating arrays of pseudorandom numbers and placing
     * them in output queue for consumption until interrupted.
     */
    @Override
    public void run() {
        while(!(Thread.currentThread().isInterrupted())) {
            try {
                output.put(getGeneratedNumbers(this.length));
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}