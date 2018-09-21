package com.wilderapps.android.mccoordinates;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.wilderapps.android.mccoordinates.data.CoordinateContract;
import com.wilderapps.android.mccoordinates.data.CoordinateDbHelper;

import java.util.ArrayList;

/**
 * Created by Masen on 1/5/2017.
 */

public class Utility {
    public Utility() {
    }

    public AddCoordinateDialog setUpAddCoordinate(Context context, int worldId, int tabSelected){
        CoordinateDbHelper dbHelper = new CoordinateDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> realmNames = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + CoordinateContract.RealmEntry.TABLE_NAME, null);
        cursor.moveToFirst();

        do {
            String name = cursor.getString(cursor.getColumnIndex(CoordinateContract.RealmEntry.COLUMN_NAME));
            realmNames.add(name);
        } while (cursor.moveToNext());

        cursor.close();
        db.close();
        dbHelper.close();

        AddCoordinateDialog addCoordinateDialog = new AddCoordinateDialog();

        Bundle args = new Bundle();
        args.putInt("id", -1);
        args.putInt("worldId", worldId);
        args.putStringArrayList("realms", realmNames);
        args.putInt("tabSelected", tabSelected);
        addCoordinateDialog.setArguments(args);

        return addCoordinateDialog;
    }

    public AddCoordinateDialog setUpAddCoordinate(Context context, Bundle bundle){
        CoordinateDbHelper dbHelper = new CoordinateDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> realmNames = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + CoordinateContract.RealmEntry.TABLE_NAME, null);
        cursor.moveToFirst();

        do {
            String name = cursor.getString(cursor.getColumnIndex(CoordinateContract.RealmEntry.COLUMN_NAME));
            realmNames.add(name);
        } while (cursor.moveToNext());

        cursor.close();
        db.close();
        dbHelper.close();

        AddCoordinateDialog addCoordinateDialog = new AddCoordinateDialog();

        bundle.putStringArrayList("realms", realmNames);

        addCoordinateDialog.setArguments(bundle);

        return addCoordinateDialog;
    }

    public String getRealmName(Context context, int realmId){
        CoordinateDbHelper dbHelper = new CoordinateDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String whereClause = CoordinateContract.RealmEntry._ID + " = ?";
        String[] whereArgs = {Integer.toString(realmId)};

        Cursor cursor = db.query(CoordinateContract.RealmEntry.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        cursor.moveToFirst();

        String realmName = cursor.getString(cursor.getColumnIndex(CoordinateContract.RealmEntry.COLUMN_NAME));

        cursor.close();
        db.close();
        dbHelper.close();

        return realmName;
    }
}
