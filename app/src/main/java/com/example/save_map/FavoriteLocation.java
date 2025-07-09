package com.example.save_map;

public class FavoriteLocation {
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String iconType; // "home", "work", "school", "custom"
    private boolean isPinned;

    public FavoriteLocation(String name, String address, double latitude, double longitude, String iconType) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.iconType = iconType;
        this.isPinned = false;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getIconType() { return iconType; }
    public void setIconType(String iconType) { this.iconType = iconType; }

    public boolean isPinned() { return isPinned; }
    public void setPinned(boolean pinned) { isPinned = pinned; }
}
