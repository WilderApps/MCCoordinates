package com.wilderapps.android.mccoordinates;

/**
 * Created by Owner on 12/27/2016.
 */

public class World {
    private int mId = -1;
    private String mName = "";
    private String mSeed = "";
    public World() {
    }

    public World(String name, String seed) {
        mName = name;
        mSeed = seed;
    }

    public World(int id, String name, String seed) {
        mId = id;
        mName = name;
        mSeed = seed;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSeed() {
        return mSeed;
    }

    public void setSeed(String seed) {
        mSeed = seed;
    }


    public boolean compare(World world) {
        Boolean equal = false;
        if(mId == world.getId() &&
                mName.equals(world.getName()) &&
                mSeed.equals(world.getSeed())){
            equal = true;
        }
        return equal;
    }
}
