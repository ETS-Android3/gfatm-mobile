package com.ihsinformatics.gfatmmobile.medication.utils;

import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.DrugOrderEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MedicationUtils {

    public static boolean isCurrentActive(DrugOrderEntity entity) {
        try {
            if (entity.getDateStopped() != null) {
                String stopDate = entity.getDateStopped().substring(0, 10);
                Date dateStopped = null;

                dateStopped = new SimpleDateFormat("yyy-MM-dd").parse(stopDate);

                if (new Date().getTime() > dateStopped.getTime()) {
                    return false;
                }
                return false;
            }

            if (entity.getAutoExpireDate() != null) {
                String autoExp = entity.getAutoExpireDate().substring(0, 10);
                Date autoExpireDate = new SimpleDateFormat("yyy-MM-dd").parse(autoExp);
                if (new Date().getTime() > autoExpireDate.getTime()) {
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }

        return true;
    }
}
