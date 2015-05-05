package com.example.materialdesign_actionbar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Phuc on 4/29/2015.
 */
public class Place implements Parcelable {
    String name;
    String address;
    double lat; // latitude
    double lng; // longitude
    double distanceInDrivingMode;
    double distanceInWalkingMode;
    int iconID;
    int colorID;
    int typeID;

    public Place() {

    }

    public Place(String name, String address, double lat, double lng, double distanceInDrivingMode, double distanceInWalkingMode, int i) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.distanceInDrivingMode = distanceInDrivingMode;
        this.distanceInWalkingMode = distanceInWalkingMode;
        this.iconID = i;
    }

    public Place(Parcel source) {
        this.name = source.readString();
        this.address = source.readString();
        this.lat = source.readDouble();
        this.lng = source.readDouble();
        this.distanceInDrivingMode = source.readDouble();
        this.distanceInWalkingMode = source.readDouble();
        this.iconID = source.readInt();
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getDistanceInDrivingMode() {
        return distanceInDrivingMode;
    }

    public void setDistanceInDrivingMode(double distanceInDrivingMode) {
        this.distanceInDrivingMode = distanceInDrivingMode;
    }

    public double getDistanceInWalkingMode() {
        return distanceInWalkingMode;
    }

    public void setDistanceInWalkingMode(double distanceInWalkingMode) {
        this.distanceInWalkingMode = distanceInWalkingMode;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeDouble(this.distanceInDrivingMode);
        dest.writeDouble(this.distanceInWalkingMode);
        dest.writeInt(this.iconID);
    }

    public final static Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel source) {
            return new Place(source);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
}
