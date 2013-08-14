package com.thoughtworks.trakemoi.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.thoughtworks.trakemoi.R;
import com.thoughtworks.trakemoi.activities.CreateZoneActivity;
import com.thoughtworks.trakemoi.activities.HomeActivity;
import com.thoughtworks.trakemoi.activities.SetLocationActivity;
import com.thoughtworks.trakemoi.models.Zone;
import roboguice.fragment.RoboDialogFragment;

public class SaveZoneDialogFragment extends RoboDialogFragment {

    private Zone zone;

    public SaveZoneDialogFragment(Zone zone) {
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
                        SetLocationActivity callingActivity = (SetLocationActivity) getActivity();
                        callingActivity.saveZone(zone);
                        dialog.dismiss();
                        Intent intent = new Intent(callingActivity.getBaseContext(), HomeActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        Toast.makeText(getActivity().getBaseContext(), "Zone Saved!", Toast.LENGTH_LONG).show();

        return builder.create();
    }

}
