package com.example.bloom.rewards;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RewardManager {
    private static final String PREFS = "bloom_rewards";
    private final SharedPreferences prefs;

    public RewardManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static String today() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Calendar.getInstance().getTime());
    }

    private static String yesterday() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -1);
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(c.getTime());
    }

    public int getXp()      { return prefs.getInt("xp", 0); }
    public int getStreak()  { return prefs.getInt("streak", 0); }
    public int getLevel()   { return getXp() / 100 + 1; }
    public int getProgress(){ return getXp() % 100; }
    public String getName() { return prefs.getString("name", "friend"); }
    public void setName(String n) { prefs.edit().putString("name", n).apply(); }

    public int getAge() { return prefs.getInt("age", 0); }
    public void setAge(int a) { prefs.edit().putInt("age", a).apply(); }

    public int getHeight() { return prefs.getInt("height", 0); }
    public void setHeight(int h) { prefs.edit().putInt("height", h).apply(); }

    public int getWeight() { return prefs.getInt("weight", 0); }
    public void setWeight(int w) { prefs.edit().putInt("weight", w).apply(); }

    public boolean isLoggedIn() { return prefs.getBoolean("is_logged_in", false); }
    public void setLoggedIn(boolean b) { prefs.edit().putBoolean("is_logged_in", b).apply(); }

    public boolean isOnboarded() { return prefs.getBoolean("is_onboarded", false); }
    public void setOnboarded(boolean b) { prefs.edit().putBoolean("is_onboarded", b).apply(); }

    public String getEmail() { return prefs.getString("auth_email", null); }
    public String getPassword() { return prefs.getString("auth_password", null); }
    public void setAuth(String email, String password) {
        prefs.edit().putString("auth_email", email).putString("auth_password", password).apply();
    }

    public void signOut() {
        prefs.edit().putBoolean("is_logged_in", false).apply();
    }

    public void createNewAccount(Context context, String email, String password) {
        prefs.edit().clear().apply();
        prefs.edit()
                .putString("auth_email", email)
                .putString("auth_password", password)
                .putBoolean("is_logged_in", true)
                .putBoolean("is_onboarded", false)
                .apply();

        if (context != null) {
            java.util.concurrent.Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    com.example.bloom.data.BloomDatabase.getInstance(context).clearAllTables();
                } catch (Exception ignored) {}
            });
        }
    }

    public java.util.List<String> getFriendsList() {
        java.util.List<String> list = new java.util.ArrayList<>(java.util.Arrays.asList("Sara", "Ema", "Leo", "Dina"));
        String custom = prefs.getString("custom_friends", "");
        if (!custom.isEmpty()) {
            String[] arr = custom.split(",");
            for (String f : arr) {
                if (!f.trim().isEmpty() && !list.contains(f.trim())) {
                    list.add(f.trim());
                }
            }
        }
        return list;
    }

    public void addFriend(String name) {
        if (name == null || name.trim().isEmpty()) return;
        String trimmed = name.trim();
        java.util.List<String> current = getFriendsList();
        if (!current.contains(trimmed)) {
            String custom = prefs.getString("custom_friends", "");
            if (!custom.isEmpty()) custom += ",";
            custom += trimmed;
            prefs.edit().putString("custom_friends", custom).apply();
        }
    }

    public int getFriendsCount() {
        return getFriendsList().size();
    }

    public int getChallengesCompletedToday() {
        int count = 0;
        if (getSteps() >= 5000) count++;
        if (getWater() >= 8) count++;
        String prefix = "task_" + today();
        for (String key : prefs.getAll().keySet()) {
            if (key.startsWith(prefix)) count++;
        }
        return count;
    }

    public java.util.List<String> getCompletedChallengesList() {
        java.util.List<String> list = new java.util.ArrayList<>();
        if (getSteps() >= 5000) {
            list.add("👟 Hit 5,000 steps today (" + getSteps() + " steps)");
        }
        if (getWater() >= 8) {
            list.add("💧 Drink 2L of water (8/8 glasses)");
        }
        if (isDoneToday("mindfulness_5min")) {
            list.add("🧘 5 minutes of mindfulness");
        }
        String prefix = "task_" + today();
        for (String key : prefs.getAll().keySet()) {
            if (key.startsWith(prefix)) {
                String taskName = key.substring(prefix.length() + 1);
                if (!taskName.equals("mindfulness_5min")) {
                    list.add("✨ Completed quest: " + taskName);
                }
            }
        }
        if (list.isEmpty()) {
            list.add("No challenges completed yet today. Tap active challenges below to complete!");
        }
        return list;
    }

    public java.util.List<String> getAchievementsList() {
        java.util.List<String> list = new java.util.ArrayList<>();
        if (isOnboarded()) list.add("✨ Profile Setup Complete");
        if (getStreak() >= 3) list.add("🔥 3-Day Streak Achieved");
        if (getStreak() >= 7) list.add("⚡ 7-Day Streak Master");
        if (getLevel() >= 2) list.add("🌟 Level 2 Explorer");
        if (getLevel() >= 5) list.add("👑 Level 5 Champion");
        if (getXp() >= 500) list.add("🏆 500+ Total XP");
        if (getWater() >= 8) list.add("💧 Hydration Hero (8 Glasses)");
        if (getSteps() >= 5000) list.add("👟 Step Goal Master (5,000+ Steps)");

        if (list.isEmpty()) {
            list.add("No achievements unlocked yet. Keep progressing!");
        }
        return list;
    }

    public int getAchievementsCount() {
        int count = 0;
        if (isOnboarded()) count++;
        if (getStreak() >= 3) count++;
        if (getStreak() >= 7) count++;
        if (getLevel() >= 2) count++;
        if (getLevel() >= 5) count++;
        if (getXp() >= 500) count++;
        if (prefs.getInt("water_" + today(), 0) >= 8) count++;
        return count;
    }

    public int getSteps() { return prefs.getInt("steps_" + today(), 0); }
    public void addSteps(int s) {
        prefs.edit().putInt("steps_" + today(), getSteps() + s).apply();
    }

    public int getWater() { return prefs.getInt("water_" + today(), 0); }
    public void incrementWater() {
        int val = getWater();
        if (val < 8) {
            prefs.edit().putInt("water_" + today(), val + 1).apply();
        } else {
            prefs.edit().putInt("water_" + today(), 0).apply();
        }
    }

    public void clearAll(Context context) {
        prefs.edit().clear().apply();
        if (context != null) {
            java.util.concurrent.Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    com.example.bloom.data.BloomDatabase.getInstance(context).clearAllTables();
                } catch (Exception ignored) {}
            });
        }
    }


    public boolean completeTask(String taskId, int xp) {
        String key = "task_" + today() + "_" + taskId;
        if (prefs.getBoolean(key, false)) return false;
        bumpStreak();
        prefs.edit()
                .putBoolean(key, true)
                .putInt("xp", getXp() + xp)
                .apply();
        return true;
    }

    public boolean isDoneToday(String taskId) {
        return prefs.getBoolean("task_" + today() + "_" + taskId, false);
    }

    public void bumpStreak() {
        String last = prefs.getString("lastActive", null);
        String t = today();
        if (t.equals(last)) return;
        int streak = yesterday().equals(last) ? getStreak() + 1 : 1;
        prefs.edit().putString("lastActive", t).putInt("streak", streak).apply();
    }


    public String difficultyFor(String mood) {
        if (mood == null) return "balanced";
        if (mood.equals("amazing") || (mood.equals("good") && getStreak() >= 3)) return "challenging";
        if (mood.equals("low") || mood.equals("stressed")) return "gentle";
        return "balanced";
    }

    public String getSelectedAvatar() {
        return prefs.getString("selected_avatar", "🌸");
    }

    public void setSelectedAvatar(String avatar) {
        prefs.edit().putString("selected_avatar", avatar).apply();
    }

    public String getAvatarTitle(String emoji) {
        if ("🌸".equals(emoji)) return "Gentle Bloomer";
        if ("🐻".equals(emoji)) return "Bibo's Bestie";
        if ("🌿".equals(emoji)) return "Daily Habit Sprout";
        if ("🐱".equals(emoji)) return "Mindful Zen Master";
        if ("🥑".equals(emoji)) return "Nourish Champion";
        if ("🦋".equals(emoji)) return "Mindful Transformer";
        if ("🚀".equals(emoji)) return "Unstoppable Explorer";
        if ("🦄".equals(emoji)) return "Hydration Legend";
        if ("🌈".equals(emoji)) return "Mood Master";
        if ("🐲".equals(emoji)) return "Wellness Champion";
        if ("🌟".equals(emoji)) return "Streak Superstar";
        if ("☕".equals(emoji)) return "Cozy Wellness Friend";
        return "Bloom Adventurer";
    }

    public boolean isAvatarUnlocked(String emoji) {
        if ("🌸".equals(emoji)) return true;
        if ("🥑".equals(emoji)) return true;
        if ("🐻".equals(emoji)) return getLevel() >= 2;
        if ("🌿".equals(emoji)) return getStreak() >= 3;
        if ("🐱".equals(emoji)) return getAchievementsCount() >= 1;
        if ("🦋".equals(emoji)) return getXp() >= 200;
        if ("🚀".equals(emoji)) return getLevel() >= 3;
        if ("🦄".equals(emoji)) return getWater() >= 8;
        if ("🌈".equals(emoji)) return getStreak() >= 5;
        if ("🐲".equals(emoji)) return getLevel() >= 5;
        if ("🌟".equals(emoji)) return getStreak() >= 7;
        if ("☕".equals(emoji)) return getChallengesCompletedToday() >= 1;
        return true;
    }

    public String getAvatarUnlockCondition(String emoji) {
        if ("🌸".equals(emoji)) return "Unlocked";
        if ("🥑".equals(emoji)) return "Unlocked";
        if ("🐻".equals(emoji)) return "Reach Level 2";
        if ("🌿".equals(emoji)) return "3-Day Streak";
        if ("🐱".equals(emoji)) return "Unlock 1 Achievement";
        if ("🦋".equals(emoji)) return "200+ XP";
        if ("🚀".equals(emoji)) return "Reach Level 3";
        if ("🦄".equals(emoji)) return "Hydration Hero (8 Glasses)";
        if ("🌈".equals(emoji)) return "5-Day Streak";
        if ("🐲".equals(emoji)) return "Reach Level 5";
        if ("🌟".equals(emoji)) return "7-Day Streak";
        if ("☕".equals(emoji)) return "Complete 1 Quest Today";
        return "Unlocked";
    }
}
