package android.lib.patterns.list;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.lib.patterns.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class EndlessListView extends ListView {
    public abstract class EndlessAdapter extends BaseAdapter {
        public abstract boolean hasMoreItems();

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();

            if (EndlessListView.this.endlessView != null) {
                if (EndlessListView.this.endAnimation == null) {
                    EndlessListView.this.endlessView.setVisibility(View.GONE);
                } else {
                    EndlessListView.this.endlessView.startAnimation(EndlessListView.this.endAnimation);
                }
            }
        }
    }

    private View endlessView;

    private Animation startAnimation;
    private Animation endAnimation;

    private int minItemCount;

    private EndlessScrollListener endlessScrollListener;
    private OnScrollListener      onScrollListener;

    public EndlessListView(final Context context) {
        this(context, null);
    }

    public EndlessListView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EndlessListView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        if (attrs == null) {
            this.minItemCount = context.getResources().getInteger(R.integer.endless_min_item_count);
        } else {
            final TypedArray array         = context.obtainStyledAttributes(attrs, R.styleable.EndlessListView);
            final int        endlessViewId = array.getResourceId(R.styleable.EndlessListView_endlessView, 0);

            if (array.hasValue(R.styleable.EndlessListView_minItemCount)) {
                final int minItemCountId = array.getResourceId(R.styleable.EndlessListView_minItemCount, 0);

                if (minItemCountId == 0) {
                    this.minItemCount = context.getResources().getInteger(minItemCountId);
                } else {
                    this.minItemCount = context.getResources().getInteger(minItemCountId);
                }
            } else {
                this.minItemCount = context.getResources().getInteger(R.integer.endless_min_item_count);
            }

            if (context instanceof Activity) {
                this.setEndlessView(endlessViewId == 0 ? null : ((Activity)context).findViewById(endlessViewId));
            } else {
                throw new IllegalArgumentException("context must be an Activity of a FragmentActivity"); //$NON-NLS-1$
            }

            final int startAnimationId = array.getResourceId(R.styleable.EndlessListView_startAnimation, 0);
            this.setStartAnimation(startAnimationId == 0 ? null : AnimationUtils.loadAnimation(context, startAnimationId));

            final int endAnimationId = array.getResourceId(R.styleable.EndlessListView_startAnimation, 0);
            this.setEndAnimation(endAnimationId == 0 ? null : AnimationUtils.loadAnimation(context, endAnimationId));

            array.recycle();
        }
    }

    public void setMinItemCount(final int minItemCount) {
        this.minItemCount = minItemCount;

        if (this.endlessScrollListener != null) {
            this.endlessScrollListener.setMinItemCount(minItemCount);
        }
    }

    public void setEndlessView(final View endlessView) {
        this.endlessView = endlessView;
        this.endlessView.setVisibility(View.GONE);
    }

    public void setStartAnimation(final Animation animation) {
        this.startAnimation = animation;
    }

    public void setEndAnimation(final Animation animation) {
        this.endAnimation = animation;

        if (this.endAnimation != null) {
            this.endAnimation.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(final Animation animation) {
                }

                @Override
                public void onAnimationEnd(final Animation animation) {
                    if (EndlessListView.this.endlessView != null) {
                        EndlessListView.this.endlessView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(final Animation animation) {
                }
            });
        }
    }

    public void setAdapter(final EndlessAdapter adapter) {
        this.endlessScrollListener = new EndlessScrollListener(adapter, this.minItemCount);

        if (this.onScrollListener != null) {
            this.endlessScrollListener.setMinItemCount(this.minItemCount);
            this.endlessScrollListener.setOnScrollListener(this.onScrollListener);
        }

        super.setAdapter(adapter);
    }

    @Override
    public void setOnScrollListener(final OnScrollListener listener) {
        this.onScrollListener = listener;

        if (this.endlessScrollListener != null) {
            this.endlessScrollListener.setOnScrollListener(listener);
        }
    }

    private final class EndlessScrollListener implements OnScrollListener {
        private final EndlessAdapter adapter;

        private int     minItemCount;
        private int     totalItemCount;
        private boolean loading      = true;
        private boolean hasMoreItems = true;

        private OnScrollListener listener;

        protected EndlessScrollListener(final EndlessAdapter adapter, final int minItemCount) {
            this.adapter      = adapter;
            this.minItemCount = minItemCount;
        }

        public void setMinItemCount(final int minItemCount) {
            this.minItemCount = minItemCount;
        }

        public void setOnScrollListener(final OnScrollListener listener) {
            this.listener = listener;
        }

        @Override
        public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
            if (this.loading) {
                if (totalItemCount > this.totalItemCount) {
                    this.loading        = false;
                    this.totalItemCount = totalItemCount;
                }
            }

            if (!this.loading && totalItemCount - visibleItemCount <= firstVisibleItem + this.minItemCount) {
                this.loading = true;

                if (this.hasMoreItems) {
                    if (this.adapter.hasMoreItems()) {
                        if (EndlessListView.this.endlessView != null) {
                            if (EndlessListView.this.startAnimation != null) {
                                EndlessListView.this.endlessView.startAnimation(EndlessListView.this.startAnimation);
                            }

                            EndlessListView.this.endlessView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        this.hasMoreItems = false;
                    }
                }
            }

            if (this.listener != null) {
                this.listener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }

        @Override
        public void onScrollStateChanged(final AbsListView view, final int viewState) {
            if (this.listener != null) {
                this.listener.onScrollStateChanged(view, viewState);
            }
        }
    }
}
