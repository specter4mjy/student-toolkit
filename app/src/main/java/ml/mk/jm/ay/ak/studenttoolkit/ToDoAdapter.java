package ml.mk.jm.ay.ak.studenttoolkit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ml.mk.jm.ay.ak.studenttoolkit.database.DatabaseConnection;

/**
 * Created by Marc on 10/11/2015.
 */
public class ToDoAdapter extends CursorAdapter implements ListAdapter {

    Context context;
    DatabaseConnection db;//= MainActivity.db;
    Cursor c;// = db.getReadableDatabase().rawQuery("select * from todo",null);
    ArrayList<Todo> todos;

    public ToDoAdapter(Context context, Cursor cursor){//}, int flags) {
        super(context, cursor);//, flags);
        db = MainActivity.db;
        todos = new ArrayList<>();
        this.c = cursor;
        this.context = context;
        while(c.moveToNext()){
          Todo todo = new Todo(c.getInt(DatabaseConnection.ID), c.getString(DatabaseConnection.TITLE),c.getString(DatabaseConnection.DESCRIPTION), new Date());
            todos.add(todo);
        }
    }

//    public ToDoAdapter(Context context) {
//
//        notifyDataSetChanged();
//
//        this.context = context;
//    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {    }

    @Override
    public int getCount() {
        return c.getCount() ;
    }

    @Override
    public Object getItem(int position) {
        /*while(ToDoActivity.c.moveToNext()){
            Integer id = c.getInt(DatabaseConnection.ID);
            String title = c.getString(DatabaseConnection.TITLE);
            String description = c.getString(DatabaseConnection.DESCRIPTION);
            String due = c.getString(DatabaseConnection.DUE);
            if(id == position){
                DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                Date date = new Date();
                try {
                    date = format.parse(due);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return new Todo(id, title, description,date);
            }
        }
        return null ;*/
        return todos.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.to_do_adapter, null);
        }

        TextView listItemText = (TextView) view.findViewById(R.id.list_todo);
        Todo todo = todos.get(position);
        listItemText.setText(String.valueOf(todo.getId()) +(CharSequence) todo.getTitle());
  /*      while(c.moveToNext()){
            String id = c.getString(0);
            String title = c.getString(DatabaseConnection.TITLE);
            String description = c.getString(DatabaseConnection.DESCRIPTION);
            String due = c.getString(DatabaseConnection.DUE);
            listItemText.setText(title);//"Title: " + title + "\nDue: " + due);
            Log.w("DATABASE HERE!", id + title + description + due);
        }*/

//        listItemText.setText("F");

        //listItemText.setText("Title: " + MainActivity.todos.get(position).getTitle() + "\nDue: " + sdf.format(MainActivity.todos.get(position).getDue()));

        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.delete_button);
        ImageButton showBtn = (ImageButton)view.findViewById(R.id.show_button);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.todos.remove(position);
                notifyDataSetChanged();
                Toast toast = Toast.makeText(context, "To-Do successfully removed!", Toast.LENGTH_SHORT);
                toast.show();
                context.startActivity(intent);
            }
        });

        showBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(context , ShowToDoActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                bundle.putParcelable("todo",MainActivity.todos.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.to_do_adapter, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView listItemText = (TextView)view.findViewById(R.id.list_todo);
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        listItemText.setText(title);
    }

    @Override
    public int getItemViewType(int position) {
        return position -1;
    }

    @Override
    public int getViewTypeCount() {
        if (c.getCount() == 0) {
            return 1;
        } else {
            return c.getCount();
        }
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
