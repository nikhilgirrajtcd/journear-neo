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
import com.journear.app.core.PersistentStore;
import com.journear.app.core.ServerFunctions;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        email = findViewById(R.id.edit_email);
        password = findViewById(R.id.edit_password);
        login = findViewById(R.id.btn_login);

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                Response.Listener responseListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("Tag1", "Pakoda");
                            //Process os success response
                            if (response.get("Message").toString().equals("Success")) {
                                afterLoginSuccess();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("tag2", "LODA");
                    }
                };


                ServerFunctions.getInstance(LoginActivity.this).authenticate(email.getText().toString(), password.getText().toString(),
                        responseListener, responseErrorListener);

            }});


    }

    private void afterLoginSuccess() {
        PersistentStore.getInstance(LoginActivity.this).setItem("currentUser", email.getText().toString(), true);

        Intent loginSuccessIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(loginSuccessIntent);
        finish();
    }
}





