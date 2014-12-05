package com.javernaut.recyclerviewtest.android.component;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import com.javernaut.recyclerviewtest.R;
import com.javernaut.recyclerviewtest.model.DragItem;
import com.javernaut.recyclerviewtest.rvstuff.adapter.DragAdapter;
import com.javernaut.recyclerviewtest.rvstuff.decoration.OffsetDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fragment that displays elements, that can be moved by dragging.
 */
public class DragAndDropFragment extends BaseFragment {

    private DragAdapter dragAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                getResources().getInteger(R.integer.column_count),
                StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(dragAdapter = new DragAdapter(getActivity(), makeFakeData(100)));
        recyclerView.addItemDecoration(new OffsetDecoration(getActivity()));
        recyclerView.setOnDragListener(new MyDragListener(recyclerView, dragAdapter));
        recyclerView.setScrollingTouchSlop(RecyclerView.TOUCH_SLOP_PAGING);
    }

    @Override
    protected final int getOptionMenuResId() {
        return R.menu.drag;
    }

    private static List<DragItem> makeFakeData(int count) {
        List<DragItem> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(new DragItem(times("Text " + i, i % 5 + 1)));
        }
        return result;
    }

    private static String times(String s, int times) {
        String[] arr = new String[times];
        Arrays.fill(arr, s);
        return TextUtils.join("\n", arr);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shuffle:
                dragAdapter.shuffle();
                break;

            case R.id.action_15:
                dragAdapter.setData(makeFakeData(15));
                break;

            case R.id.action_100:
                dragAdapter.setData(makeFakeData(100));
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        recyclerView.scrollToPosition(0);
        return true;
    }

    private static class MyDragListener implements View.OnDragListener {
        private final Scroller scroller;
        private final RecyclerView recyclerView;
        private final DragAdapter dragAdapter;

        private final int dragMovingOffset;

        private MyDragListener(RecyclerView recyclerView, DragAdapter dragAdapter) {
            this.recyclerView = recyclerView;
            this.dragAdapter = dragAdapter;
            this.scroller = new Scroller(recyclerView);
            this.dragMovingOffset = recyclerView.getResources().getDimensionPixelSize(R.dimen.drag_moving_offset);
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    if (!recyclerView.getItemAnimator().isRunning()) {
                        View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
                        if (child != null) {
                            dragAdapter.swapWithDragged(recyclerView.getChildPosition(child));
                        }
                    }

                    if (event.getY() < dragMovingOffset) {
                        scroller.startScrollUp();
                    } else if (event.getY() > recyclerView.getHeight() - dragMovingOffset) {
                        scroller.startScrollDown();
                    } else {
                        scroller.stop();
                    }

                    return true;
                case DragEvent.ACTION_DROP:
                case DragEvent.ACTION_DRAG_ENDED:
                    finishDrag();
                    return true;
                default:
                    return false;
            }
        }

        private void finishDrag() {
            scroller.stop();
            dragAdapter.clearDragState(recyclerView);
        }
    }

    /**
     * Helper class for permanent scrolling to up or down.
     */
    private static class Scroller {
        private static final int DELAY = 100;
        private RecyclerView recyclerView;
        private Handler handler = new Handler();

        private final int SCROLL_PER_TICK;

        private int scrollValue;
        boolean posted = false;

        private Scroller(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            this.SCROLL_PER_TICK = recyclerView.getResources().getDimensionPixelSize(R.dimen.scroll_per_tick);
        }

        void startScrollDown() {
            scrollValue = SCROLL_PER_TICK;
            rescheduleTask();
        }

        void startScrollUp() {
            scrollValue = -SCROLL_PER_TICK;
            rescheduleTask();
        }

        private void rescheduleTask() {
            if (!posted) {
                posted = true;
                handler.postDelayed(scrollerTask, DELAY);
            }
        }

        void stop() {
            if (posted) {
                handler.removeCallbacks(scrollerTask);
                posted = false;
            }
        }

        private final Runnable scrollerTask = new Runnable() {
            @Override
            public void run() {
                recyclerView.smoothScrollBy(0, scrollValue);
                handler.postDelayed(this, DELAY);
            }
        };
    }
}
