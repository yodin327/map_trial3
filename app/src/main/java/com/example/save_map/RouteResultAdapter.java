package com.example.save_map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import java.util.List;

public class RouteResultAdapter extends RecyclerView.Adapter<RouteResultAdapter.RouteViewHolder> {
    private List<RouteSearchResult> routes;
    private OnRouteClickListener listener;

    public interface OnRouteClickListener {
        void onRouteClick(RouteSearchResult route);
        void onRouteSave(RouteSearchResult route);
    }

    public RouteResultAdapter(List<RouteSearchResult> routes) {
        this.routes = routes;
    }

    public void setOnRouteClickListener(OnRouteClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route_result, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        RouteSearchResult route = routes.get(position);
        
        holder.routeTitle.setText(route.getRouteTitle());
        holder.duration.setText(route.getDuration());
        holder.distance.setText(route.getDistance());
        holder.transitMode.setText(route.getTransitMode());
        holder.transferInfo.setText(route.getTransferText());
        holder.cost.setText(route.getCost());
        
        // 시간 정보
        if (route.getDepartureTime() != null && route.getArrivalTime() != null) {
            holder.timeInfo.setVisibility(View.VISIBLE);
            holder.timeInfo.setText(route.getDepartureTime() + " → " + route.getArrivalTime());
        } else {
            holder.timeInfo.setVisibility(View.GONE);
        }

        // 접근성 아이콘
        holder.accessibleIcon.setVisibility(route.isAccessible() ? View.VISIBLE : View.GONE);

        // 환경 정보
        if (route.getCarbonFootprint() > 0) {
            holder.carbonChip.setVisibility(View.VISIBLE);
            holder.carbonChip.setText("CO₂ " + route.getCarbonFootprint() + "g 절약");
        } else {
            holder.carbonChip.setVisibility(View.GONE);
        }

        // 클릭 리스너
        holder.card.setOnClickListener(v -> {
            if (listener != null) listener.onRouteClick(route);
        });

        holder.saveButton.setOnClickListener(v -> {
            if (listener != null) listener.onRouteSave(route);
        });
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public void updateRoutes(List<RouteSearchResult> newRoutes) {
        this.routes = newRoutes;
        notifyDataSetChanged();
    }

    public static class RouteViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView card;
        TextView routeTitle;
        TextView duration;
        TextView distance;
        TextView transitMode;
        TextView transferInfo;
        TextView timeInfo;
        TextView cost;
        ImageView accessibleIcon;
        ImageView saveButton;
        Chip carbonChip;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.route_card);
            routeTitle = itemView.findViewById(R.id.route_title);
            duration = itemView.findViewById(R.id.route_duration);
            distance = itemView.findViewById(R.id.route_distance);
            transitMode = itemView.findViewById(R.id.route_transit_mode);
            transferInfo = itemView.findViewById(R.id.route_transfer_info);
            timeInfo = itemView.findViewById(R.id.route_time_info);
            cost = itemView.findViewById(R.id.route_cost);
            accessibleIcon = itemView.findViewById(R.id.route_accessible_icon);
            saveButton = itemView.findViewById(R.id.route_save_button);
            carbonChip = itemView.findViewById(R.id.route_carbon_chip);
        }
    }
}
