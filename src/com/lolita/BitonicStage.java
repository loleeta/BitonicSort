package com.lolita;

class BitonicStage implements Runnable {
    @Override
    public void run() {
        System.out.println("Inside : " + Thread.currentThread().getName());
    }
}