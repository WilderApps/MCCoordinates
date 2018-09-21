package com.wilderapps.android.mccoordinates;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Owner on 12/6/2016.
 */

public class AddCoordinateDialog extends DialogFragment {

    AddCoordinateListener mListener;
    PassCoordinate mPassCoordinate;
    AlertDialog mDialog;
    int mRealm;
    int mWorldId;
    int mId;
    Spinner mSpinner;
    ArrayList<String> mRealmNames = new ArrayList<>();
    Utility mUtility = new Utility();
    Bundle mArgs;
    EditText mTitle;
    EditText mX;
    EditText mY;
    EditText mZ;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mArgs = getArguments();
        mRealmNames.addAll(mArgs.getStringArrayList("realms"));
        mWorldId = mArgs.getInt("worldId");
        mId = mArgs.getInt("id");

        if(mId == -1){
            mRealm = mArgs.getInt("tabSelected");
        } else{
            mRealm = mRealmNames.indexOf(mUtility.getRealmName(getContext(), mArgs.getInt("realmId")));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.add_coordinate_dialog,null))
        .setPositiveButton("Add Coordinate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onDialogNegativeClick(AddCoordinateDialog.this);
            }
        });

        mDialog = builder.create();
        mDialog.show();
        mSpinner = (Spinner) mDialog.findViewById(R.id.realm_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mDialog.getContext(), android.R.layout.simple_spinner_item, mRealmNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(mRealm);

        mDialog.findViewById(R.id.add_coordinate_title).requestFocus();
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        if(mId != -1){
            mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setText(getString(R.string.update_coords));
        }

        mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if(mTitle.getText().toString().equals("") ||
                            mX.getText().toString().equals("") ||
                            mY.getText().toString().equals("") ||
                            mZ.getText().toString().equals("")){
                        throw new NullPointerException();
                    } else if(mId == -1){
                        String strTitle = mTitle.getText().toString();
                        int intX = Integer.parseInt(mX.getText().toString());
                        int intY = Integer.parseInt(mY.getText().toString());
                        int intZ = Integer.parseInt(mZ.getText().toString());
                        mRealm = mSpinner.getSelectedItemPosition();

                        Coordinate newCoordinate = new Coordinate(strTitle, mWorldId, mRealm, intX, intY, intZ);

                        mPassCoordinate.getNewCoordinate(newCoordinate);
                        mListener.onDialogPositiveClick(AddCoordinateDialog.this);
                        mDialog.dismiss();
                    } else{
                        String strTitle = mTitle.getText().toString();
                        int intX = Integer.parseInt(mX.getText().toString());
                        int intY = Integer.parseInt(mY.getText().toString());
                        int intZ = Integer.parseInt(mZ.getText().toString());
                        mRealm = mSpinner.getSelectedItemPosition();

                        Coordinate newCoordinate = new Coordinate(mId, strTitle, mWorldId, mRealm, intX, intY, intZ);

                        strTitle = mArgs.getString("name");
                        intX = mArgs.getInt("xCoord");
                        intY = mArgs.getInt("yCoord");
                        intZ = mArgs.getInt("zCoord");

                        Coordinate oldCoordinate = new Coordinate(mId, strTitle, mWorldId, mRealm, intX, intY, intZ);

                        boolean match = newCoordinate.compare(oldCoordinate);

                        if(match){
                            mDialog.dismiss();
                        } else {
                            mPassCoordinate.getNewCoordinate(newCoordinate);
                            mListener.onDialogPositiveClick(AddCoordinateDialog.this);
                            mDialog.dismiss();
                        }
                    }
                } catch (NullPointerException e){
                    Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e){
                    Toast.makeText(getContext(), "Make sure only whole numbers are used for coordinates", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return mDialog;
    }

    public interface AddCoordinateListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    public interface PassCoordinate{
        void getNewCoordinate(Coordinate coordinate);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListener = (AddCoordinateListener) context;
            mPassCoordinate = (PassCoordinate) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement AddCoordinateListener and PassCoordinate");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

            mTitle = (EditText) getDialog().findViewById(R.id.add_coordinate_title);
            mX = (EditText) getDialog().findViewById(R.id.add_x);
            mY = (EditText) getDialog().findViewById(R.id.add_y);
            mZ = (EditText) getDialog().findViewById(R.id.add_z);


        if(mId != -1){

            mTitle.setText(mArgs.getString("name"));
            mX.setText(Integer.toString(mArgs.getInt("xCoord")));
            mY.setText(Integer.toString(mArgs.getInt("yCoord")));
            mZ.setText(Integer.toString(mArgs.getInt("zCoord")));
        }
    }
}
