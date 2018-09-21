package com.wilderapps.android.mccoordinates.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by Owner on 12/17/2016.
 */

public class TestCoordinates extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
        insertRecords();
    }

    public void deleteAllRecords(){
        CoordinateDbHelper dbHelper = new CoordinateDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(CoordinateContract.CoordinateEntry.TABLE_NAME, null, null);

        Cursor cursor = db.query(CoordinateContract.CoordinateEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertEquals("Error: table not properly delete", 0, cursor.getCount());
        db.close();
    }

    public void insertRecords(){
        CoordinateDbHelper dbHelper = new CoordinateDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues coordinateValues = new ContentValues();

        db.delete(CoordinateContract.WorldEntry.TABLE_NAME, null, null);

        ContentValues values1 = new ContentValues();
        values1.put(CoordinateContract.WorldEntry.COLUMN_NAME, "Boo Roo");
        values1.put(CoordinateContract.WorldEntry.COLUMN_SEED, "602115640565612316");

        long newRowId = db.insert(CoordinateContract.WorldEntry.TABLE_NAME, null, values1);
        Log.d(LOG_TAG, "New Row Id: " + newRowId);
        assertTrue(newRowId != -1);

        Cursor cursor = db.query(CoordinateContract.WorldEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        TestUtilities.validateCursor("insertRecords. Error validating WorldEntry.", cursor, values1);

        assertEquals("Error: no data entered", 1, cursor.getCount());

        cursor = db.query(CoordinateContract.WorldEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        Log.d(LOG_TAG, cursor.getString(cursor.getColumnIndex(CoordinateContract.WorldEntry.COLUMN_NAME)));

        ContentValues updateValues = new ContentValues();
        updateValues.put(CoordinateContract.WorldEntry.COLUMN_NAME, "Table updated");
        String whereClause = CoordinateContract.WorldEntry.COLUMN_NAME + " = ?";
        String[] whereArgs = {"Boo Roo"};
        db.update(CoordinateContract.WorldEntry.TABLE_NAME, updateValues, whereClause, whereArgs);

        cursor = db.query(CoordinateContract.WorldEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        Log.d(LOG_TAG, cursor.getString(cursor.getColumnIndex(CoordinateContract.WorldEntry.COLUMN_NAME)));
        String worldName = cursor.getString(cursor.getColumnIndex(CoordinateContract.WorldEntry.COLUMN_NAME));
        assertTrue(worldName.equals(updateValues.getAsString(CoordinateContract.WorldEntry.COLUMN_NAME)));

        coordinateValues.put(CoordinateContract.CoordinateEntry.COLUMN_WORLD_KEY, cursor.getInt(cursor.getColumnIndex(CoordinateContract.WorldEntry._ID)));


        Log.d(LOG_TAG, coordinateValues.getAsString(CoordinateContract.CoordinateEntry.COLUMN_WORLD_KEY));

        db.delete(CoordinateContract.RealmEntry.TABLE_NAME, null, null);

        ContentValues values = new ContentValues();
        values.put(CoordinateContract.RealmEntry.COLUMN_NAME, "Overworld");

        newRowId = db.insert(CoordinateContract.RealmEntry.TABLE_NAME, null, values);
        assertTrue(newRowId != -1);

        cursor = db.query(CoordinateContract.RealmEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        coordinateValues.put(CoordinateContract.CoordinateEntry.COLUMN_REALM_KEY, cursor.getInt(cursor.getColumnIndex(CoordinateContract.RealmEntry._ID)));

        Log.d(LOG_TAG, coordinateValues.getAsString(CoordinateContract.CoordinateEntry.COLUMN_REALM_KEY));

        TestUtilities.validateCursor("insert records.  Error validating RealmEntry.", cursor, values);

        double coords = 45.0;
        Log.d(LOG_TAG, "Double coords is " + coords);

        coordinateValues.put(CoordinateContract.CoordinateEntry.COLUMN_NAME, "Home");
        coordinateValues.put(CoordinateContract.CoordinateEntry.COLUMN_X_COORDINATE, coords);
        coordinateValues.put(CoordinateContract.CoordinateEntry.COLUMN_Y_COORDINATE, coords);
        coordinateValues.put(CoordinateContract.CoordinateEntry.COLUMN_Z_COORDINATE, coords);

        newRowId = db.insert(CoordinateContract.CoordinateEntry.TABLE_NAME, null, coordinateValues);
        assertTrue("Error: no data inserted", newRowId != -1);

        cursor = db.query(CoordinateContract.CoordinateEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        double dbValue = cursor.getDouble(cursor.getColumnIndex(CoordinateContract.CoordinateEntry.COLUMN_X_COORDINATE));
        double cvValue = coordinateValues.getAsDouble(CoordinateContract.CoordinateEntry.COLUMN_X_COORDINATE);

        Log.d(LOG_TAG, "Cursor value: " + dbValue + " CV value: " + cvValue);
//        TestUtilities.validateCursor("insertRecords.  Error validating CoordinateEntry", cursor, coordinateValues);

        whereArgs[0] = "Table updated";
        int deleted = db.delete(CoordinateContract.WorldEntry.TABLE_NAME, CoordinateContract.WorldEntry.COLUMN_NAME + " = ?", whereArgs);
        Log.d(LOG_TAG, "Number of records deleted: " + deleted);
        cursor = db.query(CoordinateContract.CoordinateEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        String coordinate = cursor.toString();
        Log.d(LOG_TAG, coordinate);
        Log.d(LOG_TAG, "Records found: " + cursor.getCount());
        assertTrue("Error: results found", cursor.getCount() == 0);
    }
}
