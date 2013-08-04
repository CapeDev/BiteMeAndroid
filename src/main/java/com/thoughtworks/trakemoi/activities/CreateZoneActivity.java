package com.thoughtworks.trakemoi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.thoughtworks.trakemoi.R;
import roboguice.inject.InjectView;

public class CreateZoneActivity extends TrakemoiActivity {

    @InjectView(R.id.zone_name_input)
    private EditText zoneName;

    @InjectView(R.id.zone_select_location_button)
    private Button setLocation;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTitle("Create Zone");
        setContentView(R.layout.create_zone);
        setUpActionBar();
        zoneName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean ready = s.length() > 0;
                setLocation.setEnabled(ready);            }
        });
    }

    public void selectLocation(View unused) {
        Intent intent = new Intent(this, LocationActivity.class);
        intent.putExtra("zoneName", zoneName.getText().toString());
        startActivity(intent);
    }

}