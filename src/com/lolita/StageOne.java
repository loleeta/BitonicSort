package com.lolita;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;

class StageOne implements Runnable {
    private SortDirection sortDirection;
    private SynchronousQueue<Double[]> input;
    private SynchronousQueue<Double[]> output;

    public StageOne(SortDirection sortDirection, SynchronousQueue<Double[]> input,
                    SynchronousQueue<Double[]> output) {
        this.sortDirection = sortDirection;
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
        try {
            Double[] arr = input.poll(20, TimeUnit.SECONDS);     //read array in
            assert arr != null;
            switch (this.sortDirection) {
                case Ascending:
                    sortAsc(arr);
                    break;
                case Descending:
                    sortDesc(arr);
                    break;
            }
            output.put(arr);                //write to output
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("StageOne: run()");
    }

    public void sortAsc(Double[] arr) {
        Arrays.sort(arr);
    }

    public void sortDesc(Double[] arr) {
        Comparator<Double> cr = Collections.reverseOrder();
        Arrays.sort(arr, cr);
    }


}