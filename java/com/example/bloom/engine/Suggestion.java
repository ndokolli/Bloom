package com.example.bloom.engine;

public class Suggestion {
    public final String id, title, emoji, detail, duration, intensity;
    public final int xp, difficulty;
    public String ingredients = "";
    public String instructions = "";
    public String movements = "";
    public String focus = "";

    public Suggestion(String id, String title, String emoji, String detail, String duration, int xp, String intensity, int difficulty) {
        this.id = id; this.title = title; this.emoji = emoji;
        this.detail = detail; this.duration = duration; this.xp = xp; this.intensity = intensity;
        this.difficulty = difficulty;
    }

    public Suggestion withRecipe(String ingredients, String instructions) {
        this.ingredients = ingredients;
        this.instructions = instructions;
        return this;
    }

    public Suggestion withExercise(String movements, String focus) {
        this.movements = movements;
        this.focus = focus;
        return this;
    }
}
