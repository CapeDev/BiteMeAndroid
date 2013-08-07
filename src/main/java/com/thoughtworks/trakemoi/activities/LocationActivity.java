package com.thoughtworks.trakemoi.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.*;

import com.google.android.gms.maps.*;

import com.thoughtworks.trakemoi.R;

import roboguice.activity.RoboFragmentActivity;

public class LocationActivity extends RoboFragmentActivity {

    public static final String TAG = "Trakemoi";

    protected GoogleMap map;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it
     *                           is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.location);
        setUpActionBar();
        setUpMap();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setUpMap() {
        if (map == null) {
            map = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            map.setMyLocationEnabled(true);
        }
    }

    private void setUpActionBar() {
        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}