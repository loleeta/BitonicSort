package com.lolita;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class BitonicStageTest {

    SynchronousQueue<Double[]> inputAsc = null;
    SynchronousQueue<Double[]> inputDesc = null;
    SynchronousQueue<Double[]> output = null;

    private Double[] getGeneratedNumbers(long seed, int count) {
        Random rand = new Random(seed);
        Double [] arr = new Double[count];
        for (int i = 0; i < count; i++) {
            arr[i] = rand.nextDouble();
        }
        return arr;
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        inputAsc = new SynchronousQueue<>();
        inputDesc = new SynchronousQueue<>();
        output = new SynchronousQueue<>();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        inputAsc = null;
        inputDesc = null;
        output = null;
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
    void sorts4ValuesDesc() {
        try {
            BitonicStage bstage = new BitonicStage(SortDirection.Descending, this.inputAsc, this.inputDesc,this.output);
            Thread bstageThread = new Thread(bstage);
            bstageThread.start();

            this.inputAsc.put(new Double[]{1., 4.});
            this.inputDesc.put(new Double[]{9., 0.});

            Double[] expectedOut = new Double[]{9., 4., 1., 0.};
            Double[] actualOut = this.output.poll(2, TimeUnit.SECONDS);
            assertArrayEquals(expectedOut, actualOut);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void sorts8ValuesAsc() {
        try {
            BitonicStage bstage = new BitonicStage(SortDirection.Ascending, this.inputAsc, this.inputDesc,this.output);
            Thread bstageThread = new Thread(bstage);
            bstageThread.start();

            this.inputAsc.put(new Double[]{1., 2., 3., 8.});
            this.inputDesc.put(new Double[]{9., 4., 1., 0.});

            Double[] expectedOut = new Double[]{0., 1., 1., 2., 3., 4., 8., 9.};
            Double[] actualOut = this.output.poll(2, TimeUnit.SECONDS);
            assertArrayEquals(expectedOut, actualOut);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @org.junit.jupiter.api.Test
    void sorts16ValuesAsc() {
        try {
            BitonicStage bstage = new BitonicStage(SortDirection.Ascending, this.inputAsc, this.inputDesc,this.output);
            Thread bstageThread = new Thread(bstage);
            bstageThread.start();

            this.inputAsc.put(new Double[]{
                    1.,
                    2.,
                    3.,
                    6.,
                    8.,
                    9.,
                    11.,
                    13.,
                    14.,
                    16.,
                    17.,
                    19.,
                    20.,
                    22.,
                    24.,
                    25.,
                    26.,
                    27.,
                    28.,
                    31.,
                    32.,
                    34.,
                    37.,
                    39.,
                    40.,
                    41.,
                    42.,
                    43.,
                    44.,
                    47.,
                    48.,
                    50.,
                    51.,
                    52.,
                    54.,
                    55.,
                    56.,
                    58.,
                    60.,
                    61.,
                    63.,
                    64.,
                    65.,
                    67.,
                    68.,
                    70.,
                    71.,
                    73.,
                    74.,
                    76.,
                    77.,
                    79.,
                    80.,
                    81.,
                    84.,
                    85.,
                    88.,
                    89.,
                    91.,
                    92.,
                    93.,
                    95.,
                    97.,
                    98.,
            });
            this.inputDesc.put(new Double[]{
                    100.,
                    99.,
                    98.,
                    97.,
                    96.,
                    95.,
                    94.,
                    93.,
                    92.,
                    91.,
                    90.,
                    89.,
                    88.,
                    87.,
                    86.,
                    85.,
                    84.,
                    83.,
                    82.,
                    81.,
                    80.,
                    79.,
                    78.,
                    77.,
                    76.,
                    75.,
                    74.,
                    73.,
                    72.,
                    71.,
                    70.,
                    69.,
                    68.,
                    67.,
                    66.,
                    65.,
                    64.,
                    63.,
                    62.,
                    61.,
                    60.,
                    59.,
                    58.,
                    57.,
                    56.,
                    55.,
                    54.,
                    53.,
                    52.,
                    51.,
                    50.,
                    49.,
                    48.,
                    47.,
                    46.,
                    45.,
                    44.,
                    43.,
                    42.,
                    41.,
                    40.,
                    39.,
                    38.,
                    37.,
            });

            Double[] expectedOut = new Double[]{
                    1.,
                    2.,
                    3.,
                    6.,
                    8.,
                    9.,
                    11.,
                    13.,
                    14.,
                    16.,
                    17.,
                    19.,
                    20.,
                    22.,
                    24.,
                    25.,
                    26.,
                    27.,
                    28.,
                    31.,
                    32.,
                    34.,
                    37.,
                    37.,
                    38.,
                    39.,
                    39.,
                    40.,
                    40.,
                    41.,
                    41.,
                    42.,
                    42.,
                    43.,
                    43.,
                    44.,
                    44.,
                    45.,
                    46.,
                    47.,
                    47.,
                    48.,
                    48.,
                    49.,
                    50.,
                    50.,
                    51.,
                    51.,
                    52.,
                    52.,
                    53.,
                    54.,
                    54.,
                    55.,
                    55.,
                    56.,
                    56.,
                    57.,
                    58.,
                    58.,
                    59.,
                    60.,
                    60.,
                    61.,
                    61.,
                    62.,
                    63.,
                    63.,
                    64.,
                    64.,
                    65.,
                    65.,
                    66.,
                    67.,
                    67.,
                    68.,
                    68.,
                    69.,
                    70.,
                    70.,
                    71.,
                    71.,
                    72.,
                    73.,
                    73.,
                    74.,
                    74.,
                    75.,
                    76.,
                    76.,
                    77.,
                    77.,
                    78.,
                    79.,
                    79.,
                    80.,
                    80.,
                    81.,
                    81.,
                    82.,
                    83.,
                    84.,
                    84.,
                    85.,
                    85.,
                    86.,
                    87.,
                    88.,
                    88.,
                    89.,
                    89.,
                    90.,
                    91.,
                    91.,
                    92.,
                    92.,
                    93.,
                    93.,
                    94.,
                    95.,
                    95.,
                    96.,
                    97.,
                    97.,
                    98.,
                    98.,
                    99.,
                    100.,
            };
            Double[] actualOut = this.output.poll(2, TimeUnit.SECONDS);
            assertArrayEquals(expectedOut, actualOut);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void sortsLargeValuesAsc() {
        try {
            int outSize = 1<<22;
            int inSize = outSize / 2;
            Double[] randExpectOut = this.getGeneratedNumbers(1, outSize);
            Double[] randInAsc = Arrays.copyOfRange(randExpectOut, 0, inSize);
            Double[] randInDesc = Arrays.copyOfRange(randExpectOut, inSize, outSize);

            long startTime = System.nanoTime();
            Arrays.sort(randExpectOut);
            long endTime = System.nanoTime();
            double elapsedSecs = (endTime - startTime) / 1000000000.0;
            System.out.println("Array.sort took " + elapsedSecs + " secs.");

            Arrays.sort(randInAsc);
            Arrays.sort(randInDesc, Collections.reverseOrder());

            BitonicStage bstage = new BitonicStage(SortDirection.Ascending, this.inputAsc, this.inputDesc,this.output);
            Thread bstageThread = new Thread(bstage);
            bstageThread.start();

            startTime = System.nanoTime();
            this.inputAsc.put(randInAsc);
            this.inputDesc.put(randInDesc);
            Double[] actualOut = this.output.poll(10, TimeUnit.SECONDS);
            endTime = System.nanoTime();
            elapsedSecs = (endTime - startTime) / 1000000000.0;
            System.out.println("BitonicStage took " + elapsedSecs + " secs.");

            assertArrayEquals(randExpectOut, actualOut);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}