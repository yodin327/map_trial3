package com.example.save_map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

public class PlaceSearchAdapter extends RecyclerView.Adapter<PlaceSearchAdapter.PlaceViewHolder> {
    private List<PlaceSearchResult> places;
    private OnPlaceClickListener listener;

    public interface OnPlaceClickListener {
        void onPlaceClick(PlaceSearchResult place);
    }

    public PlaceSearchAdapter(List<PlaceSearchResult> places) {
        this.places = places;
    }

    public void setOnPlaceClickListener(OnPlaceClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_search, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        PlaceSearchResult place = places.get(position);
        holder.name.setText(place.getName());
        holder.address.setText(place.getAddress());
        
        // 장소 유형별 아이콘
        if (place.isTransitStation()) {
            holder.icon.setImageResource(android.R.drawable.ic_menu_mapmode);
        } else {
            holder.icon.setImageResource(android.R.drawable.ic_dialog_map);
        }

        // 평점 표시 (있는 경우)
        if (place.getRating() > 0) {
            holder.rating.setVisibility(View.VISIBLE);
            holder.rating.setText(String.format("★ %.1f", place.getRating()));
        } else {
            holder.rating.setVisibility(View.GONE);
        }

        // 클릭 리스너
        holder.card.setOnClickListener(v -> {
            if (listener != null) listener.onPlaceClick(place);
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public void updatePlaces(List<PlaceSearchResult> newPlaces) {
        this.places = newPlaces;
        notifyDataSetChanged();
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView card;
        ImageView icon;
        TextView name;
        TextView address;
        TextView rating;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.place_card);
            icon = itemView.findViewById(R.id.place_icon);
            name = itemView.findViewById(R.id.place_name);
            address = itemView.findViewById(R.id.place_address);
            rating = itemView.findViewById(R.id.place_rating);
        }
    }
}
