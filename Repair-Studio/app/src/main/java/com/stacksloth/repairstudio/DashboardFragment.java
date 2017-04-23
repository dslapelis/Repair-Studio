package com.stacksloth.repairstudio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel Slapelis on 4/8/2017.
 */

public class DashboardFragment extends Fragment {

    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.dashboard_fragment, container, false);
        TextView mainNameView = (TextView) v.findViewById(R.id.mainNameView);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        mainNameView.setText("Hello,\n" + sharedPref.getString("name","DNE"));
        return v;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }
}
