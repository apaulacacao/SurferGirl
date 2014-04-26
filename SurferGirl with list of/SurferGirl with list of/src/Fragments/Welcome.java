package Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loop_to_infinity.surfergirl.R;

import org.json.JSONArray;

import java.util.ArrayList;

import Adapters.MyCustomSpinnerAdapter;
import Json.WeatherData;
import Utilities.Magic_Spots_ID;
import volley.RequestQueue;
import volley.Response;
import volley.VolleyError;
import volley.toolbox.JsonArrayRequest;
import volley.toolbox.Volley;

/**
 * Created by Unknown1 on 12/4/13.
 */
public class Welcome extends Fragment {

    private RequestQueue rq;
    private Typeface roboto;
    private Context ctx;
    private String choosenCity;
    private int spinnerPosition;
    private int countSelections = 0;
    private JsonArrayRequest jsArrayRequest;
    private String TAG = "Welcome";


    // MagicSeaWeed spot url
    private String url;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Set variables.
        ctx = getActivity();
        rq = Volley.newRequestQueue(ctx);
        final FragmentManager fm = getFragmentManager();
        roboto = Typeface.createFromAsset(ctx.getAssets(), "fonts/rthis.ttf");
        final String[] cities = ctx.getResources().getStringArray(R.array.country_arrays);


        final View view;
        view = inflater.inflate(R.layout.welcome, container,
                false);
        super.onCreateView(inflater, container, savedInstanceState);

        loadSavedPreferences();


        // Set views and fonts
        TextView welcome = (TextView) view.findViewById(R.id.welcome);
        welcome.setTypeface(roboto);

        TextView chzCity = (TextView) view.findViewById(R.id.chooseLabel);
        chzCity.setTypeface(roboto);

        Button go = (Button) view.findViewById(R.id.go);
        go.setTypeface(roboto);


        MyCustomSpinnerAdapter myCitiesAdapter = new MyCustomSpinnerAdapter(ctx, android.R.layout.simple_list_item_1, cities, 26);


        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setAdapter(myCitiesAdapter);
        spinner.setSelection(spinnerPosition);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinnerPosition = position;
                choosenCity = cities[spinnerPosition].toString();

                countSelections++;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getActivity(), "Nothing", Toast.LENGTH_SHORT).show();

            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (countSelections != 0) {

                    // Get correct url
                    url = Magic_Spots_ID.getCitySpots(spinnerPosition + 1);


                    final ProgressDialog progressDialog = new ProgressDialog(ctx);
                    progressDialog.setMessage(choosenCity + " - " + ctx.getResources().getString(R.string.loadingMSG));
                    progressDialog.setCancelable(false);
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();


                    // Get JSONArray from MagicSeeWeed
                    jsArrayRequest = new JsonArrayRequest(url,
                            new Response.Listener<JSONArray>() {

                                @Override
                                public void onResponse(JSONArray response) {

                                    ArrayList<WeatherData> weatherDataArrayList = new ArrayList<WeatherData>();


                                    // extract Json data and put in arraylist


                                    for (int i = 0; i <= 38; i++) {
                                        WeatherData wd = new WeatherData();
                                        wd.parseJSON(response, i);
                                        weatherDataArrayList.add(wd);

                                    }


                                    MainFragment mf = new MainFragment(choosenCity, weatherDataArrayList);
                                    FragmentTransaction ft = fm.beginTransaction();
                                    ft.replace(R.id.frame, mf);
                                    ft.commit();

                                    progressDialog.dismiss();


                                }


                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Dismiss loading dialog
                            progressDialog.dismiss();

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
                    }
                    );

                    rq.add(jsArrayRequest);


                }

            }
        });


        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void loadSavedPreferences() {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        spinnerPosition = sharedPreferences.getInt("spinnerPosition", 0);

    }
}
