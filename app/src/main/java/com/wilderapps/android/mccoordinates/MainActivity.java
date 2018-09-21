package com.wilderapps.android.mccoordinates;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.wilderapps.android.mccoordinates.data.CoordinateContract;
import com.wilderapps.android.mccoordinates.data.CoordinateDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
implements AddWorldDialog.AddWorldListener, AddWorldDialog.PassWorld, DeletionDialog.DeleteWorld {
    private AdView mAdView;
    ArrayList<String> mWorlds = new ArrayList<>();
    AddWorldDialog mAddWorldDialog;
    World mWorld = new World();
    World mLongPressedWorld;
    CoordinateDbHelper mDbHelper;
    SQLiteDatabase mDb;
    ListView mWorldList;
    WorldAdapter mWorldAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(getApplicationContext(), getString(R.string.adMob_id));

        mAdView = (AdView) findViewById(R.id.mainAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mWorlds.add("Testing");
        mWorlds.add("More Testing");
        mWorlds.add("Much test. Very wow");

        mWorldList = (ListView) findViewById(R.id.world_list);

        setWorldAdapter(worldCursor());

        mWorldList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), WorldActivity.class);

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                String worldName = cursor.getString(cursor.getColumnIndex(CoordinateContract.WorldEntry.COLUMN_NAME));
                intent.putExtra("world", worldName);

                int worldId = cursor.getInt(cursor.getColumnIndex(CoordinateContract.WorldEntry._ID));
                intent.putExtra("id", worldId);

                startActivity(intent);
            }
        });

        mWorldList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) mWorldList.getItemAtPosition(i);
                int id = cursor.getInt(cursor.getColumnIndex(CoordinateContract.WorldEntry._ID));
                String name = cursor.getString(cursor.getColumnIndex(CoordinateContract.WorldEntry.COLUMN_NAME));
                String seed = cursor.getString(cursor.getColumnIndex(CoordinateContract.WorldEntry.COLUMN_SEED));
                mLongPressedWorld = new World(id, name, seed);
                DialogFragment alterWorldDialog = AlterDialog.newInstance(cursor, true);
                alterWorldDialog.show(getSupportFragmentManager(), "AlterWorldDialog");
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.add){
            mAddWorldDialog = new AddWorldDialog();
            mAddWorldDialog.show(getSupportFragmentManager(), "AddWorldDialog");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getWorld(World world) {
        mWorld = world;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (mWorld.getId() != -1) {
            if (!mWorld.compare(mLongPressedWorld)) {
                WorldTask worldTask = new WorldTask(getApplicationContext());
                worldTask.execute(mWorld);
            }
        } else{
            WorldTask worldTask = new WorldTask(getApplicationContext());
            worldTask.execute(mWorld);
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }

    private Cursor worldCursor(){
        mDbHelper = new CoordinateDbHelper(getApplicationContext());
        mDb = mDbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + CoordinateContract.WorldEntry.TABLE_NAME;

        return mDb.rawQuery(query, null);
    }

    private void setWorldAdapter(Cursor cursor){
        if(mWorldAdapter == null) {
            mWorldAdapter = new WorldAdapter(this, cursor);

            mWorldList.setAdapter(mWorldAdapter);

            mDbHelper.close();
            mDb.close();
        } else{
            mWorldAdapter.changeCursor(cursor);
        }
    }

    @Override
    public void deleteWorld(World world) {
        WorldDeleteTask deleteTask = new WorldDeleteTask(getApplicationContext());
        deleteTask.execute(world);
    }

    private class WorldTask extends AsyncTask<World, Void, Cursor> {
        private Context mContext;

        public WorldTask(Context context) {
            mContext = context;
        }

        @Override
        protected Cursor doInBackground(World... worlds) {
            CoordinateDbHelper dbHelper = new CoordinateDbHelper(mContext);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            if(worlds[0].getId() != -1) {
                values.put(CoordinateContract.WorldEntry._ID, worlds[0].getId());
            }
            values.put(CoordinateContract.WorldEntry.COLUMN_NAME, worlds[0].getName());
            if (!worlds[0].getSeed().equals("")) {
                values.put(CoordinateContract.WorldEntry.COLUMN_SEED, worlds[0].getSeed());
            }

            if (worlds[0].getId() == -1) {
                db.insert(CoordinateContract.WorldEntry.TABLE_NAME, null, values);
            } else {
                String whereClaus = CoordinateContract.WorldEntry._ID + " = ?";
                String[] whereArgs = {Integer.toString(worlds[0].getId())};

                db.update(CoordinateContract.WorldEntry.TABLE_NAME, values, whereClaus, whereArgs);
            }

            return db.query(CoordinateContract.WorldEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            setWorldAdapter(cursor);
        }
    }

    private class WorldDeleteTask extends AsyncTask<World, Void, Cursor>{
        private Context mContext;

        public WorldDeleteTask(Context context){
            mContext = context;
        }

        @Override
        protected Cursor doInBackground(World... worlds) {
            CoordinateDbHelper dbHelper = new CoordinateDbHelper(mContext);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            String whereClause = CoordinateContract.WorldEntry._ID + " = ?";
            String[] whereArgs = {Integer.toString(worlds[0].getId())};

            db.delete(CoordinateContract.WorldEntry.TABLE_NAME, whereClause, whereArgs);

            return db.rawQuery("SELECT * FROM " + CoordinateContract.WorldEntry.TABLE_NAME, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            setWorldAdapter(cursor);
        }
    }
}
