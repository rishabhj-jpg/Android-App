package com.example.testapp;

import android.app.Application;
import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        ActivityLifecycleCallback.register(this);
        super.onCreate();

        // Initialize CleverTap
        CleverTapAPI cleverTapAPI = CleverTapAPI.getDefaultInstance(getApplicationContext());

        if (cleverTapAPI != null) {
            // Set default notification channel (optional)
            CleverTapAPI.createNotificationChannel(getApplicationContext(),
                    "MancCityChannel",   // Channel ID
                    "Manchester City",   // Channel Name
                    "Team updates and player news", // Channel Description
                    5,                  // Importance
                    true               // Show badge
            );

            // Enable personalization
            cleverTapAPI.enablePersonalization();
            CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);
            // Record app launched event
            cleverTapAPI.pushEvent("App Launched");
        }
    }
}