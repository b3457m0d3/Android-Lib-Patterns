package android.lib.patterns.nav;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.lib.patterns.R;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

public class Dashboard extends LinearLayout {
    private final ViewPager               pager;
    private final DashboardPagerAdapter pagerAdapter;

    private final ViewGroup indicatorGroup;

    private final int transitionTime;

    private int height;
    private int padding;

    private int                  drawableResId;
    private TransitionDrawable[] drawables;

    private int currentItem;

    private int numColumns;
    private int numRows;

    private ListAdapter adapter;

    /**
     * Creates a new {@link Dashboard}.
     * @param context An instance of {@link FragmentActivity}.
     * @throws ClassCastException if the given <code>context</code> is not an instance of
     * {@link FragmentActivity}.
     */
    public Dashboard(final Context context) {
        this(context, null);
    }

    /**
     * Creates a new {@link Dashboard}.
     * @param context An instance of {@link FragmentActivity}.
     * @param attrs
     * @throws ClassCastException if the given <code>context</code> is not an instance of
     * {@link FragmentActivity}.
     */
    public Dashboard(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Creates a new {@link Dashboard}.
     * @param context An instance of {@link FragmentActivity}.
     * @param attrs
     * @param defStyle
     * @throws ClassCastException if the given <code>context</code> is not an instance of
     * {@link FragmentActivity}.
     */
    public Dashboard(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater.from(context).inflate(R.layout.dashboard, this, true);

        this.pager          = (ViewPager)this.findViewById(R.id.dashboard_pager);
        this.indicatorGroup = (ViewGroup)this.findViewById(R.id.dashboard_page_indicator);
        this.numColumns     = context.getResources().getInteger(R.integer.dashboard_num_columns);
        this.numRows        = context.getResources().getInteger(R.integer.dashboard_num_rows);
        this.transitionTime = context.getResources().getInteger(R.integer.dashboard_page_indicator_transition);

        if (attrs != null) {
            final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Dashboard);

            this.numColumns = array.getResourceId(R.styleable.Dashboard_numColumns, 0);
            if (this.numColumns == 0) {
                this.numColumns = array.getInteger(R.styleable.Dashboard_numColumns, this.numColumns);
            }

            this.numRows = array.getResourceId(R.styleable.Dashboard_numRows, 0);
            if (this.numRows == 0) {
                this.numRows = array.getInteger(R.styleable.Dashboard_numRows, this.numRows);
            }

            int height = array.getResourceId(R.styleable.Dashboard_pageIndicatorHeight, 0);
            if (height == 0) {
                height = array.getDimensionPixelSize(R.styleable.Dashboard_pageIndicatorHeight, context.getResources().getDimensionPixelSize(R.dimen.dashboard_page_indicator_height));
            }
            this.setPageIndicatorHeight(height);

            int padding = array.getResourceId(R.styleable.Dashboard_pageIndicatorPadding, 0);
            if (padding == 0) {
                padding = array.getDimensionPixelSize(R.styleable.Dashboard_pageIndicatorPadding, context.getResources().getDimensionPixelSize(R.dimen.dashboard_page_indicator_padding));
            }
            this.setPageIndicatorPadding(padding);

            this.drawableResId = array.getResourceId(R.styleable.Dashboard_pageIndicatorSrc, 0);

            array.recycle();
        }

        this.pagerAdapter = new DashboardPagerAdapter(context, this.numColumns, this.numRows);

        this.setOnPageChangeListener(null);
    }

    /**
     * Sets the adapter that provides the data and the views to represents the data in this widget.
     * @param adapter The adapter to use to create this view's content.
     */
    public void setAdapter(final ListAdapter adapter) {
        this.pagerAdapter.setAdapter(adapter);

        this.pager.setAdapter(this.pagerAdapter);

        this.pagerAdapter.notifyDataSetChanged();

        this.adapter = adapter;

        this.createPageIndicators();
    }

    /**
     * Sets the number of columns in the {@link Dashboard}.
     * @param numColumns The desired number of columns.
     */
    public void setNumColumns(final int numColumns) {
        this.pagerAdapter.setNumColumns(numColumns);

        this.numColumns = numColumns;

        this.createPageIndicators();
    }

    /**
     * Sets the number of rows in the {@link Dashboard}.
     * @param numRows The desired number of rows.
     */
    public void setNumRows(final int numRows) {
        this.pagerAdapter.setNumRows(numRows);

        this.numRows = numRows;

        this.createPageIndicators();
    }

    /**
     * Sets the currently selected page.
     * @param item Page index to select.
     */
    public void setCurrentItem(final int item) {
        this.pager.setCurrentItem(item);

        this.selectPageIndicator(this.currentItem, item, 1);

        this.currentItem = item;
    }

    /**
     * Sets the currently selected page.
     * @param item Page index to select.
     * @param smoothScroll <code>true</code> to smoothly scroll to the new page; <code>false</code>
     * to transition immediately.
     */
    public void setCurrentItem(final int item, final boolean smoothScroll) {
        this.pager.setCurrentItem(item, smoothScroll);

        this.selectPageIndicator(this.currentItem, item, this.transitionTime);

        this.currentItem = item;
    }

    /**
     * Sets the height of each page indicator area at the bottom of the {@link Dashboard}.
     * @param height The desired height of each page indicator area to set.
     */
    public void setPageIndicatorHeight(final int height) {
        this.height = height;

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, this.height, 0);
        params.gravity = Gravity.CENTER_HORIZONTAL;

        this.indicatorGroup.setLayoutParams(params);
    }

    /**
     * Sets the padding of each page indicator image.
     * @param padding The desired padding of each page indicator to set.
     */
    public void setPageIndicatorPadding(final int padding) {
        this.padding = padding;

        for (int i = 0; i < this.indicatorGroup.getChildCount(); i++) {
            this.indicatorGroup.getChildAt(i).setPadding(this.padding, this.padding, this.padding, this.padding);
        }
    }

    /**
     * Registers a callback to be invoked when an item in this {@link Dashboard} has been clicked.
     * @param listener The callback that will be invoked.
     * @see GridView#OnItemClickListener
     */
    public void setOnItemClickListener(final GridView.OnItemClickListener listener) {
        this.pagerAdapter.setOnItemClickListener(new OnItemClickListener(listener));
    }

    /**
     * Registers a callback to be invoked when an item in this {@link Dashboard} has been clicked and held.
     * @param listener The callback that will be invoked.
     * @see GridView#OnItemLongClickListener
     */
    public void setOnItemLongClickListener(final GridView.OnItemLongClickListener listener) {
        this.pagerAdapter.setOnItemLongClickListener(new OnItemLongClickListener(listener));
    }

    /**
     * Registers a callback to be invoked when the page changes or is incrementally scrolled.
     * @param listener The callback that will be invoked.
     * @see ViewPager#OnPageChangeListener
     */
    public void setOnPageChangeListener(final ViewPager.OnPageChangeListener listener) {
        this.pager.setOnPageChangeListener(new OnPageChangeListener(listener));
    }

    /**
     * Sets a {@link ViewPager#PageTransformer} that will be called for each attached page when the
     * scroll position is changed. This allows the application to apply custom property
     * transformations to each page, overriding the default sliding look and feel.
     * @param reverseDrawingOrder <code>true</code> if the supplied
     * {@link ViewPager#PageTransformer} requires page views to be drawn from last to first instead
     * of first to last.
     * @param transformer {@link ViewPager#PageTransformer} that will modify each page's animation
     * properties.
     */
    public void setPageTransformer(final boolean reverseDrawingOrder, final ViewPager.PageTransformer transformer) {
        this.pager.setPageTransformer(reverseDrawingOrder, transformer);
    }

    private void createPageIndicators() {
        this.indicatorGroup.removeAllViews();

        final int pageCount = (this.adapter.getCount() + this.numColumns * this.numRows - 1) / (this.numColumns * this.numRows);

        this.drawables = new TransitionDrawable[pageCount];

        for (int i = 0; i < pageCount; i++) {
            final Drawable drawable = this.getContext().getResources().getDrawable(this.drawableResId);

            if (drawable instanceof TransitionDrawable) {
                this.drawables[i] = (TransitionDrawable)drawable;
            } else {
                this.drawables[i] = (TransitionDrawable)this.getContext().getResources().getDrawable(R.drawable.dashboard_page_indicator);
            }

            final ImageView imageView = new ImageView(this.getContext());
            imageView.setImageDrawable(this.drawables[i]);
            imageView.setScaleType(ScaleType.CENTER_INSIDE);
            imageView.setPadding(this.padding, this.padding, this.padding, this.padding);

            this.indicatorGroup.addView(imageView, new LinearLayout.LayoutParams(this.height, ViewGroup.LayoutParams.FILL_PARENT, 1));
        }

        this.selectPageIndicator(0, -1, this.transitionTime);
    }

    private void selectPageIndicator(final int oldIndex, final int newIndex, final int transitionTime) {
        if (newIndex >= 0) {
            this.drawables[newIndex].startTransition(transitionTime);
        }

        if (oldIndex >= 0) {
            this.drawables[oldIndex].reverseTransition(transitionTime);
        }
    }

    private final class OnItemClickListener implements GridView.OnItemClickListener {
        private final GridView.OnItemClickListener listener;

        public OnItemClickListener(final GridView.OnItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
            if (this.listener != null) {
                this.listener.onItemClick(parent, view, Dashboard.this.pagerAdapter.getItemPosition(view) * Dashboard.this.numColumns * Dashboard.this.numRows + position, id);
            }
        }
    }

    private final class OnItemLongClickListener implements GridView.OnItemLongClickListener {
        private final GridView.OnItemLongClickListener listener;

        public OnItemLongClickListener(final GridView.OnItemLongClickListener listener) {
            this.listener = listener;
        }

        @Override
        public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, final long id) {
            if (this.listener != null) {
                return this.listener.onItemLongClick(parent, view, Dashboard.this.pagerAdapter.getItemPosition(view) * Dashboard.this.numColumns * Dashboard.this.numRows + position, id);
            }

            return false;
        }
    }

    private final class OnPageChangeListener implements ViewPager.OnPageChangeListener {
        private final ViewPager.OnPageChangeListener listener;

        public OnPageChangeListener(final ViewPager.OnPageChangeListener listener) {
            this.listener = listener;
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            if (this.listener != null) {
                this.listener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
            if (this.listener != null) {
                this.listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(final int position) {
            Dashboard.this.selectPageIndicator(Dashboard.this.currentItem, position, Dashboard.this.transitionTime);

            Dashboard.this.currentItem = position;

            if (this.listener != null) {
                this.listener.onPageSelected(position);
            }
        }
    }
}
