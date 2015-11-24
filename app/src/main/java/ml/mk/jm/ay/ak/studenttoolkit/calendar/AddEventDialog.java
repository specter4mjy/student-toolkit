package ml.mk.jm.ay.ak.studenttoolkit.calendar;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import ml.mk.jm.ay.ak.studenttoolkit.R;

import static ml.mk.jm.ay.ak.studenttoolkit.calendar.helper.TimeFormatHelper.millisToHourAndMinuteStr;

/**
 * Created by specter on 11/3/15.
 */
public class AddEventDialog extends DialogFragment {
    private EditText edTitle;
    private TextView startTimeText;
    private TextView endTimeText;
    private Button btCancel;
    private Button btSave;
    private Switch swAllDay;
    private Switch swAlarm;
    private EditText edDescription;
    private EditText edLocation;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private static long startFreeTimeMillis;
    private static long endFreeTimeMillis;
    private static int day_of_month;
    private static RecyclerViewAdapter recyclerViewAdapter;




    public static AddEventDialog newInstance(EventDataModel eventDataModel,RecyclerViewAdapter recyclerViewAdapter) {
        AddEventDialog frag = new AddEventDialog();
        frag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        startFreeTimeMillis = eventDataModel.startTimeMillis;
        endFreeTimeMillis = eventDataModel.endTimeMillis;
        day_of_month = eventDataModel.day_of_month;
        AddEventDialog.recyclerViewAdapter = recyclerViewAdapter;
        return frag;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_event_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupHourAndMinute();
        startTimeText = (TextView) view.findViewById(R.id.start_time);
        endTimeText = (TextView) view.findViewById(R.id.end_time);
        startTimeText.setText(millisToHourAndMinuteStr(day_of_month, startFreeTimeMillis));

        final TimePickerDialog.OnTimeSetListener onStartTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startHour = hourOfDay;
                startMinute = minute;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(startFreeTimeMillis);
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                startFreeTimeMillis = calendar.getTimeInMillis();
                startTimeText.setText(millisToHourAndMinuteStr(day_of_month, startFreeTimeMillis));
            }
        };
        startTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.dialogTheme, onStartTimeSetListener, startHour, startMinute, true);
                timePickerDialog.show();

            }
        });
        endTimeText.setText(millisToHourAndMinuteStr(day_of_month, endFreeTimeMillis));

        final TimePickerDialog.OnTimeSetListener onEndTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endHour = hourOfDay;
                endMinute = minute;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(endFreeTimeMillis);
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                endFreeTimeMillis = calendar.getTimeInMillis();
                endTimeText.setText(millisToHourAndMinuteStr(day_of_month, endFreeTimeMillis));
            }
        };
        endTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.dialogTheme, onEndTimeSetListener, endHour, endMinute, true);
                timePickerDialog.show();

            }
        });

        edTitle = (EditText) view.findViewById(R.id.title);
        edTitle.requestFocus();

        swAllDay = (Switch) view.findViewById(R.id.allDaySwitch);
        swAlarm = (Switch) view.findViewById(R.id.alarmSwitch);
        edDescription = (EditText) view.findViewById(R.id.description_edit);
        edLocation = (EditText) view.findViewById(R.id.location);

        btSave = (Button) view.findViewById(R.id.saveButton);
        btCancel = (Button) view.findViewById(R.id.cancelButton);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewEvent();
                dismiss();
            }
        });

    }

    private void setupHourAndMinute() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startFreeTimeMillis);
        startHour = calendar.get(Calendar.HOUR_OF_DAY);
        startMinute = calendar.get(Calendar.MINUTE);
        calendar.setTimeInMillis(endFreeTimeMillis);
        endHour = calendar.get(Calendar.HOUR_OF_DAY);
        endMinute = calendar.get(Calendar.MINUTE);
    }

    private void saveNewEvent() {


        EventDataModel eventDataModel = new EventDataModel();
        eventDataModel.startTimeMillis = startFreeTimeMillis;
        eventDataModel.endTimeMillis = endFreeTimeMillis;
        eventDataModel.allDay = swAllDay.isChecked() ? 1 : 0;
        eventDataModel.hasAlarm = swAlarm.isChecked() ? 1 : 0;
        eventDataModel.title = edTitle.getText().toString();
        eventDataModel.description = edDescription.getText().toString();
        eventDataModel.location = edLocation.getText().toString();
        eventDataModel.addIcon = false;
        recyclerViewAdapter.addEvent(eventDataModel,0);
    }

}
