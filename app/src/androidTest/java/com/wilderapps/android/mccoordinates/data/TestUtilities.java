package com.wilderapps.android.mccoordinates.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by Owner on 12/13/2016.
 */

public class TestUtilities extends AndroidTestCase {

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues){
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues){
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createWorldValues(){
        ContentValues worldValues = new ContentValues();
        worldValues.put(CoordinateContract.WorldEntry.COLUMN_NAME, "First World Problems");
        worldValues.put(CoordinateContract.WorldEntry.COLUMN_SEED, "485465123132961502301655141");

        return worldValues;
    }

    static ArrayList<ContentValues> createDefaultRealms(){
        ArrayList<ContentValues> defaultRealms = new ArrayList<>();

        ContentValues overworld = new ContentValues();
        overworld.put(CoordinateContract.RealmEntry.COLUMN_NAME, "Overworld");

        ContentValues nether = new ContentValues();
        nether.put(CoordinateContract.RealmEntry.COLUMN_NAME, "Nether");

        ContentValues end = new ContentValues();
        end.put(CoordinateContract.RealmEntry.COLUMN_NAME, "The End");

        defaultRealms.add(overworld);
        defaultRealms.add(nether);
        defaultRealms.add(end);

        return defaultRealms;
    }

    static ContentValues createCoordinate(float worldId, float realmId){
        ContentValues coordinate = new ContentValues();

        coordinate.put(CoordinateContract.CoordinateEntry.COLUMN_WORLD_KEY, worldId);
        coordinate.put(CoordinateContract.CoordinateEntry.COLUMN_REALM_KEY, realmId);
        coordinate.put(CoordinateContract.CoordinateEntry.COLUMN_NAME, "Home");
        coordinate.put(CoordinateContract.CoordinateEntry.COLUMN_X_COORDINATE, 16.75);
        coordinate.put(CoordinateContract.CoordinateEntry.COLUMN_Y_COORDINATE, 84.45);
        coordinate.put(CoordinateContract.CoordinateEntry.COLUMN_Z_COORDINATE, -75.5);

        return coordinate;
    }
}
