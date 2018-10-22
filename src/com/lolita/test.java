package com.lolita;
import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        Integer [] arr = {10, 50, 30, 40, 80, 60, 70, 20};
        sort(arr, 0, 8, "UP");
        System.out.println(Arrays.toString(arr));

    }


    public static void sort(Integer[] seq, int start, int size, String direction) {
        if (size > 1) {
            bitonic_sequence(seq, start, size);
            bitonic_sort(seq, start, size, direction);
        }
    }

    private static void bitonic_sequence(Integer[] seq, int start, int size){
        if (size > 1) {
            sort(seq, start, size/2, "UP");
            sort(seq, start+(size/2), size/2, "DOWN");
        }
    }

    public static void bitonic_sort(Integer[] seq, int start, int size, String direction) {
        if (size > 1) {
            bitonic_merge(seq, start, size, direction);
            bitonic_sort(seq, start, size/2, direction);
            bitonic_sort(seq, start+(size/2), size/2, direction);
        }
    }

    private static void bitonic_merge(Integer[] seq, int start, int size, String direction) {
        if (direction.equals("UP")) {
            for (int i = start; i < start+size/2; i++)
                if (seq[i] > seq[i + (size/2)])
                    swap(seq, i, (i + (size/2)));
        }
        if (direction.equals("DOWN")) {
            for (int i = start; i < start+size/2; i++)
                if (seq[i] < seq[i + (size/2)])
                    swap(seq, i, (i + (size/2)));
        }
    }

    public static void swap(Integer[] arr, int indx1, int indx2) {
        Integer temp = arr[indx1];
        arr[indx1] = arr[indx2];
        arr[indx2] = temp;
    }
}