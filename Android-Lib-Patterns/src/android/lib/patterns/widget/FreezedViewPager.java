package android.lib.patterns.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public final class FreezedViewPager extends ViewPager {
    private boolean freezed;

    public FreezedViewPager(final Context context) {
        super(context);
    }

    public FreezedViewPager(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent ev) {
        return this.freezed ? false : super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent ev) {
        return this.freezed ? false : super.onTouchEvent(ev);
    }

    public boolean isFreezed() {
        return this.freezed;
    }

    public void setFreezed(final boolean freezed) {
        this.freezed = freezed;
    }
}
