package com.example.bloom.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface MoodDao {
    @Insert
    void insert(MoodEntry entry);

    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC LIMIT 7")
    List<MoodEntry> getLastWeek();

    @Query("SELECT mood FROM mood_entries ORDER BY timestamp DESC LIMIT 1")
    String latestMood();

    @Query("SELECT note FROM mood_entries ORDER BY timestamp DESC LIMIT 1")
    String latestNote();

    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC LIMIT 1")
    MoodEntry getLatestEntry();

    @Query("DELETE FROM mood_entries WHERE date = :date")
    void deleteForDate(String date);
}
