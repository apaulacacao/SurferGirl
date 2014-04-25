package Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
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

/**
 * Created by Unknown1 on 8/18/13.
 */
public class MainFragment extends android.support.v4.app.Fragment {

    private Welcome wl;
    private String city;
    private Typeface roboto;
    private String TAG = "MainFragment";

    public MainFragment(String cityLabel) {
        this.city = cityLabel;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.summary_list_frag, container,
                false);
        super.onCreateView(inflater, container, savedInstanceState);

        Context ctx = getActivity();
        final Cities_Adapters ca;
        roboto = Typeface.createFromAsset(ctx.getAssets(), "fonts/rthis.ttf");


        TextView cityLabelView = (TextView) view.findViewById(R.id.cityLabelLayoutTextView);
        cityLabelView.setText(city);
        cityLabelView.setTypeface(roboto);


        // ArrayList for days displayed
        ArrayList<String> days = new ArrayList<String>();

        try {
            days.add(wl.data.get(0).getDayString());
            days.add(wl.data.get(8).getDayString());
            days.add(wl.data.get(16).getDayString());
            days.add(wl.data.get(24).getDayString());
            days.add(wl.data.get(32).getDayString());
        } catch (ArrayIndexOutOfBoundsException ex) {
            Log.d(TAG, ex.getMessage());
        }
        int daysCount = 0;

        // Array for Israel Cities and beaches
        List<item> items = new ArrayList<item>();


        for (int i = 0; i < 39; i++) {
            if (i % 8 == 0) {
                items.add(new Header(ctx, inflater, days.get(daysCount)));

                items.add(new List_Item(ctx, inflater, wl.data.get(i).getWavesHeight(), wl.data.get(i).getWindSpeed(), wl.data.get(i).getCurrentDayTimeStamp(), wl.data.get(i).getRawMinimumWavesHeight()));
                if (daysCount < 5) {
                    daysCount++;
                }

            } else if (i != 8) {
                items.add(new List_Item(ctx, inflater, wl.data.get(i).getWavesHeight(), wl.data.get(i).getWindSpeed(), wl.data.get(i).getCurrentDayTimeStamp(), wl.data.get(i).getRawMinimumWavesHeight()));
            }

        }
        ca = new Cities_Adapters(ctx,
                items);

        ListView lv = (ListView) view.findViewById(R.id.list);
        lv.setAdapter(ca);


        return view;

    }


}
