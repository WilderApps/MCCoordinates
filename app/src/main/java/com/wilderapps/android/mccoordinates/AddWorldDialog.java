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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Owner on 12/10/2016.
 */

public class AddWorldDialog extends DialogFragment {
    public static final String LOG_TAG = AddWorldDialog.class.getSimpleName();
    AlertDialog mDialog;
    AddWorldListener mListener;
    PassWorld mPassWorld;
    World mWorld = new World();

    public static AddWorldDialog newInstance(Bundle bundle){
        AddWorldDialog dialog = new AddWorldDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try{
            mWorld.setId(getArguments().getInt("id"));
            mWorld.setName(getArguments().getString("name"));
            mWorld.setSeed(getArguments().getString("seed"));

        } catch (NullPointerException e){

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.add_world_dialog, null))
                .setPositiveButton("Add World", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onDialogNegativeClick(AddWorldDialog.this);
                    }
                });
        mDialog = builder.create();
        mDialog.show();


        EditText worldTitle = (EditText) mDialog.findViewById(R.id.add_world_title);
        worldTitle.requestFocus();
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWorld();
            }
        });
        if(mWorld.getId() != -1) {
            mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setText(getString(R.string.update_world));
        }

        return mDialog;
    }

    private void addWorld() {
        try{
            EditText title = (EditText) getDialog().findViewById(R.id.add_world_title);
            EditText seed = (EditText) getDialog().findViewById(R.id.add_world_seed);

            mWorld.setName(title.getText().toString());
            mWorld.setSeed(seed.getText().toString());

            if(mWorld.getName().equals("")){
                throw new NullPointerException();
            } else{
                mPassWorld.getWorld(mWorld);
                mListener.onDialogPositiveClick(AddWorldDialog.this);
                mDialog.dismiss();
            }
        } catch (NullPointerException e){
            Toast.makeText(getContext(), "Please give the new world a title", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        EditText name = (EditText) getDialog().findViewById(R.id.add_world_title);
        name.setText(mWorld.getName());

        EditText seed = (EditText) getDialog().findViewById(R.id.add_world_seed);
        seed.setText(mWorld.getSeed());

        if(mWorld.getId() != -1) {
            TextView title = (TextView) getDialog().findViewById(R.id.world_header);
            title.setText(getString(R.string.update) + mWorld.getName());
        }
    }

    public interface AddWorldListener{
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    public interface PassWorld{
        void getWorld(World world);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListener = (AddWorldListener) context;
            mPassWorld = (PassWorld) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement AddWorldListener and PassWorld");
        }
    }


}
