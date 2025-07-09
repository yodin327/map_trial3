package com.example.save_map;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    // UI Components
    private MaterialToolbar toolbar;
    private MaterialCardView locationCard;
    private ProgressBar locationAccuracy;
    private TextView locationAddress;
    private FloatingActionButton refreshFab;
    private RecyclerView favoritesGrid;
    private RecyclerView stationList;

    // Data and Services
    private FusedLocationProviderClient fusedLocationClient;
    private FavoriteLocationAdapter favoriteAdapter;
    private TransitStationAdapter stationAdapter;
    private List<FavoriteLocation> favoriteLocations;
    private List<TransitStation> nearbyStations;
    private Location currentLocation;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        initializeComponents(view);
        setupRecyclerViews();
        setupClickListeners();
        initializeLocationService();
        loadSampleData();
        
        return view;
    }

    private void initializeComponents(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        locationCard = view.findViewById(R.id.current_location_card);
        locationAccuracy = view.findViewById(R.id.location_accuracy);
        locationAddress = view.findViewById(R.id.location_address);
        refreshFab = view.findViewById(R.id.refresh_fab);
        favoritesGrid = view.findViewById(R.id.favorites_grid);
        stationList = view.findViewById(R.id.station_list);
        
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    private void setupRecyclerViews() {
        // 즐겨찾기 그리드 설정
        favoriteLocations = new ArrayList<>();
        favoriteAdapter = new FavoriteLocationAdapter(favoriteLocations);
        favoritesGrid.setLayoutManager(new GridLayoutManager(getContext(), 2));
        favoritesGrid.setAdapter(favoriteAdapter);

        favoriteAdapter.setOnFavoriteClickListener(new FavoriteLocationAdapter.OnFavoriteClickListener() {
            @Override
            public void onFavoriteClick(FavoriteLocation favorite) {
                // 즐겨찾기 클릭 시 지도 화면으로 이동
                Toast.makeText(getContext(), favorite.getName() + "로 이동", Toast.LENGTH_SHORT).show();
                // TODO: 지도 Fragment로 전환하고 해당 위치로 이동
            }

            @Override
            public void onFavoriteLongClick(FavoriteLocation favorite) {
                // 길게 누르면 편집/삭제 옵션
                Toast.makeText(getContext(), favorite.getName() + " 편집", Toast.LENGTH_SHORT).show();
                // TODO: 편집 다이얼로그 표시
            }
        });

        // 근처 정류장 리스트 설정
        nearbyStations = new ArrayList<>();
        stationAdapter = new TransitStationAdapter(nearbyStations);
        stationList.setLayoutManager(new LinearLayoutManager(getContext()));
        stationList.setAdapter(stationAdapter);

        stationAdapter.setOnStationClickListener(station -> {
            // 정류장 클릭 시 상세 정보 표시
            Toast.makeText(getContext(), station.getName() + " 상세 정보", Toast.LENGTH_SHORT).show();
            // TODO: 정류장 상세 정보 다이얼로그 또는 지도 화면으로 이동
        });
    }

    private void setupClickListeners() {
        // 위치 새로고침 FAB
        refreshFab.setOnClickListener(v -> {
            // 회전 애니메이션
            ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(refreshFab, "rotation", 0f, 360f);
            rotateAnimator.setDuration(500);
            rotateAnimator.start();
            
            getCurrentLocation();
        });

        // 현재 위치 카드 클릭
        locationCard.setOnClickListener(v -> {
            if (currentLocation != null) {
                // 지도 화면으로 이동하여 현재 위치 표시
                Toast.makeText(getContext(), "지도에서 현재 위치 보기", Toast.LENGTH_SHORT).show();
                // TODO: MapsFragment로 전환하고 현재 위치로 이동
            }
        });
    }

    private void initializeLocationService() {
        // 위치 권한 확인 및 현재 위치 가져오기
        if (ActivityCompat.checkSelfPermission(requireContext(), 
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), 
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationAccuracy.setIndeterminate(true);
        locationAddress.setText("위치 정보 로딩 중...");

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        currentLocation = location;
                        updateLocationUI(location);
                        updateNearbyStations(location);
                    } else {
                        locationAddress.setText("위치를 가져올 수 없습니다");
                        locationAccuracy.setIndeterminate(false);
                    }
                })
                .addOnFailureListener(e -> {
                    locationAddress.setText("위치 서비스 오류");
                    locationAccuracy.setIndeterminate(false);
                    Toast.makeText(getContext(), "위치 가져오기 실패: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void updateLocationUI(Location location) {
        // 위치 정확도 업데이트
        locationAccuracy.setIndeterminate(false);
        if (location.hasAccuracy()) {
            int accuracy = Math.min(100, Math.max(0, (int)(100 - location.getAccuracy())));
            locationAccuracy.setProgress(accuracy);
        }

        // 주소 정보 업데이트 (실제로는 Geocoding API 사용)
        String address = String.format("위도: %.4f, 경도: %.4f", 
                location.getLatitude(), location.getLongitude());
        locationAddress.setText(address);
        
        // TODO: Google Places API Geocoding으로 실제 주소 가져오기
    }

    private void updateNearbyStations(Location location) {
        // 실제로는 Google Places API로 근처 대중교통 정류장 검색
        nearbyStations.clear();
        
        // BC 전체 지역 샘플 데이터 (실제 구현 시 Places API 사용)
        // 현재 위치 기반으로 적절한 지역 정류장 표시
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        
        // 밴쿠버 지역 (위도 49.0-49.5, 경도 -123.5 ~ -122.5)
        if (lat >= 49.0 && lat <= 49.5 && lng >= -123.5 && lng <= -122.5) {
            nearbyStations.add(new TransitStation("ST001", "메인 스트리트 정류장", 
                    "메인 스트리트 & 1번가, 밴쿠버", 
                    lat + 0.001, lng + 0.001,
                    new String[]{"99", "20", "25"}));
            
            nearbyStations.add(new TransitStation("ST002", "브로드웨이 정류장", 
                    "브로드웨이 & 2번가, 밴쿠버", 
                    lat + 0.002, lng - 0.001,
                    new String[]{"9", "14", "16"}));
        }
        // 빅토리아 지역 (위도 48.0-48.7, 경도 -123.8 ~ -123.0)
        else if (lat >= 48.0 && lat <= 48.7 && lng >= -123.8 && lng <= -123.0) {
            nearbyStations.add(new TransitStation("VT001", "더글라스 스트리트 터미널", 
                    "615 Douglas St, 빅토리아", 
                    lat + 0.001, lng + 0.001,
                    new String[]{"1", "4", "11"}));
            
            nearbyStations.add(new TransitStation("VT002", "업타운 쇼핑센터", 
                    "3551 Blanshard St, 빅토리아", 
                    lat + 0.002, lng - 0.001,
                    new String[]{"2", "27", "39"}));
        }
        // 켈로나 지역 (위도 49.5-50.0, 경도 -119.8 ~ -119.0)
        else if (lat >= 49.5 && lat <= 50.0 && lng >= -119.8 && lng <= -119.0) {
            nearbyStations.add(new TransitStation("KL001", "하비 애비뉴 터미널", 
                    "544 Harvey Ave, 켈로나", 
                    lat + 0.001, lng + 0.001,
                    new String[]{"8", "10", "15"}));
        }
        // 기본값 (BC 내 다른 지역)
        else {
            nearbyStations.add(new TransitStation("BC001", "지역 버스 터미널", 
                    "현재 위치 근처", 
                    lat + 0.001, lng + 0.001,
                    new String[]{"1", "2", "3"}));
        }
        
        // 추가 정류장 (지역별 특색 반영)
        if (lat >= 49.0 && lat <= 49.5 && lng >= -123.5 && lng <= -122.5) {
            // 밴쿠버 지역 추가 정류장
            nearbyStations.add(new TransitStation("ST003", "그랜빌 정류장", 
                    "그랜빌 스트리트 & 3번가, 밴쿠버", 
                    location.getLatitude() - 0.001, location.getLongitude() + 0.002,
                    new String[]{"5", "6", "19"}));
        } else if (lat >= 48.0 && lat <= 48.7 && lng >= -123.8 && lng <= -123.0) {
            // 빅토리아 지역 추가 정류장
            nearbyStations.add(new TransitStation("VT003", "베이 센터", 
                    "1150 Douglas St, 빅토리아", 
                    location.getLatitude() - 0.001, location.getLongitude() + 0.002,
                    new String[]{"6", "14", "21"}));
        }

        // 거리 계산 및 업데이트
        for (TransitStation station : nearbyStations) {
            float[] results = new float[1];
            Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                    station.getLatitude(), station.getLongitude(), results);
            station.setDistanceFromUser(results[0]);
            
            // 예상 도착 시간 설정 (실제로는 실시간 데이터 사용)
            int minutes = (int)(results[0] / 80); // 도보 속도 대략 80m/분
            station.setEstimatedArrival(minutes + "분 도보");
        }

        stationAdapter.updateStations(nearbyStations);
    }

    private void loadSampleData() {
        // BC 전체 지역 즐겨찾기 샘플 데이터
        favoriteLocations.clear();
        
        // 밴쿠버 지역
        favoriteLocations.add(new FavoriteLocation("집", "메인 스트리트 123, 밴쿠버", 
                49.2827, -123.1207, "home"));
        favoriteLocations.add(new FavoriteLocation("직장", "다운타운 오피스, 밴쿠버", 
                49.2861, -123.1139, "work"));
        favoriteLocations.add(new FavoriteLocation("UBC 캠퍼스", "University of British Columbia", 
                49.2606, -123.2460, "school"));
        favoriteLocations.add(new FavoriteLocation("메트로타운", "4700 Kingsway, 버나비", 
                49.2264, -123.0093, "custom"));
        
        // 빅토리아 지역
        favoriteLocations.add(new FavoriteLocation("빅토리아 다운타운", "615 Douglas St, 빅토리아", 
                48.4284, -123.3656, "custom"));
        favoriteLocations.add(new FavoriteLocation("UVic", "University of Victoria", 
                48.4634, -123.3117, "school"));
        
        // 켈로나 지역
        favoriteLocations.add(new FavoriteLocation("켈로나 공항", "5533 Airport Way, 켈로나", 
                49.9561, -119.3777, "custom"));
        
        // 기타 BC 주요 도시
        favoriteLocations.add(new FavoriteLocation("캠룹스 센터", "300 3rd Ave, 캠룹스", 
                50.6745, -120.3273, "custom"));

        favoriteAdapter.updateFavorites(favoriteLocations);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                         @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                locationAddress.setText("위치 권한이 필요합니다");
                Toast.makeText(getContext(), "위치 권한을 허용해주세요", Toast.LENGTH_LONG).show();
            }
        }
    }
}
