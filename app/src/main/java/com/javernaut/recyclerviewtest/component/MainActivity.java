package com.javernaut.recyclerviewtest.component;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import butterknife.ButterKnife;
import com.javernaut.recyclerviewtest.R;

/**
 * Main and the only activity in the app.
 */
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentById(R.id.container) == null) {
            manager.beginTransaction().replace(R.id.container, new PeriodicFragment()).commit();
        }
    }
}
