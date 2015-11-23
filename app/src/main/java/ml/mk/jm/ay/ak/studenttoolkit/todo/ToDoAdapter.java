package ml.mk.jm.ay.ak.studenttoolkit.todo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
    static ArrayList<Todo> todos;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyy", Locale.US);

    public ToDoAdapter(Context context, Cursor cursor){
        super(context, cursor);
        db = ToDoActivity.db;
        todos = new ArrayList<>();
        this.c = cursor;
        this.context = context;
        while(c.moveToNext()){
            Date date = new Date();
            try {
                date = simpleDateFormat.parse(c.getString(DatabaseConnection.DUE));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Todo todo = new Todo(c.getInt(DatabaseConnection.ID), c.getString(DatabaseConnection.TITLE),c.getString(DatabaseConnection.DESCRIPTION), date);// new Date());
            todos.add(todo);
        }
        c.close();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return todos.size() ;
    }

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

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , ToDoActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String update = "delete from todo where _id = " + todos.get(position).getId() + "";
                db.getWritableDatabase().execSQL(update);

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
                bundle.putParcelable("todo",todos.get(position));
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


}
