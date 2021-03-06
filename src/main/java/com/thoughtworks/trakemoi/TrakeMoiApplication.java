package com.thoughtworks.trakemoi;

import android.app.Application;
import android.content.Context;
import com.thoughtworks.trakemoi.modules.SearchGatewayModule;
import roboguice.RoboGuice;

public class TrakeMoiApplication extends Application {

//    @Override public void onCreate() {
//        RoboGuice.setBaseApplicationInjector(this, RoboGuice.DEFAULT_STAGE,
//                RoboGuice.newDefaultRoboModule(this),
//                new SearchGatewayModule(getApplicationContext()));
//    }

    private static Context context;

    public void onCreate(){
        super.onCreate();
        TrakeMoiApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return TrakeMoiApplication.context;
    }
}
