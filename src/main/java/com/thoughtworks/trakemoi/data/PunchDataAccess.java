package com.thoughtworks.trakemoi.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.thoughtworks.trakemoi.models.PunchStatus;

import java.util.ArrayList;
import java.util.List;

public class PunchDataAccess {

    private SQLiteDatabase database;
    private SQLiteOpenHelper sqLiteOpenHelper;
    private String tableName = TrakeMoiDatabase.PUNCH_TABLE;

    public PunchDataAccess(SQLiteOpenHelper sqLiteOpenHelper) {
         this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    public Iterable<PunchStatus> loadPunchDataResults() {
        Cursor cursor = null;

        List<PunchStatus> punchStatuses = new ArrayList<PunchStatus>();

        database = sqLiteOpenHelper.getReadableDatabase();

        try {

        cursor = database.query(tableName, null, null, null, null, null, TrakeMoiDatabase.ROWID + " DESC");

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                punchStatuses.add(
                        new PunchStatus.StatusBuilder(cursor.getString(cursor.getColumnIndex(TrakeMoiDatabase.STATUS)))
                        .withTime(cursor.getString(cursor.getColumnIndex(TrakeMoiDatabase.TIME)))
                        .withDate(cursor.getString(cursor.getColumnIndex(TrakeMoiDatabase.DATE)))
                        .withId(cursor.getLong(cursor.getColumnIndex(TrakeMoiDatabase.ROWID)))
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
        return punchStatuses;
    }

    public long addPunchStatus(PunchStatus punch) {
        database = sqLiteOpenHelper.getWritableDatabase();

        ContentValues punchStatus = new ContentValues();
        punchStatus.put(TrakeMoiDatabase.STATUS, punch.getStatus());
        punchStatus.put(TrakeMoiDatabase.TIME, punch.getTime());
        punchStatus.put(TrakeMoiDatabase.DATE, punch.getDate());

        long punchId = database.insert(tableName, null, punchStatus);
        database.close();

        punch.setId(punchId);

        return punchId;
    }
}
