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
}
