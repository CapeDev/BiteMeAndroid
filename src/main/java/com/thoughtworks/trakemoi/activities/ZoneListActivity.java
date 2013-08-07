package com.thoughtworks.trakemoi.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import com.thoughtworks.trakemoi.R;
import com.thoughtworks.trakemoi.adapters.ZoneListAdapter;
import com.thoughtworks.trakemoi.data.DataAccessFactory;
import com.thoughtworks.trakemoi.data.ZoneDataAccess;
import com.thoughtworks.trakemoi.models.Zone;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import javax.inject.Inject;
import java.util.Collection;

public class ZoneListActivity extends RoboActivity {

    @InjectView(R.id.zones)
    private ListView zones;

    @Inject
    DataAccessFactory dataAccessFactory;
    private ZoneDataAccess zoneRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTitle(getResources().getString(R.string.zones));
        setContentView(R.layout.zone_list);
        setUpActionBar();

        zones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
                Zone selectedZone = (Zone)(zones.getItemAtPosition(position));

                Intent intent = new Intent(ZoneListActivity.this, DisplayLocationActivity.class);
                intent.putExtra("zoneName", selectedZone.getName());
                intent.putExtra("zoneRadius", selectedZone.getRadius());
                intent.putExtra("zoneLatitude", selectedZone.getLatitude());
                intent.putExtra("zoneLongitude", selectedZone.getLongitude());
                startActivity(intent);
            }
        });

        zoneRepository = dataAccessFactory.zones(this);
        syncAndLoadData(zoneRepository);
    }

    private void syncAndLoadData(final ZoneDataAccess zoneRepository) {
        final ZoneListAdapter adapter = new ZoneListAdapter(this);
        zones.setAdapter(adapter);

        setProgressBarIndeterminateVisibility(true);
        new AsyncTask<Void, Void, Iterable<Zone>>() {
            @Override
            protected void onPreExecute() {
                adapter.clear();
            }

            @Override
            protected Iterable<Zone> doInBackground(Void... params) {
                return zoneRepository.getZones();
            }

            @Override
            protected void onPostExecute(Iterable<Zone> zones) {
                adapter.addAll((Collection<? extends Zone>) zones);
                setProgressBarIndeterminateVisibility(false);
            }

        }.execute();
    }

    private void setUpActionBar() {
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