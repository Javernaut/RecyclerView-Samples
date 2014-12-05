package com.javernaut.recyclerviewtest.component;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import butterknife.InjectView;
import com.javernaut.recyclerviewtest.R;
import com.javernaut.recyclerviewtest.stuff.*;

import java.util.ArrayList;
import java.util.List;

public class PeriodicFragment extends ButterKnifeFragment {

    private static final String KEY_COLORS = "key_colors";
    private static final String KEY_DIVIDERS = "key_dividers";

    private static RecyclerView.ItemDecoration dividerDecorator;
    private boolean useDividers = false;

    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    private MyAdapter myAdapter;
    private PeriodicLayoutManager layoutManager;
    private SingleLineDecorator lineDecorator;

    @Override
    protected final int getLayoutResId() {
        return R.layout.fragment_periodic;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(layoutManager = new PeriodicLayoutManager(getActivity()));
        recyclerView.setAdapter(myAdapter = adapterBySavedState(savedInstanceState));

        recyclerView.setItemAnimator(new MyItemAnimator());
        recyclerView.addItemDecoration(lineDecorator = new SingleLineDecorator(getActivity()));

        setDividerVisibility(useDividersBySavedState(savedInstanceState));
    }

    private MyAdapter adapterBySavedState(Bundle savedInstanceState) {
        return savedInstanceState == null ?
                new MyAdapter(getActivity(), makeData()) :
                MyAdapter.fromIntegers(getActivity(), savedInstanceState.getIntegerArrayList(KEY_COLORS));
    }

    private static boolean useDividersBySavedState(Bundle savedInstanceState) {
        return savedInstanceState != null && savedInstanceState.getInt(KEY_DIVIDERS, 0) == 1;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(KEY_COLORS, myAdapter.toIntegers());
        outState.putInt(KEY_DIVIDERS, useDividers ? 1 : 0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.periodic, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.useDividers).setChecked(useDividers);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.vertical:
                setLayoutDirection(OrientationHelper.VERTICAL);
                return true;
            case R.id.horizontal:
                setLayoutDirection(OrientationHelper.HORIZONTAL);
                return true;

            case R.id.sin:
                setPeriodicFunc(PeriodicLayoutManager.SIN);
                return true;
            case R.id.cos:
                setPeriodicFunc(PeriodicLayoutManager.COS);
                return true;

            case R.id.over:
                setLineMode(true);
                return true;
            case R.id.behind:
                setLineMode(false);
                return true;

            case R.id.remove:
                myAdapter.remove10();
                return true;
            case R.id.add:
                myAdapter.addSeveral();
                return true;

            case R.id.toFirst:
                recyclerView.smoothScrollToPosition(0);
                return true;

            case R.id.useDividers:
                item.setChecked(!item.isChecked());
                setDividerVisibility(item.isChecked());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setLayoutDirection(int direction) {
        layoutManager.setOrientation(direction);
    }

    private void setPeriodicFunc(PeriodicLayoutManager.PeriodicFunc periodicFunc) {
        layoutManager.setPeriodicFunc(periodicFunc);
    }

    private void setLineMode(boolean over) {
        lineDecorator.setDrawOver(over);
        recyclerView.invalidateItemDecorations();
    }

    private void setDividerVisibility(boolean visible) {
        RecyclerView.ItemDecoration decorator = getDividerDecorator(getActivity());
        if (visible) {
            recyclerView.addItemDecoration(decorator);
        } else {
            recyclerView.removeItemDecoration(decorator);
        }
        useDividers = visible;
        recyclerView.invalidate();
    }

    private static List<MyItem> makeData() {
        List<MyItem> result = new ArrayList<>();
        for (int pos = 0; pos < 255; pos++) {
            result.add(new MyItem(Color.rgb(pos, pos, pos)));
        }
        return result;
    }

    private static RecyclerView.ItemDecoration getDividerDecorator(Context context) {
        if (dividerDecorator == null) {
            dividerDecorator = new DividerDecorator(context);
        }
        return dividerDecorator;
    }
}
