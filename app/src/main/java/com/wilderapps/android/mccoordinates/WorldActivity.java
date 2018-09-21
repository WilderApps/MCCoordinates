package com.wilderapps.android.mccoordinates;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.wilderapps.android.mccoordinates.data.CoordinateContract;
import com.wilderapps.android.mccoordinates.data.CoordinateDbHelper;

import java.util.ArrayList;

public class WorldActivity extends AppCompatActivity
        implements AddCoordinateDialog.AddCoordinateListener, AddCoordinateDialog.PassCoordinate, DeletionDialog.DeleteCoordinate {
    private Utility mUtility= new Utility();
    private AdView mAdView;
    DialogFragment mAddCoordinateDialog;
    Coordinate mNewCoordinate;
    int mTabSelected = 0;
    int mWorldId = 0;
    private ArrayList<Realm> mRealms = new ArrayList<>();
    private RealmAdapter mRealmAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_world);
        setRealms();

        mAdView = (AdView) findViewById(R.id.worldAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        String world = getIntent().getStringExtra("world");
        setTitle(world);
        mWorldId = getIntent().getIntExtra("id", 0);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mRealmAdapter = new RealmAdapter(getSupportFragmentManager(), getApplicationContext(), getIntent().getIntExtra("id", 0), mRealms);

        mViewPager.setAdapter(mRealmAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mTabSelected = tab.getPosition();
                mViewPager.setCurrentItem(mTabSelected, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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

        if (id == R.id.add) {
            mAddCoordinateDialog = mUtility.setUpAddCoordinate(getApplicationContext(), mWorldId, mTabSelected);
            mAddCoordinateDialog.show(getSupportFragmentManager(), "AddCoordinateDialog");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        CoordinateTask task = new CoordinateTask();
        task.execute(mNewCoordinate);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }

    @Override
    public void getNewCoordinate(Coordinate coordinate) {
        mNewCoordinate = coordinate;
        int realmAcutal = mRealms.get(mNewCoordinate.getRealmId()).getId();
        mNewCoordinate.setRealmId(realmAcutal);
    }

    private void setRealms() {
        CoordinateDbHelper dbHelper = new CoordinateDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + CoordinateContract.RealmEntry.TABLE_NAME, null);
        cursor.moveToFirst();

        do {
            int id = cursor.getInt(cursor.getColumnIndex(CoordinateContract.RealmEntry._ID));
            String name = cursor.getString(cursor.getColumnIndex(CoordinateContract.RealmEntry.COLUMN_NAME));
            mRealms.add(new Realm(id, name));
        } while (cursor.moveToNext());

        cursor.close();
        dbHelper.close();
    }

    @Override
    public void deleteCoordinate(Coordinate coordinate) {
        DeleteCoordinateTask task = new DeleteCoordinateTask();
        task.execute(coordinate);
    }

    private class CoordinateTask extends AsyncTask<Coordinate, Void, Void> {
        @Override
        protected Void doInBackground(Coordinate... params) {
            CoordinateDbHelper dbHelper = new CoordinateDbHelper(getApplicationContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues cv = new ContentValues();
            if (params[0].getId() != -1) {
                cv.put(CoordinateContract.CoordinateEntry._ID, params[0].getId());
            }
            cv.put(CoordinateContract.CoordinateEntry.COLUMN_NAME, params[0].getName());
            cv.put(CoordinateContract.CoordinateEntry.COLUMN_WORLD_KEY, params[0].getWorldId());
            cv.put(CoordinateContract.CoordinateEntry.COLUMN_REALM_KEY, params[0].getRealmId());
            cv.put(CoordinateContract.CoordinateEntry.COLUMN_X_COORDINATE, params[0].getXCoordinate());
            cv.put(CoordinateContract.CoordinateEntry.COLUMN_Y_COORDINATE, params[0].getYCoordinate());
            cv.put(CoordinateContract.CoordinateEntry.COLUMN_Z_COORDINATE, params[0].getZCoordinate());

            if (params[0].getId() == -1) {
                db.insert(CoordinateContract.CoordinateEntry.TABLE_NAME, null, cv);
            } else {
                String whereClause = CoordinateContract.CoordinateEntry._ID + " = ?";
                String[] whereArgs = {Integer.toString(params[0].getId())};
                db.update(CoordinateContract.CoordinateEntry.TABLE_NAME, cv, whereClause, whereArgs);
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            int currentTab = mViewPager.getCurrentItem();
            mRealmAdapter = new RealmAdapter(getSupportFragmentManager(), getApplicationContext(), getIntent().getIntExtra("id", 0), mRealms);
            mViewPager.setAdapter(mRealmAdapter);
            mViewPager.setCurrentItem(currentTab);
        }
    }

    private class DeleteCoordinateTask extends AsyncTask<Coordinate, Void, Void>{
        @Override
        protected Void doInBackground(Coordinate... params) {
            CoordinateDbHelper dbHelper = new CoordinateDbHelper(getApplicationContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            String whereClause = CoordinateContract.CoordinateEntry._ID + " = ?";
            String[] whereArgs = {Integer.toString(params[0].getId())};

            db.delete(CoordinateContract.CoordinateEntry.TABLE_NAME, whereClause, whereArgs);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            int currentTab = mViewPager.getCurrentItem();
            mRealmAdapter = new RealmAdapter(getSupportFragmentManager(), getApplicationContext(), getIntent().getIntExtra("id", 0), mRealms);
            mViewPager.setAdapter(mRealmAdapter);
            mViewPager.setCurrentItem(currentTab);
        }
    }


}
