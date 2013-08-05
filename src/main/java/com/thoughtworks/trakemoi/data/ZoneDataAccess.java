package com.thoughtworks.trakemoi.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.thoughtworks.trakemoi.models.Zone;

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

        return zoneId;
    }

}