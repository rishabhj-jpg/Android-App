package com.example.testapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.CleverTapAPI;
import com.example.testapp.models.Player;
import java.util.HashMap;
import java.util.Map;

public class PlayerDetailActivity extends AppCompatActivity {

    private CleverTapAPI cleverTapAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detail);

        cleverTapAPI = CleverTapAPI.getDefaultInstance(this);

        // Get player object from intent
        Player player = getIntent().getParcelableExtra("player");

        if (player != null) {
            // Set up toolbar
            setSupportActionBar(findViewById(R.id.toolbar));
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(player.getName());
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            // Initialize views
            ImageView imagePlayer = findViewById(R.id.imagePlayerDetail);
            TextView textName = findViewById(R.id.textPlayerNameDetail);
            TextView textPosition = findViewById(R.id.textPositionDetail);
            TextView textNationality = findViewById(R.id.textNationalityDetail);
            TextView textJerseyNumber = findViewById(R.id.textJerseyNumberDetail);
            RatingBar ratingBar = findViewById(R.id.ratingBarDetail);

            // Set player data
            textName.setText(player.getName());
            textPosition.setText(player.getPosition());
            textNationality.setText(player.getNationality());
            textJerseyNumber.setText(String.valueOf(player.getJerseyNumber()));
            ratingBar.setRating(player.getRating());

            // Load image
            Glide.with(this)
                    .load(player.getImageResource())
                    .into(imagePlayer);

            // Record event for viewing player details
            recordPlayerDetailEvent(player);
        }
    }

    private void recordPlayerDetailEvent(Player player) {
        if (cleverTapAPI != null) {
            Map<String, Object> props = new HashMap<>();
            props.put("Player Name", player.getName());
            props.put("Position", player.getPosition());
            props.put("Nationality", player.getNationality());
            props.put("Jersey Number", player.getJerseyNumber());
            props.put("Rating", player.getRating());
            cleverTapAPI.pushEvent("Player Detail Viewed", props);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}