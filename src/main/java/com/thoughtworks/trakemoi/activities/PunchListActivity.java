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
    public PunchListActivity(){

    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTitle("Punch List");
        setContentView(R.layout.punch_list);
        setUpActionBar();

        setInitAndPunchFlagInPref();

        punchDatabase = dataAccessFactory.punch(this);
        syncAndLoadData(punchDatabase);

        setButtonTextFromPref();
    }

    private void setButtonTextFromPref() {
        if (preferences.getBoolean(punchedFlag, false)) {
            inOrOutBtn.setText(R.string.outPunch);
        }else{
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

    @Override
    public void onResume() {
        super.onResume();
        inOrOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPunchAndSyncButtonText(punchDatabase, inOrOutBtn);
                syncAndLoadData(punchDatabase);
            }
        });
    }

    private void addPunchAndSyncButtonText(PunchDataAccess punchDatabase, Button inOrOutBtn) {
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

    private void addPunchData(final PunchDataAccess punchDatabase, String punchStatus) {
        final StatusListAdapter adapter = new StatusListAdapter(this);
        punchList.setAdapter(adapter);

        Date date = new Date();
        CharSequence dataString = DateFormat.format("EEEE, MMMM d, yyyy ", date.getTime());
        CharSequence timeString = DateFormat.format("hh:mm:ss", date.getTime());

        punchDatabase.addPunchStatus(new PunchStatus.StatusBuilder(punchStatus)
                .withTime(timeString.toString())
                .withDate(dataString.toString())
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
                return punchDatabase.loadPunchDataResults();
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