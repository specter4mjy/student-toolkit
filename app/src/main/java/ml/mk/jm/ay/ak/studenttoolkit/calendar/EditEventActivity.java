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
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import ml.mk.jm.ay.ak.studenttoolkit.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditEventActivity extends AppCompatActivity implements View.OnClickListener{

    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID=1;
    ArrayAdapter<CharSequence> adapter;
    Spinner rec;
    private SpinnerListener spl;
    private Switch allDay, alarm;
    private Button bFromDate, bFromTime, bToDate, bToTime;

    private int fYear, fMonth, fDay; //select start date of activity
    private int fHour, fMin, fSec;   //start time of activity
    private int tYear, tMonth, tDay; //select end date of activity
    private int tHour, tMin, tSec;   //end time of activity

    String []WeekOfDay;
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
        bFromDate= (Button) findViewById(R.id.btnFromDate);
        bFromDate.setOnClickListener(this);

        bToDate= (Button) findViewById(R.id.btnToDate);
        bToDate.setOnClickListener(this);

        bFromTime= (Button) findViewById(R.id.btnFromTime);
        bFromTime.setOnClickListener(this);

        bToTime= (Button) findViewById(R.id.btnToTime);
        bToTime.setOnClickListener(this);

        allDay = (Switch) findViewById(R.id.switchAllday);
        allDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bFromDate.setEnabled(false);
                    bFromTime.setEnabled(false);
                    bToDate.setEnabled(false);
                    bToTime.setEnabled(false);
                } else {
                    bFromDate.setEnabled(true);
                    bFromTime.setEnabled(true);
                    bToDate.setEnabled(true);
                    bToTime.setEnabled(true);
                }

            }
        });

        alarm = (Switch)findViewById(R.id.switchAlarm);
        alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    addNotification();
                }
            }
        });

        WeekOfDay = getResources().getStringArray(R.array.WeekOfDay);
    }

    @Override
    public void onClick(View v) {
        final Calendar cal = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM, yyyy");
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
        final String am=getResources().getString(R.string.str_am);
        final String pm=getResources().getString(R.string.str_pm);

        switch (v.getId()){
            case R.id.btnFromDate:
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
                                cal.set(Calendar.YEAR,year);
                                cal.set(Calendar.MONTH,monthOfYear);
                                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                bFromDate.setText(dateFormat.format(cal.getTime()));
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
                                if(hourOfDay>12)
                                    hourOfDay-= 12;

                                cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                cal.set(Calendar.MINUTE, minute);
                                bFromTime.setText(timeFormat.format(cal.getTime()));
                            }
                        }, fHour, fMin, false);
                fromTpd.show();
                Log.d("Mukesh", "time :: " + fHour + fMin);
                break;

            case R.id.btnToDate:
                // TODO add  date picker logic
                tYear = cal.get(Calendar.YEAR);
                tMonth = cal.get(Calendar.MONTH);
                tDay = cal.get(Calendar.DAY_OF_MONTH);
                final int tWeekDay = cal.get(Calendar.DAY_OF_WEEK);

                DatePickerDialog toDpd = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                cal.set(Calendar.YEAR,year);
                                cal.set(Calendar.MONTH,monthOfYear);
                                cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                bToDate.setText(dateFormat.format(cal.getTime()));
                             //   bToDate.setText(WeekOfDay[tWeekDay]+" "+dayOfMonth + "-"
                               //         + (monthOfYear + 1) + "-" + year);

                            }
                        }, tYear, tMonth, tDay);
                toDpd.show();
                Log.d("Mukesh", "Date :: " + tYear + tMonth + tDay);
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
                                if(hourOfDay>12)
                                    hourOfDay-= 12;

                                cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                cal.set(Calendar.MINUTE, minute);
                                bToTime.setText(timeFormat.format(cal.getTime()));
                            }
                        }, tHour, tMin, false);
                toTpd.show();
                Log.d("Mukesh","Time :: "+ tHour + tMin);
                break;

        }
    }


    private class SpinnerListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String recurrence = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void addNotification(){
        final String[] choices = getResources().getStringArray(R.array.NotificationChoices);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.choose_notification));
        builder.setSingleChoiceItems(choices,-1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // TODO something with the selection
                Log.d("Mukesh","selected "+choices[item]);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }
}
