package com.example.testapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.CTInboxStyleConfig;
import com.clevertap.android.sdk.CleverTapAPI;

public class InboxActivity extends AppCompatActivity implements CTInboxListener {

    private CleverTapAPI cleverTapAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox); // Ensure this layout is simple (e.g., just a FrameLayout or CoordinatorLayout)

        // Setup Toolbar
        setSupportActionBar(findViewById(R.id.toolbar_inbox));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Team Updates");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        cleverTapAPI = CleverTapAPI.getDefaultInstance(this);

        if (cleverTapAPI != null) {
            // 1. Set this activity as the inbox listener
            cleverTapAPI.setCTNotificationInboxListener(this);
            // 2. Initialize the inbox and wait for callbacks
            cleverTapAPI.initializeInbox();
            Toast.makeText(this, "Loading notifications...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "CleverTap SDK not initialized", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    // This callback is called when the inbox is ready to be shown
    @Override
    public void inboxDidInitialize() {
        runOnUiThread(() -> {
            // Configure the look and feel (optional but recommended)
            CTInboxStyleConfig styleConfig = new CTInboxStyleConfig();
            styleConfig.setNavBarTitle("Man City FC");
            styleConfig.setNavBarTitleColor("#1C3B6F"); // Dark Blue
            styleConfig.setNavBarColor("#FFFFFF"); // White
            styleConfig.setInboxBackgroundColor("#F5F5F5"); // Light Grey
            styleConfig.setBackButtonColor("#1C3B6F");
            styleConfig.setUnselectedTabColor("#757575"); // Gray
            styleConfig.setSelectedTabColor("#1C3B6F"); // Dark Blue
            styleConfig.setSelectedTabIndicatorColor("#6CABE5"); // Light Blue
            // You can also set tab names with styleConfig.setTabs(tabsList);

            // 3. Show the pre-built inbox activity
            if (cleverTapAPI != null) {
                cleverTapAPI.showAppInbox(styleConfig); // Show with custom style
                // cleverTapAPI.showAppInbox(); // Show with default style
            }
        });
    }

    // This callback is called when messages are updated (new, read, deleted)
    @Override
    public void inboxMessagesDidUpdate() {
        // You can update a badge count here if needed
        runOnUiThread(() -> Toast.makeText(this, "Inbox updated", Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        // Clean up the listener
        if (cleverTapAPI != null) {
            cleverTapAPI.setCTNotificationInboxListener(null);
        }
        super.onDestroy();
    }
}