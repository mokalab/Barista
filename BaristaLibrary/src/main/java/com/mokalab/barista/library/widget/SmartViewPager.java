package com.mokalab.barista.library.widget;

import com.antoniotari.android.util.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * SmartViewPager.class
 * This is extension class from {@link android.support.v4.view.ViewPager} with the ability to disable the swipe.
 *
 * @author David Fernandez
 */
public class SmartViewPager extends ViewPager 
{

    private boolean mSwipeable = true;

    public SmartViewPager(Context context) {

        super(context);
    }

    public SmartViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SmartViewPager);
        try {
            mSwipeable = a.getBoolean(R.styleable.SmartViewPager_swipeable, true);
        } finally {
            a.recycle();
        }
    }


    /**
     * Set swipeable mode.
     *
     * @param swipeable true if has to swipe otherwise false
     */
    public void setSwipeable(boolean swipeable) {

        mSwipeable = swipeable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        return mSwipeable ? super.onInterceptTouchEvent(event) : false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return mSwipeable ? super.onTouchEvent(event) : false;
    }

}
