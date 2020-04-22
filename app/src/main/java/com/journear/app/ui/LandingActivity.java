package com.journear.app.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.journear.app.R;
import com.journear.app.core.LocalFunctions;
import com.journear.app.core.utils.AppConstants;
import com.journear.app.map.MapActivity;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        findViewById(R.id.askPermissionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalFunctions.requestPermissions(LandingActivity.this);
            }
        });

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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
//            case AppConstants.MY_PERMISSIONS_REQUEST_INTERNET: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                    LocalFunctions.shortToast("Internet permission granted", LandingActivity.this);
//                } else {
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    LocalFunctions.shortToast("Internet permission not granted", LandingActivity.this);
//                }
//                return;
//            }
            case AppConstants.MY_PERMISSIONS_ALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    LocalFunctions.shortToast("All permission granted", LandingActivity.this);
                    startActivity(new Intent(LandingActivity.this, MainActivity.class));
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    LocalFunctions.shortToast("All permission not granted", LandingActivity.this);
                }
                return;
            }
//            case AppConstants.MY_PERMISSIONS_REQUEST_STORAGE: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                    LocalFunctions.shortToast("Storage permission granted", LandingActivity.this);
//                } else {
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    LocalFunctions.shortToast("Storage permission not granted", LandingActivity.this);
//                }
//                return;
//            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}
