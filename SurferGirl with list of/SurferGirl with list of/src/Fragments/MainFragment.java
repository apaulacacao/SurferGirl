package Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.loop_to_infinity.surfergirl.R;

import java.util.ArrayList;
import java.util.List;

import Adapters.Cities_Adapters;
import Interfaces.item;
import Json.WeatherData;

/**
 * Created by Unknown1 on 8/18/13.
 */
public class MainFragment extends android.support.v4.app.Fragment {

    private String city;
    private ArrayList<WeatherData> weatherDataObj;
    private Typeface roboto;
    private String TAG = "MainFragment";


    public MainFragment(String cityLabel, ArrayList<WeatherData> weather) {
        this.city = cityLabel;
        this.weatherDataObj = weather;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.summary_list_frag, container,
                false);
        super.onCreateView(inflater, container, savedInstanceState);

        // Set variables...
        Context ctx = getActivity();
        final Cities_Adapters ca;
        roboto = Typeface.createFromAsset(ctx.getAssets(), "fonts/rthis.ttf");

        // Add UI
        TextView cityLabelView = (TextView) view.findViewById(R.id.cityLabelLayoutTextView);
        cityLabelView.setText(city);
        cityLabelView.setTypeface(roboto);

        // ArrayList for days displayed
        ArrayList<String> days = new ArrayList<String>();


        days.add(weatherDataObj.get(0).getDayString());
        days.add(weatherDataObj.get(8).getDayString());
        days.add(weatherDataObj.get(16).getDayString());
        days.add(weatherDataObj.get(24).getDayString());
        days.add(weatherDataObj.get(32).getDayString());

        int daysCount = 0;

        // Array for Israel Cities and beaches
        List<item> items = new ArrayList<item>();

        // If correct data was parsed in WeatherData object, start populating list
        if (days.size() != 0) {
            for (int i = 0; i < 39; i++) {
                if (i % 8 == 0) {

                    items.add(new Header(ctx, inflater, days.get(daysCount)));

                    items.add(new List_Item(ctx, inflater, weatherDataObj.get(i).getWavesHeight(), weatherDataObj.get(i).getWindSpeed(), weatherDataObj.get(i).getCurrentDayTimeStamp(), weatherDataObj.get(i).getRawMinimumWavesHeight()));
                    if (daysCount < 5) {
                        daysCount++;
                    }

                } else if (i != 8) {
                    items.add(new List_Item(ctx, inflater, weatherDataObj.get(i).getWavesHeight(), weatherDataObj.get(i).getWindSpeed(), weatherDataObj.get(i).getCurrentDayTimeStamp(), weatherDataObj.get(i).getRawMinimumWavesHeight()));
                }


            }

            ca = new Cities_Adapters(ctx,
                    items);

            ListView lv = (ListView) view.findViewById(R.id.list);
            lv.setAdapter(ca);

        } else if (days.size() == 0) {
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


        return view;

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onDestroyView() {

        weatherDataObj = null;
        city = null;
        super.onDestroyView();


    }

}
