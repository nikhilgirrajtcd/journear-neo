package com.journear.app.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.journear.app.R;
import com.journear.app.core.PersistentStore;
import com.journear.app.core.ServerFunctions;
import com.journear.app.core.entities.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class UserRegisterActivity extends AppCompatActivity {
    EditText username, password, email, phone, dob;
    RadioGroup gender;
    Button register, cancel;
    DatePickerDialog picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        username = findViewById(R.id.editName);
        password = findViewById(R.id.editPassword);
        email = findViewById(R.id.editEmail);

        gender = findViewById(R.id.editGender);
        phone = findViewById(R.id.editPhone);
        dob = findViewById(R.id.editDob);
        register = findViewById(R.id.btn_Register);
        dob.setInputType(InputType.TYPE_NULL);
        final Calendar c = Calendar.getInstance();
        c.set(2002, 04, 1);//Year,Mounth -1,Day

        //Phone and DOB Validations
        phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        dob.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(UserRegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dob.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.getDatePicker().setMaxDate(c.getTimeInMillis());
                picker.show();
            }
        });

        final Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("JSON","ONRESPONSE ERROR START");
            }
        };



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final User registeringUser = new User();

                registeringUser.setUserName( username.getText().toString());
                registeringUser.setPassword( password.getText().toString());
                registeringUser.setEmail(email.getText().toString());
                registeringUser.setPhoneValue(phone.getText().toString());
                registeringUser.setDobValue(dob.getText().toString());

                RadioButton checkedBtn = findViewById(gender.getCheckedRadioButtonId());
                registeringUser.setGender(checkedBtn.getText().toString());

                if (registeringUser.getUserName().length() > 1) {
                    Response.Listener responseListener = new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("JSON","ONRESPONSE START");
                            try {
                                //Process os success response
                                if (response.get("Message").toString().equals("Success") ) {
                                    PersistentStore.getInstance(UserRegisterActivity.this).setItem("registeredUser", registeringUser, true);
                                    Toast.makeText(UserRegisterActivity.this, "User registered!", Toast.LENGTH_SHORT).show();

                                    Intent registerSuccessIntent = new Intent(UserRegisterActivity.this, LoginActivity.class);
                                    startActivity(registerSuccessIntent);
                                    finish();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    ServerFunctions.getInstance(UserRegisterActivity.this).registerUser(registeringUser,
                            responseListener, responseErrorListener);
                }
                else {
                    Toast.makeText(UserRegisterActivity.this, "Enter the values!", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}
