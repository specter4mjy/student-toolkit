package ml.mk.jm.ay.ak.studenttoolkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShowToDoActivity extends AppCompatActivity {

    Todo todo;
    Intent intent;
    Bundle bundle;
    TextView titleView;
    TextView descriptionView;
    TextView dateView;

    Button editButton;
    Button postponeButton;

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

        editButton = (Button) findViewById(R.id.editButton);
        postponeButton = (Button) findViewById(R.id.postponeButton);

        //Sets widgets to display To-Do previously clicked on.
        titleView.setText(todo.getTitle());
        descriptionView.setText(todo.getDescription());
        dateView.setText(todo.getDue().toString());

        //Sets up click listeners.
        editButton.setOnClickListener(new Click());
        postponeButton.setOnClickListener(new Click());

    }

    class Click implements View.OnClickListener {
        Intent todoIntent;

        public void onClick(View view) {

            try {
                if (view.getId() == R.id.editButton) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("todo",todo);
                    todoIntent = new Intent(ShowToDoActivity.this, EditToDoActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    todoIntent.putExtras(bundle);
                    startActivity(todoIntent);
                } else if (view.getId() == R.id.postponeButton) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("todo",todo);
                    todoIntent = new Intent(ShowToDoActivity.this, PostponeToDo_Activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    todoIntent.putExtras(bundle);
                    startActivity(todoIntent);
                } else {

                }
            } catch (Exception E) {
                Log.e("Main Class", "Error when editing To-Do");
            }

        }
    }
}

