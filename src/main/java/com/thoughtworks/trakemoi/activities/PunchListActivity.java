package com.thoughtworks.trakemoi.activities;

import android.app.ActionBar;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import com.thoughtworks.trakemoi.R;
import com.thoughtworks.trakemoi.adapters.StatusListAdapter;
import com.thoughtworks.trakemoi.models.PunchStatus;
import com.thoughtworks.trakemoi.records.PunchStatuses;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PunchListActivity extends RoboActivity {

    @InjectView(R.id.punchListView) private final ListView punchList;

    public PunchListActivity(ListView punchList) {
        this.punchList = punchList;
    }

    public PunchListActivity() {
        this(null);
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTitle("Punch List");
        setContentView(R.layout.punch_list);
        setUpActionBar();

        final StatusListAdapter adapter = new StatusListAdapter(this);
        punchList.setAdapter(adapter);

        setProgressBarIndeterminateVisibility(true);
        new AsyncTask<Void, Void, Iterable<PunchStatus>>() {
            @Override
            protected void onPreExecute() {
                adapter.clear();
            }

            @Override
            protected Iterable<PunchStatus> doInBackground(Void... params) {
                List<PunchStatus> results = PunchStatuses.getStubs();  //dump db

                List<PunchStatus> punchStatuses = new ArrayList<PunchStatus>();

                for (PunchStatus punchStatus : results) {
                    punchStatuses.add(punchStatus);
                }

                return punchStatuses;
            }

            @Override
            protected void onPostExecute(Iterable<PunchStatus> punchStatuses) {
                adapter.addAll((Collection<? extends PunchStatus>) punchStatuses);
                setProgressBarIndeterminateVisibility(false);
            }
        }.execute();

    }

    private void setUpActionBar() {
        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
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