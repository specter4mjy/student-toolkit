package ml.mk.jm.ay.ak.studenttoolkit;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Marc on 10/11/2015.
 */
public class ToDoAdapter extends BaseAdapter implements ListAdapter {

    ArrayList<Todo> todos;
    Context context;

    public ToDoAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 2;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        todos = new ArrayList<Todo>();

        todos.add(new Todo("Title1","Description1",new Date()));
        todos.add(new Todo("Title2","Description2",new Date()));
        todos.add(new Todo("Title3", "Description3", new Date()));


        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.to_do_adapter, null);
        }

        TextView listItemText = (TextView)view.findViewById(R.id.list_todo);
        listItemText.setText(todos.get(position).toString());

        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.delete_button);
        ImageButton editBtn = (ImageButton)view.findViewById(R.id.edit_button);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                todos.remove(position);
                notifyDataSetChanged();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(context , EditToDoActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                bundle.putParcelable("todo",todos.get(position));

                bundle.putInt("position", position);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return 3;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
