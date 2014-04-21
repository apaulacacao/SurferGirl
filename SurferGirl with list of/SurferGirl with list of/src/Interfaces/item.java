package Interfaces;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Unknown1 on 11/28/13.
 */
public interface item {

    public int getViewType();

    public View getView(LayoutInflater inflater, View convertView);
}
