package com.thoughtworks.trakemoi.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.thoughtworks.trakemoi.R;

/**
 * User: lqu
 */
public class RecordsSummaryActivity extends Activity {

    private ListView punchStatuses ;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.punch_list);
        setTitle("Punch Status List");

        punchStatuses = (ListView) findViewById(R.id.punchListView);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);

        punchStatuses.setAdapter(adapter);

        adapter.add("It works!!!!");

    }

}
