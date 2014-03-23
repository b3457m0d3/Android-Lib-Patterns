package android.lib.patterns.form;

import android.content.Context;
import android.content.res.TypedArray;
import android.lib.patterns.R;
import android.lib.patterns.widget.FreezedViewPager;
import android.lib.patterns.widget.PageIndicator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class Wizard extends LinearLayout {
    private final FreezedViewPager pager;
    private final PageIndicator    indicator;

    private WizardAdapter adapter;

    public Wizard(final Context context) {
        this(context, null);
    }

    public Wizard(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Wizard(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        this.setOrientation(LinearLayout.VERTICAL);

        final View view = LayoutInflater.from(context).inflate(R.layout.wizard, this, true);

        this.pager     = (FreezedViewPager)view.findViewById(R.id.pager);
        this.indicator = (PageIndicator)view.findViewById(R.id.page_indicator);

        this.pager.setFreezed(true);

        if (attrs != null) {
            final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Wizard);

            int height = array.getResourceId(R.styleable.Wizard_pageIndicatorHeight, 0);
            height = height == 0 ? array.getDimensionPixelSize(R.styleable.Wizard_pageIndicatorHeight, context.getResources().getDimensionPixelSize(R.dimen.page_indicator_height)) : context.getResources().getDimensionPixelSize(height);

            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, height);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.weight  = 0;
            this.indicator.setLayoutParams(params);

            int padding = array.getResourceId(R.styleable.Wizard_pageIndicatorPadding, 0);
            padding = padding == 0 ? array.getDimensionPixelSize(R.styleable.Wizard_pageIndicatorPadding, context.getResources().getDimensionPixelSize(R.dimen.page_indicator_margin)) : context.getResources().getDimensionPixelSize(padding);
            this.indicator.setIndicatorMargin(padding);

            this.indicator.setIndicatorResource(array.getResourceId(R.styleable.Wizard_pageIndicatorSrc, R.drawable.page_indicator));

            array.recycle();
        }
    }

    public WizardAdapter getAdapter() {
        return this.adapter;
    }

    public void setAdapter(final WizardAdapter adapter) {
        this.pager.setOffscreenPageLimit(adapter.getCount() - 1);

        this.pager.setAdapter(adapter);
        this.indicator.setPageCount(adapter.getCount());

        this.adapter = adapter;
    }

    public PageIndicator getPageIndicator() {
        return this.indicator;
    }

    public int getCurrentItem() {
        return this.pager.getCurrentItem();
    }

    public void goBack() {
        if (this.pager.getCurrentItem() > 0) {
            this.pager.setCurrentItem(this.pager.getCurrentItem() - 1, true);

            this.indicator.setCurrentItem(this.pager.getCurrentItem());
        }
    }

    public void goNext() {
        if (this.adapter.canGoNext(this.pager.getCurrentItem())) {
            if (this.pager.getCurrentItem() < this.adapter.getCount() - 1) {
                this.pager.setCurrentItem(this.pager.getCurrentItem() + 1, true);

                this.indicator.setCurrentItem(this.pager.getCurrentItem());
            }
        }
    }
}
