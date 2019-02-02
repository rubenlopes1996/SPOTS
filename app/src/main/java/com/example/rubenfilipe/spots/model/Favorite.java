package com.example.rubenfilipe.spots.model;

public class Favorite {
    private String spotId;
    private String email;

    public Favorite(String spotId, String email) {
        this.spotId = spotId;
        this.email = email;
    }

    public Favorite() {
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "spotId='" + spotId + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
