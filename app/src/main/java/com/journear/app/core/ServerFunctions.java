package com.journear.app.core;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.journear.app.core.entities.User;
import com.journear.app.core.utils.AppConstants;

import org.json.JSONObject;


import java.util.HashMap;

public class ServerFunctions {
    static ServerFunctions singleton = null;
    private final String USERNAME = "username";
    private final String LOGIN = "user/login";
    private final String REGISTER = "user/register";
    final String loginUrl = AppConstants.BASE_URL + LOGIN ;
    final String registerUrl = AppConstants.BASE_URL + REGISTER;
    public RequestQueue volleyQueue = null;
    private ServerFunctions(Context context){
        volleyQueue = Volley.newRequestQueue(context);
    }

    public static ServerFunctions getInstance(Context context){
        if(singleton == null){
            singleton = new ServerFunctions(context);
        }

        return singleton;
    }



    public void authenticate(String username, String password, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("email", username);
        params.put("password", password);
        // the POST parameters:s
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, loginUrl, new JSONObject(params),
                listener, errorListener);

        volleyQueue.add(request_json);
    }

    public void registerUser(User user, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        HashMap<String,String> params = new HashMap<>();

        params.put("name", user.getUserName());
        params.put("email", user.getEmail());
        params.put("password", user.getPassword());
        params.put("phone", user.getPhone());
        params.put("dob", user.getDob());
        params.put("gender", user.getGender());
        params.put("ratings", "0");
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST,registerUrl , new JSONObject(params),
                listener, errorListener);

        volleyQueue.add(request_json);

    }
}
