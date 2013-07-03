package com.thoughtworks.trakemoi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.thoughtworks.trakemoi.R;
import roboguice.activity.RoboActivity;

public class HomeActivity extends RoboActivity {

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void locationService(View view) {
        Intent locationIntent = new Intent(view.getContext(), LocationActivity.class);
        startActivityForResult(locationIntent, 0);
    }

//    Hiding restaurant list view and search function
//    public void yelpSearch(View view) {
//        Intent intent = new Intent(this, SearchActivity.class);
//        startActivity(intent);
//    }

    public void punchList(View view) {
        Intent punchListIntent = new Intent(this, PunchListActivity.class);
        startActivity(punchListIntent);
    }


    public void recordsSummary(View view) {
        Intent punchListIntent = new Intent(this, RecordsSummaryActivity.class);
        startActivity(punchListIntent);
    }

}