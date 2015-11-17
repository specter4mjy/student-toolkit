package ml.mk.jm.ay.ak.studenttoolkit;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import ml.mk.jm.ay.ak.studenttoolkit.database.DatabaseConnection;

public class ToDoActivity extends AppCompatActivity {

    ListView listView;
    ToDoAdapter toDoAdapter;
    static Cursor c;
    DatabaseConnection db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        listView = (ListView) findViewById(R.id.listview);
        db = MainActivity.db;
        c = db.getReadableDatabase().rawQuery("select * from todo",null);

        ImageButton newToDoButton = (ImageButton) findViewById(R.id.newToDoButton);
        toDoAdapter = new ToDoAdapter(this.getApplicationContext(),c);
        listView.setAdapter(toDoAdapter);
        newToDoButton.setOnClickListener(new Click());
    }

    class Click implements View.OnClickListener {
        Intent newToDoIntent;
        public void onClick(View view) {

            try {
                if (view.getId() == R.id.newToDoButton) {
                    newToDoIntent = new Intent(getApplicationContext() , NewToDoActivity.class);
                    startActivity(newToDoIntent);
                }
            } catch (Exception E) {
                Log.e("ToDoActivity.java", "Error when creating To-Do");
            }

        }
    }
}
