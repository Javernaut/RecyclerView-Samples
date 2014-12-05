package com.javernaut.recyclerviewtest.android.component;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import com.javernaut.recyclerviewtest.R;

/**
 * Main and the only activity in the app.
 */
public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.theDrawer)
    DrawerLayout theDrawer;
    @InjectView(R.id.drawerList)
    ListView drawerList;

    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        drawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice,
                new String[]{"Periodic LayoutManager", "Drag And Drop"}));
        if (savedInstanceState == null) {
            drawerList.setItemChecked(0, true);
        }
        theDrawer.setDrawerListener(mDrawerToggle = makeActionBarDrawerToggle(theDrawer, this));

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentById(R.id.container) == null) {
            manager.beginTransaction().replace(R.id.container, new PeriodicFragment()).commit();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(0, !theDrawer.isDrawerOpen(drawerList));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private static ActionBarDrawerToggle makeActionBarDrawerToggle(DrawerLayout drawerLayout,
                                                                   final FragmentActivity activity) {
        return new ActionBarDrawerToggle(activity, drawerLayout, 0, 0) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                activity.supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                activity.supportInvalidateOptionsMenu();
            }
        };
    }

    @SuppressWarnings("unused")
    @OnItemClick(R.id.drawerList)
    void onDrawerListItemClicked(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new PeriodicFragment();
                break;
            case 1:
                fragment = new DragAndDropFragment();
                break;
            default:
                fragment = null;
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }

        theDrawer.closeDrawer(drawerList);
    }
}
