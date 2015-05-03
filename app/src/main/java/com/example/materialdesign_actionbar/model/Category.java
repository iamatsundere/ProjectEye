package com.example.materialdesign_actionbar.model;

/**
 * Created by Phuc on 4/29/2015.
 */
public class Category {
    String name;
    int iconID;
    int imageID;
    int colorID;

    public Category() {

    }

    public Category(String name, int i) {
        // TODO Auto-generated constructor stub
        this.name = name;
        this.iconID = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int drawable) {
        this.iconID = drawable;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public int getImageID() {
        return this.imageID;
    }

    public void setColorID(int colorID) {
        this.colorID = colorID;
    }

    public int getColorID() {
        return this.colorID;
    }

}
