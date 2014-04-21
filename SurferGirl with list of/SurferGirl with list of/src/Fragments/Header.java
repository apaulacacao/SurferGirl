package Fragments;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.loop_to_infinity.surfergirl.R;

import Adapters.Cities_Adapters;
import Interfaces.item;

/**
 * Created by Unknown1 on 11/28/13.
 */
public class Header extends Fragment implements item {
    private final String name;
    private final Typeface roboto;


    public Header(Context ctx, LayoutInflater mInflate, String name) {

        this.name = name;

        roboto = Typeface.createFromAsset(ctx.getAssets(), "fonts/rthis.ttf");
    }


    @Override
    public int getViewType() {
        return Cities_Adapters.RowType.HEADER_ITEM.ordinal();

    }

    @Override
    public View getView(LayoutInflater mInflater, View convertView) {

        ViewHolder viewHolder;


        if (convertView == null) {

            convertView = (View) mInflater.inflate(R.layout.header_item, null);
            viewHolder = new ViewHolder();

            viewHolder.tv = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);

        } else viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.tv = (TextView) convertView.findViewById(R.id.textView);
        viewHolder.tv.setTypeface(roboto);
        viewHolder.tv.setText(name);

        return convertView;
    }

    static class ViewHolder {

        public TextView tv;

    }
}
