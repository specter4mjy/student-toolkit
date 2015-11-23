package ml.mk.jm.ay.ak.studenttoolkit.calendar.helper;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Calendar;

import ml.mk.jm.ay.ak.studenttoolkit.calendar.EventDataModel;

/**
 * Created by mukesh on 23/11/2015.
 */
public class CalendarHelper {

    private static final int PERMISSIONS_REQUEST_READ_CALENDAR = 1;
    private static final int PERMISSIONS_REQUEST_WRITE_CALENDAR = 2;

    // code to update event in calendar
    public static long updateEvent(Context context, EventDataModel event) {
        Calendar cal = Calendar.getInstance();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Events.CALENDAR_ID,event.cal_id);
        contentValues.put(CalendarContract.Events.TITLE, event.title);
        contentValues.put(CalendarContract.Events.DTSTART, event.startTimeMillis);
        contentValues.put(CalendarContract.Events.DTEND, event.endTimeMillis);
        contentValues.put(CalendarContract.Events.DESCRIPTION, event.description);
        contentValues.put(CalendarContract.Events.EVENT_LOCATION, event.location);
        contentValues.put(CalendarContract.Events.HAS_ALARM, event.hasAlarm);
        contentValues.put(CalendarContract.Events.ALL_DAY, event.allDay);
        contentValues.put(CalendarContract.Events.CALENDAR_TIME_ZONE,cal.getTimeZone().toString());

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return 0;
        }
        Uri uri = context.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, contentValues);
        Log.d("Mukesh", contentValues.toString());
        return new Long(uri.getLastPathSegment());
    }

    public void deleteEvent(Context context, EventDataModel event){

    }
}
