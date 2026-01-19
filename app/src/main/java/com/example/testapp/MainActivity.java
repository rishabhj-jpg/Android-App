package com.example.testapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;
import com.example.testapp.adapters.PlayerAdapter;
import com.example.testapp.models.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements PlayerAdapter.OnPlayerClickListener, DisplayUnitListener {

    private RecyclerView recyclerView;
    private PlayerAdapter playerAdapter;
    private List<Player> playerList;
    private CleverTapAPI cleverTapAPI;

    private FrameLayout nativeAdContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cleverTapAPI = CleverTapAPI.getDefaultInstance(this);

        // Set up toolbar
        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Manchester City Players");
        }
        nativeAdContainer = findViewById(R.id.native_ad_container);

        // Initialize players list
        initializePlayers();

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerViewPlayers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        playerAdapter = new PlayerAdapter(playerList, this);
        recyclerView.setAdapter(playerAdapter);

        initializeCleverTapNativeDisplay();

        // Record app opened event
        if (cleverTapAPI != null) {
            Map<String, Object> props = new HashMap<>();
            props.put("Screen", "Main Activity");
            props.put("Player Count", playerList.size());
            cleverTapAPI.pushEvent("App Opened", props);
        }
    }

    private void initializePlayers() {
        playerList = new ArrayList<>();

        playerList.add(new Player("Erling Haaland", "Striker", "Norway", 9, 4.8f, R.drawable.haaland));
        playerList.add(new Player("Kevin De Bruyne", "Midfielder", "Belgium", 17, 4.9f, R.drawable.debruyne));
        playerList.add(new Player("Phil Foden", "Midfielder", "England", 47, 4.7f, R.drawable.foden));
        playerList.add(new Player("Ederson", "Goalkeeper", "Brazil", 31, 4.6f, R.drawable.ederson));
        playerList.add(new Player("Rúben Dias", "Defender", "Portugal", 3, 4.7f, R.drawable.dias));
        playerList.add(new Player("Kyle Walker", "Defender", "England", 2, 4.5f, R.drawable.walker));
        playerList.add(new Player("Bernardo Silva", "Midfielder", "Portugal", 20, 4.6f, R.drawable.silva));
        playerList.add(new Player("Rodri", "Midfielder", "Spain", 16, 4.8f, R.drawable.rodri));
        playerList.add(new Player("John Stones", "Defender", "England", 5, 4.6f, R.drawable.stones));
    }

    private void initializeCleverTapNativeDisplay() {
        if (cleverTapAPI != null) {
            Log.d("CleverTap", "Initializing Native Display...");
            // 1. Register for Display Unit callbacks
            cleverTapAPI.setDisplayUnitListener(this);
            Log.d("CleverTap", "DisplayUnitListener registered");

            // 2. Fetch any already loaded display units
            checkForPreloadedDisplayUnits();

            // 3. Trigger Native Display by raising an event
            // This event should match what you configure in your CleverTap campaign
            cleverTapAPI.pushEvent("Main Screen Viewed");

            // Record that Native Display was initialized
            cleverTapAPI.pushEvent("Native Display Initialized");
        }else {
            Log.e("CleverTap", "CleverTapAPI instance is NULL!");
        }
    }

    private void checkForPreloadedDisplayUnits() {
        if (cleverTapAPI != null) {
            // Get all currently available display units
            ArrayList<CleverTapDisplayUnit> units = cleverTapAPI.getAllDisplayUnits();
            if (units != null && !units.isEmpty()) {
                onDisplayUnitsLoaded(units);
            }
        }
    }

    @Override
    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> units) {
        runOnUiThread(() -> {
            if (units != null && !units.isEmpty()) {
                // Get the first available display unit
                CleverTapDisplayUnit displayUnit = units.get(0);

                // Show the native ad
                showNativeDisplay(displayUnit);

                // Record that a display unit was loaded
                Map<String, Object> props = new HashMap<>();
                props.put("Unit ID", displayUnit.getUnitID());
                props.put("Unit Type", displayUnit.getType());
                props.put("Content Count", displayUnit.getContents().size());
                cleverTapAPI.pushEvent("Native Display Loaded", props);

            } else {
                // Hide the container if no ads available
                nativeAdContainer.setVisibility(View.GONE);
            }
        });
    }

    private void showNativeDisplay(CleverTapDisplayUnit displayUnit) {
        if (displayUnit.getContents() == null || displayUnit.getContents().isEmpty()) {
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }

        // Get the first content item
        CleverTapDisplayUnitContent content = displayUnit.getContents().get(0);

        // Create a simple native display view
        com.google.android.material.card.MaterialCardView cardView =
                new com.google.android.material.card.MaterialCardView(this);
        cardView.setLayoutParams(new android.view.ViewGroup.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        cardView.setCardBackgroundColor(android.graphics.Color.parseColor("#1C3B6F"));
        cardView.setRadius(16);
        cardView.setCardElevation(8);
        cardView.setUseCompatPadding(true);
        cardView.setContentPadding(16, 16, 16, 16);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Title
        TextView titleView = new TextView(this);
        titleView.setText(content.getTitle());
        titleView.setTextSize(18);
        titleView.setTextColor(android.graphics.Color.WHITE);
        titleView.setTypeface(null, android.graphics.Typeface.BOLD);
        titleView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Message
        TextView messageView = new TextView(this);
        messageView.setText(content.getMessage());
        messageView.setTextSize(14);
        messageView.setTextColor(android.graphics.Color.parseColor("#E0E0E0"));
        messageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        messageView.setPadding(0, 8, 0, 16);

        Button actionButton = new Button(this);

        // Get action URL - use getActionUrl() method
        String actionUrl = content.getActionUrl();

        // Set button text - you can customize this or use a default
        String buttonText = "View Details"; // Default text
        // You can also check for custom properties if set in CleverTap dashboard
        if (content.getMedia() != null) {
            // If there's media, you might want different text
            buttonText = "View Offer";
        }

        actionButton.setText(buttonText);
        actionButton.setBackgroundColor(Color.parseColor("#6CABE5"));
        actionButton.setTextColor(Color.WHITE);
        actionButton.setAllCaps(false);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        actionButton.setLayoutParams(buttonParams);


        actionButton.setOnClickListener(v -> {
            // Record click event
            if (cleverTapAPI != null) {
                Map<String, Object> clickProps = new HashMap<>();
                clickProps.put("Unit ID", displayUnit.getUnitID());
                clickProps.put("Title", content.getTitle());
                clickProps.put("Action", "Native Display Clicked");
                cleverTapAPI.pushEvent("Native Display Clicked", clickProps);

                // Notify CleverTap about the engagement
                cleverTapAPI.pushDisplayUnitClickedEventForID(displayUnit.getUnitID());
            }

            // Handle the action URL - use getActionUrl() not getActionLink()
            if (actionUrl != null && !actionUrl.isEmpty()) {
                try {
                    Intent intent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(actionUrl)
                    );
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "Could not open link", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No action URL available", Toast.LENGTH_SHORT).show();
            }
        });

        // Add views to layout
        layout.addView(titleView);
        layout.addView(messageView);
        layout.addView(actionButton);
        cardView.addView(layout);

        // Clear previous views and add new card
        nativeAdContainer.removeAllViews();
        nativeAdContainer.addView(cardView);
        nativeAdContainer.setVisibility(View.VISIBLE);

        // Notify CleverTap about the impression
        if (cleverTapAPI != null) {
            cleverTapAPI.pushDisplayUnitViewedEventForID(displayUnit.getUnitID());
        }
    }

    @Override
    public void onDisplayUnitsUpdated() {
        checkForPreloadedDisplayUnits();
    }
    @Override
    public void onPlayerClick(Player player, int position) {
        // Record player viewed event
        if (cleverTapAPI != null) {
            Map<String, Object> props = new HashMap<>();
            props.put("Player Name", player.getName());
            props.put("Position", player.getPosition());
            props.put("Jersey Number", player.getJerseyNumber());
            props.put("Rating", player.getRating());
            cleverTapAPI.pushEvent("Player Viewed", props);

            // Update user property - Last Viewed Player
            Map<String, Object> profileProps = new HashMap<>();
            profileProps.put("Last Viewed Player", player.getName());
            profileProps.put("Favorite Position", player.getPosition());
            cleverTapAPI.pushProfile(profileProps);
        }

        // Navigate to player detail
        Intent intent = new Intent(this, PlayerDetailActivity.class);
        intent.putExtra("player", player);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_inbox) {
            startActivity(new android.content.Intent(this, InboxActivity.class));
            return true;
        } else if (id == R.id.menu_refresh_ads) {
            // Refresh native ads by sending another event
            if (cleverTapAPI != null) {
                cleverTapAPI.pushEvent("Refresh Ads Requested");
                Toast.makeText(this, "Refreshing ads...", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.menu_logout) {
            // Record logout event
            if (cleverTapAPI != null) {
                cleverTapAPI.pushEvent("User Logged Out");
            }

            startActivity(new android.content.Intent(this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check for any new display units when returning to the activity
        checkForPreloadedDisplayUnits();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cleverTapAPI != null) {
            cleverTapAPI.setDisplayUnitListener(null);
        }
    }
}