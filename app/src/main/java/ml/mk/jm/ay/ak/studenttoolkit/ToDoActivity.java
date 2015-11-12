package ml.mk.jm.ay.ak.studenttoolkit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

public class ToDoActivity extends AppCompatActivity {

    ListView listView;
    ToDoAdapter toDoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        listView = (ListView) findViewById(R.id.listview);
        toDoAdapter = new ToDoAdapter(this.getApplicationContext());
        listView.setAdapter(toDoAdapter);
    }
}
