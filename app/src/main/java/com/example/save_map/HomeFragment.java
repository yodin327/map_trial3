package com.example.save_map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        MaterialCardView locationCard = view.findViewById(R.id.current_location_card);
        ProgressBar locationAccuracy = view.findViewById(R.id.location_accuracy);
        TextView locationAddress = view.findViewById(R.id.location_address);
        FloatingActionButton refreshFab = view.findViewById(R.id.refresh_fab);
        RecyclerView favoritesGrid = view.findViewById(R.id.favorites_grid);
        RecyclerView stationList = view.findViewById(R.id.station_list);
        // TODO: 어댑터, 이벤트, 위치/정류장 데이터 연동 구현
        return view;
    }
}
