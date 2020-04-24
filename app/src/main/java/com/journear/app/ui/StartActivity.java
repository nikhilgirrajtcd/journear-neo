package com.journear.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.journear.app.R;
import com.journear.app.core.LocalFunctions;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class StartActivity extends AppCompatActivity {

    Button login, register, downloadMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register_activity);
        login = findViewById(R.id.btnLogIn);
        register = findViewById(R.id.btnRegister);
        downloadMap = findViewById(R.id.btnDownloadMap);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StartActivity.this, UserRegisterActivity.class);
                startActivity(intent);

            }
        });

        downloadMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {

                    LocalFunctions.shortToast(String.valueOf("Download button clicked"), StartActivity.this);
                    downloadFile("https://journear.blob.core.windows.net/data/graphhopper-ie.zip", getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
                }
                catch (Exception e){

                }

            }
        });
    }

    private void downloadFile(String url, File outputFile) throws IOException {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();
            LocalFunctions.shortToast(String.valueOf(contentLength), StartActivity.this);
            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch(FileNotFoundException e) {
            return; // swallow a 404
        } catch (IOException e) {
            return; // swallow a 404
        }
    }
}
