package ml.mk.jm.ay.ak.studenttoolkit.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ml.mk.jm.ay.ak.studenttoolkit.R;

/**
 * Created by Marc.
 * This activity is used to show a To-Do in more detail. From here, the user click on options to edit the text fields of a To-Do
 * or go to the due-date postponement screen.
 */

public class ShowToDoActivity extends AppCompatActivity {

    Todo todo;
    Intent intent;
    Bundle bundle;
    TextView titleView;
    TextView descriptionView;
    TextView dateView;
    Button editButton;
    Button postponeButton;

    final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm", Locale.ENGLISH);

    //onCreate, called when activity is created.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_to_do);

        intent = getIntent();
        bundle = intent.getExtras();
        todo = bundle.getParcelable("todo");

        //Connects widget variables with their GUI equivalents.
        titleView = (TextView) findViewById(R.id.showTitleView);
        descriptionView = (TextView) findViewById(R.id.showDescriptionView);
        dateView = (TextView) findViewById(R.id.showDateView);
        editButton = (Button) findViewById(R.id.showEditButton);
        postponeButton = (Button) findViewById(R.id.postponeButton);

        //Sets widgets to display To-Do previously clicked on.
        titleView.setText(todo.getTitle());
        descriptionView.setText(todo.getDescription());
        dateView.setText(sdf.format(todo.getDue()));

        //Sets up click listeners.
        editButton.setOnClickListener(new Click());
        postponeButton.setOnClickListener(new Click());

    }

    //override the back button and make it go to the ToDoActivity class
    //http://stackoverflow.com/questions/11807554/go-to-home-screen-instead-of-previous-activity
    public void onBackPressed() {
        Intent startMain = new Intent(ShowToDoActivity.this, ToDoActivity.class);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    //A Click class to deal with Click events
    class Click implements View.OnClickListener {
        Intent todoIntent;
        Bundle bundle;

        //When an onClick event is received, take the current To-Do object and send it to either
        //the edit activity or the postpone activity, depending on which button was pressed.
        public void onClick(View view) {

            try {
                if (view.getId() == R.id.showEditButton) {
                    bundle = new Bundle();
                    bundle.putParcelable("todo",todo);
                    todoIntent = new Intent(ShowToDoActivity.this , EditToDoActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    todoIntent.putExtras(bundle);
                    startActivity(todoIntent);
                } else if (view.getId() == R.id.postponeButton) {
                    bundle = new Bundle();
                    bundle.putParcelable("todo",todo);
                    todoIntent = new Intent(ShowToDoActivity.this, PostponeToDoActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    todoIntent.putExtras(bundle);
                    startActivity(todoIntent);
                }
            } catch (Exception E) {
                Log.e("ShowToDoActivity", "Error before editing To-Do");
            }

        }
    }
}

