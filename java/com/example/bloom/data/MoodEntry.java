package com.example.bloom.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mood_entries")
public class MoodEntry {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String mood;
    public int energy;
    public String note;
    public String date;
    public long timestamp;

    public MoodEntry(String mood, int energy, String note, String date) {
        this.mood = mood;
        this.energy = energy;
        this.note = note;
        this.date = date;
        this.timestamp = System.currentTimeMillis();
    }
}
