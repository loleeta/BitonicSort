package com.lolita;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.SynchronousQueue;

public class BitonicPipeline {
    public static final int SIZE = 1<<4;
    public enum Direction {UP, DOWN};




    public static void main(String[] args) {
        SynchronousQueue<int[]> input = new SynchronousQueue<>();
        SynchronousQueue<int[]> output = new SynchronousQueue<>();
        RandomArrayGenerator arrayGen = new RandomArrayGenerator(SIZE, input);    //talker
        StageOne threads1to4 = new StageOne("UP", input, output);           //listener

        Long then = System.nanoTime();
        //BitonicPipeline bp = new BitonicPipeline();
        //bp.exampleRun();

        Thread talker = new Thread(arrayGen);
        Thread listener = new Thread(threads1to4);

        talker.start();
        listener.start();

        Long now = System.nanoTime();

        System.out.println("BitonicPipeline took " + (now-then)/1_000_100 + " ms");
    }

    public static void exampleRun() {
        int [] a = {1, 2, 3, 4};
        int [] b = {5, 6, 7, 8};
        resultsToFile(a);
        //clearFile();
        resultsToFile(b);
    }


    //method to print sorted arrays to file
    public static void resultsToFile(int[] arr) {
        System.out.println("printing array to file");
        String s = "";
        for (int i: arr) {
            s += Integer.toString(i) + " ";
            System.out.print(i + " ");
        }
        try {
            FileWriter writer = new FileWriter("output.txt", true);
            writer.write(s);
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //method to clear file (DELETE)
    public static void clearFile() {
        try {
            FileWriter fw = new FileWriter("output.txt", false);
            PrintWriter pw = new PrintWriter(fw, false);
            pw.flush();
            pw.close();
            fw.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


}