package com.thoughtworks.trakemoi.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.thoughtworks.trakemoi.R;
import com.thoughtworks.trakemoi.data.DataAccessFactory;
import com.thoughtworks.trakemoi.data.PunchDataAccess;
import com.thoughtworks.trakemoi.data.ZoneDataAccess;
import com.thoughtworks.trakemoi.models.Zone;
import roboguice.inject.InjectView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CreateZoneActivity extends TrakemoiActivity implements
        LocationClient.OnAddGeofencesResultListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    public static final String THE_ZONE_NAME_EXISTS = "The zone Name exists";

    @InjectView(R.id.zone_name_input)
    private EditText zoneName;

    @InjectView(R.id.zone_desc_input)
    private EditText zoneDesc;

    @InjectView(R.id.zone_select_location_button)
    private Button setLocation;

    @Inject
    DataAccessFactory dataAccessFactory;
    private ZoneDataAccess zoneDataAccess;
    private PunchDataAccess punchDataAccess;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTitle("Create Zone");
        setContentView(R.layout.create_zone);
        setUpActionBar();
        zoneName.addTextChangedListener(new ZoneWatcher());
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.showSoftInput(zoneName, InputMethodManager.SHOW_IMPLICIT);
        zoneDesc.addTextChangedListener(new ZoneWatcher());
    }

    @Override
    public void onConnected(Bundle bundle) {
        processPendingZones();
    }

    private void processPendingZones() {
    }

    @Override
    public void onDisconnected() {
    }

    @Override
    public void onAddGeofencesResult(int i, String[] strings) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

//    public void addPunchToDBBasedOnZone(String ids, String transitionType) {
//        System.out.println("--------------GeoFence Punching------");
//        if (transitionType == null || transitionType.equals(""))
//            punchDataAccess = dataAccessFactory.punch(this);
//        try {
//            if (ENTERED.equals(transitionType)) {
//                Util.addPunchData(punchDatabase, "In", ids);
//                System.out.println("Entered-----------------------");
//            } else if (EXITED.equals(transitionType)) {
//                Util.addPunchData(punchDatabase, "Out", ids);
//            }
//        } catch (TrakeMoiDatabaseException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//    }

    private class ZoneWatcher implements TextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            setLocation.setEnabled(isZoneComplete());
        }

    }

    private boolean isZoneComplete() {
        if (zoneName.getText().toString().length() > 0 && zoneDesc.getText().toString().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void selectLocation(View unused) {
        Intent intent = new Intent(this, SetLocationActivity.class);

        String currentZoneName = zoneName.getText().toString();
        boolean found = compareZoneNameInDB(currentZoneName);
        if (found) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Reminder");
            builder.setMessage(THE_ZONE_NAME_EXISTS);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setIcon(R.drawable.trakemoi2);
            builder.show();
        } else {
            intent.putExtra("zoneName", currentZoneName);
            intent.putExtra("zoneDesc", zoneDesc.getText().toString());
            startActivityForResult(intent, 1);
        }
    }

    private boolean compareZoneNameInDB(String currentZoneName) {
        zoneDataAccess = dataAccessFactory.zones(this);
        List<Zone> zones = zoneDataAccess.getZones();

        List<String> zoneNames = new ArrayList<String>();
        if (zones != null) {
            for (Zone zone : zones) {
                zoneNames.add(zone.getName());
            }
        }

        if (zoneNames != null && zoneNames.contains(currentZoneName)) {
            return true;
        }
        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }
}