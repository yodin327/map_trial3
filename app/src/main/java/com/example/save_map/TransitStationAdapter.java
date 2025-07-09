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

public class TransitStationAdapter extends RecyclerView.Adapter<TransitStationAdapter.StationViewHolder> {
    private List<TransitStation> stations;
    private OnStationClickListener listener;

    public interface OnStationClickListener {
        void onStationClick(TransitStation station);
    }

    public TransitStationAdapter(List<TransitStation> stations) {
        this.stations = stations;
    }

    public void setOnStationClickListener(OnStationClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transit_station, parent, false);
        return new StationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StationViewHolder holder, int position) {
        TransitStation station = stations.get(position);
        holder.name.setText(station.getName());
        holder.description.setText(station.getDescription());
        holder.routes.setText("노선: " + station.getRoutesString());
        holder.distance.setText(String.format("%.0fm", station.getDistanceFromUser()));
        holder.estimatedTime.setText(station.getEstimatedArrival());

        // 접근성 아이콘
        holder.accessibleIcon.setVisibility(station.isAccessible() ? View.VISIBLE : View.GONE);

        // 클릭 리스너
        holder.card.setOnClickListener(v -> {
            if (listener != null) listener.onStationClick(station);
        });
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }

    public void updateStations(List<TransitStation> newStations) {
        this.stations = newStations;
        notifyDataSetChanged();
    }

    public static class StationViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView card;
        TextView name;
        TextView description;
        TextView routes;
        TextView distance;
        TextView estimatedTime;
        ImageView accessibleIcon;

        public StationViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.station_card);
            name = itemView.findViewById(R.id.station_name);
            description = itemView.findViewById(R.id.station_description);
            routes = itemView.findViewById(R.id.station_routes);
            distance = itemView.findViewById(R.id.station_distance);
            estimatedTime = itemView.findViewById(R.id.station_estimated_time);
            accessibleIcon = itemView.findViewById(R.id.station_accessible_icon);
        }
    }
}
