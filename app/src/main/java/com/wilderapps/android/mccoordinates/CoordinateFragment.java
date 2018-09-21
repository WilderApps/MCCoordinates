package com.wilderapps.android.mccoordinates;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wilderapps.android.mccoordinates.data.CoordinateContract;
import com.wilderapps.android.mccoordinates.data.CoordinateDbHelper;

import java.util.ArrayList;

/**
 * Created by Owner on 12/6/2016.
 */

public class CoordinateFragment extends Fragment {
    private CoordinateAdapter mAdapter;
    private ListView mListView;
    private ArrayList<Coordinate> mCoordinates = new ArrayList<>();
    private String mWorldTitle;
    private String mRealm;
    private int mWorldId;
    private int mRealmId;

    private Bundle mBundle;

    public CoordinateFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBundle = getArguments();
        mWorldId = mBundle.getInt("worldId");
        mRealmId = mBundle.getInt("realmId");

        View rootView = inflater.inflate(R.layout.coordinate_list, container, false);

        mAdapter = new CoordinateAdapter(getActivity(), setCursor());

        mListView = (ListView) rootView.findViewById(R.id.coordinate_list);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) mListView.getItemAtPosition(position);

                DialogFragment alterDialog = AlterDialog.newInstance(cursor, false);
                alterDialog.show(getFragmentManager(), "AlterCoordinateDialog");
                return true;
            }
        });

        mListView.setAdapter(mAdapter);

        return rootView;
    }

    private Cursor setCursor(){
        CoordinateDbHelper dbHelper = new CoordinateDbHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = CoordinateContract.CoordinateEntry.COLUMN_WORLD_KEY + " = ? AND " + CoordinateContract.CoordinateEntry.COLUMN_REALM_KEY + " = ?";
        String[] selectionArgs = {Integer.toString(mWorldId), Integer.toString(mRealmId)};
        String orderBy = CoordinateContract.CoordinateEntry.COLUMN_NAME;

        return db.query(CoordinateContract.CoordinateEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                orderBy);
    }
}
