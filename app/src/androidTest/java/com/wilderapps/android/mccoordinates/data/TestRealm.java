package com.wilderapps.android.mccoordinates.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by Owner on 12/16/2016.
 */

public class TestRealm extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteRecords();
        insertRecords();
        readRecords();
        updateRecords();
        readRecords();
    }

    public void deleteRecords(){
        CoordinateDbHelper dbHelper = new CoordinateDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(CoordinateContract.RealmEntry.TABLE_NAME, null, null);

        Cursor cursor = db.query(CoordinateContract.RealmEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertEquals("Error: Records not deleted from Table during delete", 0, cursor.getCount());
    }

    public void insertRecords(){
        CoordinateDbHelper dbHelper = new CoordinateDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CoordinateContract.RealmEntry.COLUMN_NAME, "Overworld");

        long newRowId = db.insert(CoordinateContract.RealmEntry.TABLE_NAME, null, values);
        assertTrue(newRowId != -1);

        Cursor cursor = db.query(CoordinateContract.RealmEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        TestUtilities.validateCursor("insert records.  Error validating RealmEntry.", cursor, values);

        values = new ContentValues();
        values.put(CoordinateContract.RealmEntry.COLUMN_NAME, "Nether");

        newRowId = db.insert(CoordinateContract.RealmEntry.TABLE_NAME, null, values);
        assertTrue(newRowId != -1);

        String selection = CoordinateContract.RealmEntry.COLUMN_NAME + " = ?";
        String[] selectionArgs = {"Nether"};

        cursor = db.query(CoordinateContract.RealmEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

        TestUtilities.validateCursor("insert records.  Error validating RealmEntry.", cursor, values);
        db.close();
    }

    public void updateRecords(){
        CoordinateDbHelper dbHelper = new CoordinateDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CoordinateContract.RealmEntry.COLUMN_NAME, "Hell");

        String selection = CoordinateContract.RealmEntry.COLUMN_NAME + " = ?";
        String[] selectionArgs = {"Nether"};

        int count = db.update(CoordinateContract.RealmEntry.TABLE_NAME, values, selection, selectionArgs);
        assertTrue(count == 1);

        db.close();
    }

    public void readRecords(){
        CoordinateDbHelper dbHelper = new CoordinateDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(CoordinateContract.RealmEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertTrue("Error: database has not been created correctly", cursor.moveToFirst());

        do{
            String realmName = cursor.getString(cursor.getColumnIndex(CoordinateContract.RealmEntry.COLUMN_NAME));
            String logMessage = "Realm name: " + realmName;

            Log.d(LOG_TAG, logMessage);
        } while(cursor.moveToNext());

        db.close();
    }
}
