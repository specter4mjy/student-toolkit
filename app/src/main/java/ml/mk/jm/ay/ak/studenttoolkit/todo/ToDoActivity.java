package ml.mk.jm.ay.ak.studenttoolkit.todo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import ml.mk.jm.ay.ak.studenttoolkit.R;
import ml.mk.jm.ay.ak.studenttoolkit.calendar.MainActivity;
import ml.mk.jm.ay.ak.studenttoolkit.database.DatabaseConnection;

public class ToDoActivity extends AppCompatActivity {

    ListView listView;
    ToDoAdapter toDoAdapter;
    static DatabaseConnection db;
    private NavigationView nvDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_to_do);
        listView = (ListView) findViewById(R.id.listview);

        db = new DatabaseConnection(this.getApplicationContext());
        //Uncomment the below line to add a row to the database every time this activity is created.
        //db.getWritableDatabase().execSQL("INSERT INTO todo (title, description, due) VALUES ('EXAMPLETITLE','EXAMPLEDESCRIPTION', CURRENT_TIMESTAMP)");

        Cursor cursor = db.getReadableDatabase().rawQuery("select * from todo",null);
        ImageButton newToDoButton = (ImageButton) findViewById(R.id.newToDoButton);
        toDoAdapter = new ToDoAdapter(this.getApplicationContext(),cursor);
        listView.setAdapter(toDoAdapter);
        newToDoButton.setOnClickListener(new Click());
        cursor.close();
        nvDrawer = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(nvDrawer);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    public void selectDrawerItem(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_calendar:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_todo:
                intent = new Intent(this, ToDoActivity.class);
                startActivity(intent);
                break;
        }
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
