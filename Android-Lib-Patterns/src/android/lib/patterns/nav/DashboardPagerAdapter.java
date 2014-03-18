package android.lib.patterns.nav;

import android.content.Context;
import android.lib.patterns.R;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

final class DashboardPagerAdapter extends PagerAdapter {
    private final Context context;

    private int numColumns;
    private int numRows;

    private ListAdapter   adapter;
    private ListAdapter[] adapters;
    private GridView[]    gridViews;

    private GridView.OnItemClickListener     onItemClickListener;
    private GridView.OnItemLongClickListener onItemLongClickListener;

    public DashboardPagerAdapter(final Context context, final int numColumns, final int numRows) {
        this.context    = context;
        this.numColumns = numColumns;
        this.numRows    = numRows;
    }

    @Override
    public int getCount() {
        return this.adapters == null ? 0 : this.adapters.length;
    }

    @Override
    public int getItemPosition(final Object object) {
        if (this.gridViews.length > 0) {
            for (int i = 0; i < this.gridViews.length; i++) {
                if (this.gridViews[i] == (GridView)object) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        if (this.adapters == null || this.adapters.length == 0) {
            return null;
        }

        this.gridViews[position] = (GridView)LayoutInflater.from(this.context).inflate(R.layout.dashboard_gridview, null);

        this.gridViews[position].setNumColumns(this.numColumns);
        this.gridViews[position].setAdapter(this.adapters[position]);
        this.gridViews[position].setOnItemClickListener(this.onItemClickListener);
        this.gridViews[position].setOnItemLongClickListener(this.onItemLongClickListener);

        container.addView(this.gridViews[position]);

        return this.gridViews[position];
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        if (container != null) {
            container.removeView(this.gridViews[position]);
        }
    }

    protected void setAdapter(final ListAdapter adapter) {
        final int itemCount = this.numColumns * this.numRows;
        final int pageCount = (adapter.getCount() + itemCount - 1) / itemCount;

        this.adapters  = new ListAdapter[pageCount];
        this.gridViews = new GridView[pageCount];

        for (int i = 0; i < pageCount; i++) {
            this.adapters[i] = new DashboardGridViewAdapter(adapter, i * itemCount, itemCount);
        }

        this.adapter = adapter;

        this.setOnItemClickListener(this.onItemClickListener);
        this.setOnItemLongClickListener(this.onItemLongClickListener);
    }

    protected void setNumColumns(final int numColumns) {
        this.numColumns = numColumns;

        this.setAdapter(this.adapter);

        this.notifyDataSetChanged();
    }

    protected void setNumRows(final int numRows) {
        this.numRows = numRows;

        this.setAdapter(this.adapter);

        this.notifyDataSetChanged();
    }

    protected void setOnItemClickListener(final GridView.OnItemClickListener listener) {
        this.onItemClickListener = listener;

        if (this.adapter != null) {
            for (final GridView gridView : this.gridViews) {
                if (gridView != null) {
                    gridView.setOnItemClickListener(listener);
                }
            }
        }
    }

    protected void setOnItemLongClickListener(final GridView.OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;

        if (this.adapter != null) {
            for (final GridView gridView : this.gridViews) {
                if (gridView != null) {
                    gridView.setOnItemLongClickListener(listener);
                }
            }
        }
    }
}
