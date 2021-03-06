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
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ml.mk.jm.ay.ak.studenttoolkit.R;
import ml.mk.jm.ay.ak.studenttoolkit.database.DatabaseConnection;

/**
 * Created by Marc.
 * This screen allows the user to postpone a To-Do object. We felt it necessary to distinguish between editing a date and
 * postponing a To-Do, hence why this activity was created. The user can use several widgets to postpone an activity.
 */

public class PostponeToDoActivity extends AppCompatActivity {

    Intent intent;
    Bundle bundle;
    Todo todo;
    Button updatePostpone;
    Button cancelPostpone;
    DatabaseConnection db;
    Spinner daySpinner;
    Spinner timeSpinner;
    CalendarView cv;
    TimePicker tp;
    Date due = new Date();
    Calendar calendar;

    //http://stackoverflow.com/questions/10062608/simpledateformat-unparseable-date
    final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyy", Locale.ENGLISH);

    //onCreate, called when activity is created.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postpone_to_do_);

        intent = getIntent();
        bundle = intent.getExtras();
        todo = bundle.getParcelable("todo");
        db = ToDoActivity.db;
        calendar = Calendar.getInstance();

        updatePostpone = (Button) findViewById(R.id.updatePostponeButton);
        cancelPostpone = (Button) findViewById(R.id.cancelPostponeButton);
        updatePostpone.setOnClickListener(new Click());
        cancelPostpone.setOnClickListener(new Click());

        //Setup spinner for dates/days
        daySpinner = (Spinner) findViewById(R.id.day_spinner);
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this, R.array.day_array, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        //Sets actual logic behind date/day spinner. In this case, the user either picks a pre-defined dateslot
        //or selects an option that opens a calendar view, giving them more options.
        //http://stackoverflow.com/questions/19301458/the-specified-child-already-has-a-parent-you-must-call-removeview-on-the-chil
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //When an itemSelected event is thrown...
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                cv.setVisibility(CalendarView.GONE);

                if (daySpinner.getSelectedItem().equals("Select a date...")) {
                    tp.setVisibility(CalendarView.GONE);
                    cv.setVisibility(CalendarView.VISIBLE);
                }
            }

            //An inherited method. Only required as part of Listeners contract.
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Setup spinner for time.
        timeSpinner = (Spinner) findViewById(R.id.time_spinner);
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this, R.array.time_array, android.R.layout.simple_spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);

        //Sets actual logic behind time spinner. In this case, the user either picks a pre-defined timeslot
        //or selects an option that opens a timepicker, giving them more options.
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //When an itemSelected event is thrown...
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                 if (timeSpinner.getSelectedItem().equals("Select a time...")) {
                    cv.setVisibility(CalendarView.GONE);
                    tp.setVisibility(TimePicker.VISIBLE);
                    timeSpinner.setVisibility(Spinner.GONE);
                }
            }

            //An inherited method. Only required as part of Listeners contract.
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //Setup timePicker. When the time on the timePicker is changed, change the due date.
        tp = (TimePicker) findViewById(R.id.timePicker);
        tp.setVisibility(CalendarView.GONE);
        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){

            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                due.setHours(hour);
                due.setMinutes(minute);
              }
        });

        //Setup calendarView. When the date on the timePicker is changed, change the due date.
        cv = (CalendarView)findViewById(R.id.calendar_ID);
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

                //We don't have enough space on-screen for both widgets.
                //To combat this, only show one at a time.
                if(timeSpinner.getVisibility() == Spinner.GONE){
                    tp.setVisibility(TimePicker.VISIBLE);
                }
            }
        });
    }

    //A method that, when called, determines how to calculate the date from the options the user chose
    //on-screen. These can be any combination of calendarView, timePicker and the pre-defined times and dates
    //shown in the spinners.
    //http://stackoverflow.com/questions/3747490/android-get-date-before-7-days-one-week
    public String join() {
        Date today = new Date();
        String dayText = daySpinner.getSelectedItem().toString();
        String timeText = timeSpinner.getSelectedItem().toString();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        //As long as the user is defining their own date, use whatever one was
        //selected from the pre-defined dates in the daySpinner.
        if (!dayText.equals("Select a date...")) {
            if (dayText.equals("Today")) {
                due.setDate(new Date().getDate());
            } else if (dayText.equals("Tomorrow")) {
                due.setDate(new Date().getDate() + 1);
            } else if (dayText.equals("In a week")) {
                due.setDate(new Date().getDate() + 7);
            }
        }

        //As long as the timeSpinner is GONE, we know that the user is defining their own time with the timePicker.
        //There is no option for the user to go back to using pre-defined times once they've chosen to pick the time themselves
        //with the timePicker, so its visibility is set to GONE for the duration of the activity. If it isn't, then the user
        //wants to use a pre-defined time, so act accordingly.
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
    //http://stackoverflow.com/questions/11807554/go-to-home-screen-instead-of-previous-activity
    public void onBackPressed() {
        Intent startMain = new Intent(PostponeToDoActivity.this, ToDoActivity.class);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    //A Click class to deal with Click events
    class Click implements View.OnClickListener {
        Intent menuIntent;

        //When a Click events occurs, find out which button caused the event.
        //If it was the save button, then check to make sure that the date isn't in the past (To-Do's aren't meant
        //to be created retroactively), and if it is, stop the user with an error message. If the date is in the future,
        //save this To-Do to the database and redirect.
        //If the cancel button was selected, simply return to the main screen.
        public void onClick(View view) {
            try {
                if (view.getId() == R.id.updatePostponeButton) {
                    String formattedDate = join();
                    Date possiblePastDate = sdf.parse(formattedDate);

                    if(possiblePastDate.getTime() > new Date().getTime()) {
                        menuIntent = new Intent(PostponeToDoActivity.this, ToDoActivity.class);
                        String update = "update todo set due = '" + formattedDate + "' where _id = " + todo.getId();
                        db.getWritableDatabase().execSQL(update);
                        Toast toast = Toast.makeText(getApplicationContext(), "To-Do Postponed!", Toast.LENGTH_SHORT);
                        toast.show();
                        startActivity(menuIntent);
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Cannot create To-Do in the past!", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                } else if (view.getId() == R.id.cancelPostponeButton) {
                    menuIntent = new Intent(PostponeToDoActivity.this, ToDoActivity.class);
                    Toast toast = Toast.makeText(getApplicationContext(), "Postpone cancelled!", Toast.LENGTH_SHORT);
                    toast.show();
                    startActivity(menuIntent);
                } else {
                    throw new Exception();
                }
            } catch (Exception E) {
                Log.e("Main Class", "Error when postponing To-Do");
            }

        }
    }

}