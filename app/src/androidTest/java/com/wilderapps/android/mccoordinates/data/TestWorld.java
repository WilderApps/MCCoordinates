package com.wilderapps.android.mccoordinates.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by Masen on 12/16/2016.
 */

public class TestWorld extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
        insertRecords();
        readRecords();
        updateRecords();
        readRecords();
    }

    public void deleteAllRecords() {
        CoordinateDbHelper dbHelper = new CoordinateDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(CoordinateContract.WorldEntry.TABLE_NAME, null, null);

        Cursor cursor = db.query(CoordinateContract.WorldEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        assertEquals("Error: Records not deleted from World table during delete", 0, cursor.getCount());
        db.close();
    }

    public void insertRecords() {
        CoordinateDbHelper dbHelper = new CoordinateDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        values1.put(CoordinateContract.WorldEntry.COLUMN_NAME, "Boo Roo");
        values1.put(CoordinateContract.WorldEntry.COLUMN_SEED, "602115640565612316");

        ContentValues values2 = new ContentValues();
        values2.put(CoordinateContract.WorldEntry.COLUMN_NAME, "Here goes nothing");
        values2.put(CoordinateContract.WorldEntry.COLUMN_SEED, "Everything is awful");

        long newRowId = db.insert(CoordinateContract.WorldEntry.TABLE_NAME, null, values1);
        assertTrue(newRowId != -1);

        Cursor cursor = db.query(CoordinateContract.WorldEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        TestUtilities.validateCursor("insertRecords. Error validating WorldEntry.", cursor, values1);


        newRowId = db.insert(CoordinateContract.WorldEntry.TABLE_NAME, null, values2);
        assertTrue(newRowId != -1);

        String selection = CoordinateContract.WorldEntry.COLUMN_NAME + " = ?";
        String[] selectionArgs = {values2.getAsString(CoordinateContract.WorldEntry.COLUMN_NAME)};

        cursor = db.query(CoordinateContract.WorldEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

        TestUtilities.validateCursor("insertRecords. Error validating WorldEntry.", cursor, values2);

        db.close();
    }

    public void updateRecords() {
        CoordinateDbHelper dbHelper = new CoordinateDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CoordinateContract.WorldEntry.COLUMN_NAME, "I R GROOT");

        String selection = CoordinateContract.WorldEntry.COLUMN_NAME + " = ?";
        String[] selectionArgs = {"Boo Roo"};

        int count = db.update(CoordinateContract.WorldEntry.TABLE_NAME, values, selection, selectionArgs);
        assertTrue(count == 1);

        db.close();
    }

    public void readRecords() {
        CoordinateDbHelper dbHelper = new CoordinateDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(CoordinateContract.WorldEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertTrue("Error: database has not been created correctly", cursor.moveToFirst());

        do {
            String worldName = cursor.getString(cursor.getColumnIndex(CoordinateContract.WorldEntry.COLUMN_NAME));
            String worldSeed = cursor.getString(cursor.getColumnIndex(CoordinateContract.WorldEntry.COLUMN_SEED));
            String logMessage = "World name: " + worldName + " Seed: " + worldSeed;

            Log.d(LOG_TAG, logMessage);
        } while (cursor.moveToNext());

        db.close();
    }
}
