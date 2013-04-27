package com.thoughtworks.bitemoi;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import com.thoughtworks.bitemoi.adapters.RestaurantListAdapter;
import com.thoughtworks.bitemoi.models.Restaurant;
import com.thoughtworks.yelp.service.proxies.YelpProxy;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class YelpSearchActivity extends Activity {
    @Override
    public void onCreate(Bundle b) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(b);
        setContentView(R.layout.search);
        setTitle("Food search");
    }

    private ArrayList<Restaurant> processJson(String jsonStuff) throws JSONException {
        JSONObject json = new JSONObject(jsonStuff);
        JSONArray restaurantsJson = json.getJSONArray("businesses");
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>(restaurantsJson.length());
        for (int i = 0; i < restaurantsJson.length(); i++) {
            JSONObject restaurantJson = restaurantsJson.getJSONObject(i);

            double metersToMilesConversion = 0.00062137;
            Double distance = Double.parseDouble(restaurantJson.getString("distance")) * metersToMilesConversion;
            DecimalFormat roundedDistance = new DecimalFormat("#.##");
            String formattedDistance = roundedDistance.format(distance) + "mi";
            Restaurant restaurant = new Restaurant.Builder()
                    .withName(restaurantJson.getString("name"))
                    .withDistance(formattedDistance)
                    .withImageURL(restaurantJson.getString("image_url"))
                    .withRating(restaurantJson.getString("rating"))
                    .build();

            Log.v("Json Array", restaurantJson.toString());

            restaurants.add(restaurant);
        }

        return restaurants;
    }

    public void searchForRestaurant(final View view) {
        EditText searchKey = (EditText) findViewById(R.id.search);
        final String val = searchKey.getText().toString();
        final ListView searchResultsView = (ListView) findViewById(R.id.search_results);

        List<Restaurant> restaurants = new ArrayList<Restaurant>();

        final RestaurantListAdapter adapter = new RestaurantListAdapter(this, restaurants);
        searchResultsView.setAdapter(adapter);

        setProgressBarIndeterminateVisibility(true);
        setProgressBarIndeterminateVisibility(true);
        new AsyncTask<Void, Void, List<Restaurant>>() {
            @Override protected void onPreExecute() {
                adapter.clear();
            }

            @Override
            protected List<Restaurant> doInBackground(Void... params) {
                YelpProxy yelp = YelpProxy.getYelp(YelpSearchActivity.this);
                String businesses = yelp.search(val, 37.788022, -122.399797);
                try {
                    return processJson(businesses);
                } catch (JSONException e) {
                    return new ArrayList<Restaurant>();
                }
            }

            @Override
            protected void onPostExecute(List<Restaurant> restaurants) {
                adapter.addAll(restaurants);
                setProgressBarIndeterminateVisibility(false);
            }
        }.execute();
    }
}
