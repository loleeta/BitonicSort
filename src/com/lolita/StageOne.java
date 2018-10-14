package com.lolita;

import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;

class StageOne implements Runnable {
    private int size;
    private String direction;
    private SynchronousQueue<Double[]> input;
    private SynchronousQueue<Double[]> output;
    private Double[] arr;


    public StageOne (int size, String way, SynchronousQueue<Double[]> input,
                                        SynchronousQueue<Double[]> output) {
        this.size = size;
        this.direction = way;
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
        //System.out.println("StageOne: run()");

        try {
            this.arr = input.poll(10, TimeUnit.SECONDS);     //read array in
            //System.out.println("StageOne received: " + Arrays.toString(arr));
            System.out.println();
            if (direction.equals("UP"))
                sortAsc();
            else
                sortDesc();                 //sort array according to direction
            //System.out.println("StageOne: sorted array into " + Arrays.toString(this.arr));
            output.put(arr);                //write to output
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sortAsc() {
        Arrays.sort(this.arr);
    }

    public void sortDesc() {
        Comparator<Double> cr = Collections.reverseOrder();
        Arrays.sort(this.arr, cr);
    }



}