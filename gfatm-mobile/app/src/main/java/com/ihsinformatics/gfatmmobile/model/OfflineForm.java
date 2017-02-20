package com.ihsinformatics.gfatmmobile.model;

import java.util.ArrayList;

/**
 * Created by Rabbia on 11/29/2016.
 */

public class OfflineForm {


    String id;
    String program;
    String formName;
    String pId;
    String formDate;
    String timeStamp;
    String location;
    String formObject;
    String encounterId;
    String username;
    ArrayList<String[][]> obsValue = new ArrayList<String[][]>();

    public OfflineForm(String id, String program, String formName, String pId,
                       String formDate, String timeStamp, String formObject, String location, String encounterId, String username) {

        this.id = id;
        this.program = program;
        this.formName = formName;
        this.pId = pId;
        this.formDate = formDate;
        this.timeStamp = timeStamp;
        this.location = location;
        this.formObject = formObject;
        this.encounterId = encounterId;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getFormDate() {
        return formDate;
    }

    public void setFormDate(String formDate) {
        this.formDate = formDate;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFormObject() {
        return formObject;
    }

    public void setFormObject(String formObject) {
        this.formObject = formObject;
    }

    public String getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(String encounterId) {
        this.encounterId = encounterId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String[][]> getObsValue() {
        return obsValue;
    }

    public void setObsValue(ArrayList<String[][]> obsValue) {
        this.obsValue = obsValue;
    }

    public void putObsValue(String conceptName, String value) {
        String[][] val = new String[1][2];
        val[0][0] = conceptName;
        val[0][1] = value;
        obsValue.add(val);
    }


}
