package com.example.save_map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    // UI Components
    private TextInputEditText searchInput;
    private TextInputEditText destinationInput;
    private ChipGroup filterChipGroup;
    private RecyclerView placeResultList;
    private RecyclerView routeResultList;
    
    // Data and Adapters
    private PlaceSearchAdapter placeAdapter;
    private RouteResultAdapter routeAdapter;
    private List<PlaceSearchResult> placeResults;
    private List<RouteSearchResult> routeResults;
    
    // API Services
    private GooglePlacesService placesService;
    private GoogleDirectionsService directionsService;
    
    // Search State
    private PlaceSearchResult selectedOrigin;
    private PlaceSearchResult selectedDestination;
    private boolean isSearchingOrigin = true;
    private Handler searchHandler;
    private String currentSearchFilters = "fastest"; // fastest, least_transfer, accessible
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        
        initializeComponents(view);
        setupAPIServices();
        setupRecyclerViews();
        setupSearchFunctionality();
        setupFilterChips();
        
        return view;
    }

    private void initializeComponents(View view) {
        searchInput = view.findViewById(R.id.search_input);
        destinationInput = view.findViewById(R.id.destination_input);
        filterChipGroup = view.findViewById(R.id.filter_chip_group);
        placeResultList = view.findViewById(R.id.place_result_list);
        routeResultList = view.findViewById(R.id.route_result_list);
        
        searchHandler = new Handler(Looper.getMainLooper());
    }

    private void setupAPIServices() {
        placesService = new GooglePlacesService(getContext());
        directionsService = new GoogleDirectionsService(getContext());
    }

    private void setupRecyclerViews() {
        // 장소 검색 결과 리스트
        placeResults = new ArrayList<>();
        placeAdapter = new PlaceSearchAdapter(placeResults);
        placeResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        placeResultList.setAdapter(placeAdapter);

        placeAdapter.setOnPlaceClickListener(place -> {
            // 먼저 장소 상세 정보를 가져옴
            placesService.getPlaceDetails(place.getPlaceId(), new GooglePlacesService.PlaceDetailsCallback() {
                @Override
                public void onDetailsRetrieved(PlaceSearchResult placeDetails) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            handlePlaceSelection(placeDetails);
                        });
                    }
                }

                @Override
                public void onDetailsError(String error) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            // 오류 시 기본 정보로 처리
                            handlePlaceSelection(place);
                        });
                    }
                }
            });
        });

        // 경로 검색 결과 리스트
        routeResults = new ArrayList<>();
        routeAdapter = new RouteResultAdapter(routeResults);
        routeResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        routeResultList.setAdapter(routeAdapter);

        routeAdapter.setOnRouteClickListener(new RouteResultAdapter.OnRouteClickListener() {
            @Override
            public void onRouteClick(RouteSearchResult route) {
                // 경로 상세 정보 표시 또는 지도 화면으로 이동
                Toast.makeText(getContext(), "경로 선택: " + route.getRouteTitle(), Toast.LENGTH_SHORT).show();
                // TODO: MapsFragment로 이동하여 경로 표시
            }

            @Override
            public void onRouteSave(RouteSearchResult route) {
                // 히스토리에 저장
                saveRouteToHistory(route);
                Toast.makeText(getContext(), "경로가 저장되었습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSearchFunctionality() {
        // 출발지 검색
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isSearchingOrigin = true;
                performPlaceSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        searchInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                isSearchingOrigin = true;
                if (searchInput.getText().toString().length() > 0) {
                    placeResultList.setVisibility(View.VISIBLE);
                    routeResultList.setVisibility(View.GONE);
                }
            }
        });

        // 목적지 검색
        destinationInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isSearchingOrigin = false;
                performPlaceSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        destinationInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                isSearchingOrigin = false;
                if (destinationInput.getText().toString().length() > 0) {
                    placeResultList.setVisibility(View.VISIBLE);
                    routeResultList.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setupFilterChips() {
        Chip fastestChip = filterChipGroup.findViewById(R.id.chip_fastest);
        Chip leastTransferChip = filterChipGroup.findViewById(R.id.chip_least_transfer);
        Chip accessibleChip = filterChipGroup.findViewById(R.id.chip_accessible);

        fastestChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                currentSearchFilters = "fastest";
                if (selectedOrigin != null && selectedDestination != null) {
                    searchRoutes();
                }
            }
        });

        leastTransferChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                currentSearchFilters = "least_transfer";
                if (selectedOrigin != null && selectedDestination != null) {
                    searchRoutes();
                }
            }
        });

        accessibleChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                currentSearchFilters = "accessible";
                if (selectedOrigin != null && selectedDestination != null) {
                    searchRoutes();
                }
            }
        });

        // 기본값: 최단시간 선택
        fastestChip.setChecked(true);
    }

    private void handlePlaceSelection(PlaceSearchResult place) {
        if (isSearchingOrigin) {
            selectedOrigin = place;
            searchInput.setText(place.getName());
            Toast.makeText(getContext(), "출발지: " + place.getName(), Toast.LENGTH_SHORT).show();
        } else {
            selectedDestination = place;
            destinationInput.setText(place.getName());
            Toast.makeText(getContext(), "목적지: " + place.getName(), Toast.LENGTH_SHORT).show();
        }
        
        // 장소 결과 숨기기
        placeResultList.setVisibility(View.GONE);
        
        // 출발지와 목적지가 모두 선택되면 경로 검색
        if (selectedOrigin != null && selectedDestination != null) {
            searchRoutes();
        }
    }

    private void performPlaceSearch(String query) {
        if (query.length() < 2) {
            placeResultList.setVisibility(View.GONE);
            return;
        }

        // 검색 디바운싱 (500ms 딜레이)
        searchHandler.removeCallbacksAndMessages(null);
        searchHandler.postDelayed(() -> {
            if (getContext() != null) {
                // 실제 Google Places API 호출
                searchPlacesWithAPI(query);
            }
        }, 500);
    }

    private void searchPlacesWithAPI(String query) {
        // Google Places API 호출
        placesService.searchPlaces(query, new GooglePlacesService.PlaceSearchCallback() {
            @Override
            public void onSearchComplete(List<PlaceSearchResult> results) {
                // UI 스레드에서 실행
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        placeResults.clear();
                        placeResults.addAll(results);
                        placeAdapter.updatePlaces(placeResults);
                        placeResultList.setVisibility(placeResults.size() > 0 ? View.VISIBLE : View.GONE);
                        routeResultList.setVisibility(View.GONE);
                    });
                }
            }

            @Override
            public void onSearchError(String error) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        // 오류 시 샘플 데이터로 폴백
                        searchPlacesWithSampleData(query);
                    });
                }
            }
        });
    }

    private void searchPlacesWithSampleData(String query) {
        // BC 전체 지역 샘플 데이터 검색
        placeResults.clear();
        
        // 밴쿠버 지역
        if (query.toLowerCase().contains("메인") || query.toLowerCase().contains("main")) {
            placeResults.add(new PlaceSearchResult("place1", "메인 스트리트 정류장", 
                    "123 메인 스트리트, 밴쿠버, BC", 49.2827, -123.1207, "transit_station"));
            placeResults.add(new PlaceSearchResult("place2", "메인 스트리트 도서관", 
                    "456 메인 스트리트, 밴쿠버, BC", 49.2830, -123.1210, "library"));
        } else if (query.toLowerCase().contains("브로드") || query.toLowerCase().contains("broadway")) {
            placeResults.add(new PlaceSearchResult("place3", "브로드웨이 정류장", 
                    "789 브로드웨이, 밴쿠버, BC", 49.2861, -123.1139, "transit_station"));
            placeResults.add(new PlaceSearchResult("place4", "브로드웨이 쇼핑센터", 
                    "321 브로드웨이, 밴쿠버, BC", 49.2865, -123.1142, "shopping_mall"));
        } else if (query.toLowerCase().contains("다운타운") || query.toLowerCase().contains("downtown")) {
            placeResults.add(new PlaceSearchResult("place5", "다운타운 역", 
                    "100 다운타운, 밴쿠버, BC", 49.2861, -123.1139, "transit_station"));
        }
        // 빅토리아 지역
        else if (query.toLowerCase().contains("빅토리아") || query.toLowerCase().contains("victoria")) {
            placeResults.add(new PlaceSearchResult("place6", "빅토리아 다운타운 터미널", 
                    "615 Douglas St, 빅토리아, BC", 48.4284, -123.3656, "transit_station"));
            placeResults.add(new PlaceSearchResult("place7", "빅토리아 대학교", 
                    "3800 Finnerty Rd, 빅토리아, BC", 48.4634, -123.3117, "university"));
        }
        // 켈로나 지역
        else if (query.toLowerCase().contains("켈로나") || query.toLowerCase().contains("kelowna")) {
            placeResults.add(new PlaceSearchResult("place8", "켈로나 버스 터미널", 
                    "544 Harvey Ave, 켈로나, BC", 49.8880, -119.4960, "transit_station"));
            placeResults.add(new PlaceSearchResult("place9", "켈로나 공항", 
                    "5533 Airport Way, 켈로나, BC", 49.9561, -119.3777, "airport"));
        }
        // 캠룹스 지역
        else if (query.toLowerCase().contains("캠룹스") || query.toLowerCase().contains("kamloops")) {
            placeResults.add(new PlaceSearchResult("place10", "캠룹스 트랜짓 센터", 
                    "300 3rd Ave, 캠룹스, BC", 50.6745, -120.3273, "transit_station"));
        }
        // 프린스 조지 지역
        else if (query.toLowerCase().contains("프린스") || query.toLowerCase().contains("prince george")) {
            placeResults.add(new PlaceSearchResult("place11", "프린스 조지 버스 터미널", 
                    "1566 12th Ave, 프린스 조지, BC", 53.9171, -122.7497, "transit_station"));
        }
        // 서리/랭리 지역
        else if (query.toLowerCase().contains("서리") || query.toLowerCase().contains("surrey") || 
                query.toLowerCase().contains("랭리") || query.toLowerCase().contains("langley")) {
            placeResults.add(new PlaceSearchResult("place12", "서리 센트럴 역", 
                    "10153 King George Blvd, 서리, BC", 49.1897, -122.8473, "transit_station"));
            placeResults.add(new PlaceSearchResult("place13", "랭리 시티 센터", 
                    "20399 Douglas Crescent, 랭리, BC", 49.0755, -122.6091, "shopping_mall"));
        }

        placeAdapter.updatePlaces(placeResults);
        placeResultList.setVisibility(placeResults.size() > 0 ? View.VISIBLE : View.GONE);
        routeResultList.setVisibility(View.GONE);
    }

    private void searchRoutes() {
        if (selectedOrigin == null || selectedDestination == null) {
            return;
        }

        // 실제 Google Directions API 호출
        String travelMode = getTravelModeFromFilter();
        
        directionsService.searchMultipleRouteOptions(selectedOrigin, selectedDestination, 
            new RouteSearchCallback() {
                @Override
                public void onRoutesFound(List<RouteSearchResult> routes) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            routeResults.clear();
                            routeResults.addAll(routes);
                            
                            // 필터에 따라 정렬
                            filterAndSortRoutes();
                            
                            routeAdapter.updateRoutes(routeResults);
                            placeResultList.setVisibility(View.GONE);
                            routeResultList.setVisibility(View.VISIBLE);
                        });
                    }
                }

                @Override
                public void onRouteSearchError(String error) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });
    }

    private String getTravelModeFromFilter() {
        switch (currentSearchFilters) {
            case "fastest":
                return "transit";
            case "least_transfer":
                return "transit";
            case "accessible":
                return "transit";
            default:
                return "transit";
        }
    }

    private void filterAndSortRoutes() {
        switch (currentSearchFilters) {
            case "fastest":
                routeResults.sort((r1, r2) -> r1.getDurationMinutes() - r2.getDurationMinutes());
                break;
            case "least_transfer":
                routeResults.sort((r1, r2) -> r1.getTransferCount() - r2.getTransferCount());
                break;
            case "accessible":
                // 접근성 우선 정렬
                routeResults.sort((r1, r2) -> {
                    if (r1.isAccessible() && !r2.isAccessible()) return -1;
                    if (!r1.isAccessible() && r2.isAccessible()) return 1;
                    return r1.getDurationMinutes() - r2.getDurationMinutes();
                });
                break;
        }
    }

    private void generateSampleRoutes() {
        routeResults.clear();

        // 샘플 경로 결과 생성
        RouteSearchResult route1 = new RouteSearchResult(selectedOrigin, selectedDestination, 
                "25분", "3.2km", "도보 + 버스");
        route1.setTransferCount(1);
        route1.setDepartureTime("14:30");
        route1.setArrivalTime("14:55");
        route1.setCost("요금: $3.75");
        route1.setCarbonFootprint(120);

        RouteSearchResult route2 = new RouteSearchResult(selectedOrigin, selectedDestination, 
                "32분", "4.1km", "지하철");
        route2.setTransferCount(0);
        route2.setDepartureTime("14:35");
        route2.setArrivalTime("15:07");
        route2.setCost("요금: $3.75");
        route2.setCarbonFootprint(150);

        RouteSearchResult route3 = new RouteSearchResult(selectedOrigin, selectedDestination, 
                "18분", "2.8km", "도보 + 지하철");
        route3.setTransferCount(1);
        route3.setDepartureTime("14:42");
        route3.setArrivalTime("15:00");
        route3.setCost("요금: $3.75");
        route3.setCarbonFootprint(95);

        // 필터에 따라 정렬
        switch (currentSearchFilters) {
            case "fastest":
                routeResults.add(route3); // 18분
                routeResults.add(route1); // 25분
                routeResults.add(route2); // 32분
                break;
            case "least_transfer":
                routeResults.add(route2); // 직행
                routeResults.add(route1); // 1회 환승
                routeResults.add(route3); // 1회 환승
                break;
            case "accessible":
                // 접근성 우선 - 모든 경로를 접근 가능으로 표시
                routeResults.add(route1);
                routeResults.add(route2);
                routeResults.add(route3);
                break;
        }

        routeAdapter.updateRoutes(routeResults);
    }

    private void saveRouteToHistory(RouteSearchResult route) {
        // HistoryFragment의 데이터베이스에 저장
        if (getContext() == null) return;
        
        // 백그라운드 스레드에서 DB 저장
        new Thread(() -> {
            try {
                AppDatabase db = AppDatabase.getDatabase(getContext());
                
                String currentDate = java.text.DateFormat.getDateInstance().format(new java.util.Date());
                String currentTime = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(new java.util.Date());
                
                HistoryEntity historyItem = new HistoryEntity();
                historyItem.setTitle("경로 검색: " + route.getRouteTitle());
                historyItem.setDescription(route.getTransitMode() + " • " + route.getDuration() + " • " + route.getTransferText());
                historyItem.setDate(currentDate);
                historyItem.setTime(currentTime);
                historyItem.setType("route_search");
                historyItem.setFavorite(false);
                
                // 추가 정보를 JSON 형태로 저장 (실제 구현에서는 별도 테이블 생성 권장)
                String details = String.format(
                    "출발지: %s\n목적지: %s\n소요시간: %s\n거리: %s\n요금: %s",
                    route.getOrigin().getName(),
                    route.getDestination().getName(),
                    route.getDuration(),
                    route.getDistance(),
                    route.getCost()
                );
                historyItem.setDescription(details);
                
                db.historyDao().insert(historyItem);
                
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "검색 결과가 히스토리에 저장되었습니다", Toast.LENGTH_SHORT).show();
                    });
                }
                
            } catch (Exception e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "저장 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (directionsService != null) {
            directionsService.shutdown();
        }
    }
}
