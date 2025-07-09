package com.example.save_map;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY date DESC, time DESC")
    LiveData<List<HistoryEntity>> getAll();

    @Insert
    void insert(HistoryEntity entity);

    @Update
    void update(HistoryEntity entity);

    @Delete
    void delete(HistoryEntity entity);

    @Query("DELETE FROM history WHERE id = :id")
    void deleteById(int id);
}
