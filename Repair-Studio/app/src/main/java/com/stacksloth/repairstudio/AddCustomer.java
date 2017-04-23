package com.stacksloth.repairstudio;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddCustomer extends AppCompatActivity {

    private EditText mFirstNameBox;
    private EditText mLastNameBox;
    private EditText mEmailBox;
    private EditText mPhoneBox;
    private Button mCreateCustBtn;
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        setTitle("Add Customer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFirstNameBox = (EditText) findViewById(R.id.firstNameBox);
        mLastNameBox = (EditText) findViewById(R.id.lastNameBox);
        mEmailBox = (EditText) findViewById(R.id.emailBox);
        mPhoneBox = (EditText) findViewById(R.id.phoneBox);
        mCreateCustBtn = (Button) findViewById(R.id.createCustBtn);

        mCreateCustBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean canPost = true;

                if(mFirstNameBox.getText().toString().equals("")) {
                   canPost = false;
                    mFirstNameBox.setError("This field cannot be blank.");
                }

                if(mLastNameBox.getText().toString().equals("")) {
                    canPost = false;
                    mLastNameBox.setError("This field cannot be blank.");
                }

                if(mEmailBox.getText().toString().equals("")) {
                    canPost = false;
                    mEmailBox.setError("This field cannot be blank.");
                }

                if (mPhoneBox.getText().toString().equals("")) {
                    canPost = false;
                    mPhoneBox.setError("This field cannot be blank.");
                }

                if(canPost) {
                    SharedPreferences sharedPref = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                    mToken = sharedPref.getString("token", "DNE");
                    Log.d("DEBUG", "Token: " + mToken);
                    String name = mFirstNameBox.getText().toString() + " " + mLastNameBox.getText().toString();
                    tryPost(name, mPhoneBox.getText().toString(), mEmailBox.getText().toString());
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void tryPost(String name, String phone, String email) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://stacksloth.com/api.php/api/v1/addcustomer";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("token", mToken);
            jsonBody.put("name", name);
            jsonBody.put("email", email);
            jsonBody.put("phone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(URL, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.get("status").equals("success")) {
                        Toast.makeText(AddCustomer.this, "You successfully added a customer!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
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
            }};
        requestQueue.add(jsonRequest);
    }
}
