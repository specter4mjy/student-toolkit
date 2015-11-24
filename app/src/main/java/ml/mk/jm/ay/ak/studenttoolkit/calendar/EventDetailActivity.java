package ml.mk.jm.ay.ak.studenttoolkit.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import ml.mk.jm.ay.ak.studenttoolkit.R;
import ml.mk.jm.ay.ak.studenttoolkit.calendar.helper.TimeFormatHelper;
import ml.mk.jm.ay.ak.studenttoolkit.map.MapsActivity;

public class EventDetailActivity extends AppCompatActivity{

    public static final String EVENT_DATA ="EVENT DATA";
    TextView tvTitle;
    TextView tvDate, tvTime, tvRecurrence; //for showing activity main detail
    TextView tvLocation, tvAlarm;
    Bundle extras;

    ListView listDetail;

    private String title, cal_id, event_id;
    private long startTime, endTime;
    private String location;
    private int allDay, hasAlarm;
    private String description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

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
            if(!location.equals("")){
                LinearLayout llLoc = (LinearLayout) findViewById(R.id.ll_detail_location);
                llLoc.setVisibility(View.VISIBLE);
                tvLocation = (TextView) findViewById(R.id.tv_detail_location);
                tvLocation.setText(location);
            }
            cal_id = extras.getString("calendarId");
            event_id = extras.getString("event_id");

            Log.d("Mukesh","data "+extras.toString());

        }
    }

    public void showMap(View v){
        String location = tvLocation.getText().toString();
        Intent mapIntent = new Intent(this, MapsActivity.class);
        mapIntent.putExtra("EXTRA_TEXT", location);
        startActivity(mapIntent);
    }

    public void editEvent(View v){
        Intent editIntent = new Intent(this,EditEventActivity.class);
        editIntent.putExtra("title", title);
        editIntent.putExtra("location", location);
        editIntent.putExtra("startTime", startTime);// this is milliseconds formate ,use TimeFormatHelper to convert to string
        editIntent.putExtra("endTime", endTime);
        editIntent.putExtra("description", description);
        editIntent.putExtra("allDay", allDay);
        editIntent.putExtra("hasAlarm", hasAlarm);
        editIntent.putExtra("calendarId", cal_id);
        editIntent.putExtra("event_id",event_id);
        startActivity(editIntent);
    }
}
