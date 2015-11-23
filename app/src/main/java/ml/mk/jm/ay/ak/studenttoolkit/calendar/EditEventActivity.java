package ml.mk.jm.ay.ak.studenttoolkit.calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ml.mk.jm.ay.ak.studenttoolkit.R;
import ml.mk.jm.ay.ak.studenttoolkit.calendar.helper.TimeFormatHelper;
public class EditEventActivity extends AppCompatActivity implements View.OnClickListener {

    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;
    ArrayAdapter<CharSequence> adapter;
    Spinner rec;
    private SpinnerListener spl;
    private Switch sAllDay, sAlarm;
    private Button bDate, bFromTime, bToTime;

    private TextView tvEventHead;
    private EditText etTitle, etLocation, etDescription;
    private int fYear, fMonth, fDay; //select start date of activity
    private int fHour, fMin;   //start time of activity
    private int tHour, tMin;   //end time of activity

    private String cal_id;

    String[] WeekOfDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        rec = (Spinner) findViewById(R.id.spinner);
        spl = new SpinnerListener();
        adapter = ArrayAdapter.createFromResource(this,
                R.array.recurrence_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        rec.setAdapter(adapter);
        rec.setOnItemSelectedListener(this.spl);
        bDate = (Button) findViewById(R.id.btnDate);
        bDate.setOnClickListener(this);

        bFromTime = (Button) findViewById(R.id.btnFromTime);
        bFromTime.setOnClickListener(this);

        bToTime = (Button) findViewById(R.id.btnToTime);
        bToTime.setOnClickListener(this);

        sAllDay = (Switch) findViewById(R.id.switchAllday);
        sAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bFromTime.setEnabled(false);
                    bToTime.setEnabled(false);
                } else {
                    bFromTime.setEnabled(true);
                    bToTime.setEnabled(true);
                }
            }
        });

        sAlarm = (Switch) findViewById(R.id.switchAlarm);
        sAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addNotification();
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        Log.d("Mukesh", "extras "+extras.toString());
        if (extras != null) {
            String title = extras.getString("title");
            etTitle =(EditText) findViewById(R.id.etTitle);
            etTitle.setText(title);
            String location = extras.getString("location");

            Log.d("Mukesh", "title "+title+location);

            etLocation = (EditText)findViewById(R.id.etLocation);
            if (null!=location && !location.equals("")) {
                etLocation.setText(location);
            }

            // format start and end time to show
            long startTime = extras.getLong("startTime");
            long endTime = extras.getLong("endTime");
            String date = TimeFormatHelper.millisToDate(startTime);
            bDate.setText(date);
            bFromTime.setText(TimeFormatHelper.millisToHourAndMinuteStr(startTime));
            bToTime.setText(TimeFormatHelper.millisToHourAndMinuteStr(endTime));

            String description = extras.getString("description");
            etDescription = (EditText)findViewById(R.id.etDescription);
            if (null!=description && !description.equals("")) {
                etDescription.setText(description);
            }
            int allDay = extras.getInt("allDay");
            int hasAlarm = extras.getInt("hasAlarm");

            if (allDay == 1) {
                sAllDay.setEnabled(true);
                bFromTime.setEnabled(false);
                bToTime.setEnabled(false);
            }

            if (hasAlarm == 1) {
                sAlarm.setEnabled(true);
            }

            cal_id = extras.getString("calendarId");

        }
    }

        @Override
        public void onClick (View v){
            final Calendar cal = Calendar.getInstance();
            final SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM");
            final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            switch (v.getId()) {
                case R.id.btnDate:
                    // TODO add  date picker logic
                    fYear = cal.get(Calendar.YEAR);
                    fMonth = cal.get(Calendar.MONTH);
                    fDay = cal.get(Calendar.DAY_OF_MONTH);
                    final int fWeekDay = cal.get(Calendar.DAY_OF_WEEK);

                    DatePickerDialog fromDpd = new DatePickerDialog(this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
//                                String str = new StringBuilder().append(' ').append(dayOfMonth).append(' ').append(monthOfYear)
//                                        .append(' ').append(year).toString();
//                                String.format(str, "");
                                    cal.set(Calendar.YEAR, year);
                                    cal.set(Calendar.MONTH, monthOfYear);
                                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                    bDate.setText(dateFormat.format(cal.getTime()));
                                }
                            }, fYear, fMonth, fDay);
                    fromDpd.show();
                    Log.d("Mukesh", "Date :: " + fYear + fMonth + fDay);
                    break;

                case R.id.btnFromTime:
                    // TODO add  time picker logic
                    fHour = cal.get(Calendar.HOUR_OF_DAY);
                    fMin = cal.get(Calendar.MINUTE);

                    TimePickerDialog fromTpd = new TimePickerDialog(this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    cal.set(Calendar.MINUTE, minute);
                                    bFromTime.setText(timeFormat.format(cal.getTime()));
                                }
                            }, fHour, fMin, false);
                    fromTpd.show();
                    Log.d("Mukesh", "time :: " + fHour + fMin);
                    break;

                case R.id.btnToTime:
                    // TODO add time picker logic
                    tHour = cal.get(Calendar.HOUR_OF_DAY);
                    tMin = cal.get(Calendar.MINUTE);
                    TimePickerDialog toTpd = new TimePickerDialog(this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    cal.set(Calendar.MINUTE, minute);
                                    bToTime.setText(timeFormat.format(cal.getTime()));
                                }
                            }, tHour, tMin, false);
                    toTpd.show();
                    Log.d("Mukesh", "Time :: " + tHour + tMin);
                    break;

            }
        }
        private class SpinnerListener implements AdapterView.OnItemSelectedListener {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String recurrence = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }

    private void addNotification() {
        final String[] choices = getResources().getStringArray(R.array.NotificationChoices);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.choose_notification));
        builder.setSingleChoiceItems(choices, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
                Log.d("Mukesh", "selected " + choices[item]);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void saveEvent(View v){

        EventDataModel newEvent = new EventDataModel();
        newEvent.title = etTitle.getText().toString();

        // calculate start and end time in millis to store in calendar
        Calendar cal = Calendar.getInstance();
        cal.set(fYear,fMonth,fDay,fHour,fMin);
        long startTime = cal.getTimeInMillis();

        cal.set(fYear,fMonth,fDay,tHour,tMin);
        long endTime = cal.getTimeInMillis();

        newEvent.startTimeMillis = startTime;
        newEvent.endTimeMillis = endTime;

        newEvent.allDay= sAllDay.isChecked()?1:0;
        newEvent.hasAlarm = sAlarm.isChecked()?1:0;
        newEvent.location = etLocation.getText().toString();
        newEvent.description = etDescription.getText().toString();
        newEvent.cal_id = cal_id;

     //   CalendarHelper.updateEvent(getApplicationContext(), newEvent);

    }

    public void deleteEvent(View v){

    }

}
