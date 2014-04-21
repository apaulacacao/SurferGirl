package Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.loop_to_infinity.surfergirl.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import Json.WeatherData;
import Utilities.Magic_Spots_ID;
import volley.RequestQueue;
import volley.Response;
import volley.VolleyError;
import volley.toolbox.JsonArrayRequest;
import volley.toolbox.Volley;

/**
 * Created by Unknown1 on 2/2/14.
 * A Class to parse JSON and get waves height for the upcoming days
 * Once a match of waves height was found (Compared to what was set on settings fragment),
 * alert the user about the day that will have these waves height.
 */
public class ForecastGetter {

    public ArrayList<WeatherData> dataAlarm;
    public ArrayList<WeatherData> daysWithWavesMatch = new ArrayList<WeatherData>();

    public  ForecastGetter() {

    }

    public void request(int position, Context context) {

        final Context ctx = context;
         RequestQueue rq = Volley.newRequestQueue(ctx);
        dataAlarm = new ArrayList<WeatherData>();

        // Get correct url
       String url = Magic_Spots_ID.getCitySpots(position + 1);
//        Toast.makeText(ctx,url,Toast.LENGTH_SHORT).show();


        // Get JSONArray from MagicSeeWeed
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        // extract Json data and put in arraylist

                        SharedPreferences sharedPreferences = PreferenceManager
                                .getDefaultSharedPreferences(ctx);


                        double minimumFromSettings = sharedPreferences.getFloat("settings_wave_height", (float) 0.0);
                        minimumFromSettings = (double) Math.round(minimumFromSettings * 10) / 10;


                        for (int i = 0; i <= 38; i++) {
                            WeatherData wd = new WeatherData();
                            wd.parseJSON(response, i);
                            dataAlarm.add(wd);

                        }

                        // Find if one of the entries fits the minimum waves height the user set for alarms

                        for (int i = 0; i <= 38; i++) {


                            Double minimumDoubleFromJSON = 0.0;
                            String minimum = dataAlarm.get(i).getRawMinimumWavesHeight();

                            try {
                                minimumDoubleFromJSON = Double.parseDouble(minimum);

                            } catch (NumberFormatException nf) {
                                nf.printStackTrace();
                            } catch (NullPointerException np) {
                                np.printStackTrace();
                            }
                            if (minimumDoubleFromJSON >= minimumFromSettings) {

                                // add entry to an array of objects with matching minimum height
                                daysWithWavesMatch.add(dataAlarm.get(i));

                            }

                        }


                        // Find distinct days out of matching objects
                        if(daysWithWavesMatch.size()>0) {
                            String day;
                            ArrayList<WeatherData> distinctDays = new ArrayList<WeatherData>();
                            day = daysWithWavesMatch.get(0).getDayString();
                            distinctDays.add(daysWithWavesMatch.get(0));

                            for (int j = 0; j < daysWithWavesMatch.size(); j++) {
                                if (day != daysWithWavesMatch.get(j).getDayString()) {
                                    day = daysWithWavesMatch.get(j).getDayString();
                                    distinctDays.add(daysWithWavesMatch.get(j));

                                }

                            }

                        }




                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


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


}




