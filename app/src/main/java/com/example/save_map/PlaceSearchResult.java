package com.example.save_map;

public class PlaceSearchResult {
    private String placeId;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String placeType; // "transit_station", "establishment", "point_of_interest" 등
    private float rating;
    private boolean isTransitStation;
    private int userRatingsTotal;
    private String phoneNumber;
    private String websiteUrl;

    public PlaceSearchResult(String placeId, String name, String address, 
                           double latitude, double longitude, String placeType) {
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeType = placeType;
        this.rating = 0.0f;
        this.isTransitStation = placeType != null && placeType.contains("transit");
    }

    // Getters and Setters
    public String getPlaceId() { return placeId; }
    public void setPlaceId(String placeId) { this.placeId = placeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getPlaceType() { return placeType; }
    public void setPlaceType(String placeType) { 
        this.placeType = placeType; 
        this.isTransitStation = placeType != null && placeType.contains("transit");
    }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public boolean isTransitStation() { return isTransitStation; }
    public void setTransitStation(boolean transitStation) { isTransitStation = transitStation; }

    public int getUserRatingsTotal() { return userRatingsTotal; }
    public void setUserRatingsTotal(int userRatingsTotal) { this.userRatingsTotal = userRatingsTotal; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getWebsiteUrl() { return websiteUrl; }
    public void setWebsiteUrl(String websiteUrl) { this.websiteUrl = websiteUrl; }

    public String getDisplayText() {
        return name + "\n" + address;
    }
}
