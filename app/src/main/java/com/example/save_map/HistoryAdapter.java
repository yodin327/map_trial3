package com.example.save_map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private List<HistoryItem> items;

    public HistoryAdapter(List<HistoryItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryItem item = items.get(position);
        holder.date.setText(item.date + " " + item.time);
        holder.route.setText(item.startLocation + " → " + item.endLocation);
        holder.favorite.setImageResource(item.isFavorite ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
        holder.favorite.setOnClickListener(v -> {
            item.setFavorite(!item.isFavorite());
            notifyItemChanged(position);
        });
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) longClickListener.onItemLongClick(item, position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<HistoryItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(HistoryItem item);
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(HistoryItem item, int position);
    }
    private OnItemLongClickListener longClickListener;
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView route;
        ImageView favorite;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.history_date);
            route = itemView.findViewById(R.id.history_route);
            favorite = itemView.findViewById(R.id.history_favorite);
        }
    }
}
