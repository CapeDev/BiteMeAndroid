package com.thoughtworks.bitemoi.adapters;


import com.thoughtworks.bitemoi.YelpSearchActivity;
import com.thoughtworks.bitemoi.models.Restaurant;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(RobolectricTestRunner.class)
public class RestaurantListAdapterTest {
    @Test
    public void testGetCount() throws Exception {
        YelpSearchActivity activity = new YelpSearchActivity();
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
        restaurants.add(new Restaurant.Builder()
                                   .withName("Soup Company")
                                   .withDistance(null)
                                   .withImageURL(null)
                                   .withRating(null)
                                   .build());

        restaurants.add(new Restaurant.Builder()
                                   .withName("Cafe Venue")
                                   .withDistance(null)
                                   .withImageURL(null)
                                   .withRating(null)
                                   .build());


        RestaurantListAdapter adapter = new RestaurantListAdapter(activity, restaurants);

        Assert.assertEquals(adapter.getCount(), 2);

    }
}
