package com.thoughtworks.trakemoi.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import com.thoughtworks.trakemoi.models.PunchStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.thoughtworks.trakemoi.data.TrakeMoiDatabase.*;

public class PunchDataAccess {

    private final String IN_STATUS = "In";
    private final String OUT_STATUS = "Out";
    private final String ERROR_MSG = "ERROR";

    private SQLiteDatabase database;
    private SQLiteOpenHelper sqLiteOpenHelper;
    private String tableName = PUNCH_TABLE;
    private String WAIT_FOR_OUT_PUNCH = "WAIT FOR OUT";
    private PunchStatus PUNCH_TMP = null;

    public PunchDataAccess(SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    public Iterable<PunchStatus> loadPunchDataResults() {
        Cursor cursor = null;
        List<PunchStatus> punchStatuses = new ArrayList<PunchStatus>();

        database = sqLiteOpenHelper.getReadableDatabase();

        try {
            cursor = database.query(tableName, null, null, null, null, null, ROW_ID + " DESC");

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    addToPunchStatusesFromPunchDataDB(cursor, punchStatuses);
                    cursor.moveToNext();
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        database.close();
        return punchStatuses;
    }

    private void addToPunchStatusesFromPunchDataDB(Cursor cursor, List<PunchStatus> punchStatuses) {
        String status = provideStatusBasedOnTime(cursor);
        long statusTimeStamp = getTimeStampFromDB(cursor);
        String statusTime = getStatusTime(statusTimeStamp);
        String statusDate = getStatusDate(statusTimeStamp);

        if (IN_STATUS.equals(status)) {
            punchStatuses.add(buildPunchStatusBasedOn(cursor, status, statusTime, statusDate));
        } else if (OUT_STATUS.equals(status)) {
            punchStatuses.add(buildPunchStatusBasedOn(cursor, status, statusTime, statusDate));

            long inTimeStamp = cursor.getLong(cursor.getColumnIndex(IN_TIME));
            punchStatuses.add(buildPunchStatusBasedOn(cursor, IN_STATUS, getStatusTime(inTimeStamp), getStatusDate(inTimeStamp)));

        }
    }

    private PunchStatus buildPunchStatusBasedOn(Cursor cursor, String status, String statusTime, String statusDate) {
        return new PunchStatus.StatusBuilder(status)
                .withTime(statusTime)
                .withDate(statusDate)
                .withZoneName(provideZoneNameBasedOnActivity(cursor))
                .withId(cursor.getLong(cursor.getColumnIndex(ROW_ID)))
                .build();
    }

    public String getZoneNameFromZoneId(long zoneId) {
        String[] columns = {TrakeMoiDatabase.ZONE_NAME};
        String selection = TrakeMoiDatabase.ROW_ID + " = ?";
        String[] selectionArgs = {String.valueOf(zoneId)};

        database = sqLiteOpenHelper.getReadableDatabase();

        Cursor cursor = null;
        String zoneName = null;

        try {
            cursor = database.query(TrakeMoiDatabase.ZONE_TABLE, columns, selection, selectionArgs, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
//                    zoneName = cursor.getString(cursor.getColumnIndex(TrakeMoiDatabase.ZONE_ID));
                    //TODO
                    zoneName = "TW";
                    cursor.moveToNext();
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return zoneName;
    }

    public int getZoneIdFromZoneName(String zoneName) {
        String[] columns = {TrakeMoiDatabase.ROW_ID};
        String selection = TrakeMoiDatabase.ZONE_NAME + " = ?";
        String[] selectionArgs = {zoneName};

        database = sqLiteOpenHelper.getReadableDatabase();

        Cursor cursor = null;
        int zoneId = -1;

        try {
            cursor = database.query(TrakeMoiDatabase.ZONE_TABLE, columns, selection, selectionArgs, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
//                    zoneId = cursor.getInt(cursor.getColumnIndex(TrakeMoiDatabase.ZONE_NAME));
                    //TODO
                    zoneId = 1;
                    cursor.moveToNext();
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return zoneId;
    }


    private String getStatusTime(long timeStamp) {
        Date date = new Date(timeStamp);

        CharSequence timeString = DateFormat.format("kk:mm:ss", date.getTime());

        return timeString.toString();
    }

    private String getStatusDate(long timeStamp) {
        Date date = new Date(timeStamp);

        CharSequence dateString = DateFormat.format("EEEE, MMMM d, yyyy ", date.getTime());
        return dateString.toString();
    }

    private long getTimeStampFromDB(Cursor cursor) {
        String status = provideStatusBasedOnTime(cursor);
        long timeStamp = 0;
        if (IN_STATUS.equals(status)) {
            timeStamp = cursor.getLong(cursor.getColumnIndex(IN_TIME));
        } else if (OUT_STATUS.equals(status)) {
            timeStamp = cursor.getLong(cursor.getColumnIndex(OUT_TIME));
        }

        return timeStamp;
    }

    private String provideZoneNameBasedOnActivity(Cursor cursor) {
        int zoneId = cursor.getColumnIndex(ZONE_ID);
        return getZoneNameFromZoneId(zoneId);
    }

    private String provideStatusBasedOnTime(Cursor cursor) {
        int nullFromDB = 0;

        long inTime = cursor.getLong(cursor.getColumnIndex(IN_TIME));
        long outTime = cursor.getLong(cursor.getColumnIndex(OUT_TIME));
        if (inTime != nullFromDB & outTime == nullFromDB) {
            return IN_STATUS;
        }
        if (inTime != nullFromDB & outTime != nullFromDB) {
            return OUT_STATUS;
        }
        return ERROR_MSG;
    }

    public long addPunchStatus(PunchStatus punch) {
        database = sqLiteOpenHelper.getWritableDatabase();

        int zoneId = getZoneIdFromZoneName(punch.getZoneName());
        long timestamp = convertTimeFromStringToLong(punch);

        String status = punch.getStatus();

        ContentValues punchStatus = new ContentValues();
        punchStatus.put(ZONE_ID, zoneId);

        long punchId = 0;

        if (IN_STATUS.equals(status)) {
            punchStatus.put(IN_TIME, timestamp);
            punchStatus.put(OUT_TIME, WAIT_FOR_OUT_PUNCH);
            punchId = database.insert(tableName, null, punchStatus);
        } else if (OUT_STATUS.equals(status)) {
            String selClause = "select " + ROW_ID + " FROM " + PUNCH_TABLE + " WHERE " + OUT_TIME + "= ?";
            Cursor cursor = null;
            try {
                cursor = database.rawQuery(selClause, new String[]{WAIT_FOR_OUT_PUNCH});
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        punchId = cursor.getLong(cursor.getColumnIndex(ROW_ID));
                        cursor.moveToNext();
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            punchStatus.put(OUT_TIME, timestamp);
            database.update(tableName, punchStatus, ROW_ID + "= ?", new String[]{String.valueOf(punchId)});
        }

        database.close();

        punch.setId(punchId);

        return punchId;
    }

    private long convertTimeFromStringToLong(PunchStatus punch) {
        String dateWithTime = punch.getDate() + punch.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d, yyyy kk:mm:ss", Locale.ENGLISH);

        Date date = null;

        try {
            date = formatter.parse(dateWithTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date.getTime();
    }
}
