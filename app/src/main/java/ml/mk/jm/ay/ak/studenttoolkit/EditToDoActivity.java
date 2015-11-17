package ml.mk.jm.ay.ak.studenttoolkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EditToDoActivity extends AppCompatActivity {

    Todo todo;
    Intent intent;
    Bundle bundle;
    TextView titleView;
    TextView descriptionView;
    TextView dateView;
    TextView editTitleView;
    TextView editDescriptionView;
    TextView editDateView;
    Button saveButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_to_do);
        intent = getIntent();
        todo = intent.getParcelableExtra("todo");

        //Connects widget variables with their GUI equivalents.
        titleView = (TextView) findViewById(R.id.titleView);
        descriptionView = (TextView) findViewById(R.id.descriptionView);
        dateView = (TextView) findViewById(R.id.dateView);
        editTitleView = (TextView) findViewById(R.id.editTitle);
        editDescriptionView = (TextView) findViewById(R.id.editDescription);
        editDateView = (TextView) findViewById(R.id.editDate);
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

    class Click implements View.OnClickListener {
        Intent todoIntent = new Intent(EditToDoActivity.this, MainActivity.class);

        public void onClick(View view) {

            try {
                if (view.getId() == R.id.updateEditButton) {
                    todo.setDescription(editDescriptionView.getText().toString());
                    todo.setTitle(editTitleView.getText().toString());
                    Toast toast = Toast.makeText(getApplicationContext(), "To-Do edited successfully!", Toast.LENGTH_SHORT);
                    toast.show();
                    startActivity(todoIntent);
                } else if (view.getId() == R.id.cancelEditButton) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Update cancelled!", Toast.LENGTH_SHORT);
                    toast.show();
                    startActivity(todoIntent);
                } else {

                }
            } catch (Exception E) {
                Log.e("Main Class", "Error when editing To-Do");
            }

        }
    }
}
