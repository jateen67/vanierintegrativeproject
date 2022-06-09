package com.example.physicssimulation;

public class Timer {
    long startTime;

    public Timer() {
        startTime = System.currentTimeMillis();
    }

    public double elapsed() {
        long elapsed = System.currentTimeMillis();
        return (elapsed - startTime) / 1000.0;
    }
}
