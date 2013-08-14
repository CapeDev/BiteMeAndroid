package com.thoughtworks.trakemoi.activities;

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
import roboguice.inject.InjectView;

public class CreateZoneActivity extends TrakemoiActivity implements
        LocationClient.OnAddGeofencesResultListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    @InjectView(R.id.zone_name_input)
    private EditText zoneName;

    @InjectView(R.id.zone_desc_input)
    private EditText zoneDesc;

    @InjectView(R.id.zone_select_location_button)
    private Button setLocation;

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
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onAddGeofencesResult(int i, String[] strings) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private class ZoneWatcher implements TextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            setLocation.setEnabled(isZoneComplete());
        }

    }

    private boolean isZoneComplete(){
        if(zoneName.getText().toString().length() > 0 && zoneDesc.getText().toString().length() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public void selectLocation(View unused) {
        Intent intent = new Intent(this, SetLocationActivity.class);
        intent.putExtra("zoneName", zoneName.getText().toString());
        intent.putExtra("zoneDesc", zoneDesc.getText().toString());
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                finish();
            }
        }
    }
}