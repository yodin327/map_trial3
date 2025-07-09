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

public class FavoriteLocationAdapter extends RecyclerView.Adapter<FavoriteLocationAdapter.FavoriteViewHolder> {
    private List<FavoriteLocation> favorites;
    private OnFavoriteClickListener listener;

    public interface OnFavoriteClickListener {
        void onFavoriteClick(FavoriteLocation favorite);
        void onFavoriteLongClick(FavoriteLocation favorite);
    }

    public FavoriteLocationAdapter(List<FavoriteLocation> favorites) {
        this.favorites = favorites;
    }

    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_location, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoriteLocation favorite = favorites.get(position);
        holder.name.setText(favorite.getName());
        holder.address.setText(favorite.getAddress());
        
        // 아이콘 설정
        int iconRes;
        switch (favorite.getIconType()) {
            case "home":
                iconRes = android.R.drawable.ic_dialog_map;
                break;
            case "work":
                iconRes = android.R.drawable.ic_dialog_info;
                break;
            case "school":
                iconRes = android.R.drawable.ic_menu_info_details;
                break;
            default:
                iconRes = android.R.drawable.ic_menu_mapmode;
                break;
        }
        holder.icon.setImageResource(iconRes);

        // 클릭 리스너
        holder.card.setOnClickListener(v -> {
            if (listener != null) listener.onFavoriteClick(favorite);
        });

        holder.card.setOnLongClickListener(v -> {
            if (listener != null) listener.onFavoriteLongClick(favorite);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public void updateFavorites(List<FavoriteLocation> newFavorites) {
        this.favorites = newFavorites;
        notifyDataSetChanged();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView card;
        ImageView icon;
        TextView name;
        TextView address;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.favorite_card);
            icon = itemView.findViewById(R.id.favorite_icon);
            name = itemView.findViewById(R.id.favorite_name);
            address = itemView.findViewById(R.id.favorite_address);
        }
    }
}
