package ml.mk.jm.ay.ak.studenttoolkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addItemButton = (Button) findViewById(R.id.todo_button);//exit button

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
