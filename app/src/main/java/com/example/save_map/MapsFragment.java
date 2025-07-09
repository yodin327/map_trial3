package com.example.save_map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        
        // 위치 서비스 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        FloatingActionButton fabMyLocation = view.findViewById(R.id.fab_my_location);
        fabMyLocation.setOnClickListener(v -> {
            getCurrentLocationAndMoveCamera();
        });
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);
        
        // BC 전체를 보여주는 초기 카메라 위치 설정
        // BC 중심부 (캠룹스 근처)로 설정
        LatLng bcCenter = new LatLng(54.0, -126.5); // BC 중심 좌표
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bcCenter, 6.0f)); // 줌 레벨 6으로 BC 전체 보기
        
        // BC 주요 도시들에 마커 추가
        addBCCityMarkers();
        
        // 사용자 위치가 있으면 해당 위치로 이동
        getCurrentLocationAndMoveCamera();
    }

    private void addBCCityMarkers() {
        // BC 주요 도시들에 마커 추가
        mMap.addMarker(new MarkerOptions()
            .position(new LatLng(49.2827, -123.1207))
            .title("Vancouver")
            .snippet("Lower Mainland Transit Hub"));
            
        mMap.addMarker(new MarkerOptions()
            .position(new LatLng(48.4284, -123.3656))
            .title("Victoria")
            .snippet("Vancouver Island Transit"));
            
        mMap.addMarker(new MarkerOptions()
            .position(new LatLng(49.8880, -119.4960))
            .title("Kelowna")
            .snippet("Interior Transit Hub"));
            
        mMap.addMarker(new MarkerOptions()
            .position(new LatLng(50.6745, -120.3273))
            .title("Kamloops")
            .snippet("Thompson Valley Transit"));
            
        mMap.addMarker(new MarkerOptions()
            .position(new LatLng(53.9171, -122.7497))
            .title("Prince George")
            .snippet("Northern BC Transit"));
    }

    private void getCurrentLocationAndMoveCamera() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12.0f));
                        mMap.addMarker(new MarkerOptions()
                            .position(userLocation)
                            .title("내 위치")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    }
                });
        }
    }
}
