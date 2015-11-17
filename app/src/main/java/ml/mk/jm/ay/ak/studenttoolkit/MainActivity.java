package ml.mk.jm.ay.ak.studenttoolkit;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;

import ml.mk.jm.ay.ak.studenttoolkit.database.DatabaseConnection;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Todo> todos = new ArrayList<Todo>();
    public static DatabaseConnection db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseConnection(this.getApplicationContext());

        Button addItemButton = (Button) findViewById(R.id.todo_button);//exit button
        db.getWritableDatabase().execSQL("INSERT INTO todo (title, description, due) VALUES ('EXAMPLETITLE','EXAMPLEDESCRIPTION', CURRENT_TIMESTAMP)");

        Cursor c = db.getReadableDatabase().rawQuery("select * from todo",null);
        while(c.moveToNext()){
            String id = c.getString(0);
            String title = c.getString(DatabaseConnection.TITLE);
            String description = c.getString(DatabaseConnection.DESCRIPTION);
            String due = c.getString(DatabaseConnection.DUE);
            Log.w("DATABASE HERE!", title + description + due);
        }

        c.close();

        addItemButton.setOnClickListener(new Click());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class Click implements View.OnClickListener {
        Intent todoIntent = new Intent(MainActivity.this, ToDoActivity.class);

        public void onClick(View view) {

            try {
                if (view.getId() == R.id.todo_button) {
                    startActivity(todoIntent);
                } else if (view.getId() == R.id.map_button) {
                    //go to map
                } else if (view.getId() == R.id.bldg_button) {
                    //go to building section
                }
            } catch (Exception E) {
                Log.e("Main Class", "Error when selecting buttons");
            }

        }
    }

}
