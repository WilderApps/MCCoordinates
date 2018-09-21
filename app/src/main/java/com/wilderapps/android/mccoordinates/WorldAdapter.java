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
 * Created by Owner on 12/6/2016.
 */

public class WorldAdapter extends CursorAdapter{

    public WorldAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.world_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.world_title);
        TextView seed = (TextView) view.findViewById(R.id.world_seed);

        String nameString = cursor.getString(cursor.getColumnIndex(CoordinateContract.WorldEntry.COLUMN_NAME));
        String seedString = cursor.getString(cursor.getColumnIndex(CoordinateContract.WorldEntry.COLUMN_SEED));

        name.setText(nameString);

        if(seedString == null){
            seed.setVisibility(View.INVISIBLE);
        } else {
            seed.setVisibility(View.VISIBLE);
            seed.setText(seedString);
        }
    }
}
