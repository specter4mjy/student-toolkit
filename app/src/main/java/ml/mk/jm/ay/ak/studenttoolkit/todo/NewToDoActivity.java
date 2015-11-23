package ml.mk.jm.ay.ak.studenttoolkit.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ml.mk.jm.ay.ak.studenttoolkit.R;
import ml.mk.jm.ay.ak.studenttoolkit.database.DatabaseConnection;

public class NewToDoActivity extends AppCompatActivity {

    TextView newTitleView;
    TextView newDescriptionView;
    Button saveButton;
    Button cancelButton;
    Spinner daySpinner;
    Spinner timeSpinner;
    DatabaseConnection db;
    CalendarView cv;
    TimePicker tp;
    Date due = new Date();
    Calendar calendar;

    //http://stackoverflow.com/questions/10062608/simpledateformat-unparseable-date
    final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_to_do);

        db = ToDoActivity.db;
        newTitleView = (TextView) findViewById(R.id.newTitle);
        newDescriptionView = (TextView) findViewById(R.id.newDescription);
        saveButton = (Button) findViewById(R.id.saveButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        saveButton.setOnClickListener(new Click());
        cancelButton.setOnClickListener(new Click());

        calendar = Calendar.getInstance();

        daySpinner = (Spinner) findViewById(R.id.new_date_spinner);
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this, R.array.day_array, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        //http://stackoverflow.com/questions/19301458/the-specified-child-already-has-a-parent-you-must-call-removeview-on-the-chil
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                cv.setVisibility(CalendarView.GONE);

                if (daySpinner.getSelectedItem().equals("Select a date...")) {
                    tp.setVisibility(CalendarView.GONE);
                    cv.setVisibility(CalendarView.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });



        timeSpinner = (Spinner) findViewById(R.id.new_time_spinner);
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this, R.array.time_array, android.R.layout.simple_spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);

        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (timeSpinner.getSelectedItem().equals("Select a time...")) {
                    tp.setVisibility(TimePicker.VISIBLE);
                    timeSpinner.setVisibility(Spinner.GONE);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        tp = (TimePicker) findViewById(R.id.newTimePicker);
        tp.setVisibility(CalendarView.GONE);
        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                due.setHours(hour);
                due.setMinutes(minute);
            }
        });

        cv = (CalendarView)findViewById(R.id.newCalView);
        //http://stackoverflow.com/questions/18322786/prevent-selecting-past-dates-in-timepicker-dialogfragment
        cv.setMinDate(calendar.getTimeInMillis());
        cv.setVisibility(CalendarView.GONE);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                due.setMonth(month);
                due.setDate(dayOfMonth);
                due.setYear(year);

                cv.setVisibility(CalendarView.GONE);
                if(timeSpinner.getVisibility() == Spinner.GONE){
                    tp.setVisibility(TimePicker.VISIBLE);
                }
            }
        });
    }

    //http://stackoverflow.com/questions/3747490/android-get-date-before-7-days-one-week
    public String join() {
        Date today = new Date();
        String dayText = daySpinner.getSelectedItem().toString();
        String timeText = timeSpinner.getSelectedItem().toString();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        if (!dayText.equals("Select a date...")) {
            if (dayText.equals("Today")) {

                due.setDate(new Date().getDate());
            } else if (dayText.equals("Tomorrow")) {
                due.setDate(new Date().getDate() + 1);
            } else if (dayText.equals("In a week")) {
                due.setDate(new Date().getDate() + 7);
            }
        }
        if (timeSpinner.getVisibility() != Spinner.GONE ) {

            if (timeText.equals("Morning (9:00)")) {
                due.setHours(9);
                due.setMinutes(0);
            } else if (timeText.equals("Afternoon (12:00)")) {
                due.setHours(13);
                due.setMinutes(0);
            } else if (timeText.equals("Evening (17:00)")) {
                due.setHours(17);
                due.setMinutes(0);
            } else if (timeText.equals("Night (20:00)")) {
                due.setHours(20);
                due.setMinutes(0);
            }
        }
        String formattedDate = sdf.format(due);
        return formattedDate;
    }

    //override the back button and make it go to the ToDoActivity class
    public void onBackPressed() {
        Intent startMain = new Intent(NewToDoActivity.this, ToDoActivity.class);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }


    class Click implements View.OnClickListener {
        Intent todoIntent = new Intent(NewToDoActivity.this, ToDoActivity.class);

        public void onClick(View view) {

            try {
                if (view.getId() == R.id.saveButton) {

                    String formattedDate = join();
                    Date possiblePastDate = sdf.parse(formattedDate);

                    if(possiblePastDate.getTime() > new Date().getTime()) {
                        String newToDo = "insert into todo (title, description, due) values ('" + newTitleView.getText().toString() + "', '" + newDescriptionView.getText().toString() + "', '" + formattedDate + "')";
                        db.getWritableDatabase().execSQL(newToDo);
                        Toast toast = Toast.makeText(getApplicationContext(), "To-Do created successfully!", Toast.LENGTH_SHORT);
                        toast.show();
                        startActivity(todoIntent);
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Cannot create To-Do in the past!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else if (view.getId() == R.id.cancelButton) {
                    Toast toast = Toast.makeText(getApplicationContext(), "New To-do cancelled!", Toast.LENGTH_SHORT);
                    toast.show();
                    startActivity(todoIntent);
                } else {
                    throw new Exception();
                }
            } catch (Exception E) {
                Log.e("Main Class", "Error when editing To-Do");
            }

        }
    }
}
