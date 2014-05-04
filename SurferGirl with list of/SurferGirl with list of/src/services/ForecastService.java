package services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.loop_to_infinity.surfergirl.DaysToSurfActivity;
import com.loop_to_infinity.surfergirl.R;

import org.json.JSONArray;

import java.util.ArrayList;

import Json.WeatherData;
import volley.RequestQueue;
import volley.Response;
import volley.VolleyError;
import volley.toolbox.JsonArrayRequest;
import volley.toolbox.Volley;

/**
 * Created by Unknown1 on 4/7/14.
 * <p/>
 * A service class for finding matching waves height in upcoming days
 */
public class ForecastService extends Service {

    private ArrayList<WeatherData> dataAlarm;
    private ArrayList<WeatherData> daysWithWavesMatch;


    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {


        /*** Not Good!!! What if the application gets killed while the service is started? It wont know
         what shared preferences to take the position from. FIx it!!!
         ***/
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        int spinnerPosition = sharedPreferences.getInt("spinnerPosition", 5);
        Log.d("POSITION", spinnerPosition + "");
        request(getBaseContext());
        Log.d("SERVICE", "STARTED");
        stopSelf();
        return Service.START_NOT_STICKY;
    }

    public void request(Context context) {

        final Context ctx = context;
        RequestQueue rq = Volley.newRequestQueue(ctx);
        dataAlarm = new ArrayList<WeatherData>();
        daysWithWavesMatch = new ArrayList<WeatherData>();


        // Get correct url
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        String url = sharedPreferences.getString("url", "");


        // Get JSONArray from MagicSeeWeed
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        // extract Json data and put in arraylist

                        SharedPreferences sharedPreferences = PreferenceManager
                                .getDefaultSharedPreferences(ctx);

                        // Do your thing, only if we got correct JSONArray response. "0" is not a correct response.
                        if (response.length() > 0) {
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
                            if (daysWithWavesMatch.size() > 0) {
                                String day;
                                ArrayList<WeatherData> distinctDays = new ArrayList<WeatherData>();
                                day = daysWithWavesMatch.get(0).getDayString();
                                distinctDays.add(daysWithWavesMatch.get(0));


                                for (int j = 0; j < daysWithWavesMatch.size(); j++) {
                                    if (!day.equals(daysWithWavesMatch.get(j).getDayString())) {
                                        day = daysWithWavesMatch.get(j).getDayString();
                                        distinctDays.add(daysWithWavesMatch.get(j));

                                    }

                                }

                                Log.d("SIZE", daysWithWavesMatch.size() + "");

                                if (distinctDays.size() > 0) {
                                    updateDB(distinctDays);
                                }


                                NotificationManager nm = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);
                                int icon = R.drawable.ic_launcher;
                                String tickerText = distinctDays.size() + " Good days to surf!";
                                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx).setSmallIcon(icon).setContentTitle(tickerText).setAutoCancel(true).setContentText(tickerText).setSound(sound);

                                Intent openMainActivity = new Intent(getApplicationContext(), DaysToSurfActivity.class);

                                PendingIntent pi = PendingIntent.getActivity(ctx, 5, openMainActivity, 0);
                                mBuilder.setContentIntent(pi);
                                nm.notify(0, mBuilder.build());

                            }
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("OnResponseError", error.getMessage() + "");

                /***            // Show error dialog
                 AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                 builder.setTitle(ctx.getResources().getString(R.string.error));
                 builder.setMessage(ctx.getResources().getString(R.string.errorData));
                 builder.setNegativeButton(ctx.getResources().getString(R.string.errorBtnOkay), new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                }
                });
                 builder.show();
                 ***/
            }
        }
        );

        rq.add(jsArrayRequest);


    }


    private void updateDB(ArrayList<WeatherData> distinctDays) {
        // insert downloaded weatherData objects into DB for later retrieval
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("SERVICE", "KILLED");


    }
}
