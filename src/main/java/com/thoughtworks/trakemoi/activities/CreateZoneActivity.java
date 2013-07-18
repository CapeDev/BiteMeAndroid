package com.thoughtworks.trakemoi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import com.thoughtworks.trakemoi.R;

public class CreateZoneActivity extends TrakemoiActivity {

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTitle("Create Zone");
        setContentView(R.layout.create_zone);
        setUpActionBar();

        EditText latitudeInput = (EditText) findViewById(R.id.latitude_input);
        latitudeInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        EditText longitudeInput = (EditText) findViewById(R.id.longitude_input);
        longitudeInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        EditText radiusInput = (EditText) findViewById(R.id.radius_input);
        radiusInput.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.zone_action_bar, menu);
        return true;
    }

    public void selectLocation(View unused) {
        startActivity(new Intent(this, LocationActivity.class));
        Toast.makeText(this, "I just clicked the location button!", Toast.LENGTH_LONG).show();
    }

    public void save(MenuItem unused) {
        Toast.makeText(this, "I just clicked the save button!", Toast.LENGTH_LONG).show();
    }

}