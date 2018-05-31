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
    private int ageLowerLimit;
    private int ageUpperLimit;

    public FormsObject(String name, Class<?> className, int icon, int color, String[] roles, int ageLowerLimit, int ageUpperLimit) {
        this.name = name;
        this.className = className;
        this.icon = icon;
        this.color = color;
        this.roles = roles;
        this.ageLowerLimit = ageLowerLimit;
        this.ageUpperLimit = ageUpperLimit;
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

    public int getAgeUpperLimit() {
        return ageUpperLimit;
    }

    public void setAgeUpperLimit(int ageUpperLimit) {
        this.ageUpperLimit = ageUpperLimit;
    }

    public int getAgeLowerLimit() {
        return ageLowerLimit;
    }

    public void setAgeLowerLimit(int ageUpperLimit) {
        this.ageUpperLimit = ageUpperLimit;
    }

}
