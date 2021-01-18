package com.ihsinformatics.gfatmmobile.medication;

import com.ihsinformatics.gfatmmobile.commonlab.persistance.DataAccess;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.DrugOrderEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDoseUnit;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDrug;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDuration;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationFrequency;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationRoute;

import java.util.Objects;

public class DrugModel {

    private String name;
    private String doseAmount;
    private String doseUnit;
    private String frequency;
    private String route;
    private String durationAmount;
    private String durationUnit;
    private String startDate;
    private String instructions;

    private String drugUUID;
    private String doseUnitUUID;
    private String frequencyUUID;
    private String routeUUID;
    private String durationUUID;

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

    public String getDrugUUID() {
        return drugUUID;
    }

    public void setDrugUUID(String drugUUID) {
        this.drugUUID = drugUUID;
    }

    public String getDoseUnitUUID() {
        return doseUnitUUID;
    }

    public void setDoseUnitUUID(String doseUnitUUID) {
        this.doseUnitUUID = doseUnitUUID;
    }

    public String getFrequencyUUID() {
        return frequencyUUID;
    }

    public void setFrequencyUUID(String frequencyUUID) {
        this.frequencyUUID = frequencyUUID;
    }

    public String getRouteUUID() {
        return routeUUID;
    }

    public void setRouteUUID(String routeUUID) {
        this.routeUUID = routeUUID;
    }

    public String getDurationUUID() {
        return durationUUID;
    }

    public void setDurationUUID(String durationUUID) {
        this.durationUUID = durationUUID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DrugModel drugModel = (DrugModel) o;
        return name.equals(drugModel.name) &&
                drugUUID.equals(drugModel.drugUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, drugUUID);
    }

    @Override
    public String toString() {
        return "Drug{" +
                "name='" + name + '\'' +
                ", doseAmount='" + doseAmount + '\'' +
                ", doseUnit='" + doseUnit + '\'' +
                ", frequency='" + frequency + '\'' +
                ", route='" + route + '\'' +
                ", durationAmount='" + durationAmount + '\'' +
                ", durationUnit='" + durationUnit + '\'' +
                ", startDate='" + startDate + '\'' +
                ", instructions='" + instructions + '\'' +
                ", drugUUID='" + drugUUID + '\'' +
                ", doseUnitUUID='" + doseUnitUUID + '\'' +
                ", frequencyUUID='" + frequencyUUID + '\'' +
                ", routeUUID='" + routeUUID + '\'' +
                '}';
    }

    public DrugModel copyEntity(DrugOrderEntity order) {

        MedicationDrug drug = DataAccess.getInstance().getDrugByUUID(order.getDrugUUID());
        MedicationFrequency frequency = DataAccess.getInstance().getFrequencyByUUID(order.getFrequencyUUID());
        MedicationRoute route = DataAccess.getInstance().getRouteByUUID(order.getRouteUUID());
        MedicationDuration duration = DataAccess.getInstance().getDurationByUUID(order.getDurationUnitsUUID());
        MedicationDoseUnit doseUnit = DataAccess.getInstance().getDoseUnitByUUID(order.getDoseUnitsUUID());

        this.doseAmount = order.getDose()+"";
        this.doseUnit = doseUnit==null?null:doseUnit.getDisplay();
        this.doseUnitUUID = order.getDoseUnitsUUID();

        this.drugUUID = order.getDrugUUID();
        this.name = drug.getName();

        this.durationAmount = order.getDuration()+"";
        this.durationUnit = duration==null?null:duration.getDisplay();
        this.durationUUID = order.getDurationUnitsUUID();

        this.frequency = frequency==null?null:frequency.getDisplay();
        this.frequencyUUID = order.getFrequencyUUID();

        this.instructions = order.getInstructions();

        this.route = route==null?null:route.getDisplay();
        this.routeUUID = order.getRouteUUID();

        this.startDate = order.getDateActivated();

        return this;
    }
}