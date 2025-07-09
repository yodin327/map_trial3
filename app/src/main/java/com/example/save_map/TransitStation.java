package com.example.save_map;

public class TransitStation {
    private String stationId;
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private String[] routes; // 경유 노선들
    private double distanceFromUser; // 사용자로부터의 거리 (미터)
    private boolean isAccessible; // 휠체어 접근 가능성
    private String estimatedArrival; // 예상 도착 시간 (임시)

    public TransitStation(String stationId, String name, String description, 
                         double latitude, double longitude, String[] routes) {
        this.stationId = stationId;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.routes = routes;
        this.isAccessible = true; // 기본값
        this.estimatedArrival = "실시간 정보 없음";
    }

    // Getters and Setters
    public String getStationId() { return stationId; }
    public void setStationId(String stationId) { this.stationId = stationId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String[] getRoutes() { return routes; }
    public void setRoutes(String[] routes) { this.routes = routes; }

    public double getDistanceFromUser() { return distanceFromUser; }
    public void setDistanceFromUser(double distanceFromUser) { this.distanceFromUser = distanceFromUser; }

    public boolean isAccessible() { return isAccessible; }
    public void setAccessible(boolean accessible) { isAccessible = accessible; }

    public String getEstimatedArrival() { return estimatedArrival; }
    public void setEstimatedArrival(String estimatedArrival) { this.estimatedArrival = estimatedArrival; }

    public String getRoutesString() {
        if (routes == null || routes.length == 0) return "";
        return String.join(", ", routes);
    }
}
