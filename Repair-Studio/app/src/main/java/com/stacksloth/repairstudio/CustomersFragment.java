package com.stacksloth.repairstudio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel Slapelis on 4/8/2017.
 */

public class CustomersFragment extends Fragment {

    private TextView mNumCustView;
    private SwipeRefreshLayout mRefresh;
    private HashMap<Integer, Customer> mCustomerMap;
    private String mToken;
    private CustomerViewAdapter mCustomerViewAdapter;
    private ListView mCustomerList;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.customers_fragment, container, false);
        mNumCustView = (TextView) v.findViewById(R.id.numCustView);
        mRefresh = (SwipeRefreshLayout) v.findViewById(R.id.refreshLayout);
        mCustomerMap = new HashMap<>();

        SharedPreferences sharedPref = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        mToken = sharedPref.getString("token", "DNE");

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddCustomer.class);
                startActivity(intent);
            }
        });

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCustomerData();
            }
        });

        mCustomerList = (ListView) v.findViewById(R.id.custList);
        getCustomerData();
        return v;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getCustomerData();
    }

    /**
     * Too much of a hassle to place into APIFunctions
     * Retrieves the customer data from server.
     */
    private void getCustomerData()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity());
        String URL = "https://stacksloth.com/api.php/api/v1/readcustomers";

        Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mRefresh.setRefreshing(false);
                mCustomerMap.clear();
                if(response.length() > 0) {
                    int i = 0;
                    while(i < response.length()) {
                        try {
                            JSONObject tempObj = (JSONObject) response.get(i);
                            Customer tempCust = new Customer(tempObj.getInt("id"), tempObj.getInt("user"),
                                    tempObj.getString("name"), tempObj.getString("email"), tempObj.getString("phone"),
                                    Float.parseFloat(tempObj.getString("spent")));
                            mCustomerMap.put(tempCust.hashCode(), tempCust);
                            i++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
                updateList();
            }
        };

        Response.ErrorListener jsonArrayErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        };


        JsonArrayRequest jsonRequest = new JsonArrayRequest(URL, jsonArrayListener, jsonArrayErrorListener) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = mToken;
                String auth = ("Bearer "
                        + credentials);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };

        requestQueue.add(jsonRequest);
    }

    private void updateList()
    {
        mCustomerViewAdapter = null;
        mCustomerViewAdapter = new CustomerViewAdapter(mCustomerMap);
        mCustomerList.setAdapter(mCustomerViewAdapter);
        mNumCustView.setText(mCustomerMap.size() + " Customers");
    }
}
