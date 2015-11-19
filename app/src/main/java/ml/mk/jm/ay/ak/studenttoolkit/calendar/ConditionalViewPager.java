package ml.mk.jm.ay.ak.studenttoolkit.calendar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by specter on 11/19/15.
 */
public class ConditionalViewPager extends ViewPager {
    private boolean editmode;

    public ConditionalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        editmode = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (editmode) {
            return false;
        } else
            return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (editmode)
            return false;
        else
            return super.onTouchEvent(ev);

    }


    public void setEditmode(boolean editmode) {
        this.editmode = editmode;
    }
}
