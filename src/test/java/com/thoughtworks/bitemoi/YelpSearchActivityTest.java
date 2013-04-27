package com.thoughtworks.bitemoi;

import android.widget.ListView;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class YelpSearchActivityTest {

    @Test
    @Ignore
    public void testSearchForRestaurant() throws Exception {
        YelpSearchActivity yelpSearchActivity = new YelpSearchActivity();
        yelpSearchActivity.onCreate(null);
//        yelpSearchActivity.processJson("{\"businesses\": [{\"name\": \"taco track\"}]}");

        ListView resultListView = (ListView) yelpSearchActivity.findViewById(R.id.searchResult);
        assertEquals(resultListView.getAdapter().getCount(), 2);

    }

}
