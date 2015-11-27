package ml.mk.jm.ay.ak.studenttoolkit.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ml.mk.jm.ay.ak.studenttoolkit.R;
import ml.mk.jm.ay.ak.studenttoolkit.calendar.helper.TimeFormatHelper;
import ml.mk.jm.ay.ak.studenttoolkit.map.MapsActivity;

/*
 * Class to show detailed view of any event
 */
public class EventDetailActivity extends AppCompatActivity{

    public static final String TAG = EventDetailActivity.class.getName();
    public static final String EVENT_DATA ="EVENT DATA";

    //for showing activity main detail
    TextView tvTitle;
    TextView tvDate, tvTime;
    TextView tvLocation;
    Bundle extras;

    private String title, cal_id, event_id;
    private long startTime, endTime;
    private String location;
    private int allDay, hasAlarm;
    private String description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        //parse intent to get the details of event passed from previous activity
        extras = getIntent().getExtras();
        if(extras!=null) {
            title = extras.getString("title");
            location = extras.getString("location");
            startTime = extras.getLong("startTime");
            String date = TimeFormatHelper.millisToDate(startTime);

            // format start and end time to show
            endTime = extras.getLong("endTime");
            String timeOfEvent = new StringBuilder()
                    .append(TimeFormatHelper.millisToHourAndMinuteStr(startTime))
                    .append(" - ")
                    .append(TimeFormatHelper.millisToHourAndMinuteStr(endTime)).toString();

            description = extras.getString("description");
            allDay = extras.getInt("allDay");
            hasAlarm = extras.getInt("hasAlarm");

            tvTitle = (TextView)findViewById(R.id.tv_detail_title);
            tvTitle.setText(title);

            tvDate= (TextView) findViewById(R.id.tvDate);
            tvDate.setText(date);
            if(allDay==0) {
                tvTime = (TextView) findViewById(R.id.tvTime);
                tvTime.setText(timeOfEvent);
            }

            //show layout for location only if location data for the event is available
            if(!location.equals("")){
                LinearLayout llLoc = (LinearLayout) findViewById(R.id.ll_detail_location);
                llLoc.setVisibility(View.VISIBLE);
                tvLocation = (TextView) findViewById(R.id.tv_detail_location);
                tvLocation.setText(location);
            }
            cal_id = extras.getString("calendarId");
            event_id = extras.getString("event_id");

            Log.d(TAG,"data received "+extras.toString());

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // show mapactivity once the user clicks on map icon
    public void showMap(View v){
        String location = tvLocation.getText().toString();
        Intent mapIntent = new Intent(this, MapsActivity.class);
        mapIntent.putExtra("EXTRA_TEXT", location);
        startActivity(mapIntent);
    }

    /*
     * Allows user to edit any event.
     * Once the user click on edit icon, passes the detail of events to EditEventActivity
     * so that it can use the data to make it easy for editing by user
     */
    public void editEvent(View v){
        Intent editIntent = new Intent(this,EditEventActivity.class);
        editIntent.putExtra("title", title);
        editIntent.putExtra("location", location);
        editIntent.putExtra("startTime", startTime);
        editIntent.putExtra("endTime", endTime);
        editIntent.putExtra("description", description);
        editIntent.putExtra("allDay", allDay);
        editIntent.putExtra("hasAlarm", hasAlarm);
        editIntent.putExtra("calendarId", cal_id);
        editIntent.putExtra("event_id",event_id);
        startActivity(editIntent);
    }
}
