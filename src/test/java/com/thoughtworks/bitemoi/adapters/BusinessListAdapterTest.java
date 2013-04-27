package com.thoughtworks.bitemoi.adapters;


import com.thoughtworks.bitemoi.YelpSearchActivity;
import com.thoughtworks.bitemoi.models.Business;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(RobolectricTestRunner.class)
public class BusinessListAdapterTest {
    @Test
    public void testGetCount() throws Exception {
        YelpSearchActivity activity = new YelpSearchActivity();
        ArrayList<Business> businesses = new ArrayList<Business>();
        businesses.add(new Business.Builder()
                                   .Name("Soup Company")
                                   .Distance(null)
                                   .ImageURL(null)
                                   .Rating(null)
                                   .build());

        businesses.add(new Business.Builder()
                                   .Name("Cafe Venue")
                                   .Distance(null)
                                   .ImageURL(null)
                                   .Rating(null)
                                   .build());


        BusinessListAdapter adapter = new BusinessListAdapter(activity, businesses);

        Assert.assertEquals(adapter.getCount(), 2);

    }
}
