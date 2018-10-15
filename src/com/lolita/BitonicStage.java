package com.lolita;

import java.util.Arrays;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

class BitonicStage implements Runnable {
    private static final int TIMEOUT_SECS = 200;

    private SortDirection direction;
    private SynchronousQueue<Double[]> inputAsc;
    private SynchronousQueue<Double[]> inputDesc;
    private SynchronousQueue<Double[]> output;

    public BitonicStage(SortDirection direction, SynchronousQueue<Double[]> inputAsc,
                        SynchronousQueue<Double[]> inputDesc, SynchronousQueue<Double[]> output) {
        this.direction = direction;
        this.inputAsc = inputAsc;
        this.inputDesc = inputDesc;
        this.output = output;
    }

    @Override
    public void run() {
        try {
            Double[] arrAsc = inputAsc.poll(TIMEOUT_SECS, TimeUnit.SECONDS);
            Double[] arrDesc = inputDesc.poll(TIMEOUT_SECS, TimeUnit.SECONDS);

            assert arrAsc != null;
            assert arrDesc != null;
            assert arrAsc.length == arrDesc.length;

            int inputLen = arrAsc.length;
            Double[] outArr = new Double[2 * inputLen];

            System.arraycopy(arrAsc, 0, outArr, 0, inputLen);
            System.arraycopy(arrDesc, 0, outArr, inputLen, inputLen);

            bitonic_sort(outArr, 0, outArr.length, direction);
            output.put(outArr);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("BitonicStage: run()");
    }

    private void sort(Double[] seq, int start, int n, SortDirection direction) {
        if (n > 1) {
            bitonic_sequence(seq, start, n);
            bitonic_sort(seq, start, n, direction);
        }
    }

    private void bitonic_sequence(Double[] seq, int start, int n) {
        if (n > 1) {
            sort(seq, start, n / 2, SortDirection.Ascending);
            sort(seq, start + n / 2, n / 2, SortDirection.Descending);
        }
    }

    private void bitonic_sort(Double[] seq, int start, int n, SortDirection direction) {
        if (n > 1) {
            bitonic_merge(seq, start, n, direction);
            bitonic_sort(seq, start, n / 2, direction);
            bitonic_sort(seq, start + n / 2, n / 2, direction);
        }
    }

    private void bitonic_merge(Double[] seq, int start, int n, SortDirection direction) {
        if (direction == SortDirection.Ascending) {
            for (int i = start; i < start + n / 2; i++)
                if (seq[i] > seq[i + n / 2])
                    swap(seq, i, (i + n / 2));
        } else {
            for (int i = start; i < start + n / 2; i++)
                if (seq[i] < seq[i + n / 2])
                    swap(seq, i, (i + n / 2));
        }
    }

    public void swap(Double[] arr, int indx1, int indx2) {
        Double temp = arr[indx1];
        arr[indx1] = arr[indx2];
        arr[indx2] = temp;
    }
}