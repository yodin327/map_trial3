package com.example.save_map;

public class RouteStep {
    private String instruction; // "메인 스트리트에서 99번 버스 탑승"
    private String mode; // "WALKING", "TRANSIT", "TRANSFER"
    private String duration; // "5분"
    private String distance; // "400m"
    private String transitLine; // "99 B-Line"
    private String transitVehicle; // "버스", "지하철", "페리"
    private String startLocation;
    private String endLocation;
    private boolean isAccessible;

    public RouteStep(String instruction, String mode, String duration, String distance) {
        this.instruction = instruction;
        this.mode = mode;
        this.duration = duration;
        this.distance = distance;
        this.isAccessible = true;
    }

    // Getters and Setters
    public String getInstruction() { return instruction; }
    public void setInstruction(String instruction) { this.instruction = instruction; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getDistance() { return distance; }
    public void setDistance(String distance) { this.distance = distance; }

    public String getTransitLine() { return transitLine; }
    public void setTransitLine(String transitLine) { this.transitLine = transitLine; }

    public String getTransitVehicle() { return transitVehicle; }
    public void setTransitVehicle(String transitVehicle) { this.transitVehicle = transitVehicle; }

    public String getStartLocation() { return startLocation; }
    public void setStartLocation(String startLocation) { this.startLocation = startLocation; }

    public String getEndLocation() { return endLocation; }
    public void setEndLocation(String endLocation) { this.endLocation = endLocation; }

    public boolean isAccessible() { return isAccessible; }
    public void setAccessible(boolean accessible) { isAccessible = accessible; }

    public int getIconResource() {
        switch (mode) {
            case "WALKING":
                return android.R.drawable.ic_menu_directions;
            case "TRANSIT":
                return android.R.drawable.ic_menu_mapmode;
            case "TRANSFER":
                return android.R.drawable.ic_menu_rotate;
            default:
                return android.R.drawable.ic_menu_info_details;
        }
    }
}
