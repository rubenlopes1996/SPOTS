package com.example.rubenfilipe.spots.model;

public class Rate {
    private String spotId;
    private Double value;

    public Rate(String spotId, Double value) {
        this.spotId = spotId;
        this.value = value;
    }

    public Rate() {
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
