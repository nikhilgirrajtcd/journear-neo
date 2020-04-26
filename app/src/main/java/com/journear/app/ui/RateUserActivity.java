package com.journear.app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.journear.app.R;

public class RateUserActivity extends AppCompatActivity {

    RatingBar ratingbar;
    Button rateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_user);
        addListenerOnButtonClick();
    }

    public void addListenerOnButtonClick() {
        try {
            ratingbar = findViewById(R.id.ratingBar);
            rateButton = findViewById(R.id.button_rate);
            //Performing action on Button Click
            rateButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    //Getting the rating and displaying it on the toast
                    String rating = String.valueOf(ratingbar.getRating());
                    final Intent intent = new Intent(RateUserActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("EXTRA", "");


                    startActivity(intent);
                    finish();

                    Toast.makeText(getApplicationContext(), "Rating " + rating + " will be synced.", Toast.LENGTH_LONG).show();
                }

            });
        }catch (Exception ex) {
            Log.e("Whatever", "Error in Rating Activity OnCreate", ex);
        }
    }
}
