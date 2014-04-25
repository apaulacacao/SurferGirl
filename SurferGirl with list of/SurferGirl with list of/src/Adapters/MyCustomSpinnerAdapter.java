package Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loop_to_infinity.surfergirl.R;

/**
 * Created by Unknown1 on 4/25/14.
 */
public class MyCustomSpinnerAdapter extends ArrayAdapter<String> {

    private Typeface roboto;
    private Context ctx;
    private int fontSize = 13;


    public MyCustomSpinnerAdapter(Context ctx, int TextViewResId, String[] objects) {
        super(ctx, TextViewResId, objects);
        this.ctx = ctx;
        roboto = Typeface.createFromAsset(ctx.getAssets(), "fonts/rthis.ttf");

    }

    public MyCustomSpinnerAdapter(Context ctx, int TextViewResId, String[] objects, int fontSize) {
        super(ctx, TextViewResId, objects);
        this.ctx = ctx;
        roboto = Typeface.createFromAsset(ctx.getAssets(), "fonts/rthis.ttf");
        this.fontSize = fontSize;

    }


    public View getView(int position, View convertView, ViewGroup parent) {


        View v = super.getView(position, convertView, parent);

        ((TextView) v).setTypeface(roboto);
        ((TextView) v).setTextColor(ctx.getResources().getColor(R.color.card_white));
        ((TextView) v).setTextSize(fontSize);
        return v;
    }


    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = super.getDropDownView(position, convertView, parent);
        ((TextView) v).setTypeface(roboto);


        return v;
    }

}