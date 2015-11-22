package ml.mk.jm.ay.ak.studenttoolkit.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import ml.mk.jm.ay.ak.studenttoolkit.R;

import static ml.mk.jm.ay.ak.studenttoolkit.calendar.helper.TimeFormatHelper.millisToHourAndMinuteStr;

/**
 * Created by specter on 11/3/15.
 */
public class AddEventDialog extends DialogFragment {
    private EditText mEditText;
    private TextView startTimeText;
    private TextView endTimeText;
    private static long startFreeTimeMillis ;
    private static long endFreeTimeMillis;
    private static int day_of_month;
    public AddEventDialog() {
    }

    public static AddEventDialog newInstance(EventDataModel eventDataModel) {
        AddEventDialog frag = new AddEventDialog();
        frag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        startFreeTimeMillis = eventDataModel.startTimeMillis;
        endFreeTimeMillis = eventDataModel.endTimeMillis;
        day_of_month = eventDataModel.day_of_month;
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

        startTimeText = (TextView) view.findViewById(R.id.start_time);
        endTimeText = (TextView) view.findViewById(R.id.end_time);
        startTimeText.setText(millisToHourAndMinuteStr(day_of_month,startFreeTimeMillis));
        endTimeText.setText(millisToHourAndMinuteStr(day_of_month,endFreeTimeMillis));

        mEditText = (EditText) view.findViewById(R.id.title);
        mEditText.requestFocus();
    }

}
