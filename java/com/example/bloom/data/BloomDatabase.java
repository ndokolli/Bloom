package com.example.bloom.data;


import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {MoodEntry.class}, version = 4, exportSchema = false)
public abstract class BloomDatabase extends RoomDatabase {
    public abstract MoodDao moodDao();

    private static volatile BloomDatabase INSTANCE;

    public static BloomDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (BloomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            BloomDatabase.class,
                            "bloom.db"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
