package com.stacksloth.repairstudio;

import android.app.ProgressDialog;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailBox;
    private EditText mPassBox;
    private TextView mRegView;
    private Button mLoginBtn;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Login");
        mProgress.setMessage("Sending super-fast sloths to log you in...");
        mProgress.setCancelable(false);

        mEmailBox = (EditText) findViewById(R.id.emailBox);
        mPassBox = (EditText) findViewById(R.id.passBox);
        mRegView = (TextView) findViewById(R.id.regView);
        mLoginBtn = (Button) findViewById(R.id.loginBtn);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailBox.getText().toString();
                String password = mPassBox.getText().toString();
                boolean canPost = true;
                if(email=="") {
                    canPost = false;
                }

                if(password=="") {
                    canPost = false;
                }

                if(canPost) {
                    mProgress.show();
                    APIFunctions.tryLogin(email, password,
                            LoginActivity.this, mProgress);
                }
            }
        });

        mRegView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
