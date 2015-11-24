package ml.mk.jm.ay.ak.studenttoolkit.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ml.mk.jm.ay.ak.studenttoolkit.R;
import ml.mk.jm.ay.ak.studenttoolkit.database.DatabaseConnection;


public class EditToDoActivity extends AppCompatActivity {

    Todo todo;
    Intent intent;
    TextView titleView;
    TextView descriptionView;
    TextView dateView;
    TextView editTitleView;
    TextView editDescriptionView;
    Button saveButton;
    Button cancelButton;
    DatabaseConnection db;

    //onCreate, called when activity is created.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_to_do);
        intent = getIntent();
        todo = intent.getParcelableExtra("todo");
        db = ToDoActivity.db;

        //Connects widget variables with their GUI equivalents.
        titleView = (TextView) findViewById(R.id.titleView);
        descriptionView = (TextView) findViewById(R.id.descriptionView);
        dateView = (TextView) findViewById(R.id.dateView);
        editTitleView = (TextView) findViewById(R.id.editTitle);
        editDescriptionView = (TextView) findViewById(R.id.editDescription);
        saveButton = (Button) findViewById(R.id.updateEditButton);
        cancelButton = (Button) findViewById(R.id.cancelEditButton);

        //Sets widgets to display To-Do previously clicked on.
        titleView.setText(todo.getTitle());
        descriptionView.setText(todo.getDescription());
        dateView.setText(todo.getDue().toString());

        //Sets up click listeners.
        saveButton.setOnClickListener(new Click());
        cancelButton.setOnClickListener(new Click());

    }

    //override the back button and make it go to the ToDoActivity class
    //http://stackoverflow.com/questions/11807554/go-to-home-screen-instead-of-previous-activity
    public void onBackPressed() {
        Intent startMain = new Intent(EditToDoActivity.this, ToDoActivity.class);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    //Click class to handle click events.
    //Used for updating the to-do(And updating the database)
    //as well as cancelling out of the current activity.
    class Click implements View.OnClickListener {
        Intent todoIntent = new Intent(EditToDoActivity.this, ToDoActivity.class);

        //Deals with onClick events.
        //Checks ID of calling widget before deciding on what to do.
        public void onClick(View view) {

            try {
                if (view.getId() == R.id.updateEditButton) {
                    String update = "update todo set title = '" + editTitleView.getText().toString() + "', description = '" + editDescriptionView.getText().toString() + "' where _id = " + todo.getId();
                    db.getWritableDatabase().execSQL(update);
                    Toast toast = Toast.makeText(getApplicationContext(), "To-Do edited successfully!", Toast.LENGTH_SHORT);
                    toast.show();
                    startActivity(todoIntent);
                } else if (view.getId() == R.id.cancelEditButton) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Update cancelled!", Toast.LENGTH_SHORT);
                    toast.show();
                    startActivity(todoIntent);
                }
            } catch (Exception E) {
                Log.e("EditToDoActivity", "Error when editing To-Do");
            }

        }
    }
}
