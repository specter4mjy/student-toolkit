package ml.mk.jm.ay.ak.studenttoolkit.calendar.helper;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by specter on 11/18/15.
 */
public class TimeFormatHelper {
    public static String millisToHourAndMinuteStr(int date, long millis) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((millis));
//        String hour = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
//        if (hour.equals("00") && date != calendar.get(Calendar.DAY_OF_MONTH)) {
//            hour = "24";
//        }
//        String minute = String.format("%02d", calendar.get(Calendar.MINUTE));
        String s= calendar.get(Calendar.DAY_OF_MONTH)+"";
        return s;
//        return hour + " : " + minute;
    }

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


