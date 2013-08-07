package com.thoughtworks.trakemoi.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.*;
import com.thoughtworks.trakemoi.R;
import com.thoughtworks.trakemoi.data.DataAccessFactory;
import com.thoughtworks.trakemoi.data.ZoneDataAccess;
import com.thoughtworks.trakemoi.dialog.SaveZoneDialogFragment;
import com.thoughtworks.trakemoi.models.Zone;

import javax.inject.Inject;

public class SetLocationActivity extends LocationActivity implements GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener, LocationListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private Circle circle = null;
    private Marker marker = null;
    private String zoneName;
    private String zoneDesc;
    private static final int ZONE_RADIUS = 250;

    @Inject
    DataAccessFactory dataAccessFactory;
    private ZoneDataAccess zoneRepository;

    private LocationClient locationClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

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
        setTitle(getResources().getString(R.string.set_location));

        zoneRepository = dataAccessFactory.zones(this);

        locationClient = new LocationClient(this, this, this);

        setupMapListeners();

        Bundle extras = getIntent().getExtras();
        zoneName = extras.getString("zoneName");
        zoneDesc = extras.getString("zoneDesc");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.location_action_bar, menu);
        return true;
    }

    private void setupMapListeners() {
        map.setOnMapClickListener(this);
        map.setOnMarkerDragListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        deleteCircle();
        deleteMarker();

        marker = map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(zoneName)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.building)));

        circle = map.addCircle(getCircleOptions(latLng));
    }

    private void deleteCircle(){
        if(circle != null) {
            circle.remove();
        }
    }

    private void deleteMarker(){
        if(marker != null) {
            marker.remove();
        }
    }

    public CircleOptions getCircleOptions(LatLng latLng) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(ZONE_RADIUS);
        circleOptions.fillColor(Color.argb(25, 139, 0, 255));
        circleOptions.strokeColor(Color.argb(150, 139, 0, 255));
        circleOptions.strokeWidth(3.0f);
        return circleOptions;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        deleteCircle();
        circle = map.addCircle(getCircleOptions(marker.getPosition()));
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        deleteCircle();
        circle = map.addCircle(getCircleOptions(marker.getPosition()));
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        deleteCircle();
        circle = map.addCircle(getCircleOptions(marker.getPosition()));
    }

    public static class ErrorDialogFragment extends DialogFragment {

        private Dialog dialog;

        public ErrorDialogFragment() {
            super();
            dialog = null;
        }

        public void setDialog(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return dialog;
        }

    }

    public void save(MenuItem unused) {
        if(marker != null)
        {
            Zone zone = prepareZone();
            DialogFragment newFragment = new SaveZoneDialogFragment(zone);
            newFragment.show(getSupportFragmentManager(), "save");
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Save Zone");
            builder.setMessage("Zone location not specified");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setIcon(R.drawable.trakemoi2);
            builder.show();
        }
    }

    private Zone prepareZone(){
        return new Zone.Builder()
                .withName(zoneName)
                .withDesc(zoneDesc)
                .withRadius(ZONE_RADIUS)
                .withLatitude(marker.getPosition().latitude)
                .withLongitude(marker.getPosition().longitude)
                .build();
    }

    // TODO: This is bad
    public void saveZone(Zone zone){
        zoneRepository.addZone(zone);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        changeLocation(locationClient.getLastLocation());
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        changeLocation(location);
    }

    private void changeLocation(Location location){
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(this, "Enabled new provider " + s,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(this, "Disabled new provider " + s,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private void showErrorDialog(int errorCode) {
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,
                this,
                CONNECTION_FAILURE_RESOLUTION_REQUEST);

        if (errorDialog != null) {
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();
            errorFragment.setDialog(errorDialog);
            errorFragment.show(getSupportFragmentManager(), TAG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.d(TAG, getString(R.string.resolved));
                        break;
                    default:
                        Log.d(TAG, getString(R.string.no_resolution));
                        break;
                }
            default:
                Log.d(TAG, getString(R.string.unknown_activity_request_code, requestCode));
                break;
        }
    }

    private boolean servicesConnected() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {
            Log.d(TAG, "Google Play services is available.");
            return true;
        } else {
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    resultCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            if (errorDialog != null) {
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getSupportFragmentManager(), TAG);
            }
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(servicesConnected()){
            locationClient.connect();
        }
    }

    @Override
    protected void onStop() {
        locationClient.disconnect();
        super.onStop();
    }

}