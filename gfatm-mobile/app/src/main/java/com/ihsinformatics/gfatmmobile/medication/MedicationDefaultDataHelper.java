package com.ihsinformatics.gfatmmobile.medication;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.DataAccess;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDrug;
import com.ihsinformatics.gfatmmobile.medication.gson_pojos.Drug;
import com.ihsinformatics.gfatmmobile.medication.gson_pojos.MedicationDoseUnit;
import com.ihsinformatics.gfatmmobile.medication.gson_pojos.MedicationFrequency;
import com.ihsinformatics.gfatmmobile.medication.gson_pojos.MedicationRoute;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MedicationDefaultDataHelper {

    public void insert(Context context) {
        // reading data
        String drugString = getJsonFromAssets(context, "drugs.json");
        String frequencyString = getJsonFromAssets(context, "medication_frequency.json");
        String routeString = getJsonFromAssets(context, "medication_route.json");
        String dosageString = getJsonFromAssets(context, "medication_dosage.json");

        // Preparing data
        Gson gson = new Gson();
        Type drugType = new TypeToken<List<Drug>>() { }.getType();
        List<Drug> drugs = gson.fromJson(drugString, drugType);
        List<MedicationDrug> drugEntities = new ArrayList<>();
        for(Drug d: drugs) {
            drugEntities.add(Drug.copyProperties(new MedicationDrug(), d));
        }

        Type doseType = new TypeToken<List<MedicationDoseUnit>>() { }.getType();
        List<MedicationDoseUnit> doses = gson.fromJson(dosageString, doseType);
        List<com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDoseUnit> dosesEntities = new ArrayList<>();
        for(MedicationDoseUnit d: doses) {
            dosesEntities.add(MedicationDoseUnit.copyProperties(new com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDoseUnit(), d));
        }

        Type frequencyType = new TypeToken<List<MedicationFrequency>>() { }.getType();
        List<MedicationFrequency> frequencies = gson.fromJson(frequencyString, frequencyType);
        List<com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationFrequency> frequencyEntities = new ArrayList<>();
        for(MedicationFrequency d: frequencies) {
            frequencyEntities.add(MedicationFrequency.copyProperties(new com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationFrequency(), d));
        }

        Type routeType = new TypeToken<List<MedicationRoute>>() { }.getType();
        List<MedicationRoute> routes = gson.fromJson(routeString, routeType);
        List<com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationRoute> routEntities = new ArrayList<>();
        for(MedicationRoute d: routes) {
            routEntities.add(MedicationRoute.copyProperties(new com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationRoute(), d));
        }

        DataAccess dataAccess = DataAccess.getInstance();
        dataAccess.insertAllDrugs(drugEntities);
        dataAccess.insertAllDoses(dosesEntities);
        dataAccess.insertAllFrequencies(frequencyEntities);
        dataAccess.insertAllRoutes(routEntities);
    }

    static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }
}
