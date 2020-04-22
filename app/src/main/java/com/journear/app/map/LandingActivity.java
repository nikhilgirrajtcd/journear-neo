/***
 * 
 */
package com.journear.app.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.journear.app.R;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);


        findViewById(R.id.openMapButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this, MapActivity.class);
                double[] dddddd = {0, 0, 0, 0, 0, 0};
                intent.putExtra(MapActivity.incomingIntentName, dddddd);
                startActivity(intent);
            }
        });
    }
}
