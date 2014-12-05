package com.javernaut.recyclerviewtest.android.component;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import butterknife.InjectView;
import com.javernaut.recyclerviewtest.R;
import com.javernaut.recyclerviewtest.model.DragItem;
import com.javernaut.recyclerviewtest.rvstuff.adapter.DragAdapter;
import com.javernaut.recyclerviewtest.rvstuff.decoration.OffsetDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DragAndDropFragment extends BaseFragment {

    @InjectView(R.id.recycler)
    RecyclerView recyclerView;
    private DragAdapter dragAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                getResources().getInteger(R.integer.column_count),
                StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(dragAdapter = new DragAdapter(getActivity(), makeFakeData(100)));
        recyclerView.addItemDecoration(new OffsetDecoration());
        recyclerView.setOnDragListener(new MyDragListener(recyclerView, dragAdapter));
        recyclerView.setScrollingTouchSlop(RecyclerView.TOUCH_SLOP_PAGING);
    }

    @Override
    protected final int getLayoutResId() {
        return R.layout.fragment_drag;
    }

    @Override
    protected int getOptionMenuResId() {
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

        private MyDragListener(RecyclerView recyclerView, DragAdapter dragAdapter) {
            this.recyclerView = recyclerView;
            this.dragAdapter = dragAdapter;
            this.scroller = new Scroller(recyclerView);
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
                            long id = getChildId(event);
                            dragAdapter.swap(recyclerView.getChildPosition(child), id);
                        }
                    }

                    float y = event.getY();

                    if (y < 100) {
                        scroller.startScrollUp();
                    } else if (y > recyclerView.getHeight() - 100) {
                        scroller.startScrollDown();
                    } else {
                        scroller.stop();
                    }


                    return true;
                case DragEvent.ACTION_DROP:
                    scroller.stop();
                    dragAdapter.clearDragState(recyclerView);
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    scroller.stop();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    scroller.stop();
                    return true;
                default:
                    return false;
            }
        }

        private long getChildId(DragEvent event) {
            return (long) event.getLocalState();
        }
    }

    private static class Scroller {
        private static final int DELAY = 100;
        private RecyclerView recyclerView;
        private Handler handler = new Handler();

        private int scrollValue;
        boolean posted = false;

        private Scroller(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        void startScrollDown() {
            scrollValue = 50;
            rescheduleTask();
        }

        void startScrollUp() {
            scrollValue = -50;
            rescheduleTask();
        }

        private void rescheduleTask() {
            if (!posted) {
                posted = true;
                log("posting task");
                handler.postDelayed(scrollerTask, DELAY);
            }
        }

        void stop() {
            if (posted) {
                log("removing task");
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

    private static void log(String msg) {
        Log.e("SCROOOL", msg);
    }
}
