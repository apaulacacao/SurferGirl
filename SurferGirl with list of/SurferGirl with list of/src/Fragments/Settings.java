package Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loop_to_infinity.surfergirl.R;

import java.text.DecimalFormat;

import Adapters.MyCustomSpinnerAdapter;
import Utilities.Magic_Spots_ID;
import services.ForecastService;

/**
 * A settings fragment to get input and changes from user defined values.
 */
public class Settings extends Fragment {

    private static Settings settingsInstance = null;
    private DecimalFormat df = new DecimalFormat("#.#");
    private Context ctx;
    private Typeface roboto;
    private int count = 0;
    private double wavesHeightGlobal = 0.0;
    private int spinnerPosition = 0;
    private int visibility = 0;
    private boolean buttonVisible = false;
    private boolean enableAlarms = false;
    private long wait = 0;
    private long wait2 = 120000;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view;
        view = inflater.inflate(R.layout.settings, container, false);
        super.onCreateView(inflater, container, savedInstanceState);


        ctx = getActivity();
        roboto = Typeface.createFromAsset(ctx.getAssets(), "fonts/rthis.ttf");
        loadSavedPreferences();

        // Defined views and controls


        final Button save = (Button) view.findViewById(R.id.save_settingsBTN);

        TextView setAlarms = (TextView) view.findViewById(R.id.setAlarms_Settings);
        setAlarms.setTypeface(roboto);

        final TextView wavesHeight = (TextView) view.findViewById(R.id.waves_Height_Settings_Label);
        wavesHeight.setTypeface(roboto);
        wavesHeight.setVisibility(visibility);

        final EditText wavesHeight_Edit = (EditText) view.findViewById(R.id.waves_Height_Settings_EditText);
        wavesHeight_Edit.setTypeface(roboto);
        wavesHeight_Edit.setVisibility(visibility);
        wavesHeight_Edit.setText("" + df.format(wavesHeightGlobal));
        wavesHeight_Edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wavesHeight_Edit.setTextColor(Color.WHITE);
                try {
                    wavesHeightGlobal = Double.valueOf(wavesHeight_Edit.getText().toString());


                } catch (NumberFormatException nf) {
                    wavesHeight_Edit.setTextColor(Color.RED);
                    wavesHeightGlobal = 0.0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Adapter for custom spinner

        final String[] cities = ctx.getResources().getStringArray(R.array.country_arrays);

        MyCustomSpinnerAdapter myCitiesAdapter = new MyCustomSpinnerAdapter(ctx, android.R.layout.simple_list_item_1, cities);

        final Spinner spinner = (Spinner) view.findViewById(R.id.settings_spinner);
        spinner.setAdapter(myCitiesAdapter);
        spinner.setSelection(spinnerPosition);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Save user data
                if (count > 0) {
                    visibility = View.VISIBLE;
                    wavesHeight.setVisibility(visibility);
                    wavesHeight_Edit.setVisibility(visibility);
                    spinnerPosition = spinner.getSelectedItemPosition();
                    save.setEnabled(true);

                }
                count++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final String[] intervalArr = ctx.getResources().getStringArray(R.array.interval_array);
        MyCustomSpinnerAdapter myIntervalAdapter = new MyCustomSpinnerAdapter(ctx, android.R.layout.simple_list_item_1, intervalArr);


        // Intervals spinner
        final Spinner interSpinner = (Spinner) view.findViewById(R.id.interval_spinner);
        interSpinner.setAdapter(myIntervalAdapter);
        interSpinner.setSelection(0); // Get selection from shared pref
        interSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wait2 = interSpinner.getSelectedItemPosition();
                Log.d("wait2", wait2 + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Set alarm enabled/disabled

        final CheckBox alarmBox = (CheckBox) view.findViewById(R.id.alarms_CheckBox);
        alarmBox.setTypeface(roboto);
        alarmBox.setChecked(enableAlarms);
        alarmBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!((CheckBox) v).isChecked()) {
                    Intent stopAlarm = new Intent(ctx, ForecastService.class);
                    PendingIntent sender = PendingIntent.getService(ctx, 10, stopAlarm, 0);
                    AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
                    am.cancel(sender);
                }
            }
        });


        // Once clicked save, all data should save to shared preferences

        save.setTypeface(roboto);
        save.setEnabled(buttonVisible);
        save.setTextColor(getResources().getColor(R.color.card_white));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                wavesHeightGlobal = Double.valueOf(wavesHeight_Edit.getText().toString());
                int position = spinner.getSelectedItemPosition();
                String url = Magic_Spots_ID.getCitySpots(position + 1);
                savePreferences("settings_wave_height", wavesHeightGlobal);
                savePreferences("spinnerPosition", position);
                savePreferences("visibility", visibility);
                savePreferences("url", url);
                savePreferences("buttonVisibility", save.isEnabled());
                savePreferences("alarmBOX", alarmBox.isChecked());

                Toast.makeText(ctx, "Saved", Toast.LENGTH_SHORT).show();

                // Start forecast service and set alarm manager
                if (alarmBox.isChecked()) {
                    AlarmManager am = (AlarmManager) ctx.getSystemService(ctx.ALARM_SERVICE);
                    int alarmType = am.RTC;

                    Intent forecastIntent = new Intent(ctx, ForecastService.class);
                    forecastIntent.putExtra("position2", position);
                    PendingIntent alarmIntent = PendingIntent.getService(ctx, 10, forecastIntent, 0);
                    am.setInexactRepeating(alarmType, wait, AlarmManager.INTERVAL_HALF_DAY, alarmIntent);
                }

                InputMethodManager im = (InputMethodManager) ctx.getSystemService(ctx.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(wavesHeight_Edit.getWindowToken(), 0);

            }
        });

        return view;
    }

    private void loadSavedPreferences() {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        wavesHeightGlobal = sharedPreferences.getFloat("settings_wave_height", (float) 0.0);
        spinnerPosition = sharedPreferences.getInt("spinnerPosition", 5);
        visibility = sharedPreferences.getInt("visibility", 8);
        buttonVisible = sharedPreferences.getBoolean("buttonVisibility", false);
        enableAlarms = sharedPreferences.getBoolean("alarmBOX", false);


    }

    private void savePreferences(String key, double value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, (float) value);
        editor.commit();
    }

    private void savePreferences(String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private void savePreferences(String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}

