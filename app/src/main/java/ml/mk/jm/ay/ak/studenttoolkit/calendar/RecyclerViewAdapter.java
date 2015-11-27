package ml.mk.jm.ay.ak.studenttoolkit.calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ml.mk.jm.ay.ak.studenttoolkit.R;
import ml.mk.jm.ay.ak.studenttoolkit.calendar.helper.CalendarProviderHelper;

import static ml.mk.jm.ay.ak.studenttoolkit.calendar.helper.TimeFormatHelper.millisToHourAndMinuteStr;

/**
 * Created by specter on 10/24/15.
 * Recycler View Adapter
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<EventDataModel> events;
    private SparseBooleanArray selectedItems;
    private Context context;
    private AppCompatActivity activity;
    private final int ADD = 0, EVENT = 1;

    /**
     * constructor
     * @param activity
     * @param events
     */
    public RecyclerViewAdapter(Activity activity, List<EventDataModel> events) {
        this.events = events;
        this.activity = (AppCompatActivity) activity;
    }

    /**
     * add event in List
     * @param newModelData
     * @param position
     */
    public void addEvent(EventDataModel newModelData, int position) {
        events.add(position, newModelData);
        notifyItemInserted(position);
    }


    /**
     * remove event in List
     * @param position
     */
    public void removeEvent(int position) {
        events.remove(position);
        notifyItemRemoved(position);
    }


    /**
     * get the event in postion
     * @param position
     * @return
     */
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
                eventViewHolder.label.setText(model.title.replaceAll("^COMP\\d{5}: ", ""));
                eventViewHolder.label.setTextColor(model.eventColor);
                eventViewHolder.startTime.setText(millisToHourAndMinuteStr(false, model.startTimeMillis));
                eventViewHolder.endTime.setText(millisToHourAndMinuteStr(true, model.endTimeMillis));
                if (model.location == null || model.location.equals("")) {
                    eventViewHolder.location.setVisibility(View.GONE);
                } else {
                    Pattern pattern = Pattern.compile("\\((.*?)\\)");
                    Matcher matcher = pattern.matcher(model.location);
                    if (matcher.find())
                        eventViewHolder.location.setText(matcher.group(1));
                    else
                        eventViewHolder.location.setText(model.location);
                    eventViewHolder.location.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    /**
     * return current item is add button or a event
     * @param position
     * @return
     */
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


    /**
     * this is for event item
     */
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


        /**
         * called when user click the event in RecyclerView and it will trigger the edit event activity
         * @param v
         */
        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            EventDataModel event = events.get(position);
            Intent intent = new Intent(activity, EventDetailActivity.class);
            intent.putExtra("event_id", event.event_id);
            intent.putExtra("title", event.title);
            intent.putExtra("location", event.location);
            intent.putExtra("startTime", event.startTimeMillis);// this is milliseconds formate ,use TimeFormatHelper to convert to string
            intent.putExtra("endTime", event.endTimeMillis);
            intent.putExtra("description", event.description);
            intent.putExtra("allDay", event.allDay);
            intent.putExtra("hasAlarm", event.hasAlarm);
            intent.putExtra("calendarId", event.cal_id);

            Log.d("Mukesh", "calendar id " + event.cal_id);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, (View) label, "event_title");
            activity.startActivity(intent, options.toBundle());


        }
    }

    /**
     * theis is for add button
     */
    public class AddViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivAdd;


        public AddViewHolder(View itemView) {
            super(itemView);
            ivAdd = (ImageView) itemView.findViewById(R.id.ivAdd);
            itemView.setOnClickListener(this);
        }

        /**
         * called when user click the add button and a create event dialog will be created
         * @param v
         */
        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            EventDataModel eventDataModel = events.get(position);
            AddEventDialog addEventDialog = AddEventDialog.newInstance(eventDataModel, RecyclerViewAdapter.this, activity);

            addEventDialog.show(((AppCompatActivity) activity).getSupportFragmentManager(), "add event");

        }
    }

    /**
     * add swipe to delete item function
     */
    public class SwipeTouchHelper extends ItemTouchHelper.SimpleCallback {

        public SwipeTouchHelper() {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        /**
         * return 0 to make swipe disabled in add icon
         * @param recyclerView
         * @param viewHolder
         * @return
         */
        @Override
        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() == ADD || ((MainActivity) activity).isEditMode() == false)
                return 0;
            else
                return super.getSwipeDirs(recyclerView, viewHolder);
        }

        /**
         * triggered by user swipe on event item and pop up a snack bar
         * @param viewHolder
         * @param direction
         */
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            final boolean[] yesClicked = {false};
            final EventDataModel eventData = events.get(viewHolder.getAdapterPosition());
            removeEvent(viewHolder.getAdapterPosition());
            Snackbar.make(viewHolder.itemView, "Event deleted", Snackbar.LENGTH_LONG)
                    .setAction("undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            yesClicked[0] = true;
                        }
                    })
                    .setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            super.onDismissed(snackbar, event);
                            if (yesClicked[0] == false) {
                                CalendarProviderHelper.deleteEvent(activity, eventData);
                            }
                            ((MainActivity) activity).refreshEvents();
                        }
                    })
                    .show();
        }
    }
}
