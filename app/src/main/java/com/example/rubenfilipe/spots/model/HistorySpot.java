package com.example.rubenfilipe.spots.model;

import java.util.Date;

public class HistorySpot {
    private boolean available;
    private int id;
    private String date;
    private long idPark;

    public HistorySpot(boolean available, int id, String date, long idPark) {
        this.available = available;
        this.id = id;
        this.date = date;
        this.idPark = idPark;
    }

    public HistorySpot(){

    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getIdPark() {
        return idPark;
    }

    public void setIdPark(int idPark) {
        this.idPark = idPark;
    }
}
