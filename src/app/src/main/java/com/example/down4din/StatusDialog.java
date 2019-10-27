package com.example.down4din;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class StatusDialog extends AppCompatDialogFragment {

    private EditText doingInput, addressInput;
    private StatusDialogListener listener;
    public String address;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {




        Activity activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        try {
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_status, null);

            doingInput = view.findViewById(R.id.doingInput);
            addressInput = view.findViewById(R.id.addressInput);
            addressInput.setFocusable(false);
            addressInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { address = openAddressPicker();}
            });

            builder.setView(view)
                    .setTitle("Update Status")
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String doing = doingInput.getText().toString();
                            address = addressInput.getText().toString();
                            addressInput.setText(address);
                            if (doing.length() == 0 || address.length() == 0) {
                                Toast.makeText(getContext(), "Please fill in all fields",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                listener.updateValue(doing, address);
                            }
                        }
                    });
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return builder.create();
    }

    public interface StatusDialogListener {
        void updateValue(String doing, String address);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (StatusDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "Must implement StatusDialogListener.");
        }
    }

    // TODO: create address picker
    private String openAddressPicker() {
        Intent i = new Intent(getContext(), MapActivity.class);
        getContext().startActivity(i);

        if(getArguments() != null)
            return getArguments().getString("place");
        return null;
    }
}
