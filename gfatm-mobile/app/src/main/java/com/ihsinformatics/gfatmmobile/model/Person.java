/* Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. */
/**
 *
 */

package com.ihsinformatics.gfatmmobile.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author owais.hussain@irdresearch.org
 */
public class Person extends AbstractModel {
    public static final String FIELDS = "uuid,name";

    String givenName;
    String familyName;
    int age;
    String birthdate;
    String gender;
    String birthPlace;
    String citizenship;
    String maritalStatus;
    String healthCenter;
    String healthDistrict;
    String motherName;
    String primaryContact;
    String primaryContactOwner;
    String secondaryContact;
    String secondaryContactOwner;
    String ethnicity;
    String educationLevel;
    String employmentStatus;
    String occupation;
    String motherTongue;
    String incomeClass;
    String nationalId;
    String nationalIdOwner;
    String guardianName;
    String address1;
    String address2;
    String address3;
    String stateProvince;
    String cityVillage;
    String country;
    String tertiaryContact;
    String quaternaryContact;


    public Person(String uuid, String givenName, String familyName, int age, String birthdate, String gender, String birthPlace, String citizenship, String maritalStatus, String healthCenter, String healthDistrict, String motherName,
                  String primaryContact, String primaryContactOwner, String secondaryContact, String secondaryContactOwner, String tertiaryContact, String quaternaryContact,
                  String ethnicity, String educationLevel, String employmentStatus, String occupation, String motherTongue, String incomeClass,
                  String nationalId, String nationalIdOwner, String guardianName,
                  String address1, String address2, String address3, String stateProvince, String cityVillage, String country) {

        super(uuid);
        this.givenName = givenName;
        this.familyName = familyName;
        this.age = age;
        this.birthdate = birthdate;
        this.gender = gender;
        this.birthPlace = birthPlace;
        this.citizenship = citizenship;
        this.maritalStatus = maritalStatus;
        this.healthCenter = healthCenter;
        this.healthDistrict = healthDistrict;
        this.motherName = motherName;
        this.primaryContact = primaryContact;
        this.primaryContactOwner = primaryContactOwner;
        this.secondaryContact = secondaryContact;
        this.secondaryContactOwner = secondaryContactOwner;
        this.ethnicity = ethnicity;
        this.educationLevel = educationLevel;
        this.employmentStatus = employmentStatus;
        this.occupation = occupation;
        this.motherTongue = motherTongue;
        this.incomeClass = incomeClass;
        this.nationalId = nationalId;
        this.nationalIdOwner = nationalIdOwner;
        this.guardianName = guardianName;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.stateProvince = stateProvince;
        this.cityVillage = cityVillage;
        this.country = country;
        this.tertiaryContact = tertiaryContact;
        this.quaternaryContact = quaternaryContact;
    }

    public static Person parseJSONObject(JSONObject json) {
        Person person = null;
        String uuid = "";
        String givenName = "";
        String familyName = "";
        int age = 0;
        String birthdate = "";
        String gender = "";

        String birthPlace = "";
        String citizenship = "";
        String maritalStatus = "";
        String healthCenter = "";
        String healthDistrict = "";
        String motherName = "";
        String primaryContact = "";
        String primaryContactOwner = "";
        String secondaryContact = "";
        String secondaryContactOwner = "";
        String ethnicity = "";
        String educationLevel = "";
        String employmentStatus = "";
        String occupation = "";
        String motherTongue = "";
        String incomeClass = "";
        String nationalId = "";
        String nationalIdOwner = "";
        String guardianName = "";
        String address1 = "";
        String address2 = "";
        String address3 = "";
        String stateProvince = "";
        String cityVillage = "";
        String country = "";
        String tertiaryContact = "";
        String quaternaryContact = "";

        try {
            uuid = json.getString("uuid");
            String[] names = json.getString("display").split(" ");
            givenName = names[0];
            familyName = names[1];
            age = json.getInt("age");
            birthdate = json.getString("birthdate");
            gender = json.getString("gender");

            JSONObject addressObject = json.getJSONObject("preferredAddress");
            if (addressObject.getString("address1") != null)
                address1 = addressObject.getString("address1");
            if (addressObject.getString("address2") == null)
                address2 = addressObject.getString("address2");
            if (addressObject.getString("stateProvince") == null)
                stateProvince = addressObject.getString("stateProvince");
            if (addressObject.getString("cityVillage") == null)
                cityVillage = addressObject.getString("cityVillage");
            if (addressObject.getString("country") == null)
                country = addressObject.getString("country");
            if (addressObject.getString("address3") == null)
                address3 = addressObject.getString("address3");

            JSONArray attributes = json.getJSONArray("attributes");

            for (int i = 0; i < attributes.length(); i++) {

                JSONObject object = attributes.getJSONObject(i);
                String display = object.getString("display");

                String[] displayString = display.split(" = ");
                if (displayString.length == 2) {
                    if (displayString[0].equals("Birthplace")) {
                        birthPlace = displayString[1];
                    } else if (displayString[0].equals("Citizenship")) {
                        citizenship = displayString[1];
                    } else if (displayString[0].equals("Health District")) {
                        healthDistrict = displayString[1];
                    } else if (displayString[0].equals("Mother Name")) {
                        motherName = displayString[1];
                    } else if (displayString[0].equals("Primary Contact")) {
                        primaryContact = displayString[1];
                    } else if (displayString[0].equals("Secondary Contact")) {
                        secondaryContact = displayString[1];
                    } else if (displayString[0].equals("National ID")) {
                        nationalId = displayString[1];
                    } else if (displayString[0].equals("Guardian Name")) {
                        guardianName = displayString[1];
                    } else if (displayString[0].equals("Tertiary Contact")) {
                        guardianName = displayString[1];
                    } else if (displayString[0].equals("Quaternary Contact")) {
                        guardianName = displayString[1];
                    }
                } else {

                    JSONObject attributeTypeObj = object.getJSONObject("attributeType");
                    String attributeType = attributeTypeObj.getString("display");

                    if (attributeType.equals("Marital Status")) {
                        maritalStatus = display;
                    } else if (attributeType.equals("Health Center")) {
                        healthCenter = display;
                    } else if (attributeType.equals("Primary Contact Owner")) {
                        primaryContactOwner = display;
                    } else if (attributeType.equals("Secondary Contact Owner")) {
                        secondaryContactOwner = display;
                    } else if (attributeType.equals("Ethnicity")) {
                        ethnicity = display;
                    } else if (attributeType.equals("Education Level")) {
                        educationLevel = display;
                    } else if (attributeType.equals("Employment Status")) {
                        employmentStatus = display;
                    } else if (attributeType.equals("Occupation")) {
                        occupation = display;
                    } else if (attributeType.equals("Mother Tongue")) {
                        motherTongue = display;
                    } else if (attributeType.equals("Income Class")) {
                        incomeClass = display;
                    } else if (attributeType.equals("National ID")) {
                        nationalId = display;
                    } else if (attributeType.equals("National ID Owner")) {
                        nationalIdOwner = display;
                    } else if (attributeType.equals("Guardian Name")) {
                        guardianName = display;
                    } else if (displayString[0].equals("Tertiary Contact")) {
                        guardianName = displayString[1];
                    } else if (displayString[0].equals("Quaternary Contact")) {
                        guardianName = displayString[1];
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            person = null;
        }
        person = new Person(uuid, givenName, familyName, age, birthdate, gender,
                birthPlace, citizenship, maritalStatus, healthCenter, healthDistrict, motherName,
                primaryContact, primaryContactOwner, secondaryContact, secondaryContactOwner, tertiaryContact, quaternaryContact,
                ethnicity, educationLevel, employmentStatus, occupation, motherTongue, incomeClass,
                nationalId, nationalIdOwner, guardianName,
                address1, address2, address3, stateProvince, cityVillage, country);
        return person;
    }

    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", givenName);
        } catch (JSONException e) {
            e.printStackTrace();
            jsonObject = null;
        }
        return jsonObject;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSecondaryContactOwner() {
        return secondaryContactOwner;
    }

    public void setSecondaryContactOwner(String secondaryContactOwner) {
        this.secondaryContactOwner = secondaryContactOwner;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getHealthCenter() {
        return healthCenter;
    }

    public void setHealthCenter(String healthCenter) {
        this.healthCenter = healthCenter;
    }

    public String getHealthDistrict() {
        return healthDistrict;
    }

    public void setHealthDistrict(String healthDistrict) {
        this.healthDistrict = healthDistrict;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(String primaryContact) {
        this.primaryContact = primaryContact;
    }

    public String getPrimaryContactOwner() {
        return primaryContactOwner;
    }

    public void setPrimaryContactOwner(String primaryContactOwner) {
        this.primaryContactOwner = primaryContactOwner;
    }

    public String getSecondaryContact() {
        return secondaryContact;
    }

    public void setSecondaryContact(String secondaryContact) {
        this.secondaryContact = secondaryContact;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getMotherTongue() {
        return motherTongue;
    }

    public void setMotherTongue(String motherTongue) {
        this.motherTongue = motherTongue;
    }

    public String getIncomeClass() {
        return incomeClass;
    }

    public void setIncomeClass(String incomeClass) {
        this.incomeClass = incomeClass;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getNationalIdOwner() {
        return nationalIdOwner;
    }

    public void setNationalIdOwner(String nationalIdOwner) {
        this.nationalIdOwner = nationalIdOwner;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getCityVillage() {
        return cityVillage;
    }

    public void setCityVillage(String cityVillage) {
        this.cityVillage = cityVillage;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + givenName;
    }
}