package com.thoughtworks.bitemoi;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import com.thoughtworks.yelp.service.proxies.YelpProxy;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class YelpSearchActivity extends Activity {

    private ListView searchResultsView;

    @Override
    public void onCreate(Bundle b) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(b);
        setContentView(R.layout.search);
        setTitle("Food search");
    }

    protected List<Restaurant> processJson(String jsonStuff) throws JSONException {
        JSONObject json = new JSONObject(jsonStuff);
        JSONArray businesses = json.getJSONArray("businesses");
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>(businesses.length());
        for (int i = 0; i < businesses.length(); i++) {
            JSONObject business = businesses.getJSONObject(i);
            String imageUrl = business.getString("image_url");
            String name = business.getString("name");
            double rating = business.getDouble("rating");
            restaurants.add(new Restaurant(name, rating, imageUrl));
        }
        return restaurants;
    }

    public void searchForRestaurant(View view) {
        EditText searchKey = (EditText) findViewById(R.id.search);
        final String val = searchKey.getText().toString();
        searchResultsView = (ListView) findViewById(R.id.searchResult);

        final RestaurantListAdapter adapter = new RestaurantListAdapter(this, R.layout.restaurant);
        searchResultsView.setAdapter(adapter);

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
                    return null;
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
