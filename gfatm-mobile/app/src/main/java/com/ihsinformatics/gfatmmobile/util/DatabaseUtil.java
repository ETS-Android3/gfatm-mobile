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

import com.ihsinformatics.gfatmmobile.R;

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
    private static final int DB_VERSION = 12;
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
}
