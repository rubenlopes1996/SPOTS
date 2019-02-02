package com.example.rubenfilipe.spots.model;

public class AlgorithmDuration {
    private String algorithm;
    private double seconds;

    public AlgorithmDuration() {
    }

    public AlgorithmDuration(String algorithm, double seconds) {
        this.algorithm = algorithm;
        this.seconds = seconds;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public double getSeconds() {
        return seconds;
    }

    public void setSeconds(double seconds) {
        this.seconds = seconds;
    }
}
