package ml.mk.jm.ay.ak.studenttoolkit.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import ml.mk.jm.ay.ak.studenttoolkit.R;

public class EventDetailActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvDate, tvTime, tvRecurrence; //for showing activity main detail
    TextView tvLocation, tvAlarm;

    ListView listDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Intent intent = new Intent(this,EditEventActivity.class);
        startActivity(intent);

      //  label = (TextView) findViewById(R.id.lbl_event_title);
      //  label.setText(getIntent().getExtras().getString("label"));
    }
}
