package com.wilderapps.android.mccoordinates;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wilderapps.android.mccoordinates.data.CoordinateContract;

/**
 * Created by Owner on 12/27/2016.
 */

public class AlterDialog extends DialogFragment
        implements AddWorldDialog.AddWorldListener, AddCoordinateDialog.AddCoordinateListener {
    String LOG_TAG = AlterDialog.class.getSimpleName();
    Bundle mArgs;
    public boolean mWorldBool;
    AlertDialog mDialog;
    Utility mUtility = new Utility();

    public static AlterDialog newInstance(Cursor cursor, boolean world){
        if(world) {
            AlterDialog dialog = new AlterDialog();
            Bundle args = new Bundle();
            args.putInt("id", cursor.getInt(cursor.getColumnIndex(CoordinateContract.WorldEntry._ID)));
            args.putString("name", cursor.getString(cursor.getColumnIndex(CoordinateContract.WorldEntry.COLUMN_NAME)));
            args.putString("seed", cursor.getString(cursor.getColumnIndex(CoordinateContract.WorldEntry.COLUMN_SEED)));
            args.putBoolean("type", world);
            dialog.setArguments(args);
            return dialog;
        } else {
            AlterDialog dialog = new AlterDialog();
            Bundle args = new Bundle();
            args.putInt("id", cursor.getInt(cursor.getColumnIndex(CoordinateContract.CoordinateEntry._ID)));
            args.putString("name", cursor.getString(cursor.getColumnIndex(CoordinateContract.CoordinateEntry.COLUMN_NAME)));
            args.putInt("worldId", cursor.getInt(cursor.getColumnIndex(CoordinateContract.CoordinateEntry.COLUMN_WORLD_KEY)));
            args.putInt("realmId", cursor.getInt(cursor.getColumnIndex(CoordinateContract.CoordinateEntry.COLUMN_REALM_KEY)));
            args.putInt("xCoord", cursor.getInt(cursor.getColumnIndex(CoordinateContract.CoordinateEntry.COLUMN_X_COORDINATE)));
            args.putInt("yCoord", cursor.getInt(cursor.getColumnIndex(CoordinateContract.CoordinateEntry.COLUMN_Y_COORDINATE)));
            args.putInt("zCoord", cursor.getInt(cursor.getColumnIndex(CoordinateContract.CoordinateEntry.COLUMN_Z_COORDINATE)));
            args.putBoolean("type", world);
            dialog.setArguments(args);
            return dialog;
        }
    }
    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mArgs = getArguments();
        mWorldBool = mArgs.getBoolean("type");
        if(mWorldBool) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.alter_dialog, null));
            mDialog = builder.create();
            mDialog.show();

            Button update = (Button) mDialog.findViewById(R.id.update);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddWorldDialog dialog = AddWorldDialog.newInstance(mArgs);
                    dialog.show(getFragmentManager(), "UpdateWorldDialog");
                    mDialog.dismiss();
                }
            });

            Button delete = (Button) mDialog.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeletionDialog dialog = DeletionDialog.newInstance(mArgs);
                    dialog.show(getFragmentManager(), "DeleteWorldDialog");
                    mDialog.dismiss();
                }
            });
        } else {
            mArgs = getArguments();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.alter_dialog, null));
            mDialog = builder.create();
            mDialog.show();

            Button update = (Button) mDialog.findViewById(R.id.update);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddCoordinateDialog dialog = mUtility.setUpAddCoordinate(getContext(), mArgs);
                    dialog.show(getFragmentManager(), "UpdateCoordinateDialog");
                    mDialog.dismiss();
                }
            });

            Button delete = (Button) mDialog.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeletionDialog dialog = DeletionDialog.newInstance(mArgs);
                    dialog.show(getFragmentManager(), "DeleteWorldDialog");
                    mDialog.dismiss();
                }
            });
        }

        TextView header = (TextView) mDialog.findViewById(R.id.alter_header);
        header.setText(getString(R.string.alter) + " " + mArgs.getString("name"));

        return mDialog;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        mDialog.dismiss();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
