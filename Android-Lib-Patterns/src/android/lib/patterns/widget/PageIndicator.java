package android.lib.patterns.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.lib.patterns.R;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class PageIndicator extends LinearLayout {
    private int                  drawableResId;
    private TransitionDrawable[] drawables;

    private int margin;
    private int transitionTime;
    private int currentItem = -1;

    public PageIndicator(final Context context) {
        this(context, null);
    }

    public PageIndicator(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageIndicator(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        this.setOrientation(LinearLayout.HORIZONTAL);

        this.transitionTime = context.getResources().getInteger(R.integer.page_indicator_transition);
        this.drawableResId  = R.drawable.page_indicator;

        if (attrs != null) {
            final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PageIndicator);

            int margin = array.getResourceId(R.styleable.PageIndicator_indicatorMargin, 0);
            margin = margin == 0 ? array.getDimensionPixelSize(R.styleable.PageIndicator_indicatorMargin, context.getResources().getDimensionPixelSize(R.dimen.page_indicator_margin)) : context.getResources().getDimensionPixelSize(margin);
            this.setIndicatorMargin(margin);

            this.drawableResId = array.getResourceId(R.styleable.PageIndicator_indicatorSrc, this.drawableResId);

            array.recycle();
        }
    }

    public void setIndicatorResource(final int resourceId) {
        this.drawableResId = resourceId;
    }

    /**
     * Sets the margin of each page indicator image.
     * @param margin The desired margin of each page indicator to set.
     */
    public void setIndicatorMargin(final int margin) {
        this.margin = margin;

        for (int i = 0; i < this.getChildCount(); i++) {
            this.getChildAt(i).setPadding(margin, margin, margin, margin);
        }
    }

    public void setTransitionTime(final int transitionTime) {
        this.transitionTime = transitionTime;
    }

    public void setPageCount(final int pageCount) {
        this.removeAllViews();

        this.drawables = new TransitionDrawable[pageCount];

        for (int i = 0; i < pageCount; i++) {
            final Drawable drawable = this.getContext().getResources().getDrawable(this.drawableResId);

            if (drawable instanceof TransitionDrawable) {
                this.drawables[i] = (TransitionDrawable)drawable;
            } else {
                this.drawables[i] = (TransitionDrawable)this.getContext().getResources().getDrawable(R.drawable.page_indicator);
            }

            final ImageView imageView = new ImageView(this.getContext());
            imageView.setImageDrawable(this.drawables[i]);
            imageView.setScaleType(ScaleType.CENTER_INSIDE);
            imageView.setPadding(this.margin, this.margin, this.margin, this.margin);

            this.addView(imageView, new LinearLayout.LayoutParams(this.getHeight(), this.getHeight(), 1));
        }

        this.setCurrentItem(0);
    }

    public void setCurrentItem(final int index) {
        if (index >= 0) {
            this.drawables[index].startTransition(this.transitionTime);
        }

        if (this.currentItem >= 0) {
            this.drawables[this.currentItem].reverseTransition(this.transitionTime);
        }

        this.currentItem = index;
    }

    @Override
    protected void onLayout(final boolean changed, final int l, final int t, final int r, final int b) {
        super.onLayout(changed, l, t, r, b);

        for (int i = 0; i < this.getChildCount(); i++) {
            this.getChildAt(i).setLayoutParams(new LinearLayout.LayoutParams(this.getHeight(), this.getHeight(), 1));
        }
    }
}
