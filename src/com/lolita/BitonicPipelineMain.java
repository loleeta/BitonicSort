package com.lolita;

import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;

/**
 * BitonicPipelineMain programs implements a sorting pipeline using bitonic sort.
 * It uses 7 threads to parallelize the work done in each phase of the sort.
 * @author Lolita Lam
 */
public class BitonicPipelineMain {
    private static final int SIZE = 1<<19;         //total size of array


    /**
     * Creates RandomArrayGenerator objects and threads
     * @param fourthSize        1/4 the size of the total array
     * @param randNumQueues     SynchronousQueues for the double arrays
     * @return                  array of 4 RandomArrayGenerator threads
     */
    public static ArrayList<Thread> startArrayGeneratorTheads(int fourthSize,
                          ArrayList<SynchronousQueue<Double[]>> randNumQueues) {
        ArrayList<Thread> numGeneratorThreads = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            RandomArrayGenerator arrayGen = new RandomArrayGenerator(fourthSize,
                    randNumQueues.get(i), System.nanoTime());
            Thread arrayGenThread = new Thread(arrayGen);
            arrayGenThread.start();
            numGeneratorThreads.add(arrayGenThread);
        }
        return numGeneratorThreads;
    }

    /**
     * Main method which calls the creation of all threads from BitonicPipeline
     */
    public static void main(String[] args) {
        System.out.println("Starting BitonicPipeline");
        int inputSize = SIZE / 4;

        //create array of SynchronousQueues for number generator
        ArrayList<SynchronousQueue<Double[]>> randNumQueues =
                                                        new ArrayList<>(4);
        for (int i = 0; i < 4; i++)
            randNumQueues.add(new SynchronousQueue<Double[]>());

        SynchronousQueue<Double[]> outputQueue = new SynchronousQueue<>();

        //create BitonicPipeline
        BitonicPipeline bitonicPipeline = new BitonicPipeline(randNumQueues,
                                                              outputQueue,
                                                              inputSize);

        //start and return array generator threads
        ArrayList<Thread> generatorTheads = startArrayGeneratorTheads(inputSize,
                                                                      randNumQueues);
        //start and return threads1-7 for sorting
        ArrayList<Thread> bitonicTheads = bitonicPipeline.createAndStartThreads();

        //check whether arrays are sorted in time frame
        int sortedArrayCount = monitorOutputQueue(outputQueue, 10000);

        System.out.println("Processed " + sortedArrayCount + " " + SIZE +
                                                    "-sized arrays in 10 seconds");

        //clean up threads and exit
        cleanup(generatorTheads, bitonicTheads, 5000);
        System.exit(0);
    }

    /**
     * Listens for sorted arrays in the output queue for the given duration.
     *
     * @param outputQueue    listen for arrays.
     * @param durationMillis
     * @return Number of sorted arrays received in the given duration.
     */
    private static int monitorOutputQueue(SynchronousQueue<Double[]> outputQueue,
                                          int durationMillis) {
        Long start = System.currentTimeMillis(); //fetch starting time
        Long endTime = start + durationMillis;
        int sortedCount = 0;
        while (System.currentTimeMillis() < endTime) {
            try {
                Double[] arr = outputQueue.take();
                if (isSorted(arr)) {
                    sortedCount++;
                } else {
                    System.out.println("RECEIVED UNSORTED ARRAY, EXITING EARLY");
                    return -1;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("CRITICAL ERROR, EXITING EARLY");
                return -1;
            }
        }
        return sortedCount;
    }

    /**
     * Accepts an array and checks if it's sorted in ascending order
     * @param arr   array of Double
     * @return      true if sorted, false otherwise
     */
    private static boolean isSorted(Double[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] > arr[i]) {
                System.out.printf("arr[%d] > arr[%d]: %f, %f", i - 1, (i), arr[i - 1], arr[i]);
                return false;
            }
        }
        return true;
    }

    /**
     * Joins all threads after a duration of time has passed.
     * @param arrayGenThreads   array of threads for RandomNumberGenerator
     * @param bitonicThreads    array of threads for StageOne and BitonicStage
     * @param durationMillis time to wait
     */
    public static void cleanup(ArrayList<Thread> arrayGenThreads,
                               ArrayList<Thread> bitonicThreads,
                               int durationMillis) {
        for (Thread t: arrayGenThreads)
            if(t.isAlive())
                try {
                    t.join(durationMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        for (Thread t: bitonicThreads)
            if(t.isAlive())
                try {
                    t.join(durationMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
    }

}
