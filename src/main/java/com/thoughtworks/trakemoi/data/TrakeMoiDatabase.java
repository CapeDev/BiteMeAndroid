package com.thoughtworks.trakemoi.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TrakeMoiDatabase extends SQLiteOpenHelper {

    private static TrakeMoiDatabase trakeMoiDatabase;
    private static final String CLASS_TAG = "TrakeMoiDatabase";
    private static final String DATABASE_NAME = "TrakeMoiDatabase";
    private static final int DATABASE_VERSION = 1;

    public static final String ROW_ID = "_ID";

    public static final String PUNCH_TABLE = "punch";
    public static final String ZONE_ID = "zone_id";
    public static final String IN_TIME = "in_time";
    public static final String OUT_TIME = "out_time";

    public static final String ZONE_TABLE = "zone";
    public static final String ZONE_NAME = "zone_name";
    public static final String DESC = "desc";
    public static final String RADIUS = "radius";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    private static final String ZONE_TABLE_CREATE_STMT =
            "create table " + ZONE_TABLE
                    + " ("
                    + ROW_ID + " integer primary key autoincrement, "
                    + ZONE_NAME + " text not null, "
                    + DESC + " text not null, "
                    + RADIUS + " integer not null, "
                    + LATITUDE + " double not null, "
                    + LONGITUDE + " double not null"
                    + ");";

    private static final String PUNCH_TABLE_CREATE_STMT =
            "create table " + PUNCH_TABLE
                    + " ("
                    + ROW_ID + " integer primary key autoincrement, "
                    + ZONE_ID + " integer not null, "
                    + IN_TIME + " integer not null, "
                    + OUT_TIME + " integer, "
                    + "foreign key(" + ZONE_ID + ") references " + ZONE_TABLE + "(" + ROW_ID + ") "
                    + ");";

    private TrakeMoiDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static TrakeMoiDatabase getInstance(Context context) {
        if (null == trakeMoiDatabase) {
            trakeMoiDatabase = new TrakeMoiDatabase(context);
        }
        return trakeMoiDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(PUNCH_TABLE_CREATE_STMT);
        sqLiteDatabase.execSQL(ZONE_TABLE_CREATE_STMT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.v(CLASS_TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + " which will destroy all old data.");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PUNCH_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ZONE_TABLE);
        onCreate(sqLiteDatabase);
    }

    public synchronized void close() {
        super.close();
    }

}