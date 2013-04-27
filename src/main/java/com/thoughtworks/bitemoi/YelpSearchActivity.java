package com.thoughtworks.bitemoi;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.TextView;
import com.thoughtworks.bitemoi.adapters.BusinessListAdapter;
import com.thoughtworks.yelp.service.proxies.YelpProxy;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class YelpSearchActivity extends Activity {

    private TextView mSearchResultsText;

    @Override
    public void onCreate(Bundle b){
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(b);
        setContentView(R.layout.search);
        setTitle("Food search");
    }

    private ArrayList<Business> processJson(String jsonStuff) throws JSONException {
        JSONObject json = new JSONObject(jsonStuff);
        JSONArray businesses = json.getJSONArray("businesses");
        ArrayList<Business> businesList = new ArrayList<Business>(businesses.length());
        for (int i = 0; i < businesses.length(); i++) {
            JSONObject business = businesses.getJSONObject(i);

            double metersToMilesConversion = 0.00062137;
            Double distance = Double.parseDouble(business.getString("distance")) * metersToMilesConversion;
            DecimalFormat roundedDistance = new DecimalFormat("#.##");
            String formattedDistance = roundedDistance.format(distance) + "mi";
            Business newBusiness = new Business.Builder()
                                                .Name(business.getString("name"))
                                                .Distance(formattedDistance)
                                                .ImageURL(business.getString("image_url"))
                                                .Rating(business.getString("rating"))
                                                .build();
            Log.v("Json Array", business.toString());
            businesList.add(newBusiness);

        }

        return businesList;
    }

    public void searchForRestaurant(final View v)  {

        EditText searchKey = (EditText)findViewById(R.id.search);
        final String val = searchKey.getText().toString();
        final ListView view = (ListView) findViewById(R.id.searchResult);

        ArrayList<Business> businesses = new ArrayList<Business>();

        final ArrayAdapter<Business> adapter = new BusinessListAdapter(this, businesses);
        view.setAdapter(adapter);

        setProgressBarIndeterminateVisibility(true);
        new AsyncTask<Void, Void, ArrayList<Business>>() {
            @Override
            protected ArrayList<Business> doInBackground(Void... params) {
                YelpProxy yelp = YelpProxy.getYelp(YelpSearchActivity.this);
                String businesses = yelp.search(val, 37.788022, -122.399797);
                try {
                    return processJson(businesses);
                } catch (JSONException e) {
                    return new ArrayList<Business>();
                }
            }
            @Override
            protected void onPostExecute(ArrayList<Business> result) {
                adapter.addAll(result);
                setProgressBarIndeterminateVisibility(false);
            }
        }.execute();

    }

}
