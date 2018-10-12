package com.lolita;

class RandomArrayGenerator implements Runnable{
    @Override
    public void run() {
        System.out.println("Inside : " + Thread.currentThread().getName());
    }
}