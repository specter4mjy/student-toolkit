package ml.mk.jm.ay.ak.studenttoolkit.calendar;

/**
 * Created by specter on 10/24/15.
 * Cooperate with Muckesh
 */

/**
 * event data structure
 */
public class EventDataModel {
    public String event_id;
    public String cal_id;
    public String title;
    public long startTimeMillis;
    public long endTimeMillis;
    public String location;
    public int eventColor;
    public boolean addIcon;
    public int allDay;
    public int hasAlarm;
    public String description;
    public int day_of_month;
}
