package com.thoughtworks.trakemoi.data;

import android.content.Context;

import javax.inject.Inject;

public class DataAccessFactory {

    @Inject
    DatabaseFactory database;

    public PunchDataAccess punch(Context context) {
        return new PunchDataAccess(database.instance(context));
    }

    public ZoneDataAccess zones(Context context) {
        return new ZoneDataAccess(database.instance(context));
    }

}