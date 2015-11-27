package ml.mk.jm.ay.ak.studenttoolkit.calendar.helper;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by specter on 11/18/15.
 * Cooperate with Muckesh
 */

/**
 * some helper function to convert time format
 */
public class TimeFormatHelper {
    /**
     * convert milliseconds to hour : minute String
     * @param endTime is it the end tiem , if true, change hour 00 to 24
     * @param millis
     * @return
     */
    public static String millisToHourAndMinuteStr(boolean endTime, long millis) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((millis));
        String hour = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
        if (hour.equals("00") && endTime) {
            hour = "24";
        }
        String minute = String.format("%02d", calendar.get(Calendar.MINUTE));
        return hour + " : " + minute;
    }

    /**
     * convert milliseconds to hour:minute String
     * @param millis
     * @return
     */
    public static String millisToHourAndMinuteStr(long millis) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((millis));
        String hour = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.format("%02d", calendar.get(Calendar.MINUTE));
        return hour + ":" + minute;
    }

    /**
     * convert milliseconds to date String
     * @param millis
     * @return
     */
    public static String millisToDate(long millis) {
        // String array containing name of week days
        String[] WeekOfDay = {"SUN", "MON", "TUE", "WED", "THU", "FRI","SAT"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        String dayOfWeek = WeekOfDay[calendar.get(Calendar.DAY_OF_WEEK)];
        String day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.format("%02d", calendar.get(Calendar.MONTH));

        String date = new StringBuilder().append(dayOfWeek).append(", ").append(day).append('/')
                .append(month).toString();
        return date;
    }

    /**
     * get day, month ,year from miliseccond
     * @param field
     * @param position day offset
     * @return
     */
    public static String getTimeField(int field, int position) {
        Calendar day = Calendar.getInstance();
        day.add(Calendar.DATE, -dayOfWeekConverter(day.get(Calendar.DAY_OF_WEEK)) + position);
        String result = null;
        switch (field) {
            case Calendar.DAY_OF_MONTH:
                result= day.get(Calendar.DAY_OF_MONTH) + "";
                break;
            case Calendar.MONTH:
                DateFormat monthFormatter = new SimpleDateFormat("MMMM");
                result= monthFormatter.format(day.getTime());
                break;
            case Calendar.YEAR:
                result= day.get(Calendar.YEAR) + "";
                break;
        }
        return result;
    }

    /**
     * int value of dayOfWeed converter
     * because in android sunday= 1  monday=2 ....saturday=7 , we need monday=0,tuesday=1....sunday=6
     * @param day
     * @return
     */
    public static int dayOfWeekConverter(int day) {
        int result = 0;
        switch (day) {
            case 1:
                result=6;
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                result=day - 2;
                break;
        }
        return result;
    }
}


