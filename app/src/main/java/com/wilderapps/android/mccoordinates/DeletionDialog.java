package com.wilderapps.android.mccoordinates;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.TextView;

import static com.wilderapps.android.mccoordinates.R.id.deletion_header;

/**
 * Created by Owner on 12/31/2016.
 */

public class DeletionDialog extends DialogFragment {
    boolean mWorldBool;
    Bundle mArgs;
    AlertDialog mDialog;
    World mWorld;
    Coordinate mCoordinate;
    DeleteWorld mDeleteWorld;
    DeleteCoordinate mDeleteCoordinate;

    public static DeletionDialog newInstance(Bundle args){
        DeletionDialog dialog = new DeletionDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(mWorldBool) {
            mWorld = new World(mArgs.getInt("id"),
                    mArgs.getString("name"), mArgs.getString("seed"));

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.deletion_dialog, null))
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mDeleteWorld.deleteWorld(mWorld);
                            mDialog.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            mDialog = builder.create();
            mDialog.show();

            TextView deletion = (TextView) mDialog.findViewById(R.id.deletion);
            deletion.setText("Are you sure you wish to delete the world " + mArgs.getString("name") +
                    " and all coordinates attached to it? This is irreversible.");
        } else{
            mCoordinate = new Coordinate(mArgs.getInt("id"), mArgs.getString("name"), mArgs.getInt("worldId"),
                    mArgs.getInt("realmId"), mArgs.getInt("xCoord"), mArgs.getInt("yCoord"), mArgs.getInt("zCoord"));

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.deletion_dialog, null))
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mDeleteCoordinate.deleteCoordinate(mCoordinate);
                            mDialog.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            mDialog = builder.create();
            mDialog.show();

            TextView deletion = (TextView) mDialog.findViewById(R.id.deletion);
            deletion.setText("Are you sure you wish to delete the coordinate " + mArgs.getString("name") +
                    "? This is irreversible.");
        }
        TextView header = (TextView) mDialog.findViewById(deletion_header);
        header.setText("Delete " + mArgs.getString("name") + "?");

        return mDialog;
    }

    public interface DeleteWorld {
        void deleteWorld(World world);
    }

    public interface DeleteCoordinate{
        void deleteCoordinate(Coordinate coordinate);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mArgs = getArguments();
        mWorldBool = mArgs.getBoolean("type");
        try{
            if(mWorldBool) {
                mDeleteWorld = (DeleteWorld) context;
            } else {
                mDeleteCoordinate = (DeleteCoordinate) context;
            }
        } catch (ClassCastException e){
            if(mWorldBool) {
                throw new ClassCastException(context.toString() + " must implement DeleteWorld");
            } else {
                throw new ClassCastException(context.toString() + " must implement DeleteCoordinate");
            }
        }
    }
}
