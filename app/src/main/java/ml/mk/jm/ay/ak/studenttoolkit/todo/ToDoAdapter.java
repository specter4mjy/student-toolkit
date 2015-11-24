package ml.mk.jm.ay.ak.studenttoolkit.todo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ml.mk.jm.ay.ak.studenttoolkit.R;
import ml.mk.jm.ay.ak.studenttoolkit.database.DatabaseConnection;

/**
 * Created by Marc on 10/11/2015.
 */
public class ToDoAdapter extends CursorAdapter implements ListAdapter {

    Context context;
    DatabaseConnection db;
    Cursor c;
    Activity parent;
    static ArrayList<Todo> todos;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyy", Locale.US);

    //Constructor, used to create To-Do adapter.
    //The todoActivty argument was a necessary change in order to get the parent activity
    //for the alert dialog. Since this class technically isn't an activity, it wasn't possible to
    //use this class instead.
    public ToDoAdapter(Context context, Cursor cursor, Activity todoActivity){
        super(context, cursor);
        db = ToDoActivity.db;
        todos = new ArrayList<>();
        this.c = cursor;
        this.parent = todoActivity;
        this.context = context;
        while(c.moveToNext()){
            Date date = new Date();
            try {
                date = simpleDateFormat.parse(c.getString(DatabaseConnection.DUE));


                /*//setAlarm
                //long milliseconds = d.getTime();
                long alarmTime = date.getTime();
                //long alarmTime = new GregorianCalendar().getTimeInMillis()+2*1000;
                //long alarmTime = new GregorianCalendar().getTimeInMillis()+milliseconds;
                Log.d("Ademola", "Alarm time [" + alarmTime + " ]");
                //String msg, String msgText, String msgAlert
                Intent alarmIntent = new Intent(context, AlertReceiver.class);
                alarmIntent.putExtra("TODO_ID", c.getInt(DatabaseConnection.ID) );
                alarmIntent.putExtra("TODO_TITLE", c.getString(DatabaseConnection.TITLE) );
                alarmIntent.putExtra("TODO_DESCRIPTION", c.getString(DatabaseConnection.DESCRIPTION));

                //, c.getInt(DatabaseConnection.ID)
                *//*Intent intent = new Intent(getBaseContext(), SignoutActivity.class);
                intent.putExtra("EXTRA_SESSION_ID",);
                startActivity(intent)*//*

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, PendingIntent.getBroadcast(context, 1,
                        alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                Toast.makeText(context, "Alarm has been set for " + alarmTime + " seconds", Toast.LENGTH_SHORT).show();
                Log.d("Ademola", "Alarm Manager [" + alarmManager + " ]");

                //end setAlarm*/


            } catch (ParseException e) {
                e.printStackTrace();
            }
            Todo todo = new Todo(c.getInt(DatabaseConnection.ID), c.getString(DatabaseConnection.TITLE),c.getString(DatabaseConnection.DESCRIPTION), date);// new Date());
            todos.add(todo);
        }
        c.close();
        notifyDataSetChanged();
    }

    //Required over-write for adapter to work.
    //Must return the size of contents in the adapter.
    @Override
    public int getCount() {
        return todos.size() ;
    }

    //Required over-write for adapter to work.
    //This refers to each To-Do in the adapter.
    //Allows the user to view or delete (with a prompt) each To-Do that the adapter holds.
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.to_do_adapter, null);
        }

        TextView listItemText = (TextView) view.findViewById(R.id.list_todo);
        Todo todo = todos.get(position);
        listItemText.setText((CharSequence) todo.getTitle());

        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.delete_button);
        ImageButton showBtn = (ImageButton)view.findViewById(R.id.show_button);

        //We don't want the user to accidentally delete a To-Do, so prompt them first.
        //http://stackoverflow.com/questions/23195208/how-to-pop-up-a-dialog-to-confirm-delete-when-user-long-press-on-the-list-item
        //http://stackoverflow.com/questions/10333730/how-to-make-baseadapter-show-alertdialog-android-application
        final AlertDialog.Builder alert = new AlertDialog.Builder(parent.getContext());
        alert.setTitle("Warning!");
        alert.setMessage("Are you sure to this To-Do?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, ToDoActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String update = "delete from todo where _id = " + todos.get(position).getId() + "";
                db.getWritableDatabase().execSQL(update);

                notifyDataSetChanged();
                Toast toast = Toast.makeText(context, "To-Do successfully removed!", Toast.LENGTH_SHORT);
                toast.show();
                context.startActivity(intent);
                dialog.dismiss();

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //If delete is pressed, show the alert dialog.
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.show();
            }
        });

        //If the show button is pressed, put the current To-Do in a bundle and send it off
        //to the show activity.
        showBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(context , ShowToDoActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                bundle.putParcelable("todo",todos.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return view;
    }

    //Required over-write to make adapter work.
    //Create actual layout used by To-Do's held by adapter.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.to_do_adapter, parent, false);
    }

    //Required over-write to make adapter work.
    //This ties the To-Do contents to the view held by the adapter.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView listItemText = (TextView)view.findViewById(R.id.list_todo);
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        listItemText.setText(title);
    }


}
