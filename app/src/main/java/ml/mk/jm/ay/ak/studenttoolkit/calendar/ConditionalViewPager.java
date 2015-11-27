package ml.mk.jm.ay.ak.studenttoolkit.calendar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by specter on 11/19/15.
 *  forbid swipe base on condition i.e. editmode value
 */

public class ConditionalViewPager extends ViewPager {
    private boolean editmode;

    public ConditionalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        editmode = false;
    }

    /**
     * when in edit mode, forbid user swipe left and right in viewpager
     * @param ev
     * @return
     */
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


    /**
     * set the editmode value
     * @param editmode
     */
    public void setEditmode(boolean editmode) {
        this.editmode = editmode;
    }
}
