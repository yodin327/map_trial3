package com.example.save_map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        FloatingActionButton fabMyLocation = view.findViewById(R.id.fab_my_location);
        fabMyLocation.setOnClickListener(v -> {
            if (mMap != null) {
                // 예시: 밴쿠버로 이동
                LatLng vancouver = new LatLng(49.2827, -123.1207);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(vancouver, 13f));
            }
        });
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);
        LatLng vancouver = new LatLng(49.2827, -123.1207);
        mMap.addMarker(new MarkerOptions().position(vancouver).title("Vancouver"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vancouver, 12f));
    }
}
