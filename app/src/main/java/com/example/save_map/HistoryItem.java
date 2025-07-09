package com.example.save_map;

public class HistoryItem {
    public String date;           // 이용 날짜 (예: 2025-07-08)
    public String time;           // 이용 시간 (예: 14:30)
    public String startLocation;  // 출발지명 또는 정류장명
    public String endLocation;    // 도착지명 또는 정류장명
    public boolean isFavorite;    // 즐겨찾기 여부
    public int tripCount;         // 해당 주간/월간 이용 횟수

    public HistoryItem(String date, String time, String startLocation, String endLocation, boolean isFavorite, int tripCount) {
        this.date = date;
        this.time = time;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.isFavorite = isFavorite;
        this.tripCount = tripCount;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }
    public boolean isFavorite() {
        return isFavorite;
    }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getFrom() { return startLocation; }
    public String getTo() { return endLocation; }
    public int getTripCount() { return tripCount; }
}
