package com.stacksloth.repairstudio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmailText;
    private EditText mNameText;
    private EditText mPasswordText;
    private EditText mConfPasswordText;
    private TextView mLoginView;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailText = (EditText) findViewById(R.id.emailBox);
        mNameText = (EditText) findViewById(R.id.nameBox);
        mPasswordText = (EditText) findViewById(R.id.passBox);
        mConfPasswordText = (EditText) findViewById(R.id.confPassBox);
        mLoginView = (TextView) findViewById(R.id.loginView);
        mRegisterButton = (Button) findViewById(R.id.regBtn);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryPost(mEmailText.getText().toString(), mNameText.getText().toString(),
                        mPasswordText.getText().toString());
            }
        });

        mLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void tryPost(String email, String name, String pass) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://stacksloth.com/api.php/api/v1/register";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", pass);
            jsonBody.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(URL, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response.optString("status").equals("success")) {
                    Toast.makeText(RegisterActivity.this, "Nice! You made an account. Now you can login.", Toast.LENGTH_SHORT).show();
                    finish();
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
        };
        requestQueue.add(jsonRequest);
    }
}
