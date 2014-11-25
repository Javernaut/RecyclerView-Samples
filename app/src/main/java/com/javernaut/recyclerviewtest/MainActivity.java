package com.javernaut.recyclerviewtest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemSelected;

import java.util.ArrayList;
import java.util.List;

/**
 * Main and the only activity in the app.
 */
public class MainActivity extends ActionBarActivity {

    private static final String COLORS = "key_colors";

    @InjectView(R.id.theToolbar)
    Toolbar theToolbar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    private MyAdapter myAdapter;
    private PeriodicLayoutManager layoutManager;
    private SingleLineDecorator lineDecorator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSupportActionBar(theToolbar);
        setTitle(null);

        recyclerView.setLayoutManager(layoutManager = new PeriodicLayoutManager(this));
        recyclerView.setAdapter(myAdapter = adapterBySavedInstanceState(savedInstanceState));
        recyclerView.setItemAnimator(new MyItemAnimator());
        recyclerView.addItemDecoration(new MyItemDecorator(this));
        recyclerView.addItemDecoration(lineDecorator = new SingleLineDecorator(this));
    }

    private MyAdapter adapterBySavedInstanceState(Bundle savedInstanceState) {
        return savedInstanceState == null ?
                new MyAdapter(this, makeData()) :
                MyAdapter.fromIntegers(this, savedInstanceState.getIntegerArrayList(COLORS));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(COLORS, myAdapter.toIntegers());
    }

    @SuppressWarnings("unused")
    @OnItemSelected(R.id.orientation)
    void onOrientationSelected(int position) {
        layoutManager.setOrientation(position == 0 ? OrientationHelper.VERTICAL : OrientationHelper.HORIZONTAL);
    }

    @SuppressWarnings("unused")
    @OnItemSelected(R.id.periodicFunc)
    void onPeriodicFuncSelected(int position) {
        layoutManager.setPeriodicFunc(position == 0 ? PeriodicLayoutManager.SIN : PeriodicLayoutManager.COS);
    }

    @SuppressWarnings("unused")
    @OnItemSelected(R.id.lineMode)
    void onLineModeSelected(int position) {
        lineDecorator.setDrawOver(position == 1);
        recyclerView.invalidateItemDecorations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove:
                myAdapter.remove10();
                return true;
            case R.id.add:
                myAdapter.addSeveral();
                return true;
            case R.id.toFirst:
                recyclerView.smoothScrollToPosition(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static List<MyItem> makeData() {
        List<MyItem> result = new ArrayList<MyItem>();
        for (int pos = 0; pos < 255; pos++) {
            result.add(new MyItem(Color.rgb(pos, pos, pos)));
        }
        return result;
    }
}
