package com.thoughtworks.trakemoi.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import com.thoughtworks.trakemoi.models.Zone;

import java.util.ArrayList;
import java.util.List;

public class ZoneDataAccess {

    private SQLiteDatabase database;
    private SQLiteOpenHelper sqLiteOpenHelper;
    private String tableName = TrakeMoiDatabase.ZONE_TABLE;

    public ZoneDataAccess(SQLiteOpenHelper sqLiteOpenHelper) {
         this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    public long addZone(Zone zone) {
        database = sqLiteOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TrakeMoiDatabase.NAME, zone.getName());
        contentValues.put(TrakeMoiDatabase.DESC, zone.getDesc());
        contentValues.put(TrakeMoiDatabase.RADIUS, zone.getRadius());
        contentValues.put(TrakeMoiDatabase.LATITUDE, zone.getLatitude());
        contentValues.put(TrakeMoiDatabase.LONGITUDE, zone.getLongitude());

        long zoneId = database.insert(tableName, null, contentValues);
        database.close();

        zone.setId(zoneId);
        System.out.println("INSERT Zone ********************** " + zoneId);
        return zoneId;
    }

    public List<Zone> getZones() {
        Cursor cursor = null;

        List<Zone> zones = new ArrayList<Zone>();

        database = sqLiteOpenHelper.getReadableDatabase();
        try {

            cursor = database.query(tableName, null, null, null, null, null, TrakeMoiDatabase.ROWID + " DESC");

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    zones.add(
                            new Zone.Builder()
                                    .withName(cursor.getString(cursor.getColumnIndex(TrakeMoiDatabase.NAME)))
                                    .withDesc(cursor.getString(cursor.getColumnIndex(TrakeMoiDatabase.DESC)))
                                    .withId(cursor.getLong(cursor.getColumnIndex(TrakeMoiDatabase.ROWID)))
                                    .withRadius(cursor.getInt(cursor.getColumnIndex(TrakeMoiDatabase.RADIUS)))
                                    .withLatitude(cursor.getDouble(cursor.getColumnIndex(TrakeMoiDatabase.LATITUDE)))
                                    .withLongitude(cursor.getDouble(cursor.getColumnIndex(TrakeMoiDatabase.LONGITUDE)))
                                    .build()
                    );
                    cursor.moveToNext();
                }
            }
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
        database.close();
        for(Zone zone : zones){
            System.out.println("GET Zones ******************************************************* " + zone.getId());
        }
        return zones;
    }

}