package com.wilderapps.android.mccoordinates;

/**
 * Created by Owner on 12/3/2016.
 */

public class Coordinate {
    private int mId = -1;
    private String mName;
    private int mWorldId;
    private int mRealmId;
    private int mXCoordinate;
    private int mYCoordinate;
    private int mZCoordinate;

    public Coordinate() {
    }

    public Coordinate(String name, int worldId, int realmId, int XCoordinate, int YCoordinate, int ZCoordinate) {
        mName = name;
        mWorldId = worldId;
        mRealmId = realmId;
        mXCoordinate = XCoordinate;
        mYCoordinate = YCoordinate;
        mZCoordinate = ZCoordinate;
    }

    public Coordinate(int id, String name, int worldId, int realmId, int XCoordinate, int YCoordinate, int ZCoordinate) {
        mId = id;
        mName = name;
        mWorldId = worldId;
        mRealmId = realmId;
        mXCoordinate = XCoordinate;
        mYCoordinate = YCoordinate;
        mZCoordinate = ZCoordinate;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getXCoordinate() {
        return Integer.toString(mXCoordinate);
    }

    public void setXCoordinate(int XCoordinate) {
        mXCoordinate = XCoordinate;
    }

    public String getYCoordinate() {
        return Integer.toString(mYCoordinate);
    }

    public void setYCoordinate(int YCoordinate) {
        mYCoordinate = YCoordinate;
    }

    public String getZCoordinate() {
        return Integer.toString(mZCoordinate);
    }

    public void setZCoordinate(int ZCoordinate) {
        mZCoordinate = ZCoordinate;
    }

    public int getWorldId() {
        return mWorldId;
    }

    public void setWorldId(int worldId) {
        mWorldId = worldId;
    }

    public int getRealmId() {
        return mRealmId;
    }

    public void setRealmId(int realmId) {
        mRealmId = realmId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    @Override
    public String toString() {
        return "Coordinate: " + mName + " WorldId: " + mWorldId + " RealmId: " + mRealmId +
                " X: " + mXCoordinate + " Y: " + mYCoordinate + " Z: " + mZCoordinate;
    }

    public boolean compare(Coordinate coordinate){
        boolean match = false;
        if(mId == coordinate.getId()
                && mName == coordinate.getName()
                && mWorldId == coordinate.getWorldId()
                && mRealmId == coordinate.getRealmId()
                && mXCoordinate == Integer.valueOf(coordinate.getXCoordinate())
                && mYCoordinate == Integer.valueOf(coordinate.getYCoordinate())
                && mZCoordinate == Integer.valueOf(coordinate.getZCoordinate())){
            match = true;
        }
        return match;
    }
}
