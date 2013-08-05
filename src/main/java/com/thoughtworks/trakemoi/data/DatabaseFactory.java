package com.thoughtworks.trakemoi.data;

import android.content.Context;

import javax.inject.Inject;

public class DatabaseFactory {

    @Inject
    public DatabaseFactory(){
    }

    public TrakeMoiDatabase instance(Context context){
        return TrakeMoiDatabase.getInstance(context);
    }
}
