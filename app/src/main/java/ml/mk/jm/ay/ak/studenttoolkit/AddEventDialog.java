package ml.mk.jm.ay.ak.studenttoolkit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by specter on 11/3/15.
 */
public class AddEventDialog extends DialogFragment {
    private EditText mEditText;
    public AddEventDialog() {
    }

    public static AddEventDialog newInstance() {
        AddEventDialog frag = new AddEventDialog();
        frag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
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

        mEditText = (EditText) view.findViewById(R.id.title);
        mEditText.requestFocus();
    }

}
