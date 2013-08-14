package com.thoughtworks.trakemoi.geofence;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.thoughtworks.trakemoi.R;
import com.thoughtworks.trakemoi.activities.HomeActivity;
import com.thoughtworks.trakemoi.data.DataAccessFactory;

import javax.inject.Inject;
import java.util.List;

public class ReceiveTransitionsIntentService extends IntentService {

    private static final String ENTERED = "Entered";
    private static final String EXITED = "Exited";
    @Inject
    DataAccessFactory dataAccessFactory;

    public static final String CATEGORY_LOCATION_SERVICES =
            "com.thoughtworks.trakemoi.geofence.CATEGORY_LOCATION_SERVICES";

    public static final String ACTION_GEOFENCE_ERROR =
            "com.thoughtworks.trakemoi.geofence.ACTION_GEOFENCES_ERROR";

    public static final String EXTRA_GEOFENCE_STATUS =
            "com.thoughtworks.trakemoi.geofence.EXTRA_GEOFENCE_STATUS";

    public static final CharSequence GEOFENCE_ID_DELIMITER = ",";

    public ReceiveTransitionsIntentService() {
        super("ReceiveTransitionsIntentService");
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.addCategory(CATEGORY_LOCATION_SERVICES);

        if (LocationClient.hasError(intent)) {
            int errorCode = LocationClient.getErrorCode(intent);
            String errorMessage = LocationServiceErrorMessages.getErrorString(this, errorCode);

            System.out.println("Something went wrong in handle intent: " + errorMessage);

            broadcastIntent.setAction(ACTION_GEOFENCE_ERROR)
                    .putExtra(EXTRA_GEOFENCE_STATUS, errorMessage);

            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
        } else {
            int transition = LocationClient.getGeofenceTransition(intent);
            if (transition == Geofence.GEOFENCE_TRANSITION_ENTER || transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                List<Geofence> geofences = LocationClient.getTriggeringGeofences(intent);
                String[] geofenceIds = new String[geofences.size()];
                for (int index = 0; index < geofences.size(); index++) {
                    geofenceIds[index] = geofences.get(index).getRequestId();
                }
                String ids = TextUtils.join(GEOFENCE_ID_DELIMITER, geofenceIds);
                String transitionType = getTransitionString(transition);
                //TODO: LINA

//                addPunchToDBBasedOnZone(ids, transitionType);

                sendNotification(transitionType, ids);

                Log.d("TRAKEMOI",
                        getString(
                                R.string.geofence_transition_notification_title,
                                transitionType,
                                ids));
                Log.d("TRAKEMOI",
                        getString(R.string.geofence_transition_notification_text));

            } else {
                Log.e("TRAKEMOI",
                        getString(R.string.geofence_transition_invalid_type, transition));
            }
        }
    }

//    private void addPunchToDBBasedOnZone(String ids, String transitionType) {
//
//    }

    private void sendNotification(String transitionType, String ids) {
        Intent notificationIntent =
                new Intent(getApplicationContext(), HomeActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(HomeActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle(getString(R.string.geofence_transition_notification_title,
                        transitionType, ids))
                .setContentText(getString(R.string.geofence_transition_notification_text))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(notificationPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, builder.build());
    }

    public String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_transition_entered);

            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return getString(R.string.geofence_transition_exited);

            default:
                return getString(R.string.geofence_transition_unknown);
        }
    }
}
