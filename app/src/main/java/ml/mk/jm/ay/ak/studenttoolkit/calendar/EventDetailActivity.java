package ml.mk.jm.ay.ak.studenttoolkit.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import ml.mk.jm.ay.ak.studenttoolkit.R;
import ml.mk.jm.ay.ak.studenttoolkit.calendar.helper.TimeFormatHelper;

public class EventDetailActivity extends AppCompatActivity{

    TextView tvTitle;
    TextView tvDate, tvTime, tvRecurrence; //for showing activity main detail
    TextView tvLocation, tvAlarm;

    ListView listDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            String title = extras.getString("title");
            String location = extras.getString("location");
            long startTime = extras.getLong("startTime");
            String date = TimeFormatHelper.millisToDate(startTime);

            // format start and end time to show
            long endTime = extras.getLong("endTime");
            String timeOfEvent = TimeFormatHelper.millisToHourAndMinuteStr(startTime)
                         + TimeFormatHelper.millisToHourAndMinuteStr(endTime);

            String description = extras.getString("description");
            int allDay = extras.getInt("allDay");
            int hasAlarm = extras.getInt("hasAlarm");

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


        }
    }

    public void showMap(){
        String location = tvLocation.getText().toString();
        Intent mapIntent = new Intent(this,MapsActivity.class);
        mapIntent.putExtra("EXTRA_TEXT",location);
    }

    public void editEvent(){

    }
}
