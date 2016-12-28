package com.ihsinformatics.gfatmmobile.shared;

import android.app.Activity;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;

/**
 * Created by Rabbia on 11/20/2016.
 */

public class FormsObject {


    private String name;
    private Class<?> className;
    private int icon;
    private int color;

    public FormsObject(String name, Class<?> className, int icon, int color) {
        this.name = name;
        this.className = className;
        this.icon = icon;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getClassName() {
        return className;
    }

    public void setClassName(Class<?> className) {
        this.className = className;
    }


}
