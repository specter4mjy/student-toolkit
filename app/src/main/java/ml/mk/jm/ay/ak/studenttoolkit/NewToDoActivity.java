package ml.mk.jm.ay.ak.studenttoolkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class NewToDoActivity extends AppCompatActivity {

    TextView newTitleView;
    TextView newDescriptionView;
    TextView newDateView;
    Button saveButton;
    Button cancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_to_do);

        newTitleView = (TextView) findViewById(R.id.newTitle);
        newDescriptionView = (TextView) findViewById(R.id.newDescription);
        newDateView = (TextView) findViewById(R.id.newDate);
        saveButton = (Button) findViewById(R.id.saveButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        saveButton.setOnClickListener(new Click());
        cancelButton.setOnClickListener(new Click());
        Log.w("Todos!",MainActivity.todos.toString());
    }

    class Click implements View.OnClickListener {
        Intent todoIntent = new Intent(NewToDoActivity.this, MainActivity.class);

        public void onClick(View view) {

            try {
                if (view.getId() == R.id.saveButton) {
                    Todo todo = new Todo(10001,newTitleView.getText().toString(), newDescriptionView.getText().toString(), new Date());
                    MainActivity.todos.add(todo);
                    Toast toast = Toast.makeText(getApplicationContext(), "To-Do created successfully!", Toast.LENGTH_SHORT);
                    toast.show();
                    startActivity(todoIntent);
                } else if (view.getId() == R.id.cancelButton) {
                    Toast toast = Toast.makeText(getApplicationContext(), "New To-do cancelled!", Toast.LENGTH_SHORT);
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
