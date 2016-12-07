package com.akshaysonvane.cmpe273;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.akshaysonvane.cmpe273.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class SplashActivity extends AppCompatActivity
{
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    Handler handler;
    Runnable runnable;
    SharedPreferences cmpe273prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);

        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        long cacheExpiration = 3600; // 1 hour in seconds.
        // If in developer mode cacheExpiration is set to 0 so each fetch will retrieve values from
        // the server.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled())
        {
            cacheExpiration = 0;
        }

        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Once the config is successfully fetched it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        }
                        else
                        {
                        }

                        handler = new Handler();
                        runnable = new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                                Utils.getInstance().BASE_URL = mFirebaseRemoteConfig.getString("BASE_URL");
                                //Toast.makeText(getApplication(), Utils.getInstance().BASE_URL, Toast.LENGTH_LONG).show();
                                startActivity(i);
                                finish();
                            }
                        };
                        handler.postDelayed(runnable, 1000);
                    }
                });
    }
}
