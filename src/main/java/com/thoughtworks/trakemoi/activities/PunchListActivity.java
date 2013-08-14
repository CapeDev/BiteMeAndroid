package com.thoughtworks.trakemoi.activities;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.thoughtworks.trakemoi.R;
import com.thoughtworks.trakemoi.adapters.StatusListAdapter;
import com.thoughtworks.trakemoi.data.DataAccessFactory;
import com.thoughtworks.trakemoi.data.PunchDataAccess;
import com.thoughtworks.trakemoi.data.TrakeMoiDatabaseException;
import com.thoughtworks.trakemoi.models.PunchStatus;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Date;

public class PunchListActivity extends RoboActivity {

    @InjectView(R.id.punchListView)
    private ListView punchList;

    @InjectView(R.id.inOrOutBtn)
    private Button inOrOutBtn;

    @Inject
    DataAccessFactory dataAccessFactory;
    private PunchDataAccess punchDatabase;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private String punchedFlag = "punched";
    private String initFlag = "initialized";

    public PunchListActivity(ListView punchList, Button inOrOutBtn) {
        this.punchList = punchList;
        this.inOrOutBtn = inOrOutBtn;
    }

    public PunchListActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTitle(getResources().getString(R.string.punchList));
        setContentView(R.layout.punch_list);
        setUpActionBar();
        setInitAndPunchFlagInPref();

        punchDatabase = dataAccessFactory.punch(this);
        try {
            checkZoneAvailability(punchDatabase);
        } catch (TrakeMoiDatabaseException e) {
            Toast.makeText(getBaseContext(), "You need have at least one zone saved to start here!", Toast.LENGTH_LONG).show();
            super.onBackPressed();
        }
        syncAndLoadData(punchDatabase);
        setButtonTextFromPref();
    }

    @Override
    public void onResume() {
        super.onResume();
        inOrOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addPunchAndSyncButtonText(punchDatabase, inOrOutBtn);
                } catch (TrakeMoiDatabaseException e) {
                    Toast.makeText(getBaseContext(), "You need have at least one zone saved to start here!", Toast.LENGTH_LONG).show();
                }
                syncAndLoadData(punchDatabase);
            }
        });
    }

    private void checkZoneAvailability(PunchDataAccess punchDatabase) throws TrakeMoiDatabaseException {
        punchDatabase.checkZoneAvailability();
    }

    private void setButtonTextFromPref() {
        if (preferences.getBoolean(punchedFlag, false)) {
            inOrOutBtn.setText(R.string.outPunch);
        } else {
            inOrOutBtn.setText(R.string.inPunch);
        }
    }

    private void setInitAndPunchFlagInPref() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        if (!preferences.getBoolean(initFlag, false)) {
            editor.putBoolean(initFlag, true);
            editor.putBoolean(punchedFlag, false);
            editor.commit();
        }
    }

    private void addPunchAndSyncButtonText(PunchDataAccess punchDatabase, Button inOrOutBtn) throws TrakeMoiDatabaseException {
        if (inOrOutBtn == null || inOrOutBtn.getText() == null)
            return;

        String inPunch = getString(R.string.inPunch);
        String outPunch = getString(R.string.outPunch);

        if (!preferences.getBoolean(punchedFlag, false)) {
            editor.putBoolean(punchedFlag, true);
            addPunchData(punchDatabase, inPunch);
            inOrOutBtn.setText(R.string.outPunch);
            editor.commit();
        } else if (preferences.getBoolean(punchedFlag, false)) {
            editor.putBoolean(punchedFlag, false);
            addPunchData(punchDatabase, outPunch);
            inOrOutBtn.setText(R.string.inPunch);
            editor.commit();
        }
    }

    private void addPunchData(final PunchDataAccess punchDatabase, String punchStatus) throws TrakeMoiDatabaseException {
        final StatusListAdapter adapter = new StatusListAdapter(this);
        punchList.setAdapter(adapter);

        Date date = new Date();
        CharSequence dateString = DateFormat.format("EEEE, MMMM d, yyyy ", date.getTime());
        CharSequence timeString = DateFormat.format("kk:mm:ss", date.getTime());

        //TODO
        String zoneName  = "TW" ;
        punchDatabase.addPunchStatus(new PunchStatus.StatusBuilder(punchStatus)
                .withTime(timeString.toString())
                .withDate(dateString.toString())
                .withZoneName(zoneName)
                .build()
        );
    }

    private void syncAndLoadData(final PunchDataAccess punchDatabase) {
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
                try {
                    return punchDatabase.loadPunchDataResults();
                } catch (TrakeMoiDatabaseException e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Iterable<PunchStatus> punchStatuses) {
                if (punchStatuses != null) {
                    adapter.addAll((Collection<? extends PunchStatus>) punchStatuses);
                }
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