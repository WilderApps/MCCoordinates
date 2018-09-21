package com.wilderapps.android.mccoordinates;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Owner on 12/6/2016.
 */

public class RealmAdapter extends FragmentPagerAdapter {
    public static final String LOG_TAG = RealmAdapter.class.getSimpleName();

    private Context mContext;
    private int mWorldId;
    private ArrayList<Realm> mRealms = new ArrayList<>();

    public RealmAdapter(FragmentManager fm, Context context, int worldId, ArrayList<Realm> realms) {
        super(fm);
        mContext = context;
        mWorldId = worldId;
        Log.d(LOG_TAG, "World Id: " + mWorldId);
        mRealms.clear();
        mRealms.addAll(realms);
    }

    @Override
    public CoordinateFragment getItem(int position) {
        Bundle args = new Bundle();
        args.putInt("realmId", mRealms.get(position).getId());
        args.putString("realmName", mRealms.get(position).getName());
        args.putInt("worldId", mWorldId);

        CoordinateFragment coordinateFragment = new CoordinateFragment();
        coordinateFragment.setArguments(args);

        return coordinateFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mRealms.get(position).getName();
    }

    @Override
    public int getCount() {
        return mRealms.size();
    }
}
