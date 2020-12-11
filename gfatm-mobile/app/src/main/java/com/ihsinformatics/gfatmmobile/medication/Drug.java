package com.ihsinformatics.gfatmmobile.medication;

public class Drug {
    private String name;
    private String doseAmount;
    private String doseUnit;
    private String frequency;
    private String route;
    private String durationAmount;
    private String durationUnit;
    private String startDate;
    private String instructions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoseAmount() {
        return doseAmount;
    }

    public void setDoseAmount(String doseAmount) {
        this.doseAmount = doseAmount;
    }

    public String getDoseUnit() {
        return doseUnit;
    }

    public void setDoseUnit(String doseUnit) {
        this.doseUnit = doseUnit;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDurationAmount() {
        return durationAmount;
    }

    public void setDurationAmount(String durationAmount) {
        this.durationAmount = durationAmount;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}