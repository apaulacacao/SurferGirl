package Fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import volley.RequestQueue;
import volley.Response;
import volley.Response.Listener;
import volley.VolleyError;
import volley.toolbox.JsonArrayRequest;
import volley.toolbox.Volley;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loop_to_infinity.surfergirl.R;

/**
 * Created by Unknown1 on 8/18/13.
 */
public class WaitFirst extends android.support.v4.app.Fragment {

	private Context ctx;
	private RequestQueue rq;

	// MagicSeaWeed beach in Rishon Le Zion
	private String url = "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3976&units=eu";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_base, container,
				false);
		super.onCreateView(inflater, container, savedInstanceState);



		return view;

	}

}
