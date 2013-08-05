package com.thoughtworks.trakemoi.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import com.thoughtworks.trakemoi.R;
import com.thoughtworks.trakemoi.activities.LocationActivity;
import com.thoughtworks.trakemoi.models.Zone;
import roboguice.fragment.RoboDialogFragment;

import java.text.DecimalFormat;

public class SaveZoneDialogFragment extends RoboDialogFragment {

    private Zone zone;
    private DecimalFormat df = new DecimalFormat("#.##");

    public SaveZoneDialogFragment(Zone zone){
        this.zone = zone;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Save Zone")
                .setMessage(zone.getName())
                .setIcon(R.drawable.trakemoi2)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                LocationActivity callingActivity = (LocationActivity) getActivity();
                callingActivity.saveZone(zone);
                dialog.dismiss();
            }})
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }});
        return builder.create();
    }

}
