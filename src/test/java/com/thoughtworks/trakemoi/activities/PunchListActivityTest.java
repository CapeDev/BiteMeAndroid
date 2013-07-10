package com.thoughtworks.trakemoi.activities;

import android.widget.ListView;
import com.thoughtworks.trakemoi.adapters.StatusListAdapter;
import com.thoughtworks.trakemoi.records.PunchStatuses;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class PunchListActivityTest {

    @Test
    public void testPunchListDisplay(){
        ListView punchList = mock(ListView.class);
        StatusListAdapter statusListAdapter = mock(StatusListAdapter.class);
        punchList.setAdapter(statusListAdapter);

        statusListAdapter.addAll(PunchStatuses.getStubs());


        PunchListActivity punchListActivity = new PunchListActivity(punchList);

        punchListActivity.onCreate(null);

//        ListView resultListView = (ListView) punchListActivity.findViewById(R.id.punchListView);

        verify(punchList).setAdapter(statusListAdapter);
//        assertEquals(resultListView.getAdapter().getCount(), 3);

    }


}