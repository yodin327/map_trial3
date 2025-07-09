package com.example.save_map;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
public class HistoryEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String date;
    public String time;
    public String startLocation;
    public String endLocation;
    public boolean isFavorite;
    public int tripCount;
    public String title;
    public String description;
    public String type;

    public HistoryEntity(String date, String time, String startLocation, String endLocation, boolean isFavorite, int tripCount) {
        this.date = date;
        this.time = time;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.isFavorite = isFavorite;
        this.tripCount = tripCount;
    }

    // 기본 생성자 추가
    public HistoryEntity() {
    }

    // Setter 메서드들 추가
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }
}
