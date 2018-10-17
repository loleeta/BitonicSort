package com.lolita;

import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;


/**
 * BitonicPipeline represents the pipeline for bitonic sort. It has 4 StageOne
 * objects, 3 BitonicStage objects, and 7 threads total. It contains a method
 * for creating the objects and threads.
 */
public class BitonicPipeline {
    private ArrayList<SynchronousQueue<Double[]>> stageOneInputs;
    ArrayList<SynchronousQueue<Double[]>> stageOneOutput =
            new ArrayList<>(4); //4 queues for threads 1-4 output
    ArrayList<SynchronousQueue<Double[]>> stageTwoOutput =
            new ArrayList<>(2); //2 queues for threads 5-6 output
    private SynchronousQueue<Double[]> finalOutput; //for thread 7 output
    private int inputSize;

    // Objects and threads created for the pipeline
    ArrayList<StageOne> stageOnes = new ArrayList<>(4); //4 StageOne objs
    ArrayList<BitonicStage> bitonicStages = new ArrayList<>(3); //3 BitonicStage objs
    ArrayList<Thread> pipelineThreads = new ArrayList<>(7); //7 threads total


    /**
     * Constructor for BitonicPipeline
     * @param randNumQueues SynchronousQueues for RandomArrayGenerator
     * @param output        SynchronousQueue for BitonicStage output
     * @param inputSize     Size of the array
     */
    public BitonicPipeline(ArrayList<SynchronousQueue<Double[]>> randNumQueues,
                           SynchronousQueue<Double[]> output, int inputSize) {
        this.stageOneInputs = randNumQueues;
        this.finalOutput = output;
        this.inputSize = inputSize;
        for (int i = 0; i < 4; i++)
            this.stageOneOutput.add(new SynchronousQueue<Double[]>());
        for (int i = 0; i < 2; i++)
            this.stageTwoOutput.add(new SynchronousQueue<Double[]>());
    }

    /**
     * Creates objects and threads for StageOne and BitonicStage
     * @return ArrayList of total threads in pipeline
     */
    public ArrayList<Thread> createAndStartThreads() {
        //Create 4 StageOne objects
        for (int i = 0; i < 4; i++) {
            SortDirection direction = i % 2 == 0 ?
                    SortDirection.Ascending : SortDirection.Descending;
            StageOne stageOne = new StageOne(direction,
                                             this.stageOneInputs.get(i),
                                             this.stageOneOutput.get(i));
            this.stageOnes.add(stageOne);
        }

        //Create 3 BitonicStage objects
        this.bitonicStages.add(
                        new BitonicStage(
                                SortDirection.Ascending,
                                this.stageOneOutput.get(0),
                                this.stageOneOutput.get(1),
                                this.stageTwoOutput.get(0)));
        this.bitonicStages.add(
                        new BitonicStage(
                                SortDirection.Descending,
                                this.stageOneOutput.get(2),
                                this.stageOneOutput.get(3),
                                this.stageTwoOutput.get(1)));
        this.bitonicStages.add(
                        new BitonicStage(
                                SortDirection.Ascending,
                                this.stageTwoOutput.get(0),
                                this.stageTwoOutput.get(1),
                                finalOutput));

        //Creates 4 threads for StageOne
        for (StageOne stageOne : stageOnes)
            this.pipelineThreads.add(new Thread(stageOne));

        //Create 3 threads for BitonicStage
        for (BitonicStage bStage : this.bitonicStages)
            this.pipelineThreads.add(new Thread(bStage));

        //Starts all threads
        for (Thread t : this.pipelineThreads)
            t.start();

        return this.pipelineThreads;
    }
}