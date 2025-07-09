package com.example.save_map;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {HistoryEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HistoryDao historyDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_db").build();
                }
            }
        }
        return INSTANCE;
    }

    public static AppDatabase getDatabase(Context context) {
        return getInstance(context);
    }
}
