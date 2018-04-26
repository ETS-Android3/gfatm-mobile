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

package com.ihsinformatics.gfatmmobile.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.shared.Metadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author owais.hussain@irdinformatics.org
 */
public class DatabaseUtil extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseUtil";
    private static final String DB_NAME = "globalfund.db";
    private static final int DB_VERSION = 62;
    private Context context;

    public DatabaseUtil(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == newVersion) {
            return;
        }

        InputStream insertsStream = null;

        switch (oldVersion) {
            case 0: // Script to upgrade from version 0 to 1
                break;
            case 1: // Script to upgrade from version 1 to 2
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v2);
                break;
            case 2: // Script to upgrade from version 2 to 3
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v3);
                break;
            case 3: // Script to upgrade from version 3 to 4
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v4);
                break;
            case 4: // Script to upgrade from version 4 to 5
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v5);
                break;
            case 5: // Script to upgrade from version 5 to 6
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v6);
                break;
            case 6: // Script to upgrade from version 6 to 7
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v7);
                break;
            case 7: // Script to upgrade from version 7 to 8
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v8);
                break;
            case 8: // Script to upgrade from version 8 to 9
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v9);
                break;
            case 9: // Script to upgrade from version 9 to 10
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v10);
                break;
            case 10: // Script to upgrade from version 10 to 11
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v11);
                break;
            case 11: // Script to upgrade from version 11 to 12
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v12);
                break;
            case 12: // Script to upgrade from version 12 to 13
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v13);
                break;
            case 13: // Script to upgrade from version 13 to 14
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v14);
                break;
            case 14: // Script to upgrade from version 14 to 15
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v15);
                break;
            case 15: // Script to upgrade from version 15 to 16
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v16);
                break;
            case 16: // Script to upgrade from version 16 to 17
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v17);
                break;
            case 17: // Script to upgrade from version 17 to 18
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v18);
                break;
            case 18: // Script to upgrade from version 18 to 19
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v19);
                break;
            case 19: // Script to upgrade from version 19 to 20
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v20);
                break;
            case 20: // Script to upgrade from version 20 to 21
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v21);
                break;
            case 21: // Script to upgrade from version 21 to 22
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v22);
                break;
            case 22: // Script to upgrade from version 22 to 23
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v23);
                break;
            case 23: // Script to upgrade from version 23 to 24
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v24);
                break;
            case 24: // Script to upgrade from version 24 to 25
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v25);
                break;
            case 25: // Script to upgrade from version 25 to 26
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v26);
                break;
            case 26: // Script to upgrade from version 26 to 27
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v27);
                break;
            case 27: // Script to upgrade from version 27 to 28
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v28);
                break;
            case 28: // Script to upgrade from version 28 to 29
                if (!isColumnExists("form", "autoSyncTries")) {
                    insertsStream = context.getResources().openRawResource(R.raw.db_update_v29_offline_forms_sync);
                }
                break;
            case 29: // Script to upgrade from version 29 to 30
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v30);
                break;
            case 30: // Script to upgrade from version 30 to 31
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v31);
                break;
            case 31: // Script to upgrade from version 31 to 32
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v32);
                break;
            case 32: // Script to upgrade from version 32 to 33
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v33);
                break;
            case 33: // Script to upgrade from version 33 to 34
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v34);
                break;
            case 34: // Script to upgrade from version 34 to 35
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v35);
                break;
            case 35: // Script to upgrade from version 35 to 36
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v36);
                break;
            case 36: // Script to upgrade from version 36 to 37
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v37);
                break;
            case 37: // Script to upgrade from version 37 to 38
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v38);
                break;
            case 38: // Script to upgrade from version 38 to 39
                // Removed due to bug in live apk
                //insertsStream = context.getResources().openRawResource(R.raw.db_update_v39);
                break;
            case 39: // Script to upgrade from version 39 to 40
                // Removed due to bug in live apk
                break;
            case 40: // Script to upgrade from version 40 to 41
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v41);
                break;
            case 41: // Script to upgrade from version 41 to 42
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v42);
                break;
            case 42: // Script to upgrade from version 42 to 43
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v43);
                break;
            case 43: // Script to upgrade from version 43 to 44

                int birthplaceAttrId = 2;
                int citizenshipAttrId = 3;
                int maritalStatusAttrId = 5;
                int healthCenterAttrId = 7;
                int healthDistrictAttrId = 6;
                int motherNameAttrId = 4;
                int primaryContactAttrId = 8;
                int primaryContactOwnerAttrId = 11;
                int secondaryContactAttrId = 12;
                int secondaryContactOwnerAttrId = 13;
                int ethinicityAttrId = 14;
                int educationLevelAttrId = 15;
                int employmentStatusAttrId = 16;
                int occupationAttrId = 17;
                int incomeClassAttrId = 20;
                int motherTongueAttrId = 18;
                int nationIdAttrId = 21;
                int nationalIdOwnerAttrId = 22;
                int guardianNameAttrId = 26;
                int tertiaryContactAttrId = 23;
                int quaternaryContactAttrId = 24;
                int treatmentSupporterAttrId = 25;

                if (isColumnExists(Metadata.PATIENT, "birthplace")) {
                    String query = "Select patient_id, birthplace from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String birthplace = result[1];

                        if (!(birthplace == null || birthplace.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", birthplaceAttrId);
                            values.put("value", birthplace);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "citizenship")) {
                    String query = "Select patient_id, citizenship from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String citizenship = result[1];

                        if (!(citizenship == null || citizenship.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", citizenshipAttrId);
                            values.put("value", citizenship);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "maritalstatus")) {
                    String query = "Select patient_id, maritalstatus from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String maritalStatus = result[1];

                        if (!(maritalStatus == null || maritalStatus.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", maritalStatusAttrId);
                            values.put("value", maritalStatus);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "healthcenter")) {
                    String query = "Select patient_id, healthcenter from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String healthCenter = result[1];

                        if (!(healthCenter == null || healthCenter.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", healthCenterAttrId);
                            values.put("value", healthCenter);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "healthdistrict")) {
                    String query = "Select patient_id, healthdistrict from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] healthDistricts = data.toArray(new String[][]{});

                    for (String[] result : healthDistricts) {

                        String pid = result[0];
                        String birthplace = result[1];

                        if (!(birthplace == null || birthplace.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", healthDistrictAttrId);
                            values.put("value", birthplace);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "mothername")) {
                    String query = "Select patient_id, mothername from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String motherName = result[1];

                        if (!(motherName == null || motherName.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", motherNameAttrId);
                            values.put("value", motherName);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "primarycontact")) {
                    String query = "Select patient_id, primarycontact from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String primaryContact = result[1];

                        if (!(primaryContact == null || primaryContact.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", primaryContactAttrId);
                            values.put("value", primaryContact);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "primarycontactowner")) {
                    String query = "Select patient_id, primarycontactowner from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String primaryContactOwner = result[1];

                        if (!(primaryContactOwner == null || primaryContactOwner.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", primaryContactOwnerAttrId);
                            values.put("value", primaryContactOwner);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "secondarycontact")) {
                    String query = "Select patient_id, secondarycontact from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String secondaryContact = result[1];

                        if (!(secondaryContact == null || secondaryContact.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", secondaryContactAttrId);
                            values.put("value", secondaryContact);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "secondarycontactOwner")) {
                    String query = "Select patient_id, secondarycontactOwner from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String secondaryContactOwner = result[1];

                        if (!(secondaryContactOwner == null || secondaryContactOwner.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", secondaryContactOwnerAttrId);
                            values.put("value", secondaryContactOwner);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "ethnicity")) {
                    String query = "Select patient_id, ethnicity from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String ethinicity = result[1];

                        if (!(ethinicity == null || ethinicity.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", ethinicityAttrId);
                            values.put("value", ethinicity);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "educationlevel")) {
                    String query = "Select patient_id, educationlevel from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String educationLevel = result[1];

                        if (!(educationLevel == null || educationLevel.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", educationLevelAttrId);
                            values.put("value", educationLevel);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "employmentstatus")) {
                    String query = "Select patient_id, employmentstatus from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String employmentStatus = result[1];

                        if (!(employmentStatus == null || employmentStatus.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", employmentStatusAttrId);
                            values.put("value", employmentStatus);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "occupation")) {
                    String query = "Select patient_id, occupation from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String occupation = result[1];

                        if (!(occupation == null || occupation.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", occupationAttrId);
                            values.put("value", occupation);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "incomeClass")) {
                    String query = "Select patient_id, incomeClass from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String incomeClass = result[1];

                        if (!(incomeClass == null || incomeClass.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", incomeClassAttrId);
                            values.put("value", incomeClass);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "mothertongue")) {
                    String query = "Select patient_id, mothertongue from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String motherTongue = result[1];

                        if (!(motherTongue == null || motherTongue.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", motherTongueAttrId);
                            values.put("value", motherTongue);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "nationalid")) {
                    String query = "Select patient_id, nationalid from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String nationalId = result[1];

                        if (!(nationalId == null || nationalId.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", nationIdAttrId);
                            values.put("value", nationalId);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "nationalidowner")) {
                    String query = "Select patient_id, nationalidowner from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String nationalIdOwner = result[1];

                        if (!(nationalIdOwner == null || nationalIdOwner.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", nationalIdOwnerAttrId);
                            values.put("value", nationalIdOwner);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "guardianname")) {
                    String query = "Select patient_id, guardianname from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String guardiaName = result[1];

                        if (!(guardiaName == null || guardiaName.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", guardianNameAttrId);
                            values.put("value", guardiaName);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "tertiarycontact")) {
                    String query = "Select patient_id, tertiarycontact from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String tertiaryContact = result[1];

                        if (!(tertiaryContact == null || tertiaryContact.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", tertiaryContactAttrId);
                            values.put("value", tertiaryContact);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "quaternarycontact")) {
                    String query = "Select patient_id, quaternarycontact from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String quaternaryContact = result[1];

                        if (!(quaternaryContact == null || quaternaryContact.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", quaternaryContactAttrId);
                            values.put("value", quaternaryContact);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                if (isColumnExists(Metadata.PATIENT, "treatmentsupporter")) {
                    String query = "Select patient_id, treatmentsupporter from " + Metadata.PATIENT;

                    ArrayList<String[]> data = new ArrayList<String[]>();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor != null) {
                        int columns = cursor.getColumnCount();
                        if (cursor.moveToFirst()) {
                            do {
                                String[] record = new String[columns];
                                for (int i = 0; i < columns; i++)
                                    record[i] = cursor.getString(i);
                                data.add(record);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[][] results = data.toArray(new String[][]{});

                    for (String[] result : results) {

                        String pid = result[0];
                        String treatmentSupporter = result[1];

                        if (!(treatmentSupporter == null || treatmentSupporter.equals(""))) {
                            ContentValues values = new ContentValues();
                            values.put("person_attribute_type", treatmentSupporterAttrId);
                            values.put("value", treatmentSupporter);
                            values.put("patient_id", pid);
                            db.insert(Metadata.PERSON_ATTRIBUTE, null, values);
                        }
                    }
                }

                break;
            case 44: // Script to upgrade from version 44 to 45
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v45);
                break;
            case 45: // Script to upgrade from version 45 to 46
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v46);
                break;
            case 46: // Script to upgrade from version 46 to 47

                ArrayList<String> count = new ArrayList<String>();

                Cursor cursor = db.rawQuery("select count(*) from " + Metadata.PATIENT, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            count.add(cursor.getString(0));
                        }
                        while (cursor.moveToNext());
                    }
                }
                String[] results = count.toArray(new String[]{});
                int patientCount = Integer.parseInt(String.valueOf(results[0]));

                if(patientCount > App.PATIENT_COUNT_CAP){

                    int limit = patientCount - App.PATIENT_COUNT_CAP;

                    ArrayList<String> data = new ArrayList<String>();
                    cursor = db.rawQuery("Select patient_id from " + Metadata.PATIENT + " where 1=1 order by patient_id asc limit " + limit, null);
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            do {
                                data.add(cursor.getString(0));
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    String[] result = data.toArray(new String[]{});

                    for(int i = 0; i < result.length; i++) {
                        String patientId = result[i];
                        db.delete(Metadata.PATIENT, "patient_id=?", new String[]{patientId});
                        db.delete(Metadata.PERSON_ATTRIBUTE, "patient_id=?", new String[]{patientId});

                        ArrayList<String> data1 = new ArrayList<String>();
                        cursor = db.rawQuery("select encounter_id from " + Metadata.ENCOUNTER + " where patientId='" + patientId + "'", null);
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                do {
                                    data1.add(cursor.getString(0));
                                }
                                while (cursor.moveToNext());
                            }
                        }
                        String[] encounter = data1.toArray(new String[]{});

                        if (!(encounter.length < 1)) {
                            for (int j = 0; j < encounter.length; j++) {
                                db.delete(Metadata.ENCOUNTER, "encounter_id=?", new String[]{String.valueOf(encounter[j])});
                                db.delete(Metadata.OBS, "encounter_id=?", new String[]{String.valueOf(encounter[j])});
                            }
                        }
                        db.delete(Metadata.TEST_ID, "pid=?", new String[]{patientId});
                        ContentValues values = new ContentValues();
                        values.put("p_id", "");
                        db.update(Metadata.FORM, values, "p_id=?", new String[]{patientId});
                    }

                }
                break;
            case 47: // Script to upgrade from version 47 to 48
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v48);
                break;
            case 48: // Script to upgrade from version 48 to 49
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v49);
                break;
            case 49: // Script to upgrade from version 49 to 50
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v50);
                break;
            case 50: // Script to upgrade from version 50 to 51
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v51);
                break;
            case 51: // Script to upgrade from version 51 to 52
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v52);
                break;
            case 52: // Script to upgrade from version 52 to 53
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v53);
                break;
            case 53: // Script to upgrade from version 53 to 54
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v54);
                break;
            case 54: // Script to upgrade from version 54 to 55
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v55);
                break;
            case 55: // Script to upgrade from version 55 to 56
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v56);
                break;
            case 56: // Script to upgrade from version 56 to 57
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v57);
                break;
            case 57: // Script to upgrade from version 57 to 58
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v58);
                break;
            case 58: // Script to upgrade from version 58 to 59
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v59);
                break;
            case 59: // Script to upgrade from version 59 to 60
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v60);
                break;
            case 60: // Script to upgrade from version 59 to 60
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v61);
                break;
            case 61: // Script to upgrade from version 60 to 61
                insertsStream = context.getResources().openRawResource(R.raw.db_update_v62);
                break;

        }

        if(insertsStream !=  null) {
            BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));
            String insertStmt = "";
            try {
                while (insertReader.ready()) {
                    insertStmt = insertReader.readLine();
                    if (!(insertsStream == null || insertStmt.startsWith("--") || insertStmt.equals("") || insertStmt.equals("null")))
                        db.execSQL(insertStmt);
                }
                insertReader.close();
            } catch (Exception e) {
                Log.e(TAG, "insert error:" + insertStmt);
            }
        }

        onUpgrade(db, ++oldVersion, newVersion);
    }

    public boolean doesDatabaseExist() {
        File dbFile = context.getDatabasePath(DB_NAME);
        return dbFile.exists();
    }

    /**
     * Builds Sqlite database using schema queries defined in resources. If
     * createNew is true, then all existing table will be and recreated
     *
     * @param createNew
     */
    public void buildDatabase(boolean createNew) {
        if (createNew)
            context.deleteDatabase(DB_NAME);
        String[] tables = context.getResources().getStringArray(R.array.tables);
        SQLiteDatabase db = getWritableDatabase();
        for (String s : tables) {
            db.execSQL(s);
        }
        Log.i(TAG, "Database created.");

        InputStream insertsStream = context.getResources().openRawResource(R.raw.metadata);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));
        String insertStmt = "";
        try {
            while (insertReader.ready()) {
                insertStmt = insertReader.readLine();
                if (!(insertsStream == null || insertStmt.startsWith("--") || insertStmt.equals("") || insertStmt.equals("null")))
                    db.execSQL(insertStmt);
            }
            insertReader.close();

        } catch (Exception e) {
            Log.e(TAG, "insert error:" + insertStmt);
        }
        Log.i(TAG, "Inserts completed.");
        db.close();
    }

    /**
     * Insert records into local database
     *
     * @param table
     * @param values
     * @return
     */
    public synchronized boolean insert(String table, ContentValues values) {
        DatabaseUtil util = new DatabaseUtil(context);
        SQLiteDatabase writableDatabase = util.getWritableDatabase();
        long result = writableDatabase.insert(table, null, values);
        if (result == -1) {
            result = writableDatabase.insert(table, null, values);
        }
        writableDatabase.close();
        boolean check = (result != -1);
        if (check) {
            Log.i(TAG, "Record inserted in table: " + table);
        } else {
            Log.i(TAG, "Record not inserted in table: " + table + ". Error code: " + result);
        }
        return check;
    }

    public synchronized boolean inserts(String table, ContentValues[] values) {
        for(ContentValues cv : values){
            boolean check = insert(table,cv);
            if(!check)
                return false;
        }
        return true;
    }

    /**
     * Delete records from local database
     *
     * @param table
     * @param whereClause where = value1 = ? AND value2 = ? AND value3 = ?
     * @param whereArgs   whereArgs = {string1, string2, string3}
     * @return
     */
    public synchronized boolean delete(String table, String whereClause, String[] whereArgs) {
        DatabaseUtil util = new DatabaseUtil(context);
        SQLiteDatabase writableDatabase = util.getWritableDatabase();
        long result = writableDatabase.delete(table, whereClause, whereArgs);
        writableDatabase.close();
        boolean check = (result != -1);
        if (check) {
            Log.i(TAG, "Record deleted from table: " + table);
        } else {
            Log.i(TAG, "Record not deleted from table: " + table + ". Error code: " + result);
        }
        return check;
    }

    /**
     * Update records in local database
     *
     * @param table
     * @param values
     * @param whereClause where = value1 = ? AND value2 = ? AND value3 = ?
     * @param whereArgs   whereArgs = {string1, string2, string3}
     * @return
     */
    public synchronized boolean update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        DatabaseUtil util = new DatabaseUtil(context);
        SQLiteDatabase writableDatabase = util.getWritableDatabase();
        long result = writableDatabase.update(table, values, whereClause, whereArgs);
        writableDatabase.close();
        boolean check = (result != -1);
        if (check) {
            Log.i(TAG, "Record updated in table: " + table);
        } else {
            Log.i(TAG, "Record not updated in table: " + table + ". Error code: " + result);
        }
        return check;
    }

    /**
     * Get an object in string from local database
     *
     * @param query
     * @return
     */
    public String getObject(String query) {
        String result = null;
        try {
            result = getColumnData(query)[0];
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * Get an object in string from local database using given parameters
     *
     * @param table
     * @param column
     * @param whereClause
     * @return
     */
    public String getObject(String table, String column, String whereClause) {
        return getObject("SELECT " + column + " FROM " + table + " WHERE " + whereClause);
    }

    /**
     * Get a list of values in a column from local database using given
     * parameters
     *
     * @param table
     * @param column
     * @param whereClause
     * @param distinct
     * @return
     */
    public String[] getColumnData(String table, String column, String whereClause, boolean distinct) {
        String[] result = new String[0];
        try {
            String query = "SELECT " + (distinct ? "DISTINCT " : "") + column + " FROM " + table + " WHERE " + whereClause;
            result = getColumnData(query);
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * Get a list of values in a column from local database using given query
     *
     * @param query
     * @return
     */
    public String[] getColumnData(String query) {
        DatabaseUtil util = new DatabaseUtil(context);
        SQLiteDatabase readableDatabase = util.getReadableDatabase();
        ArrayList<String> data = new ArrayList<String>();
        Cursor cursor = readableDatabase.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    data.add(cursor.getString(0));
                }
                while (cursor.moveToNext());
            }
        }
        readableDatabase.close();
        return data.toArray(new String[]{});
    }

    /**
     * Get a single record local database using given query and return the
     * values in an array
     *
     * @param query
     * @return
     */
    public String[] getRecord(String query) {
        DatabaseUtil util = new DatabaseUtil(context);
        SQLiteDatabase readableDatabase = util.getReadableDatabase();
        ArrayList<String> data = new ArrayList<String>();
        Cursor cursor = readableDatabase.rawQuery(query, null);
        if (cursor != null) {
            int columns = cursor.getColumnCount();
            if (cursor.moveToFirst()) {
                for (int i = 0; i < columns; i++)
                    data.add(cursor.getString(i));
            }
        }
        readableDatabase.close();
        return data.toArray(new String[]{});
    }

    /**
     * Get data in a 2-D table from local database using given parameters
     *
     * @param table
     * @param columns
     * @param whereClause
     * @return
     */
    public String[][] getTableData(String table, String columns, String whereClause) {
        String query = "SELECT " + columns + " FROM " + table + " WHERE " + whereClause;
        return getTableData(query);
    }

    /**
     * Get data in a 2-D table from local database using given query
     *
     * @param query
     * @return
     */
    public String[][] getTableData(String query) {
        DatabaseUtil util = new DatabaseUtil(context);
        SQLiteDatabase readableDatabase = util.getReadableDatabase();
        ArrayList<String[]> data = new ArrayList<String[]>();
        Cursor cursor = readableDatabase.rawQuery(query, null);
        if (cursor != null) {
            int columns = cursor.getColumnCount();
            if (cursor.moveToFirst()) {
                do {
                    String[] record = new String[columns];
                    for (int i = 0; i < columns; i++)
                        record[i] = cursor.getString(i);
                    data.add(record);
                }
                while (cursor.moveToNext());
            }
        }
        readableDatabase.close();
        return data.toArray(new String[][]{});
    }


    /**
     * Get data in a 2-D table from local database using given query
     *
     * @param query
     * @return
     */
    public Object[][] getFormTableData(String query) {
        DatabaseUtil util = new DatabaseUtil(context);
        SQLiteDatabase readableDatabase = util.getReadableDatabase();
        ArrayList<Object[]> data = new ArrayList<Object[]>();
        Cursor cursor = readableDatabase.rawQuery(query, null);
        if (cursor != null) {
            int columns = cursor.getColumnCount();
            if (cursor.moveToFirst()) {
                do {
                    Object[] record = new Object[columns];
                    for (int i = 0; i < columns; i++) {
                        if (cursor.getColumnName(i).equals("form_object"))
                            record[i] = cursor.getBlob(i);
                        else
                            record[i] = cursor.getString(i);
                    }
                    data.add(record);
                }
                while (cursor.moveToNext());
            }
        }
        readableDatabase.close();
        return data.toArray(new Object[][]{});
    }

    public static String getDbName(){
        return DB_NAME;
    }

    public boolean isColumnExists(String tableName, String columnToFind) {

        DatabaseUtil util = new DatabaseUtil(context);
        SQLiteDatabase readableDatabase = util.getReadableDatabase();

        Cursor cursor = null;

        try {
            cursor = readableDatabase.rawQuery(
                    "PRAGMA table_info(" + tableName + ")",
                    null
            );

            int nameColumnIndex = cursor.getColumnIndexOrThrow("name");

            while (cursor.moveToNext()) {
                String name = cursor.getString(nameColumnIndex);

                if (name.equals(columnToFind)) {
                    return true;
                }
            }

            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
