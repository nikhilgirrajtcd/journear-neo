package com.journear.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.journear.app.R;
import com.journear.app.core.LocalFunctions;
import com.journear.app.core.ServerFunctions;
import com.journear.app.core.entities.User;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button login;
    final String logTag = "LoginActivity";

    Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(logTag, "Server communication error while trying to log in.", error);
        }
    };

    Response.Listener responseListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                Log.d(logTag, "Received server response.");
                //Process os success response
                if (response.get("Message").toString().equals("Success")) {
                    onLoginSuccess();
                    if(response.has("user"))
                    {
                        JSONObject userJsonObj = response.getJSONObject("user");
                        User user = new User();
                        user.setDobValue(userJsonObj.get("dob").toString());
                        user.setGender(userJsonObj.get("gender").toString());
                        user.setUserID(userJsonObj.get("id").toString());
                        user.setEmail(userJsonObj.get("username").toString());
                        user.name = userJsonObj.get("name").toString();
                        LocalFunctions.setCurrentUser(LoginActivity.this, user);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        email = findViewById(R.id.edit_email);
        password = findViewById(R.id.edit_password);
        login = findViewById(R.id.btn_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndLogin();
            }
        });


    }

    private void validateAndLogin() {
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();

        if(validateInputs(emailString, passwordString))
            ServerFunctions.getInstance(LoginActivity.this).authenticate(emailString, passwordString,
                responseListener, responseErrorListener);
    }

    private boolean validateInputs(String email, String password) {
        return true;
//        return IsValid.email(email) & IsValid.password(password);
    }

    /**
     * Move to another activity or save the response of the Login call to server.
     */
    private void onLoginSuccess() {
        Intent loginSuccessIntent;
        if(LocalFunctions.requestPermissions(LoginActivity.this))
            loginSuccessIntent = new Intent(LoginActivity.this, MainActivity.class);
        else
            loginSuccessIntent = new Intent(LoginActivity.this, LandingActivity.class);
        startActivity(loginSuccessIntent);
        finish();
    }
}





