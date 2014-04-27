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

    private DecimalFormat df = new DecimalFormat("#.#");
    private Context ctx;
    private Typeface roboto;
    private int count = 0;
    private double wavesHeightGlobal = 0.5;
    private int spinnerPosition = 0;
    private int visibility = View.VISIBLE;
    private boolean buttonVisible = false;
    private boolean enableAlarms = false;
    private int checkForUpdateInterval = 0;


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

        // Adapter for custom cities spinner

        final String[] cities = ctx.getResources().getStringArray(R.array.country_arrays);
        MyCustomSpinnerAdapter myCitiesAdapter = new MyCustomSpinnerAdapter(ctx, android.R.layout.simple_list_item_1, cities);

        final Spinner spinner = (Spinner) view.findViewById(R.id.settings_spinner);
        spinner.setAdapter(myCitiesAdapter);
        spinner.setVisibility(visibility);
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


        // Adapter for Interval selection

        final String[] intervalArr = ctx.getResources().getStringArray(R.array.interval_array);
        MyCustomSpinnerAdapter myIntervalAdapter = new MyCustomSpinnerAdapter(ctx, android.R.layout.simple_list_item_1, intervalArr);


        // Intervals spinner
        final Spinner interSpinner = (Spinner) view.findViewById(R.id.interval_spinner);
        interSpinner.setAdapter(myIntervalAdapter);
        interSpinner.setVisibility(visibility);
        interSpinner.setSelection(checkForUpdateInterval); // Get selection from shared pref
        interSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkForUpdateInterval = interSpinner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final TextView checkEvery = (TextView) view.findViewById(R.id.IntervalLabel);
        checkEvery.setTypeface(roboto);
        checkEvery.setVisibility(visibility);

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

                    visibility = View.GONE;
                    wavesHeight.setVisibility(visibility);
                    wavesHeight_Edit.setVisibility(visibility);
                    spinner.setVisibility(visibility);
                    checkEvery.setVisibility(visibility);
                    interSpinner.setVisibility(visibility);

                } else if (((CheckBox) v).isChecked()) {
                    visibility = View.VISIBLE;
                    wavesHeight.setVisibility(visibility);
                    wavesHeight_Edit.setVisibility(visibility);
                    spinner.setVisibility(visibility);
                    checkEvery.setVisibility(visibility);
                    interSpinner.setVisibility(visibility);

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
                savePreferences("updateInterval", interSpinner.getSelectedItemPosition());

                Toast.makeText(ctx, "Saved", Toast.LENGTH_SHORT).show();

                // Start forecast service and set alarm manager
                if (alarmBox.isChecked()) {
                    AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
                    int alarmType = AlarmManager.RTC;

                    Intent forecastIntent = new Intent(ctx, ForecastService.class);
                    forecastIntent.putExtra("position2", position);
                    PendingIntent alarmIntent = PendingIntent.getService(ctx, 10, forecastIntent, 0);
                    am.setInexactRepeating(alarmType, 0, getIntervalByPosition(checkForUpdateInterval), alarmIntent);
                }

                InputMethodManager im = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(wavesHeight_Edit.getWindowToken(), 0);

            }
        });

        return view;
    }

    private void loadSavedPreferences() {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        wavesHeightGlobal = sharedPreferences.getFloat("settings_wave_height", (float) 0.5);
        spinnerPosition = sharedPreferences.getInt("spinnerPosition", 0);
        visibility = sharedPreferences.getInt("visibility", 8);
        buttonVisible = sharedPreferences.getBoolean("buttonVisibility", false);
        enableAlarms = sharedPreferences.getBoolean("alarmBOX", false);
        checkForUpdateInterval = sharedPreferences.getInt("updateInterval", 0);


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

    private long getIntervalByPosition(int position) {

        long returnVal = 00;


        switch (position) {
            case 4:
                returnVal = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
                break;

            case 3:
                returnVal = AlarmManager.INTERVAL_HALF_HOUR;
                break;
            case 2:
                returnVal = AlarmManager.INTERVAL_HOUR;
                break;
            case 1:
                returnVal = AlarmManager.INTERVAL_HALF_DAY;
                break;
            case 0:
                returnVal = AlarmManager.INTERVAL_DAY;
                break;
        }


        return returnVal;
    }
}

