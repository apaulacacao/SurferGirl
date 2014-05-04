package com.loop_to_infinity.surfergirl;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;

import Fragments.MainFragment;
import Fragments.Settings;
import Fragments.Welcome;
import Json.WeatherData;
import Utilities.Magic_Spots_ID;
import volley.RequestQueue;
import volley.Response;
import volley.VolleyError;
import volley.toolbox.JsonArrayRequest;
import volley.toolbox.Volley;

/**
 * Created by Unknown1 on 4/26/14.
 */
public class DaysToSurfActivity extends FragmentActivity {


    private Context ctx = this;
    private String chosenCity;
    private int spinnerPosition;
    private JsonArrayRequest jsArrayRequest;
    private RequestQueue rq;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);

        try {
            fm = getSupportFragmentManager();
            Magic_Spots_ID.initPlaces();

            loadSavedPreferences();


            // Set variables.
            rq = Volley.newRequestQueue(ctx);
            final String[] cities = ctx.getResources().getStringArray(R.array.country_arrays);
            chosenCity = cities[spinnerPosition];
        } catch (Exception ex) {
            System.out.print("" + ex.getMessage());
        }

    }


    @Override
    public void onResume() {
        super.onResume();

        // Get correct url
        String url = Magic_Spots_ID.getCitySpots(spinnerPosition + 1);

        final ProgressDialog progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage(chosenCity + " - " + ctx.getResources().getString(R.string.loadingMSG));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        // Get JSONArray from MagicSeeWeed
        jsArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        ArrayList<WeatherData> weatherDataArrayList = new ArrayList<WeatherData>();


                        // extract Json data and put in arraylist

                        if (response.length() > 0) {
                            for (int i = 0; i <= 38; i++) {
                                WeatherData wd = new WeatherData();
                                wd.parseJSON(response, i);
                                weatherDataArrayList.add(wd);

                            }

                            MainFragment mf = new MainFragment(chosenCity, weatherDataArrayList);
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.replace(R.id.frame, mf);
                            ft.commit();

                            progressDialog.dismiss();

                        } else
                            Toast.makeText(ctx, "Error", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Dismiss loading dialog
                progressDialog.dismiss();

                // Show error dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle(ctx.getResources().getString(R.string.error));
                builder.setMessage(ctx.getResources().getString(R.string.errorData));
                builder.setNegativeButton(ctx.getResources().getString(R.string.errorBtnOkay), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        }
        );

        rq.add(jsArrayRequest);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();

        jsArrayRequest = null;
        rq = null;
        fm = null;

    }

    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame, new Welcome());
        ft.commit();


    }


    private void loadSavedPreferences() {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        spinnerPosition = sharedPreferences.getInt("spinnerPosition", 0);

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

}
