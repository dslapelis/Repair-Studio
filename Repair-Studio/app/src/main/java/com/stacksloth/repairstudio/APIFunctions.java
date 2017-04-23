package com.stacksloth.repairstudio;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by Daniel Slapelis on 4/22/2017.
 */

public class APIFunctions {

    /**
     * Function for initial login.
     * @param email
     * @param pass
     * @param activity
     * @param progressDialog
     */
    public static void tryLogin(String email, String pass, final Activity activity, final ProgressDialog progressDialog)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String URL = "https://www.stacksloth.com/api.php/api/v1/login";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonRequest = new JsonObjectRequest(URL, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response.opt("token") != null) {
                    String token;
                    token = response.optString("token");

                    SharedPreferences sharedPref = activity.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.clear();
                    editor.putString("token", token);
                    editor.commit();
                    progressDialog.hide();
                    getUserData(token, activity);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                TextView email = (TextView) activity.findViewById(R.id.emailBox);
                email.setError("Your username or password is incorrect");
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        requestQueue.add(jsonRequest);
    }

    /**
     * Function for checking a user's token if it is in shared preferences.
     * @param token
     * @param activity
     */
    public static void checkToken(final String token, final Activity activity)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String URL = "https://stacksloth.com/api.php/api/v1/authenticate";
        JSONObject jsonBody = new JSONObject();

        Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response.opt("status").equals("success")) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
        };

        Response.ErrorListener jsonObjectErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
                activity.finish();
            }
        };

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, URL, jsonBody, jsonObjectListener, jsonObjectErrorListener) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String auth = ("Bearer "
                        + token);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };
        requestQueue.add(jsonRequest);
    }

    /**
     * Function for retrieving user data and storing it in shared preferences.
     * @param token
     * @param activity
     */
    public static void getUserData(final String token, final Activity activity)
    {

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String URL = "https://stacksloth.com/api.php/api/v1/data";
        JSONObject jsonBody = new JSONObject();

        Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                SharedPreferences sharedPref = activity.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                String name = ""; String email = ""; String membership = "";
                try
                {
                    name = response.getString("name");
                    email = response.getString("email");
                    membership = response.getString("membership");
                    editor.putString("name", name);
                    editor.putString("email", email);
                    editor.putString("membership", membership);
                } catch (Exception e) {}
                editor.commit();

                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
                activity.finish();
            }
        };

        Response.ErrorListener jsonObjectErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, URL, jsonBody, jsonObjectListener, jsonObjectErrorListener) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = token;
                String auth = ("Bearer "
                        + credentials);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };
        requestQueue.add(jsonRequest);
    }
}
