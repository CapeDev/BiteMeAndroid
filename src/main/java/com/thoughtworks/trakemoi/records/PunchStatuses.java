package com.thoughtworks.trakemoi.records;

import com.thoughtworks.trakemoi.models.PunchStatus;

import java.util.ArrayList;
import java.util.List;

public class PunchStatuses {
    public static List<PunchStatus> getStubs(){
        List<PunchStatus> stubs = new ArrayList<PunchStatus>();

        PunchStatus status1 = new PunchStatus.StatusBuilder("In").withDate("2013-06-21").withTime("09:00").build();
        PunchStatus status2 = new PunchStatus.StatusBuilder("Out").withDate("2013-06-21").withTime("20:00").build();
        PunchStatus status3 = new PunchStatus.StatusBuilder("In").withDate("2013-06-22").withTime("10:00").build();
        PunchStatus status4 = new PunchStatus.StatusBuilder("Out").withDate("2013-06-22").withTime("19:00").build();

        stubs.add(status1);
        stubs.add(status2);
        stubs.add(status3);
        stubs.add(status4);

        return stubs;
    }
}
