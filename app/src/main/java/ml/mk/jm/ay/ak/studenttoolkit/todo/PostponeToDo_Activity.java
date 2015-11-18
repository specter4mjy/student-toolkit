package ml.mk.jm.ay.ak.studenttoolkit.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ml.mk.jm.ay.ak.studenttoolkit.R;

public class PostponeToDo_Activity extends AppCompatActivity {

    TextView titleView;
    TextView dateView;
    Intent intent;
    Bundle bundle;
    Todo todo;
    Button updatePostpone;
    Button cancelPostpone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postpone_to_do_);

        intent = getIntent();
        bundle = intent.getExtras();
        todo = bundle.getParcelable("todo");

        titleView = (TextView) findViewById(R.id.currentTitle);
        dateView = (TextView) findViewById(R.id.currentDate);
        updatePostpone = (Button) findViewById(R.id.updatePostponeButton);
        cancelPostpone = (Button) findViewById(R.id.cancelPostponeButton);

        titleView.setText(todo.getTitle());
        dateView.setText(todo.getDue().toString());
        titleView.setOnClickListener(new Click());
        dateView.setOnClickListener(new Click());

        Spinner daySpinner = (Spinner) findViewById(R.id.day_spinner);
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this,R.array.day_array, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        Spinner timeSpinner = (Spinner) findViewById(R.id.time_spinner);
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this,R.array.time_array, android.R.layout.simple_spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);

    }

    class Click implements View.OnClickListener {
        Intent menuIntent;

        public void onClick(View view) {

            try {
                if (view.getId() == R.id.updatePostponeButton) {
                    menuIntent = new Intent(PostponeToDo_Activity.this, ToDoActivity.class);
                    Toast toast = Toast.makeText(getApplicationContext(), "To-Do Postponed!", Toast.LENGTH_SHORT);
                    toast.show();
                    startActivity(menuIntent);
                } else if (view.getId() == R.id.cancelPostponeButton) {
                    menuIntent = new Intent(PostponeToDo_Activity.this, ToDoActivity.class);
                    Toast toast = Toast.makeText(getApplicationContext(), "Postpone cancelled!", Toast.LENGTH_SHORT);
                    toast.show();
                    startActivity(menuIntent);
                } else {

                }
            } catch (Exception E) {
                Log.e("Main Class", "Error when editing To-Do");
            }

        }
    }

}
