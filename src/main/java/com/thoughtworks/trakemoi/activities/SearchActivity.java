package com.thoughtworks.trakemoi.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import com.google.inject.Inject;
import com.thoughtworks.trakemoi.R;
import com.thoughtworks.trakemoi.adapters.RestaurantListAdapter;
import com.thoughtworks.trakemoi.gateways.search.SearchGateway;
import com.thoughtworks.trakemoi.gateways.search.wiretypes.BusinessWireType;
import com.thoughtworks.trakemoi.gateways.search.wiretypes.SearchWireType;
import com.thoughtworks.trakemoi.models.Restaurant;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends RoboActivity {
    @InjectView(R.id.search) private final EditText searchBox;
    @InjectView(R.id.search_results) private final ListView searchResults;
    @Inject private final SearchGateway searchGateway;

    public SearchActivity() { this(null, null, null); }

    public SearchActivity(
            EditText searchBox,
            ListView searchResults,
            SearchGateway yelpProxy) {
        this.searchBox = searchBox;
        this.searchResults = searchResults;
        this.searchGateway = yelpProxy;
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setTitle("Food search");
        setContentView(R.layout.search);
    }

    public void searchForRestaurant(View view) {
        final String searchText = searchBox.getText().toString();

        final RestaurantListAdapter adapter = new RestaurantListAdapter(this);
        searchResults.setAdapter(adapter);

        setProgressBarIndeterminateVisibility(true);
        new AsyncTask<Void, Void, Iterable<Restaurant>>() {
            @Override protected void onPreExecute() {
                adapter.clear();
            }

            @Override
            protected Iterable<Restaurant> doInBackground(Void... params) {
                SearchWireType results = searchGateway.search(searchText, 37.788022, -122.399797);
                List<Restaurant> restaurants = new ArrayList<Restaurant>();
                for (BusinessWireType businessWireType : results.getBusinesses()) {
                    Double metersToMilesConversion = 0.00062137;
                    Double distanceInMeters = businessWireType.getDistance() * metersToMilesConversion;
                            DecimalFormat roundedDistance = new DecimalFormat("#.##");
                    String formattedDistance = roundedDistance.format(distanceInMeters) + "mi";
                    restaurants.add(new Restaurant.Builder()
                            .withName(businessWireType.getName())
                            .withImageURL(businessWireType.getImageUrl())
                            .withDistance(formattedDistance)
                            .withRating(businessWireType.getRating())
                            .build());
                }
                return restaurants;
            }

            @Override
            protected void onPostExecute(Iterable<Restaurant> restaurants) {
                adapter.addAll(restaurants);
                setProgressBarIndeterminateVisibility(false);
            }
        }.execute();
    }
}
