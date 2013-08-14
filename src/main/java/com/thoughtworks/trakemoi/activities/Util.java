package com.thoughtworks.trakemoi.activities;

import android.text.format.DateFormat;
import com.thoughtworks.trakemoi.data.PunchDataAccess;
import com.thoughtworks.trakemoi.data.TrakeMoiDatabaseException;
import com.thoughtworks.trakemoi.models.PunchStatus;

import java.util.Date;

public class Util {

    public static void addPunchData(final PunchDataAccess punchDatabase, String punchStatus, String zoneId) throws TrakeMoiDatabaseException {

        Date date = new Date();
        CharSequence dateString = DateFormat.format("EEEE, MMMM d, yyyy ", date.getTime());
        CharSequence timeString = DateFormat.format("kk:mm:ss", date.getTime());

        String zoneName;
        if (zoneId != null) {
            zoneName = punchDatabase.getZoneNameFromZoneId(Long.valueOf(zoneId).longValue());
            System.out.println("*****************************zoneName" + zoneName);
        } else {
            zoneName = "Default Unknown zone";
        }

        punchDatabase.addPunchStatus(new PunchStatus.StatusBuilder(punchStatus)
                .withTime(timeString.toString())
                .withDate(dateString.toString())
                .withZoneName(zoneName)
                .build()
        );
    }

}
