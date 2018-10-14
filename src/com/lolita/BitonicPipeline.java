package com.lolita;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;


public class BitonicPipeline implements Runnable{
    public static final int SIZE = 1<<4;
    public static Double[] arr;
    public static SynchronousQueue<Double[]> bpInput;
    public static SynchronousQueue<Double[]> stageOneInput = new SynchronousQueue<>(); //used by StageOne, threads 1-4
    public static SynchronousQueue<Double[]> stageTwoAsc = new SynchronousQueue<>(); //used by BitonicStage, threads 5-7
    public static SynchronousQueue<Double[]> stageTwoDesc = new SynchronousQueue<>();
    public static SynchronousQueue<Double[]> stageTwoOutput = new SynchronousQueue<>();
    public static SynchronousQueue<Double[]> finalOutput = new SynchronousQueue<>();

    public BitonicPipeline(SynchronousQueue<Double[]> input) {
        this.bpInput = input;
    }

    public static void main(String[] args) {


        RandomArrayGenerator arrayGen = new RandomArrayGenerator(SIZE/4, stageOneInput, 1);

        StageOne ascThread1 = new StageOne(SIZE/4, "UP", stageOneInput, stageTwoAsc);
        StageOne descThread2 = new StageOne(SIZE/4, "DOWN", stageOneInput, stageTwoDesc);
        StageOne ascThread3 = new StageOne(SIZE/4, "UP", stageOneInput, stageTwoAsc);
        StageOne descThread4 = new StageOne(SIZE/4, "DOWN", stageOneInput, stageTwoDesc);
        BitonicStage bitonicThread5 = new BitonicStage(SIZE/2, "UP", stageTwoAsc,stageTwoOutput);
        BitonicStage bitonicThread6 = new BitonicStage(SIZE/2, "DOWN", stageTwoDesc, stageTwoOutput);
        BitonicStage bitonicThread7 = new BitonicStage(SIZE, "UP", stageTwoOutput, finalOutput);

        BitonicPipeline bp = new BitonicPipeline(finalOutput);
        Thread mainThread = new Thread(bp);
        // Four threads for randomArrayGenerator, all of which write to input1
        Thread arrayGen1 = getArrayGeneratorThreads(System.nanoTime());
        Thread arrayGen2 = getArrayGeneratorThreads(System.nanoTime());
        Thread arrayGen3 = getArrayGeneratorThreads(System.nanoTime());
        Thread arrayGen4 = getArrayGeneratorThreads(System.nanoTime());
        // Four threads to do initial processing of array, listening on input1 and writing to output
        Thread thread1 = new Thread(ascThread1);
        Thread thread2 = new Thread(descThread2);
        Thread thread3 = new Thread(ascThread3);
        Thread thread4 = new Thread(descThread4);
        // Three threads to do last of bitonic sort; listening on input1-2 and writing to output
        Thread thread5 = new Thread(bitonicThread5);
        Thread thread6 = new Thread(bitonicThread6);
        Thread thread7 = new Thread(bitonicThread7);

        mainThread.start();
        arrayGen1.start();
        arrayGen2.start();
        arrayGen3.start();
        arrayGen4.start();
        Long start = System.currentTimeMillis();
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();

        try {
            arrayGen1.join();
            arrayGen2.join();
            arrayGen3.join();
            arrayGen4.join();

            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
            thread6.join();
            thread7.join();
            mainThread.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }


        Long end = System.currentTimeMillis();
        System.out.println("Processed " + SIZE + "-sized array in " + ((end-start)/1000) + " seconds.");
    }

    @Override
    public void run() {
        System.out.println("BitonicPipeline: run()");
        try {
            this.arr = bpInput.poll(100, TimeUnit.SECONDS);
            System.out.println("BitonicPipeline received: " + Arrays.toString(this.arr));
            for (int i = 0; i < SIZE - 1; i++) {
                if (arr[i] > arr[i + 1]) {
                    System.out.println("Sorting did not work.");
                    System.out.println(arr[i] + " > " + arr[i + 1]);
                    break;
                }
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Thread getArrayGeneratorThreads(long seed) {
        RandomArrayGenerator arrayGen = new RandomArrayGenerator(SIZE/4, stageOneInput, seed);
        return new Thread(arrayGen);
    }

}