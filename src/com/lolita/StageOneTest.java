package com.lolita;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class StageOneTest {
    SynchronousQueue<Double[]> input = null;
    SynchronousQueue<Double[]> output = null;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        input = new SynchronousQueue<>();
        output = new SynchronousQueue<>();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        input = null;
        output = null;
    }

    void putInput(Double[] arr){
        try {
            this.input.put(arr);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    Double[] pollOutput() {
        try {
            return this.output.poll(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @org.junit.jupiter.api.Test
    void sorts2ValuesAsc() {
        StageOne testStage = new StageOne(SortDirection.Ascending ,this.input, this.output);
        Thread testStageThread = new Thread(testStage);
        testStageThread.start();

        Double[] inArr = {2., 1.};
        this.putInput(inArr);
        Double[] outArr = pollOutput();

        assertArrayEquals(inArr, new Double[]{1., 2.});
        assertArrayEquals(outArr, new Double[]{1., 2.});
    }

    @org.junit.jupiter.api.Test
    void sorts4ValuesDesc() {
        StageOne testStage = new StageOne(SortDirection.Ascending ,this.input, this.output);
        Thread testStageThread = new Thread(testStage);
        testStageThread.start();

        Double[] inArr = {3., 4., 2., 1.};
        this.putInput(inArr);
        Double[] outArr = pollOutput();

        assertArrayEquals(inArr, new Double[]{1., 2., 3., 4.});
        assertArrayEquals(outArr, new Double[]{1., 2., 3., 4.});
    }

}