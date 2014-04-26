package Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loop_to_infinity.surfergirl.R;

import java.text.DecimalFormat;

import Adapters.Cities_Adapters;
import Interfaces.item;

/**
 * Created by Unknown1 on 11/28/13.
 */
public class List_Item extends Fragment implements item {

    private final String waveQuickInfo;
    private final String date;
    private final String wind;
    private final String rawWaveHeight;
    private final LayoutInflater mInflater;
    private final Typeface robotoLight;
    private final Context ctx;
    private DecimalFormat df = new DecimalFormat("#.#");


    public List_Item(Context ctx, LayoutInflater mInflater, String waveQuick, String wind, String date, String rawWaveHeight) {
        this.waveQuickInfo = waveQuick;
        this.date = date;
        this.wind = wind;
        this.mInflater = mInflater;
        this.rawWaveHeight = rawWaveHeight;
        this.ctx = ctx;
        robotoLight = Typeface.createFromAsset(ctx.getAssets(), "fonts/rlight.ttf");
    }


    @Override
    public int getViewType() {
        return Cities_Adapters.RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {

        ViewHolder viewHolder;
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        double wavesHeightGlobal = sharedPreferences.getFloat("settings_wave_height", (float) 0.5);
        boolean enableAlarms = sharedPreferences.getBoolean("alarmBOX", false);

        String tempVal = df.format(wavesHeightGlobal);
        wavesHeightGlobal = Double.valueOf(tempVal);
        double wavesFromJSON = Double.valueOf(rawWaveHeight);

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.cities_row, null);
            viewHolder = new ViewHolder();
            viewHolder.date = (TextView) convertView.findViewById(R.id.timeStamp);
            viewHolder.waveQuickInfo = (TextView) convertView.findViewById(R.id.WaveHeight);
            viewHolder.wind = (TextView) convertView.findViewById(R.id.windSpeed);
            viewHolder.IV = (ImageView) convertView.findViewById(R.id.imageView);

            if ((wavesFromJSON >= wavesHeightGlobal) && (enableAlarms)) {
                viewHolder.IV.setVisibility(View.VISIBLE);
            } else viewHolder.IV.setVisibility(View.GONE);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if ((wavesFromJSON >= wavesHeightGlobal) && (enableAlarms)) {
                viewHolder.IV.setVisibility(View.VISIBLE);
            } else viewHolder.IV.setVisibility(View.GONE);
        }

        viewHolder.waveQuickInfo.setTypeface(robotoLight);
        viewHolder.waveQuickInfo.setText(ctx.getResources().getString(R.string.WaveHeight) + " " + this.waveQuickInfo);


        viewHolder.wind.setTypeface(robotoLight);
        viewHolder.wind.setText(ctx.getResources().getString(R.string.Wind) + " " + this.wind);


        viewHolder.date.setTypeface(robotoLight);
        viewHolder.date.setText(ctx.getResources().getString(R.string.Date) + " " + this.date);


        return convertView;
    }

    static class ViewHolder {

        public TextView waveQuickInfo;
        public TextView date;
        public TextView wind;
        public ImageView IV;
    }
}
