package ml.mk.jm.ay.ak.studenttoolkit.todo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

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

    //declare global
    private EditText editTextTitle;
    private EditText editTextDescription;
    // end declare global



    //http://stackoverflow.com/questions/10062608/simpledateformat-unparseable-date
    final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyy", Locale.ENGLISH);

    //onCreate, called when activity is created.
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

        //Setup spinner for dates/days
        daySpinner = (Spinner) findViewById(R.id.new_date_spinner);
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
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //Setup spinner for time.
        timeSpinner = (Spinner) findViewById(R.id.new_time_spinner);
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
        tp = (TimePicker) findViewById(R.id.newTimePicker);
        tp.setVisibility(CalendarView.GONE);
        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                due.setHours(hour);
                due.setMinutes(minute);
            }
        });

        //Setup calendarView. When the date on the timePicker is changed, change the due date.
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

                //We don't have enough space on-screen for both widgets.
                //To combat this, only show one at a time.
                cv.setVisibility(CalendarView.GONE);
                if(timeSpinner.getVisibility() == Spinner.GONE){
                    tp.setVisibility(TimePicker.VISIBLE);
                }
            }
        });




        /**************Diamond********************/
        //start share
        //get the EditText

        EditText txtDescription = (EditText) findViewById(R.id.newDescription);
        Intent receivedIntent = getIntent();
        //String action = receivedIntent.getAction();
        String receivedType = receivedIntent.getType();
        //get the action
        String receivedAction = receivedIntent.getAction();

        if(Intent.ACTION_SEND.equals(receivedAction) && receivedType != null){
            //content is being shared
            if(receivedType.startsWith("text/")){
                //handle sent text
                String receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT);
                if (receivedText != null) {
                    //set the text
                    txtDescription.setText(receivedText);
                }
            }

        }
        else if(Intent.ACTION_MAIN.equals(receivedAction)){
            //app has been launched directly, not from share list
            txtDescription.setText("Description");
        }
        //end share

    }//end onCreate

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        ImageButton imgVoice = (ImageButton) findViewById(R.id.imageButtonSpeak);
        TextView txtViewVoice =  (TextView) findViewById(R.id.txtViewTap);
        switch(view.getId()){
            case R.id.checkboxShowVoice:
                if(checked)
                {
                    imgVoice.setVisibility(View.GONE);
                    txtViewVoice.setVisibility(View.GONE);
                }
                else
                {
                    imgVoice.setVisibility(View.VISIBLE);
                    txtViewVoice.setVisibility(View.VISIBLE);
                }
                break;

        }//end switch
    }


   /* private View.OnFocusChangeListener myEditTextFocus =  new View.OnFocusChangeListener() {
        public void onFocusChange(View view, boolean gainFocus) {
            //onFocus
            if (gainFocus) {
                //set the text
                ((EditText) view).setText("In focus now");
            }
            //onBlur
            else {
                //clear the text
                ((EditText) view).setText("");
            }
        };
    };*/


    public void onClickSpeak(View view) {
        //declare global
        editTextTitle = (EditText) findViewById(R.id.newTitle);
        editTextDescription = (EditText) findViewById(R.id.newDescription);
        // end declare global
        if(view.getId()==R.id.imageButtonSpeak && editTextTitle.hasFocus()){
           // EditText editText = (EditText) findViewById(R.id.newTitle);
            callSpeakTitle();
        }
        else if(view.getId()==R.id.imageButtonSpeak && editTextDescription.hasFocus())
            callSpeakDescription();

    }
    private void callSpeakTitle() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");
        try {
            startActivityForResult(intent, 500);
        }
        catch(ActivityNotFoundException anfe)
        {
            Toast.makeText(NewToDoActivity.this, "Sorry! Your device does not recognize speech.", Toast.LENGTH_SHORT).show();
        }

    }
    private void callSpeakDescription() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");
        try {
            startActivityForResult(intent, 1000);
        }
        catch(ActivityNotFoundException anfe)
        {
            Toast.makeText(NewToDoActivity.this, "Sorry! This device does not recognize speech.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        switch (requestCode)
        {
            case 500:
                if(resultCode==RESULT_OK && dataIntent!= null){
                    ArrayList<String> resultValue = dataIntent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editTextTitle.setText(resultValue.get(0));
                }
                break;
            case 1000:
                if(resultCode==RESULT_OK && dataIntent!= null){
                    ArrayList<String> resultValue = dataIntent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editTextDescription.setText(resultValue.get(0));
                }
                break;
        }


    }
    /*****************Diamond****************/



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
        Intent startMain = new Intent(NewToDoActivity.this, ToDoActivity.class);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    //A Click class to deal with Click events
    class Click implements View.OnClickListener {
        Intent todoIntent = new Intent(NewToDoActivity.this, ToDoActivity.class);

        //When a Click events occurs, find out which button caused the event.
        //If it was the save button, then check to make sure that the date isn't in the past (To-Do's aren't meant
        //to be created retroactively), and if it is, stop the user with an error message. If the date is in the future,
        //save this To-Do to the database and redirect.
        //If the cancel button was selected, simply return to the main screen.
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


                        //setAlarm
                        Cursor cursor = db.getReadableDatabase().rawQuery("select * from todo",null);



                        while(cursor.moveToNext()){
                            try {
                                Date d = sdf.parse(cursor.getString(DatabaseConnection.DUE));
                                //Date d = sdf.parse(formattedDate);
                        //long milliseconds = d.getTime();
                        long alarmTime = d.getTime();
                        //Random rand = new Random(10000000);
                        //int rand = (int) (Math.random() * 1000);
                        //long alarmTime = new GregorianCalendar().getTimeInMillis()+2*1000;
                        //long alarmTime = new GregorianCalendar().getTimeInMillis()+milliseconds;
                        Log.d("AlarmTime", "Alarm time [" + alarmTime + " ]");
                        //String msg, String msgText, String msgAlert
                                int rand = cursor.getInt(DatabaseConnection.ID);
                                String title = cursor.getString(DatabaseConnection.TITLE);
                                String desc = cursor.getString(DatabaseConnection.DESCRIPTION);
                                        //, date
                        Intent alarmIntent = new Intent(view.getContext(), AlertReceiver.class);
                        alarmIntent.putExtra("TODO_ID", rand);
                        alarmIntent.putExtra("TODO_TITLE", title );
                        alarmIntent.putExtra("TODO_DESCRIPTION", desc);

                        //, c.getInt(DatabaseConnection.ID)
                /*Intent intent = new Intent(getBaseContext(), SignoutActivity.class);
                intent.putExtra("EXTRA_SESSION_ID",);
                startActivity(intent)*/

                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, PendingIntent.getBroadcast(view.getContext(), 1,
                                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                                Date dt = new Date(alarmTime);
                        Toast.makeText(view.getContext(), "Alarm has been set for " + dt , Toast.LENGTH_SHORT).show();
                        Log.d("Ademola", "Alarm Manager [" + alarmManager + " ]");



                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //Todo todo = new Todo(c.getInt(DatabaseConnection.ID), c.getString(DatabaseConnection.TITLE),c.getString(DatabaseConnection.DESCRIPTION), date);// new Date());
                            //todos.add(todo);
                        }
                        cursor.close();
                        //end setAlarm


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
