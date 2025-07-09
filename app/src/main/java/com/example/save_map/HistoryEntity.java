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

    public HistoryEntity(String date, String time, String startLocation, String endLocation, boolean isFavorite, int tripCount) {
        this.date = date;
        this.time = time;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.isFavorite = isFavorite;
        this.tripCount = tripCount;
    }
}
