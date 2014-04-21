package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loop_to_infinity.surfergirl.R;

import java.util.List;

import Interfaces.item;

public class Cities_Adapters extends ArrayAdapter<item> {
    private LayoutInflater mInflater;


    public enum RowType {
        HEADER_ITEM, LIST_ITEM
    }

    public Cities_Adapters(Context ctx, List<item> items) {
        super(ctx, 0, items);
        mInflater = LayoutInflater.from(ctx);

    }

    @Override
    public int getViewTypeCount() {
        return RowType.values().length;

    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getItem(position).getView(mInflater, convertView);


    }

    static class ViewHolder {

        public TextView waveQuickInfo;
        public TextView date;
        public TextView wind;
    }

}
