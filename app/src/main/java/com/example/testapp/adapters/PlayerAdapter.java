package com.example.testapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.testapp.R;
import com.example.testapp.models.Player;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private List<Player> playerList;
    private OnPlayerClickListener listener;

    public interface OnPlayerClickListener {
        void onDisplayUnitsUpdated();

        void onPlayerClick(Player player, int position);
    }

    public PlayerAdapter(List<Player> playerList, OnPlayerClickListener listener) {
        this.playerList = playerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = playerList.get(position);

        holder.textPlayerName.setText(player.getName());
        holder.textPosition.setText(player.getPosition());
        holder.textNationality.setText(player.getNationality());
        holder.textJerseyNumber.setText("#" + player.getJerseyNumber());
        holder.ratingBar.setRating(player.getRating());

        // Load image using Glide
        Glide.with(holder.itemView.getContext())
                .load(player.getImageResource())
                .placeholder(R.drawable.ic_player_placeholder)
                .into(holder.imagePlayer);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlayerClick(player, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    static class PlayerViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePlayer;
        TextView textPlayerName;
        TextView textPosition;
        TextView textNationality;
        TextView textJerseyNumber;
        RatingBar ratingBar;

        PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePlayer = itemView.findViewById(R.id.imagePlayer);
            textPlayerName = itemView.findViewById(R.id.textPlayerName);
            textPosition = itemView.findViewById(R.id.textPosition);
            textNationality = itemView.findViewById(R.id.textNationality);
            textJerseyNumber = itemView.findViewById(R.id.textJerseyNumber);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}