package com.wilderapps.android.mccoordinates;

/**
 * Created by Owner on 1/2/2017.
 */

public class Realm {
    private int mId;
    private String mName;

    public Realm(int id, String name) {
        mId = id;
        mName = name;
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
}
