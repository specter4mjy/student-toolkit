package ml.mk.jm.ay.ak.studenttoolkit.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ml.mk.jm.ay.ak.studenttoolkit.R;

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

        editButton = (Button) findViewById(R.id.showEditButton);
        postponeButton = (Button) findViewById(R.id.postponeButton);

        //Sets widgets to display To-Do previously clicked on.
        titleView.setText(todo.getTitle());
        descriptionView.setText(todo.getDescription());
        dateView.setText(todo.getDue().toString());

        //Sets up click listeners.
        editButton.setOnClickListener(new Click());
        postponeButton.setOnClickListener(new Click());

    }

    //override the back button and make it go to the ToDoActivity class
    public void onBackPressed() {
        Intent startMain = new Intent(ShowToDoActivity.this, ToDoActivity.class);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

   class Click implements View.OnClickListener {
        Intent todoIntent;
        Bundle bundle;

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
                    todoIntent = new Intent(ShowToDoActivity.this, PostponeToDo_Activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    todoIntent.putExtras(bundle);
                    startActivity(todoIntent);
                }
            } catch (Exception E) {
                Log.e("ShowToDoActivity", "Error before editing To-Do");
            }

        }
    }
}

