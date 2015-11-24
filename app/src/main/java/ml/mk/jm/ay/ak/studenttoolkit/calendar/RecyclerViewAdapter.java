package ml.mk.jm.ay.ak.studenttoolkit.calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ml.mk.jm.ay.ak.studenttoolkit.R;

import static ml.mk.jm.ay.ak.studenttoolkit.calendar.helper.TimeFormatHelper.millisToHourAndMinuteStr;

/**
 * Created by specter on 10/24/15.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<EventDataModel> events;
    private SparseBooleanArray selectedItems;
    private Context context;
    private Activity activity;
    private final int ADD = 0, EVENT = 1;

    public RecyclerViewAdapter(Activity activity, List<EventDataModel> events) {
        this.events = events;
        this.activity = activity;
    }

    public void addEvent(EventDataModel newModelData, int position) {
        events.add(position, newModelData);
        notifyItemInserted(position);
    }


    public void removeEvent(int position) {
        events.remove(position);
        if (getItemCount() >= 2 && events.get(position - 1).addIcon && events.get(position).addIcon) {
            events.remove(position);
            notifyItemRangeRemoved(position, 2);
        } else
            notifyItemRemoved(position);
    }
    public void updateAllEvents(int direction,List<EventDataModel> newevents) {
        for (int i = events.size() - 1; i >= 0; i--) {
            events.remove(i);
            notifyItemRemoved(i);
        }
        for (int i = newevents.size()-1; i >=0 ; i--) {
            events.add(0,newevents.get(i));
            notifyItemInserted(0);
        }
    }


    public EventDataModel getEvent(int position) {
        return events.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case ADD:
                View addView = inflater.inflate(R.layout.add_item, parent, false);
                viewHolder = new AddViewHolder(addView);
                break;
//            case EVENT:
            default:
                View eventView = inflater.inflate(R.layout.event_item, parent, false);
                viewHolder = new EventViewHolder(eventView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case ADD:
                AddViewHolder addViewHolder = (AddViewHolder) holder;
                break;
            case EVENT:
                EventDataModel model = events.get(position);
                EventViewHolder eventViewHolder = (EventViewHolder) holder;
                eventViewHolder.label.setText(model.title);
                eventViewHolder.label.setTextColor(model.eventColor);
                eventViewHolder.startTime.setText(millisToHourAndMinuteStr(model.day_of_month,model.startTimeMillis));
                eventViewHolder.endTime.setText(millisToHourAndMinuteStr(model.day_of_month,model.endTimeMillis));
                if (model.location==null || model.location.equals("")) {
                    eventViewHolder.location.setVisibility(View.GONE);
                } else {
                    eventViewHolder.location.setText(model.location);
                    eventViewHolder.location.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (events.get(position).addIcon) {
            return ADD;
        } else {
            return EVENT;
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }


    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView label;
        TextView startTime;
        TextView location;
        TextView endTime;
        CardView cardView;

        public EventViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card);
            label = (TextView) itemView.findViewById(R.id.txt_label_item);
            location = (TextView) itemView.findViewById(R.id.txt_label_location);
            startTime = (TextView) itemView.findViewById(R.id.txt_start_time);
            endTime = (TextView) itemView.findViewById(R.id.txt_end_time);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            EventDataModel event = events.get(position);
            Intent intent = new Intent(activity, EventDetailActivity.class);
            intent.putExtra("event_id",event.event_id);
            intent.putExtra("title", event.title);
            intent.putExtra("location", event.location);
            intent.putExtra("startTime", event.startTimeMillis);// this is milliseconds formate ,use TimeFormatHelper to convert to string
            intent.putExtra("endTime", event.endTimeMillis);
            intent.putExtra("description", event.description);
            intent.putExtra("allDay", event.allDay);
            intent.putExtra("hasAlarm", event.hasAlarm);
            intent.putExtra("calendarId",event.cal_id);

            Log.d("Mukesh", "calendar id "+event.cal_id);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, (View) label, "event_title");
            activity.startActivity(intent, options.toBundle());


        }
    }

    public class AddViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivAdd;


        public AddViewHolder(View itemView) {
            super(itemView);
            ivAdd = (ImageView) itemView.findViewById(R.id.ivAdd);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            EventDataModel eventDataModel = events.get(position);
            AddEventDialog addEventDialog = AddEventDialog.newInstance(eventDataModel,RecyclerViewAdapter.this);

            addEventDialog.show(((AppCompatActivity) activity).getSupportFragmentManager(), "add event");

        }
    }

    public class SwipeTouchHelper extends ItemTouchHelper.SimpleCallback {

        public SwipeTouchHelper() {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() == ADD  || ((MainActivity)activity).isEditMode() == false)
                return 0;
            else
                return super.getSwipeDirs(recyclerView, viewHolder);
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            removeEvent(viewHolder.getAdapterPosition());
        }
    }
}
