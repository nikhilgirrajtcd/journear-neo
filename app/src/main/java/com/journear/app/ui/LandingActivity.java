package com.journear.app.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.journear.app.R;
import com.journear.app.core.LocalFunctions;
import com.journear.app.core.entities.StringWrapper;
import com.journear.app.core.services.ServiceLocator;
import com.journear.app.core.utils.AppConstants;
import com.journear.app.map.MapActivity;

import org.apache.commons.lang3.StringUtils;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
    }

    private boolean hasNecessaryPermissions() {
        return LocalFunctions.isStoragePermissionGiven(this) && LocalFunctions.isLocationPermissionGiven(this);
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
                    proceedToMainActivity();
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (!hasNecessaryPermissions()) {
            StringWrapper firstTimeUse = (StringWrapper) ServiceLocator.getPersistentStore().getItem("FirstTimeUse", StringWrapper.class);

            if (firstTimeUse == null || StringUtils.isEmpty(firstTimeUse.toString())) {
                findViewById(R.id.askPermissionButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringWrapper sw = new StringWrapper("" + System.currentTimeMillis());
                        LocalFunctions.requestPermissions(LandingActivity.this);
                        if(hasNecessaryPermissions())
                        {
                            proceedToMainActivity();
                        }
                    }
                });
            } else {
                StringWrapper sw = new StringWrapper("" + System.currentTimeMillis());
                proceedToMainActivity();
            }
        } else {
            proceedToMainActivity();
        }
    }

    private void proceedToMainActivity() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(LandingActivity.this, MainActivity.class));
        finish();
    }

}
