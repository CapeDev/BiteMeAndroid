package com.thoughtworks.trakemoi.activities;

import android.app.ActionBar;
import android.view.MenuItem;
import roboguice.activity.RoboActivity;

public class TrakemoiActivity extends RoboActivity {

    protected void setUpActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}