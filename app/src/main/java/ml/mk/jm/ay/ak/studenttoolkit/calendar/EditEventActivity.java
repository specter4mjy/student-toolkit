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
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ml.mk.jm.ay.ak.studenttoolkit.R;
import ml.mk.jm.ay.ak.studenttoolkit.calendar.helper.CalendarProviderHelper;
import ml.mk.jm.ay.ak.studenttoolkit.calendar.helper.TimeFormatHelper;

/*
* Activity to allow user to edit some or all of the details of an event
* Also allows to delete event.
 */
public class EditEventActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = EditEventActivity.class.getName();

    // UI elements to handle recurrence of an event
    private ArrayAdapter<CharSequence> adapter;
    private Spinner rec;
    private SpinnerListener spl;

    private Switch sAllDay, sAlarm;
    private Button bDate, bFromTime, bToTime;

    private EditText etTitle, etLocation, etDescription;
    private int fYear, fMonth, fDay; //select start date of activity
    private int fHour, fMin;   //start time of activity
    private int tHour, tMin;   //end time of activity

    private String cal_id, event_id;

    private static long begintimeMillis, endtimeMillis;


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

        // buttons to select time/date of event
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

        // switch to add/remove alarm associated with the event
        sAlarm = (Switch) findViewById(R.id.switchAlarm);
        sAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addNotification();
                }
            }
        });

        /* parse data received to present this information to the User
         * and allow easy modification  of some or all of these details
        */

        Bundle extras = getIntent().getExtras();
        Log.d(TAG, "data received "+extras.toString());
        if (extras != null) {
            String title = extras.getString("title");
            etTitle =(EditText) findViewById(R.id.etTitle);
            etTitle.setText(title);
            String location = extras.getString("location");

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
            event_id = extras.getString("event_id");
            Log.d(TAG, "event_id "+event_id);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // callback when the user selects to change date and time information of the event
    @Override
    public void onClick (View v){
        final Calendar cal = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM");
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        switch (v.getId()) {
            case R.id.btnDate:
                fYear = cal.get(Calendar.YEAR);
                fMonth = cal.get(Calendar.MONTH);
                fDay = cal.get(Calendar.DAY_OF_MONTH);
                final int fWeekDay = cal.get(Calendar.DAY_OF_WEEK);

                /*
                 * Open datepickerdialog and set the date on textbox once the user selects a date
                 */
                DatePickerDialog fromDpd = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                cal.set(Calendar.YEAR, year);
                                cal.set(Calendar.MONTH, monthOfYear);
                                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                bDate.setText(dateFormat.format(cal.getTime()));
                            }
                        }, fYear, fMonth, fDay);
                fromDpd.show();
                Log.d(TAG, "Date :: " + fYear + fMonth + fDay);
                break;

            case R.id.btnFromTime:
                fHour = cal.get(Calendar.HOUR_OF_DAY);
                fMin = cal.get(Calendar.MINUTE);

                /*
                 * Open TimePicker to select a start time for the event
                 * and set the time on textbox once the user selects one.
                 */
                TimePickerDialog fromTpd = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                cal.set(Calendar.MINUTE, minute);
                                bFromTime.setText(timeFormat.format(cal.getTime()));
                                begintimeMillis = cal.getTimeInMillis();
                            }
                        }, fHour, fMin, false);
                fromTpd.show();
                break;

            case R.id.btnToTime:
                tHour = cal.get(Calendar.HOUR_OF_DAY);
                tMin = cal.get(Calendar.MINUTE);

                                /*
                 * Open TimePicker to select a start time for the event
                 * and set the time on textbox once the user selects one.
                 */
                TimePickerDialog toTpd = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                cal.set(Calendar.MINUTE, minute);
                                bToTime.setText(timeFormat.format(cal.getTime()));
                                endtimeMillis = cal.getTimeInMillis();
                            }
                        }, tHour, tMin, false);
                toTpd.show();
                break;

        }
    }
    private class SpinnerListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

            // do nothing
        }
    }

    /*
    * opens a chooser dialog to choose an approriate time prior to event
    * at which user wants to get notified by alarm
     */
    private void addNotification() {
        final String[] choices = getResources().getStringArray(R.array.NotificationChoices);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.choose_notification));
        builder.setSingleChoiceItems(choices, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /*
    * update calendar content provider with new values of event details
     * when the user clicks on save icon after modifying some of the details
     */
    public void saveEvent(View v){

        EventDataModel newEvent = new EventDataModel();
        newEvent.title = etTitle.getText().toString();
        newEvent.startTimeMillis = begintimeMillis;
        newEvent.endTimeMillis = endtimeMillis;
        newEvent.allDay= sAllDay.isChecked()?1:0;
        newEvent.hasAlarm = sAlarm.isChecked()?1:0;
        newEvent.location = etLocation.getText().toString();
        newEvent.description = etDescription.getText().toString();
        newEvent.cal_id = cal_id;
        newEvent.event_id=event_id;

        // update query to be called by CalendarProviderHelper with new data
        int result = CalendarProviderHelper.updateEvent(getApplicationContext(), newEvent);
        if(result>=0)
            Toast.makeText(getApplicationContext()," updated",Toast.LENGTH_SHORT).show();

        // return to last activity once the event details are saved
        finish();
    }

    /*
    * Delete an event once the user selects remove icon
     */
    public void deleteEvent(View v){
        EventDataModel newEvent = new EventDataModel();

        newEvent.cal_id = cal_id;
        newEvent.event_id=event_id;

        long result = CalendarProviderHelper.deleteEvent(getApplicationContext(), newEvent);
        if(result>=0)
            Toast.makeText(getApplicationContext(),"result : success",Toast.LENGTH_SHORT).show();
        // return to last activity once the event is deleted
        finish();


    }

}
