package com.thoughtworks.trakemoi.adapters;


import android.widget.EditText;
import android.widget.ListView;
import com.thoughtworks.trakemoi.activities.SearchActivity;
import com.thoughtworks.trakemoi.gateways.search.SearchGateway;
import com.thoughtworks.trakemoi.models.Restaurant;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class RestaurantListAdapterTest {
    @Test
    @Ignore
    public void testGetCount() throws Exception {
        EditText searchBox = mock(EditText.class);
        ListView searchResults = mock(ListView.class);
        SearchGateway searchGateway = mock(SearchGateway.class);

        SearchActivity activity = new SearchActivity(searchBox, searchResults, searchGateway);
        Restaurant restaurant1 = new Restaurant.Builder()
                .withName("Soup Company")
                .withDistance(null)
                .withImageURL(null)
                .withRating(null)
                .build();
        Restaurant restaurant2 = new Restaurant.Builder()
                .withName("Cafe Venue")
                .withDistance(null)
                .withImageURL(null)
                .withRating(null)
                .build();

        RestaurantListAdapter adapter = new RestaurantListAdapter(activity);
        adapter.addAll((Iterable<Restaurant>) asList(restaurant1, restaurant2));

        Assert.assertEquals(adapter.getCount(), 2);
    }
}
