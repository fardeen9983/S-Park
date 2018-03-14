package com.prototype.hackathon.getparked;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LUCIFER on 13-03-2018.
 */

public class SpotDetails implements Serializable{
    private double latitude;
    private double longitude;
    private int sensorCount;
    private String address;
    private int free;
    private int used;
    private double cost;
    private ArrayList<Boolean> sensors;
    public SpotDetails(double latitude, double longitude, int sensorCount, String address, int free, int used, double cost) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.sensorCount = sensorCount;
        this.address = address;
        this.free = free;
        this.used = used;
        this.cost = cost;
    }

    public SpotDetails() {
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getSensorCount() {
        return sensorCount;
    }

    public String getAddress() {
        return address;
    }

    public int getFree() {
        return free;
    }

    public int getUsed() {
        return used;
    }

    public double getCost() {
        return cost;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setSensorCount(int sensorCount) {
        this.sensorCount = sensorCount;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSensors(ArrayList<Boolean> sensors) {
        this.sensors = sensors;
    }

    @Override
    public String toString() {
        String y = "Name : " + address + ", Lat :  " + latitude + ", Long : " + longitude + " ";
        for(Boolean x : sensors){
            y+=" " +x;
        }
        return y;
    }

    public ArrayList<Boolean> getSensors() {

        return sensors;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
