package ml.mk.jm.ay.ak.studenttoolkit.calendar;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ml.mk.jm.ay.ak.studenttoolkit.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by specter on 10/24/15.
 */
public class ScreenSlideFragment extends android.support.v4.app.Fragment {
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;


    private static final String DEBUG_TAG = "MyActivity";

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
            CalendarContract.Instances.EVENT_LOCATION

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
    Calendar nowTime;

    public static ScreenSlideFragment newInstance(int page) {
        ScreenSlideFragment screenSlideFragment = new ScreenSlideFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        screenSlideFragment.setArguments(args);
        return screenSlideFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.content_main, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);


        DateFormat formatter = new SimpleDateFormat("HH/mm/ss/MM/dd/yyyy");

        int page = getArguments().getInt("page");
        Log.i("specter", page + "");

        nowTime = Calendar.getInstance();
        nowTime.add(Calendar.DATE, page - 1);
        Calendar beginTime = nowTime;
        beginTime.set(Calendar.HOUR_OF_DAY, 0);
        beginTime.set(Calendar.MINUTE, 0);
        beginTime.set(Calendar.SECOND, 0);
        Log.d(DEBUG_TAG, beginTime.getTime().toString());
        Long startOfDayMillis = beginTime.getTimeInMillis();

        Calendar endTime = nowTime;
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.SECOND, 59);
        Log.d(DEBUG_TAG, endTime.getTime().toString());
        Long endOfDayMillis = endTime.getTimeInMillis();

        Cursor cursor = null;
        ContentResolver cr = getActivity().getContentResolver();
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startOfDayMillis);
        ContentUris.appendId(builder, endOfDayMillis);

        cursor = cr.query(builder.build(),
                INSTANCE_PROJECTION,
                null,
                null, null);

        List<EventDataModel> items = new ArrayList<>();

        long lastEventEndTime=startOfDayMillis; // 0:00 of the day
        EventDataModel model = new EventDataModel();
        long eventBeginTime = 0;
        long eventEndTime = 0;
        while (cursor.moveToNext()) {
            String title;
            String location;
            int eventColor;


            eventBeginTime = cursor.getLong(PROJECTION_BEGIN_INDEX);
            eventEndTime = cursor.getLong(PROJECTION_END_INDEX);
            title = cursor.getString(PROJECTION_TITLE_INDEX);
            location = cursor.getString(PROJECTION_LOCATION_INDEX);
            eventColor = cursor.getInt(PROJECTION_COLOR_INDEX);

            model = new EventDataModel();


            model.title = title;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis((eventBeginTime));
            String hour = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
            String minute = String.format("%02d", calendar.get(Calendar.MINUTE));
            model.startTime = hour + " : " + minute;
            calendar.setTimeInMillis(eventEndTime);
            hour = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
            minute = String.format("%02d", calendar.get(Calendar.MINUTE));
            model.endTime = hour + " : " + minute;
            model.location = location;
            model.eventColor = eventColor == 0 ? Color.parseColor("#555555"):eventColor;

            if (eventBeginTime > lastEventEndTime) {
                EventDataModel addModel = new EventDataModel();
                addModel.addIcon = true;
                items.add(addModel);
            }
            lastEventEndTime = eventEndTime;
            model.addIcon = false;
            items.add(model);
        }
        if (eventEndTime < endOfDayMillis) {
            EventDataModel addModel = new EventDataModel();
            addModel.addIcon = true;
            items.add(addModel);
        }
        adapter = new RecyclerViewAdapter(getActivity(), items);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.Callback callback = adapter.new SwipeTouchHelper();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return rootView;
    }
}
