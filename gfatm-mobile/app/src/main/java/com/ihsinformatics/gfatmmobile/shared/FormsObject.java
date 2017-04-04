package com.ihsinformatics.gfatmmobile.shared;

import java.io.Serializable;

/**
 * Created by Rabbia on 11/20/2016.
 */

public class FormsObject implements Serializable {

    private String name;
    private Class<?> className;
    private int icon;
    private int color;
    private String[] roles;

    public FormsObject(String name, Class<?> className, int icon, int color, String[] roles) {
        this.name = name;
        this.className = className;
        this.icon = icon;
        this.color = color;
        this.roles = roles;
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

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }


}
