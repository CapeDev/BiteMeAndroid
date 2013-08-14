package com.thoughtworks.trakemoi.geofence;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationStatusCodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeofenceRequester implements
        LocationClient.OnAddGeofencesResultListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private final Activity callingActivity;

    private PendingIntent geofencePendingIntent;

    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    public static final String ACTION_CONNECTION_ERROR =
            "com.thoughtworks.trakemoi.geofence.ACTION_CONNECTION_ERROR";

    public static final String CATEGORY_LOCATION_SERVICES =
            "com.thoughtworks.trakemoi.geofence.CATEGORY_LOCATION_SERVICES";

    public static final String EXTRA_CONNECTION_ERROR_CODE =
            "com.thoughtworks.trakemoi.geofence.EXTRA_CONNECTION_ERROR_CODE";

    private ArrayList<Geofence> currentGeofences;

    private LocationClient locationClient;

    private boolean requestInProgress = false;

    public GeofenceRequester(Activity activityContext) {
        callingActivity = activityContext;
    }

    //TODO this seems odd
    public void setRequestInProgress(boolean flag) {
        requestInProgress = flag;
    }

    public boolean isRequestInProgress() {
        return requestInProgress;
    }

    public PendingIntent getRequestPendingIntent() {
        return createRequestPendingIntent();
    }

    public void addGeofences(List<Geofence> geofences) {
        currentGeofences = (ArrayList<Geofence>) geofences;
        if (!requestInProgress) {
            requestInProgress = true;
            requestConnection();
        }
    }

    private void requestConnection() {
        getLocationClient().connect();
    }

    private GooglePlayServicesClient getLocationClient() {
        if (locationClient == null) {
            locationClient = new LocationClient(callingActivity, this, this);
        }
        return locationClient;
    }

    private void continueAddGeofences() {
        geofencePendingIntent = createRequestPendingIntent();
        locationClient.addGeofences(currentGeofences, geofencePendingIntent, this);
    }

    @Override
    public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) {
        Intent broadcastIntent = new Intent();
        String msg;

        if (LocationStatusCodes.SUCCESS == statusCode) {
            msg = "Geofence request IDs: " + Arrays.toString(geofenceRequestIds);
            Log.d("TRAKEMOI", msg);
        } else {

            msg = "Geofence request failed for IDs: " + Arrays.toString(geofenceRequestIds);
            Log.d("TRAKEMOI", msg);
        }

        LocalBroadcastManager.getInstance(callingActivity).sendBroadcast(broadcastIntent);
        requestDisconnection();
    }

    private void requestDisconnection() {
        requestInProgress = false;
        getLocationClient().disconnect();
    }

    @Override
    public void onConnected(Bundle arg0) {
        continueAddGeofences();
    }

    @Override
    public void onDisconnected() {
        requestInProgress = false;
        locationClient = null;
    }

    private PendingIntent createRequestPendingIntent() {
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        } else {
            Intent intent = new Intent(callingActivity, ReceiveTransitionsIntentService.class);
            return PendingIntent.getService(
                    callingActivity,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        requestInProgress = false;
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(callingActivity,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);

            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Intent errorBroadcastIntent = new Intent(ACTION_CONNECTION_ERROR);
            errorBroadcastIntent.addCategory(CATEGORY_LOCATION_SERVICES)
                .putExtra(EXTRA_CONNECTION_ERROR_CODE, connectionResult.getErrorCode());
            LocalBroadcastManager.getInstance(callingActivity).sendBroadcast(errorBroadcastIntent);
        }
    }

}