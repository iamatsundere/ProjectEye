package com.example.materialdesign_actionbar.model;

/**
 * Created by Phuc on 4/29/2015.
 */
public class Place {
    String name;
    String address;
    double distance;
    int iconID;
    int colorID;
    int typeID;

    public Place() {

    }

    public Place(String name, String address, double distance, int i) {
        // TODO Auto-generated constructor stub
        this.name = name;
        this.iconID = i;
        this.address = address;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int geticonID() {
        return iconID;
    }

    public void seticonID(int iconID) {
        this.iconID = iconID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getColorID() {
        return this.colorID;
    }

    public void setColorID(int colorID) {
        this.colorID = colorID;
    }

    public int getTypeID() {
        return this.typeID;
    }

    public void getTypeID(int typeID) {
        this.typeID = typeID;
    }
}
