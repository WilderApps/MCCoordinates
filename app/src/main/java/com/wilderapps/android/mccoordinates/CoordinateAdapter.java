package com.wilderapps.android.mccoordinates;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.wilderapps.android.mccoordinates.data.CoordinateContract;

/**
 * Created by Owner on 12/3/2016.
 */

public class CoordinateAdapter extends CursorAdapter {



    public CoordinateAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.coordinate_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.coordinate_name);
        TextView xCoord = (TextView) view.findViewById(R.id.coordinate_x);
        TextView yCoord = (TextView) view.findViewById(R.id.coordinate_y);
        TextView zCoord = (TextView) view.findViewById(R.id.coordinate_z);

        String nameString = cursor.getString(cursor.getColumnIndex(CoordinateContract.CoordinateEntry.COLUMN_NAME));
        String xCoordString = Integer.toString(cursor.getInt(cursor.getColumnIndex(CoordinateContract.CoordinateEntry.COLUMN_X_COORDINATE)));
        String yCoordString = Integer.toString(cursor.getInt(cursor.getColumnIndex(CoordinateContract.CoordinateEntry.COLUMN_Y_COORDINATE)));
        String zCoordString = Integer.toString(cursor.getInt(cursor.getColumnIndex(CoordinateContract.CoordinateEntry.COLUMN_Z_COORDINATE)));

        name.setText(nameString);
        xCoord.setText(xCoordString);
        yCoord.setText(yCoordString);
        zCoord.setText(zCoordString);
    }
}
