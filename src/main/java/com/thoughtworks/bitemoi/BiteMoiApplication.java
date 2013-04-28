package com.thoughtworks.bitemoi;

import android.app.Application;
import com.thoughtworks.bitemoi.modules.SearchGatewayModule;
import roboguice.RoboGuice;

public class BiteMoiApplication extends Application {
    @Override public void onCreate() {
        RoboGuice.setBaseApplicationInjector(this, RoboGuice.DEFAULT_STAGE,
                RoboGuice.newDefaultRoboModule(this),
                new SearchGatewayModule(getApplicationContext()));
    }
}
