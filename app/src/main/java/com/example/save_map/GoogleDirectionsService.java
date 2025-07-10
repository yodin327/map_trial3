package com.example.save_map;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Google Directions API를 사용한 경로 검색 서비스
 */
public class GoogleDirectionsService {
    private static final String TAG = "GoogleDirectionsService";
    private static final String DIRECTIONS_API_URL = "https://maps.googleapis.com/maps/api/directions/json";
    
    private final Context context;
    private final OkHttpClient httpClient;
    private final ExecutorService executorService;
    private final String apiKey;

    public interface RouteSearchCallback {
        void onRoutesFound(java.util.List<RouteSearchResult> routes);
        void onRouteSearchError(String error);
    }

    public GoogleDirectionsService(Context context) {
        this.context = context;
        this.httpClient = new OkHttpClient();
        this.executorService = Executors.newFixedThreadPool(3);
        this.apiKey = context.getString(R.string.google_maps_key);
    }

    /**
     * 대중교통 경로 검색
     */
    public void searchTransitRoutes(PlaceSearchResult origin, PlaceSearchResult destination, 
                                  String travelMode, RouteSearchCallback callback) {
        
        executorService.execute(() -> {
            try {
                String originStr = origin.getLatitude() + "," + origin.getLongitude();
                String destinationStr = destination.getLatitude() + "," + destination.getLongitude();
                
                String url = buildDirectionsUrl(originStr, destinationStr, travelMode);
                
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                httpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "Directions API call failed", e);
                        callback.onRouteSearchError("경로 검색 중 네트워크 오류가 발생했습니다");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            callback.onRouteSearchError("경로 검색 실패: " + response.code());
                            return;
                        }

                        String responseBody = response.body().string();
                        parseDirectionsResponse(responseBody, origin, destination, callback);
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, "Error building directions request", e);
                callback.onRouteSearchError("경로 검색 요청 생성 중 오류가 발생했습니다");
            }
        });
    }

    /**
     * 여러 교통수단 옵션으로 경로 검색
     */
    public void searchMultipleRouteOptions(PlaceSearchResult origin, PlaceSearchResult destination,
                                         RouteSearchCallback callback) {
        // 단순화된 접근: 하나씩 검색하고 결과 합치기
        searchTransitRoutes(origin, destination, "transit", callback);
    }

    private String buildDirectionsUrl(String origin, String destination, String mode) {
        StringBuilder url = new StringBuilder(DIRECTIONS_API_URL);
        url.append("?origin=").append(URLEncoder.encode(origin, StandardCharsets.UTF_8));
        url.append("&destination=").append(URLEncoder.encode(destination, StandardCharsets.UTF_8));
        url.append("&mode=").append(mode);
        url.append("&key=").append(apiKey);
        url.append("&region=ca"); // 캐나다 지역
        url.append("&language=ko"); // 한국어
        url.append("&components=administrative_area:BC"); // BC 주 제한
        
        if ("transit".equals(mode)) {
            url.append("&transit_mode=bus|subway");
            url.append("&alternatives=true"); // 대안 경로 요청
        }
        
        return url.toString();
    }

    private void parseDirectionsResponse(String responseBody, PlaceSearchResult origin, 
                                       PlaceSearchResult destination, RouteSearchCallback callback) {
        try {
            JSONObject json = new JSONObject(responseBody);
            String status = json.getString("status");
            
            if (!"OK".equals(status)) {
                String errorMessage = json.optString("error_message", "경로를 찾을 수 없습니다");
                callback.onRouteSearchError(errorMessage);
                return;
            }

            JSONArray routes = json.getJSONArray("routes");
            List<RouteSearchResult> routeResults = new ArrayList<>();

            for (int i = 0; i < routes.length(); i++) {
                JSONObject route = routes.getJSONObject(i);
                RouteSearchResult routeResult = parseRoute(route, origin, destination);
                if (routeResult != null) {
                    routeResults.add(routeResult);
                }
            }

            callback.onRoutesFound(routeResults);

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing directions response", e);
            callback.onRouteSearchError("응답 파싱 중 오류가 발생했습니다");
        }
    }

    private RouteSearchResult parseRoute(JSONObject route, PlaceSearchResult origin, 
                                       PlaceSearchResult destination) {
        try {
            JSONArray legs = route.getJSONArray("legs");
            if (legs.length() == 0) return null;
            
            JSONObject leg = legs.getJSONObject(0);
            
            // 기본 정보
            String duration = leg.getJSONObject("duration").getString("text");
            String distance = leg.getJSONObject("distance").getString("text");
            int durationValue = leg.getJSONObject("duration").getInt("value"); // 초 단위
            
            // 교통수단 정보
            String transitMode = extractTransitMode(leg);
            
            RouteSearchResult result = new RouteSearchResult(origin, destination, 
                    duration, distance, transitMode);
            
            result.setDurationMinutes(durationValue / 60);
            
            // 대중교통 상세 정보
            if (leg.has("steps")) {
                parseTransitDetails(leg.getJSONArray("steps"), result);
            }
            
            // 요금 정보
            if (route.has("fare")) {
                JSONObject fare = route.getJSONObject("fare");
                result.setCost("요금: " + fare.getString("text"));
            } else {
                result.setCost("요금: $3.75"); // BC Transit 기본 요금
            }
            
            return result;
            
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing route", e);
            return null;
        }
    }

    private String extractTransitMode(JSONObject leg) {
        try {
            JSONArray steps = leg.getJSONArray("steps");
            List<String> modes = new ArrayList<>();
            
            for (int i = 0; i < steps.length(); i++) {
                JSONObject step = steps.getJSONObject(i);
                String travelMode = step.getString("travel_mode");
                
                if ("TRANSIT".equals(travelMode)) {
                    JSONObject transitDetails = step.getJSONObject("transit_details");
                    JSONObject line = transitDetails.getJSONObject("line");
                    String vehicle = line.getJSONObject("vehicle").getString("name");
                    
                    if (vehicle.toLowerCase().contains("bus")) {
                        modes.add("버스");
                    } else if (vehicle.toLowerCase().contains("subway") || 
                              vehicle.toLowerCase().contains("rail")) {
                        modes.add("지하철");
                    } else {
                        modes.add("대중교통");
                    }
                } else if ("WALKING".equals(travelMode)) {
                    modes.add("도보");
                }
            }
            
            if (modes.isEmpty()) {
                return "대중교통";
            } else if (modes.size() == 1) {
                return modes.get(0);
            } else {
                return String.join(" + ", modes);
            }
            
        } catch (JSONException e) {
            return "대중교통";
        }
    }

    private void parseTransitDetails(JSONArray steps, RouteSearchResult result) {
        try {
            int transferCount = 0;
            String departureTime = null;
            String arrivalTime = null;
            
            for (int i = 0; i < steps.length(); i++) {
                JSONObject step = steps.getJSONObject(i);
                
                if ("TRANSIT".equals(step.getString("travel_mode"))) {
                    transferCount++;
                    
                    JSONObject transitDetails = step.getJSONObject("transit_details");
                    
                    if (departureTime == null && transitDetails.has("departure_time")) {
                        departureTime = transitDetails.getJSONObject("departure_time").getString("text");
                    }
                    
                    if (transitDetails.has("arrival_time")) {
                        arrivalTime = transitDetails.getJSONObject("arrival_time").getString("text");
                    }
                }
            }
            
            result.setTransferCount(Math.max(0, transferCount - 1)); // 환승 횟수
            if (departureTime != null) result.setDepartureTime(departureTime);
            if (arrivalTime != null) result.setArrivalTime(arrivalTime);
            
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing transit details", e);
        }
    }

    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
