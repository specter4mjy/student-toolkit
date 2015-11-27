package ml.mk.jm.ay.ak.studenttoolkit.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ml.mk.jm.ay.ak.studenttoolkit.R;
import ml.mk.jm.ay.ak.studenttoolkit.calendar.helper.CalendarProviderHelper;

/**
 * Created by specter on 10/24/15.
 * Custom Fragment for each day
 */
public class ScreenSlideFragment extends android.support.v4.app.Fragment {
    private RecyclerView recyclerView;


    private RecyclerViewAdapter adapter;
    private int weekOffset;

    private static final String DEBUG_TAG = "MyActivity";

    /**
     * create new fragement
     * @param page paper position
     * @param weekOffset
     * @return new fragment object created
     */
    public static ScreenSlideFragment newInstance(int page, int weekOffset) {
        ScreenSlideFragment screenSlideFragment = new ScreenSlideFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        args.putInt("weekOffset",weekOffset);
        screenSlideFragment.setArguments(args);
        return screenSlideFragment;
    }

    public RecyclerViewAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     *  create linear layout recycler view adn setup the event data for recyclerView
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.content_main, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(linearLayoutManager);
        int page = getArguments().getInt("page");
        int weekOffset = getArguments().getInt("weekOffset");

        adapter = new RecyclerViewAdapter(getActivity(), CalendarProviderHelper.getTodayEvents(getActivity(), page + 7 * weekOffset));
        recyclerView.setAdapter(adapter);
        /**
         * bind item swipe gesture listener
         */
        ItemTouchHelper.Callback callback = adapter.new SwipeTouchHelper();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return rootView;
    }

}
