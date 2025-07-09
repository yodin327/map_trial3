package com.example.save_map;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceTypes;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Google Places API를 사용한 장소 검색 서비스
 */
public class GooglePlacesService {
    private static final String TAG = "GooglePlacesService";
    private final PlacesClient placesClient;
    private final AutocompleteSessionToken sessionToken;
    
    // BC (British Columbia) 전체 지역 경계 (BC Transit 서비스 지역)
    private static final RectangularBounds BC_BOUNDS = RectangularBounds.newInstance(
            new LatLng(48.3000, -139.0000), // 남서쪽 (BC 남쪽 경계, 서쪽 경계)
            new LatLng(60.0000, -114.0000)  // 북동쪽 (BC 북쪽 경계, 동쪽 경계)
    );

    public interface PlaceSearchCallback {
        void onSearchComplete(List<PlaceSearchResult> results);
        void onSearchError(String error);
    }

    public interface PlaceDetailsCallback {
        void onDetailsRetrieved(PlaceSearchResult placeDetails);
        void onDetailsError(String error);
    }

    public GooglePlacesService(Context context) {
        // Places API 초기화 (API 키는 AndroidManifest.xml에 설정됨)
        if (!Places.isInitialized()) {
            Places.initialize(context, context.getString(R.string.google_maps_key));
        }
        placesClient = Places.createClient(context);
        sessionToken = AutocompleteSessionToken.newInstance();
    }

    /**
     * 자동완성 검색 수행
     */
    public void searchPlaces(String query, PlaceSearchCallback callback) {
        if (query == null || query.trim().isEmpty()) {
            callback.onSearchComplete(new ArrayList<>());
            return;
        }

        // 검색 요청 생성
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setLocationBias(BC_BOUNDS)
                .setLocationRestriction(BC_BOUNDS)
                .setSessionToken(sessionToken)
                .setQuery(query)
                .setTypesFilter(Arrays.asList(
                        PlaceTypes.TRANSIT_STATION,
                        PlaceTypes.BUS_STATION,
                        PlaceTypes.SUBWAY_STATION,
                        PlaceTypes.ESTABLISHMENT,
                        PlaceTypes.POINT_OF_INTEREST
                ))
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            List<PlaceSearchResult> searchResults = new ArrayList<>();
            
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                PlaceSearchResult result = new PlaceSearchResult(
                        prediction.getPlaceId(),
                        prediction.getPrimaryText(null).toString(),
                        prediction.getFullText(null).toString(),
                        0, 0, // 좌표는 상세 정보에서 가져옴
                        getPlaceTypeFromPrediction(prediction)
                );
                searchResults.add(result);
            }
            
            callback.onSearchComplete(searchResults);
            
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place search failed: " + apiException.getStatusCode());
                callback.onSearchError("검색 실패: " + apiException.getMessage());
            } else {
                Log.e(TAG, "Place search failed", exception);
                callback.onSearchError("검색 중 오류가 발생했습니다");
            }
        });
    }

    /**
     * 장소 상세 정보 가져오기
     */
    public void getPlaceDetails(String placeId, PlaceDetailsCallback callback) {
        List<Place.Field> placeFields = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.TYPES,
                Place.Field.PHONE_NUMBER,
                Place.Field.WEBSITE_URI,
                Place.Field.RATING,
                Place.Field.USER_RATINGS_TOTAL
        );

        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            
            PlaceSearchResult result = new PlaceSearchResult(
                    place.getId(),
                    place.getName(),
                    place.getAddress(),
                    place.getLatLng() != null ? place.getLatLng().latitude : 0,
                    place.getLatLng() != null ? place.getLatLng().longitude : 0,
                    getPlaceTypeFromPlace(place)
            );
            
            // 추가 정보 설정
            if (place.getRating() != null) {
                result.setRating(place.getRating().floatValue());
            }
            if (place.getUserRatingsTotal() != null) {
                result.setUserRatingsTotal(place.getUserRatingsTotal());
            }
            if (place.getPhoneNumber() != null) {
                result.setPhoneNumber(place.getPhoneNumber());
            }
            if (place.getWebsiteUri() != null) {
                result.setWebsiteUrl(place.getWebsiteUri().toString());
            }
            
            callback.onDetailsRetrieved(result);
            
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place details failed: " + apiException.getStatusCode());
                callback.onDetailsError("상세 정보 가져오기 실패");
            } else {
                Log.e(TAG, "Place details failed", exception);
                callback.onDetailsError("상세 정보를 가져올 수 없습니다");
            }
        });
    }

    /**
     * 근처 교통 정류장 검색
     */
    public void searchNearbyTransitStations(LatLng location, int radiusInMeters, PlaceSearchCallback callback) {
        // Google Places Nearby Search API 사용
        // 현재는 기본 검색으로 대체
        String query = "bus station OR transit station OR skytrain station";
        searchPlaces(query, callback);
    }

    private String getPlaceTypeFromPrediction(AutocompletePrediction prediction) {
        List<Place.Type> types = prediction.getPlaceTypes();
        if (types != null && !types.isEmpty()) {
            for (Place.Type type : types) {
                if (type == Place.Type.TRANSIT_STATION || 
                    type == Place.Type.BUS_STATION || 
                    type == Place.Type.SUBWAY_STATION) {
                    return "transit_station";
                }
            }
        }
        return "establishment";
    }

    private String getPlaceTypeFromPlace(Place place) {
        List<Place.Type> types = place.getTypes();
        if (types != null && !types.isEmpty()) {
            for (Place.Type type : types) {
                if (type == Place.Type.TRANSIT_STATION || 
                    type == Place.Type.BUS_STATION || 
                    type == Place.Type.SUBWAY_STATION) {
                    return "transit_station";
                }
            }
        }
        return "establishment";
    }
}
