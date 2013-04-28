package com.thoughtworks.bitemoi.activities;

import android.widget.EditText;
import android.widget.ListView;
import com.thoughtworks.bitemoi.R;
import com.thoughtworks.bitemoi.gateways.search.SearchGateway;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class SearchActivityTest {
    @Test
    @Ignore
    public void testSearchForRestaurant() throws Exception {
        EditText searchBox = mock(EditText.class);
        ListView searchResults = mock(ListView.class);
        SearchGateway yelpProxy = mock(SearchGateway.class);

        SearchActivity searchActivity = new SearchActivity(searchBox, searchResults, yelpProxy);
        searchActivity.onCreate(null);
//        searchActivity.processJson("{\"businesses\": [{\"name\": \"taco track\"}]}");

        ListView resultListView = (ListView) searchActivity.findViewById(R.id.search_results);
        assertEquals(resultListView.getAdapter().getCount(), 2);

    }
}
