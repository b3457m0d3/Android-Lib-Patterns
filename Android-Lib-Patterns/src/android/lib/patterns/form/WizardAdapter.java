package android.lib.patterns.form;

import java.util.HashMap;
import java.util.Map;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public abstract class WizardAdapter extends PagerAdapter {
    private final Map<Integer, View>   pages  = new HashMap<Integer, View>();
    private final Map<Integer, Bundle> states = new HashMap<Integer, Bundle>();

    protected abstract void onRestorePageState(int position, View page, Bundle savedInstanceState);

    protected abstract void onSavePageState(int position, View page, Bundle outState);

    protected abstract View instantiatePage(ViewGroup container, int position);

    @SuppressWarnings({ "static-method", "unused" })
    protected void destroyPage(final ViewGroup container, final int position, final View page) {
        container.removeView(page);
    }

    protected abstract boolean canGoNext(int currentPosition, View page);

    public final boolean canGoNext(final int currentPosition) {
        return this.canGoNext(currentPosition, this.pages.get(Integer.valueOf(currentPosition)));
    }

    @Override
    public final CharSequence getPageTitle(final int position) {
        return null;
    }

    @Override
    public final float getPageWidth(final int position) {
        return super.getPageWidth(position);
    }

    @Override
    public final int getItemPosition(final Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public final boolean isViewFromObject(final View view, final Object object) {
        return this.pages.get(object) == view;
    }

    @Override
    public final Object instantiateItem(final ViewGroup container, final int position) {
        final Integer key  = Integer.valueOf(position);
        final View    page = this.instantiatePage(container, position);

        if (this.states.containsKey(key)) {
            this.onRestorePageState(position, page, this.states.get(key));
        } else {
            this.pages.put(key, page);
            this.states.put(key, new Bundle());
        }

        return key;
    }

    @Override
    public final Object instantiateItem(final View container, final int position) {
        return this.instantiateItem((ViewGroup)container, position);
    }

    @Override
    public final void destroyItem(final ViewGroup container, final int position, final Object object) {
        final Integer key = (Integer)object;
        final View    page = this.pages.get(key);

        if (page != null) {
            this.onSavePageState(position, page, this.states.get(Integer.valueOf(position)));

            this.destroyPage(container, position, page);
        }

        this.pages.remove(key);
    }

    @Override
    public final void destroyItem(final View container, final int position, final Object object) {
        this.destroyItem((ViewGroup)container, position, object);
    }

    @Override
    public final Parcelable saveState() {
        return null;
    }

    @Override
    public final void restoreState(final Parcelable state, final ClassLoader loader) {
    }

    @Override
    public final void startUpdate(final ViewGroup container) {
    }

    @Override
    public final void startUpdate(final View container) {
    }

    @Override
    public final void finishUpdate(final ViewGroup container) {
    }

    @Override
    public final void finishUpdate(final View container) {
    }

    @Override
    public final void setPrimaryItem(final ViewGroup container, final int position, final Object object) {
    }

    @Override
    public final void setPrimaryItem(final View container, final int position, final Object object) {
    }

    @Override
    public final void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public final void registerDataSetObserver(final DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @Override
    public final void unregisterDataSetObserver(final DataSetObserver observer) {
        super.unregisterDataSetObserver(observer);
    }
}
