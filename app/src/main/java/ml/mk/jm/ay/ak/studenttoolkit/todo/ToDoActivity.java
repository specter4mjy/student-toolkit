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

/**
 * Created by Marc.
 * This is the main activity for the To-Do function. It uses a ToDoAdapter to display a list of To-Do objects, which
 * the user can execute commands on (From the adapter). A navigation view is provided to allow the user to use the
 * drawer present when the user first opened the application. A database connection is also created here, which is used
 * in several other classes.
 */

public class ToDoActivity extends AppCompatActivity {

    ListView listView;
    ToDoAdapter toDoAdapter;
    static DatabaseConnection db;
    private NavigationView nvDrawer;

    //onCreate, called when activity is created.
    //How the To-Do works: It essentially rebuilds the database every time the activity is called,
    //then puts the results into an ArrayList of To-dos. This way, we can guarantee that the data
    //is correct.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_to_do);
        listView = (ListView) findViewById(R.id.listview);
        db = new DatabaseConnection(this.getApplicationContext());
        Cursor cursor = db.getReadableDatabase().rawQuery("select * from todo",null);
        ImageButton newToDoButton = (ImageButton) findViewById(R.id.newToDoButton);
        toDoAdapter = new ToDoAdapter(this.getApplicationContext(),cursor, ToDoActivity.this);
        listView.setAdapter(toDoAdapter);
        newToDoButton.setOnClickListener(new Click());
        cursor.close();
        nvDrawer = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(nvDrawer);
    }

    //Required setup for the drawer to work.
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    //When a drawer item is select, if it was the calendar, then open the calendar (Main) activity.
    //If it was the To-Do item, then open the main To-Do activity.
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

    //Override the back button and have it exit the application.
    //There is no-where else for the application to go at this point, hence the exit.
    //http://stackoverflow.com/questions/11807554/go-to-home-screen-instead-of-previous-activity
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    //A Click class to deal with Click events
    class Click implements View.OnClickListener {
        Intent newToDoIntent;

        //If the "new" button is pressed, start off the new To-Do activity.
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
