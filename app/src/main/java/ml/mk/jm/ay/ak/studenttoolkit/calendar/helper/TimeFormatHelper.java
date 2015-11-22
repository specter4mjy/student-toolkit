package ml.mk.jm.ay.ak.studenttoolkit.calendar.helper;

import java.util.Calendar;

/**
 * Created by specter on 11/18/15.
 */
public class TimeFormatHelper {
    public static String millisToHourAndMinuteStr(long millis) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((millis));
        String hour = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.format("%02d", calendar.get(Calendar.MINUTE));
        return hour + " : " + minute;
    }

    public static String millisToDate(long millis){
        // String array containing name of week days
        String []WeekOfDay = {"sun","mon","tue","wed","thu","fri","sat"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        String dayOfWeek = WeekOfDay[calendar.get(Calendar.DAY_OF_WEEK)];
        String day =String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        String month =String.format("%02d", calendar.get(Calendar.MONTH));

        String date = new StringBuilder().append(dayOfWeek).append(", ").append(day).append('/')
                                         .append(month).append('/').toString();
        return date;
    }
}
