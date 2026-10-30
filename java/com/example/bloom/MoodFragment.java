package com.example.bloom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.bloom.data.BloomDatabase;
import com.example.bloom.data.MoodEntry;
import com.example.bloom.engine.AdaptiveEngine;
import com.example.bloom.rewards.RewardManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MoodFragment extends Fragment {

    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private BloomDatabase db;
    private RewardManager rewards;
    private TextView textHistory, textBibo;
    private LineChart chart;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b) {
        return i.inflate(R.layout.fragment_mood, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle b) {
        db = BloomDatabase.getInstance(requireContext());
        rewards = new RewardManager(requireContext());
        textHistory = v.findViewById(R.id.textHistory);
        textBibo = v.findViewById(R.id.textBiboMood);
        chart = v.findViewById(R.id.chartMood);

        RadioGroup group = v.findViewById(R.id.groupMood);
        EditText note = v.findViewById(R.id.editNote);

        v.findViewById(R.id.buttonSaveMood).setOnClickListener(x -> {
            int checked = group.getCheckedRadioButtonId();
            String mood; int energy;
            if (checked == R.id.moodAmazing)       { mood = "amazing";  energy = 5; }
            else if (checked == R.id.moodGood)     { mood = "good";     energy = 4; }
            else if (checked == R.id.moodMeh)      { mood = "meh";      energy = 3; }
            else if (checked == R.id.moodLow)      { mood = "low";      energy = 2; }
            else if (checked == R.id.moodStressed) { mood = "stressed"; energy = 1; }
            else { Toast.makeText(requireContext(), R.string.toast_pick_mood, Toast.LENGTH_SHORT).show(); return; }

            String today = RewardManager.today();
            String noteText = note.getText().toString();
            
            io.execute(() -> {
                db.moodDao().deleteForDate(today);
                db.moodDao().insert(new MoodEntry(mood, energy, noteText, today));
                
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    rewards.completeTask("mood_checkin", 10);
                    note.setText("");
                    Toast.makeText(requireContext(), R.string.toast_saved, Toast.LENGTH_SHORT).show();
                    refresh();
                });
            });
        });

        v.findViewById(R.id.buttonClearHistory).setOnClickListener(x -> confirmClearHistory());

        refresh();
    }

    private void confirmClearHistory() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle(R.string.clear_history)
                .setMessage(R.string.confirm_delete_message)
                .setPositiveButton(R.string.delete, (d, w) -> {
                    io.execute(() -> {
                        db.clearAllTables();
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(this::refresh);
                    });
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void refresh() {
        io.execute(() -> {
            List<MoodEntry> entries = db.moodDao().getLastWeek();
            String latest = db.moodDao().latestMood();
            String name = rewards.getName();

            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> {
                View root = getView();
                if (root != null) {
                    updateWeeklySummary(root, entries, name);
                }

                textBibo.setText(AdaptiveEngine.biboMessage(latest, name));

                StringBuilder sb = new StringBuilder(getString(R.string.history_title));
                if (entries.isEmpty()) sb.append(getString(R.string.history_none));
                for (MoodEntry e : entries) {
                    if (e.note != null && !e.note.trim().isEmpty()) {
                        sb.append(getString(R.string.history_entry_note_format, e.date, e.mood, e.energy, e.note));
                    } else {
                        sb.append(getString(R.string.history_entry_format, e.date, e.mood, e.energy));
                    }
                }
                textHistory.setText(sb.toString());

                List<Entry> points = new ArrayList<>();
                int x = 0;
                for (int i = entries.size() - 1; i >= 0; i--) {
                    points.add(new Entry(x, entries.get(i).energy));
                    x++;
                }

                if (points.isEmpty()) { chart.clear(); return; }
                LineDataSet set = new LineDataSet(points, getString(R.string.chart_label));
                set.setLineWidth(3f);
                set.setCircleRadius(5f);
                chart.setData(new LineData(set));
                chart.getDescription().setEnabled(false);
                chart.getAxisLeft().setAxisMinimum(0f);
                chart.getAxisLeft().setAxisMaximum(5f);
                chart.getAxisRight().setEnabled(false);
                chart.invalidate();
            });
        });
    }

    private void updateWeeklySummary(View v, List<MoodEntry> entries, String name) {
        TextView textDominant = v.findViewById(R.id.textWeeklyDominant);
        TextView textNote = v.findViewById(R.id.textWeeklyBiboNote);
        TextView textCheckins = v.findViewById(R.id.textStatCheckins);
        TextView textEnergy = v.findViewById(R.id.textStatEnergy);
        TextView textStreak = v.findViewById(R.id.textStatStreak);

        if (textDominant == null) return;

        if (entries == null || entries.isEmpty()) {
            textDominant.setText("✨ No check-ins yet this week");
            textNote.setText("Log your mood above to unlock your personalized weekly wellness summary!");
            textCheckins.setText("0 Check-ins");
            textEnergy.setText("Avg: -- / 5");
            textStreak.setText("🔥 " + rewards.getStreak() + "-Day Streak");
            return;
        }

        int count = entries.size();
        float totalEnergy = 0;
        java.util.Map<String, Integer> counts = new java.util.HashMap<>();
        for (MoodEntry e : entries) {
            totalEnergy += e.energy;
            String m = e.mood != null ? e.mood.toLowerCase() : "good";
            counts.put(m, counts.getOrDefault(m, 0) + 1);
        }

        float avgEnergy = totalEnergy / count;
        String dominantMood = "good";
        int maxCount = 0;
        for (java.util.Map.Entry<String, Integer> pair : counts.entrySet()) {
            if (pair.getValue() > maxCount) {
                maxCount = pair.getValue();
                dominantMood = pair.getKey();
            }
        }

        String dominantLabel;
        String biboAdvice;
        switch (dominantMood) {
            case "amazing":
                dominantLabel = "✨ Mostly Vibrant & Energetic";
                biboAdvice = "You've been radiating positive energy this week, " + name + "! Keep channeling this momentum into your favorite activities!";
                break;
            case "good":
                dominantLabel = "🌟 Mostly Calm & Balanced";
                biboAdvice = "A steady, balanced week, " + name + "! You're building healthy emotional resilience day by day.";
                break;
            case "meh":
                dominantLabel = "☀️ Quiet & Reflective";
                biboAdvice = "A quiet week, " + name + ". In-between days are a normal part of growth — take it one step at a time!";
                break;
            case "low":
                dominantLabel = "🤍 Gentle & Low-Energy";
                biboAdvice = "You had some low-energy moments this week, " + name + ". Be extra kind to yourself and celebrate every small win.";
                break;
            case "stressed":
                dominantLabel = "🍃 Unwinding & Restful";
                biboAdvice = "You've navigated some stressful moments, " + name + ". Deep breaths and gentle movement will help melt that tension away.";
                break;
            default:
                dominantLabel = "🌟 Balanced Week";
                biboAdvice = "Great job tracking your emotional journey this week, " + name + "!";
                break;
        }

        textDominant.setText(dominantLabel);
        textNote.setText("You logged " + count + " check-in" + (count > 1 ? "s" : "") + " this week! Bibo notes: " + biboAdvice);
        textCheckins.setText(count + " Check-in" + (count > 1 ? "s" : ""));
        textEnergy.setText(String.format(java.util.Locale.US, "Avg: %.1f / 5", avgEnergy));
        textStreak.setText("🔥 " + rewards.getStreak() + "-Day Streak");
    }

    @Override public void onResume() { super.onResume(); refresh(); }
}
