package android.lib.patterns.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

final class DashboardAdapter extends BaseAdapter {
    private final Context context;
    private final int[]   icons = new int[14];

    public DashboardAdapter(final Context context) {
        this.context = context;

        this.icons[0]  = R.drawable.ic_1;
        this.icons[1]  = R.drawable.ic_2;
        this.icons[2]  = R.drawable.ic_3;
        this.icons[3]  = R.drawable.ic_4;
        this.icons[4]  = R.drawable.ic_5;
        this.icons[5]  = R.drawable.ic_6;
        this.icons[6]  = R.drawable.ic_7;
        this.icons[7]  = R.drawable.ic_8;
        this.icons[8]  = R.drawable.ic_9;
        this.icons[9]  = R.drawable.ic_10;
        this.icons[10] = R.drawable.ic_11;
        this.icons[11] = R.drawable.ic_12;
        this.icons[12] = R.drawable.ic_13;
        this.icons[13] = R.drawable.ic_14;
    }

    @Override
    public int getCount() {
        return this.icons.length;
    }

    @Override
    public Object getItem(final int position) {
        return Integer.valueOf(this.icons[position]);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final View       view;
        final ViewHolder holder;

        if (convertView == null) {
            view   = LayoutInflater.from(this.context).inflate(R.layout.dashboard_item, null);
            holder = new ViewHolder((ImageView)view.findViewById(R.id.icon), (TextView)view.findViewById(R.id.label));

            view.setTag(holder);
        } else {
            view   = convertView;
            holder = (ViewHolder)view.getTag();
        }

        holder.icon.setImageResource(this.icons[position]);
        holder.label.setText("Shortcut " + position); //$NON-NLS-1$

        return view;
    }

    private final class ViewHolder {
        public final ImageView icon;
        public final TextView  label;

        public ViewHolder(final ImageView icon, final TextView label) {
            this.icon  = icon;
            this.label = label;
        }
    }
}
