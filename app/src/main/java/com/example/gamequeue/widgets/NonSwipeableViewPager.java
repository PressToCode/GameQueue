package com.example.gamequeue.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class NonSwipeableViewPager extends ViewPager {
    public NonSwipeableViewPager(@NonNull Context context) {
        super(context);
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never intercept touch events so child views (fragments) can handle them
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Don't handle touch events (i.e., disable swiping)
        return false;
    }
}
