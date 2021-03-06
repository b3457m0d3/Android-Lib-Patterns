package android.lib.patterns.nav;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

final class DashboardGridViewAdapter extends BaseAdapter {
    private final ListAdapter adapter;
    private final int         startIndex;
    private final int         count;

    public DashboardGridViewAdapter(final ListAdapter adapter, final int startIndex, final int count) {
        this.adapter    = adapter;
        this.startIndex = startIndex;
        this.count      = count;
    }

    @Override
    public int getCount() {
        final int count = this.adapter.getCount();

        if (count > this.startIndex) {
            return Math.min(count - this.startIndex, this.count);
        }

        return 0;
    }

    @Override
    public Object getItem(final int position) {
        if (this.getCount() > 0) {
            return this.adapter.getItem(position + this.startIndex);
        }

        return null;
    }

    @Override
    public long getItemId(final int position) {
        if (this.getCount() > 0) {
            return this.adapter.getItemId(position + this.startIndex);
        }

        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        if (this.getCount() > 0) {
            return this.adapter.getView(position + this.startIndex, convertView, parent);
        }

        return null;
    }
}
