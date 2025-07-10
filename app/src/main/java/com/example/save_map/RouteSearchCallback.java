package com.example.save_map;

import java.util.List;

public interface RouteSearchCallback {
    void onRoutesFound(List<RouteSearchResult> routes);
    void onRouteSearchError(String error);
}
