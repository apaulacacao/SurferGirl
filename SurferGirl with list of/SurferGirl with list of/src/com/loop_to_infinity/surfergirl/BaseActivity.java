package com.loop_to_infinity.surfergirl;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import Fragments.Settings;
import Fragments.Welcome;
import Utilities.Magic_Spots_ID;

public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = getActionBar();
        ab.setTitle("");
        setContentView(R.layout.activity_base);


        final FragmentManager fm = getSupportFragmentManager();
        Magic_Spots_ID.initPlaces();

        Welcome wl = new Welcome();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frame, wl);
        ft.commit();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:
                final FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame, new Settings());
                ft.commit();
                break;

        }

        return true;
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame, new Welcome());
        ft.commit();


    }


}
