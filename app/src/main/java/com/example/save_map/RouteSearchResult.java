package com.example.save_map;

import java.util.List;

public class RouteSearchResult {
    private String routeId;
    private PlaceSearchResult origin;
    private PlaceSearchResult destination;
    private String duration; // "25분"
    private String distance; // "3.2km"
    private String transitMode; // "도보 + 버스", "지하철", "버스" 등
    private int transferCount;
    private List<RouteStep> steps;
    private String departureTime;
    private String arrivalTime;
    private String cost; // "요금: $3.75"
    private boolean isAccessible; // 휠체어 접근 가능
    private int carbonFootprint; // CO2 절약량 (g)
    private int durationMinutes; // 소요 시간 (분 단위)

    public RouteSearchResult(PlaceSearchResult origin, PlaceSearchResult destination,
                           String duration, String distance, String transitMode) {
        this.origin = origin;
        this.destination = destination;
        this.duration = duration;
        this.distance = distance;
        this.transitMode = transitMode;
        this.transferCount = 0;
        this.isAccessible = true;
        this.carbonFootprint = 0;
        this.cost = "요금 정보 없음";
    }

    // Getters and Setters
    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }

    public PlaceSearchResult getOrigin() { return origin; }
    public void setOrigin(PlaceSearchResult origin) { this.origin = origin; }

    public PlaceSearchResult getDestination() { return destination; }
    public void setDestination(PlaceSearchResult destination) { this.destination = destination; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getDistance() { return distance; }
    public void setDistance(String distance) { this.distance = distance; }

    public String getTransitMode() { return transitMode; }
    public void setTransitMode(String transitMode) { this.transitMode = transitMode; }

    public int getTransferCount() { return transferCount; }
    public void setTransferCount(int transferCount) { this.transferCount = transferCount; }

    public List<RouteStep> getSteps() { return steps; }
    public void setSteps(List<RouteStep> steps) { this.steps = steps; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public String getCost() { return cost; }
    public void setCost(String cost) { this.cost = cost; }

    public boolean isAccessible() { return isAccessible; }
    public void setAccessible(boolean accessible) { isAccessible = accessible; }

    public int getCarbonFootprint() { return carbonFootprint; }
    public void setCarbonFootprint(int carbonFootprint) { this.carbonFootprint = carbonFootprint; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getRouteTitle() {
        return origin.getName() + " → " + destination.getName();
    }

    public String getTransferText() {
        if (transferCount == 0) return "직행";
        return transferCount + "회 환승";
    }
}
