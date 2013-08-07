package com.thoughtworks.trakemoi.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.*;
import com.thoughtworks.trakemoi.R;
import com.thoughtworks.trakemoi.data.DataAccessFactory;
import com.thoughtworks.trakemoi.data.ZoneDataAccess;
import com.thoughtworks.trakemoi.dialog.SaveZoneDialogFragment;
import com.thoughtworks.trakemoi.models.Zone;

import javax.inject.Inject;

public class DisplayLocationActivity extends LocationActivity {

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
        Bundle extras = getIntent().getExtras();
        String zoneName = extras.getString("zoneName");
        setTitle(zoneName);
        setupZone();
    }

    private void setupZone() {
        Bundle extras = getIntent().getExtras();
        String zoneName = extras.getString("zoneName");
        double zoneLatitude = extras.getDouble("zoneLatitude");
        double zoneLongitude = extras.getDouble("zoneLongitude");
        LatLng latLng = new LatLng(zoneLatitude, zoneLongitude);
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(zoneName)
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.building)));

        map.addCircle(getCircleOptions(latLng));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(zoneLatitude, zoneLongitude), 15));
    }

    public CircleOptions getCircleOptions(LatLng latLng) {
        Bundle extras = getIntent().getExtras();
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        int zoneRadius = extras.getInt("zoneRadius");
        circleOptions.radius(zoneRadius);
        circleOptions.fillColor(Color.argb(25, 139, 0, 255));
        circleOptions.strokeColor(Color.argb(150, 139, 0, 255));
        circleOptions.strokeWidth(3.0f);
        return circleOptions;
    }

}