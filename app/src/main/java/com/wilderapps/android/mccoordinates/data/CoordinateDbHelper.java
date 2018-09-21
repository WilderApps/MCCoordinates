package com.wilderapps.android.mccoordinates.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Owner on 12/13/2016.
 */

public class CoordinateDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "coordinates.db";

    public CoordinateDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WORLD_TABLE = "CREATE TABLE " + CoordinateContract.WorldEntry.TABLE_NAME + " (" +
                CoordinateContract.WorldEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CoordinateContract.WorldEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                CoordinateContract.WorldEntry.COLUMN_SEED + " TEXT" + ");";

        final String SQL_CREATE_REALM_TABLE = "CREATE TABLE " + CoordinateContract.RealmEntry.TABLE_NAME + " (" +
                CoordinateContract.RealmEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CoordinateContract.RealmEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL);";

        final String SQL_CREATE_COORDINATE_TABLE = "CREATE TABLE " + CoordinateContract.CoordinateEntry.TABLE_NAME + " (" +
                CoordinateContract.CoordinateEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CoordinateContract.CoordinateEntry.COLUMN_WORLD_KEY + " INTEGER NOT NULL, " +
                CoordinateContract.CoordinateEntry.COLUMN_REALM_KEY + " INTEGER NOT NULL, " +
                CoordinateContract.CoordinateEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                CoordinateContract.CoordinateEntry.COLUMN_X_COORDINATE + " INTEGER NOT NULL, " +
                CoordinateContract.CoordinateEntry.COLUMN_Y_COORDINATE + " INTEGER NOT NULL, " +
                CoordinateContract.CoordinateEntry.COLUMN_Z_COORDINATE + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + CoordinateContract.CoordinateEntry.COLUMN_WORLD_KEY + ") " +
                "REFERENCES " + CoordinateContract.WorldEntry.TABLE_NAME + "(" + CoordinateContract.WorldEntry._ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY (" + CoordinateContract.CoordinateEntry.COLUMN_REALM_KEY + ") " +
                "REFERENCES " + CoordinateContract.RealmEntry.TABLE_NAME + "(" + CoordinateContract.RealmEntry._ID + ") ON DELETE CASCADE);";

        db.execSQL(SQL_CREATE_WORLD_TABLE);
        db.execSQL(SQL_CREATE_REALM_TABLE);
        db.execSQL(SQL_CREATE_COORDINATE_TABLE);

        ContentValues overworld = new ContentValues();
        overworld.put(CoordinateContract.RealmEntry.COLUMN_NAME, "Overworld");

        ContentValues nether = new ContentValues();
        nether.put(CoordinateContract.RealmEntry.COLUMN_NAME, "Nether");

        ContentValues end = new ContentValues();
        end.put(CoordinateContract.RealmEntry.COLUMN_NAME, "The End");

        db.insert(CoordinateContract.RealmEntry.TABLE_NAME, null, overworld);
        db.insert(CoordinateContract.RealmEntry.TABLE_NAME, null, nether);
        db.insert(CoordinateContract.RealmEntry.TABLE_NAME, null, end);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + CoordinateContract.WorldEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CoordinateContract.RealmEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CoordinateContract.CoordinateEntry.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}
