package ml.mk.jm.ay.ak.studenttoolkit.calendar.helper;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ml.mk.jm.ay.ak.studenttoolkit.calendar.EventDataModel;

import static ml.mk.jm.ay.ak.studenttoolkit.calendar.helper.TimeFormatHelper.dayOfWeekConverter;

/**
 * Created by specter on 11/23/15.
 */
public class CalendarProviderHelper {
    public static final String[] INSTANCE_PROJECTION = new String[]{
            CalendarContract.Instances.EVENT_ID,
            CalendarContract.Instances.BEGIN,
            CalendarContract.Instances.TITLE,
            CalendarContract.Instances.OWNER_ACCOUNT,
            CalendarContract.Instances.START_DAY,
            CalendarContract.Instances.END_DAY,
            CalendarContract.Instances.DESCRIPTION,
            CalendarContract.Instances.ALL_DAY,
            CalendarContract.Instances.END,
            CalendarContract.Instances.EVENT_COLOR,
            CalendarContract.Instances.EVENT_LOCATION,
            CalendarContract.Instances.HAS_ALARM


    };

    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_BEGIN_INDEX = 1;
    private static final int PROJECTION_TITLE_INDEX = 2;
    private static final int PROJECTION_OWNER_INDEX = 3;
    private static final int PROJECTION_SART_DAY_INDEX = 4;
    private static final int PROJECTION_END_DAY_INDEX = 5;
    private static final int PROJECTION_DESC_INDEX = 6;
    private static final int PROJECTION_ALL_DAY_INDEX = 7;
    private static final int PROJECTION_END_INDEX = 8;
    private static final int PROJECTION_COLOR_INDEX = 9;
    private static final int PROJECTION_LOCATION_INDEX = 10;
    private static final int PROJECTION_HAS_ALARM_INDEX = 11;
    private static Calendar nowTime;

    public static List<EventDataModel> getTodayEvents(Activity activity,int dayOffset) {
        nowTime = Calendar.getInstance();
        nowTime.add(Calendar.DATE, -dayOfWeekConverter(nowTime.get(Calendar.DAY_OF_WEEK)) + dayOffset);
        Calendar beginTime = nowTime;
        beginTime.set(Calendar.HOUR_OF_DAY, 0);
        beginTime.set(Calendar.MINUTE, 0);
        beginTime.set(Calendar.SECOND, 0);
        Long startOfDayMillis = beginTime.getTimeInMillis();
        Log.i("dayoffset", dayOffset + "");

        Calendar endTime = nowTime;
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.SECOND, 59);
        Long endOfDayMillis = endTime.getTimeInMillis();

        Cursor cursor = null;
        ContentResolver cr = activity.getContentResolver();
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startOfDayMillis);
        ContentUris.appendId(builder, endOfDayMillis);

        cursor = cr.query(builder.build(),
                INSTANCE_PROJECTION,
                null,
                null,
                CalendarContract.Instances.DTSTART+" ASC");

        List<EventDataModel> items = new ArrayList<>();

        long lastEventEndTime = startOfDayMillis; // 0:00 of the day
        EventDataModel model = new EventDataModel();
        long eventBeginTime = 0;
        long eventEndTime = 0;
        while (cursor.moveToNext()) {
            String title;
            String location;
            int eventColor;
            int allday;
            int hasAlarm;
            String description;


            eventBeginTime = cursor.getLong(PROJECTION_BEGIN_INDEX);
            eventEndTime = cursor.getLong(PROJECTION_END_INDEX);
            title = cursor.getString(PROJECTION_TITLE_INDEX);
            location = cursor.getString(PROJECTION_LOCATION_INDEX);
            eventColor = cursor.getInt(PROJECTION_COLOR_INDEX);
            allday = cursor.getInt(PROJECTION_ALL_DAY_INDEX);
            hasAlarm = cursor.getInt(PROJECTION_HAS_ALARM_INDEX);
            description = cursor.getString(PROJECTION_DESC_INDEX);

            if (allday == 1)
                continue;

            model = new EventDataModel();


            model.title = title;
            model.location = location;
            model.startTimeMillis = eventBeginTime;
            model.endTimeMillis = eventEndTime;
            model.eventColor = eventColor == 0 ? Color.parseColor("#555555") : eventColor;
            model.allDay = allday;
            model.description = description;
            model.hasAlarm = hasAlarm;
            model.day_of_month = nowTime.get(Calendar.DAY_OF_MONTH);

            Log.i("data", "start time" + hasAlarm+ "\n");
            Log.i("data", "end time" + allday+ "\n");

            if (eventBeginTime > lastEventEndTime) {
                EventDataModel addModel = new EventDataModel();
                addModel.startTimeMillis = lastEventEndTime;
                addModel.endTimeMillis = eventBeginTime;
                addModel.addIcon = true;
                items.add(addModel);
            }
            lastEventEndTime = eventEndTime;
            model.addIcon = false;
            items.add(model);
        }
        if (eventEndTime < endOfDayMillis) {
            EventDataModel addModel = new EventDataModel();
            addModel.startTimeMillis = lastEventEndTime;
            addModel.endTimeMillis = endOfDayMillis;
            addModel.addIcon = true;
            items.add(addModel);
        }
        return items;
    }

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
