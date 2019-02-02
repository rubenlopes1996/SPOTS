package com.example.rubenfilipe.spots.model;

public class Spot {
    private int id;
    private Double latitude;
    private Double longitude;
    private long parkId;
    private boolean available;
    private int count;

    public Spot() {
    }

    public Spot(int id,Double latitude, Double longitude, long parkId, Boolean available, int count) {
        this.id=id;
        this.count=count;
        this.latitude = latitude;
        this.longitude = longitude;
        this.parkId = parkId;
        this.available = available;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getParkId() {
        return parkId;
    }

    public void setParkId(int parkId) {
        this.parkId = parkId;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean status) {
        this.available = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Spot{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", parkId=" + parkId +
                ", available=" + available +
                '}';
    }
}