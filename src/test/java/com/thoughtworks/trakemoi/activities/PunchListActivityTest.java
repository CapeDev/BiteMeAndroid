package com.thoughtworks.trakemoi.activities;

import android.content.Context;
import android.widget.Button;
import android.widget.ListView;
import com.thoughtworks.trakemoi.adapters.StatusListAdapter;
import com.thoughtworks.trakemoi.data.DataAccessFactory;
import com.thoughtworks.trakemoi.data.PunchDataAccess;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class PunchListActivityTest {
    @Mock
    DataAccessFactory dataAccessFactory;
    @Mock
    PunchDataAccess dataAccess;

    @Before
    public void setUp() {
        initMocks(this);
        when(dataAccessFactory.punch(any(Context.class))).thenReturn(dataAccess);
    }


    @Test
    @Ignore
    public void testPunchListDisplay() {
        ListView punchList = mock(ListView.class);
        Button inOrOutBtn = mock(Button.class);
        StatusListAdapter statusListAdapter = mock(StatusListAdapter.class);
        punchList.setAdapter(statusListAdapter);

//        statusListAdapter.addAll(PunchStatuses.getStubs());


        PunchListActivity punchListActivity = new PunchListActivity(punchList,inOrOutBtn );

        punchListActivity.onCreate(null);

//        ListView resultListView = (ListView) punchListActivity.findViewById(R.id.punchListView);

        verify(punchList).setAdapter(statusListAdapter);
//        assertEquals(resultListView.getAdapter().getCount(), 3);

    }

}