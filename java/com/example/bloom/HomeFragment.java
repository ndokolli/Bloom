package com.example.bloom;

import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.bloom.data.BloomDatabase;
import com.example.bloom.data.MoodEntry;
import com.example.bloom.engine.AdaptiveEngine;
import com.example.bloom.engine.Suggestion;
import com.example.bloom.rewards.RewardManager;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private RewardManager rewards;
    private TextView textStats, textStreak;
    private ProgressBar progressXp;
    private LinearLayout questContainer;
    private Button selectedMoodButton;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup c, @Nullable Bundle b) {
        return inflater.inflate(R.layout.fragment_home, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle b) {
        rewards = new RewardManager(requireContext());
        textStats = v.findViewById(R.id.textStats);
        textStreak = v.findViewById(R.id.textStreak);
        progressXp = v.findViewById(R.id.progressXp);
        questContainer = v.findViewById(R.id.questContainer);

        v.findViewById(R.id.moodAmazingHome).setOnClickListener(x ->
                saveMoodFromHome("amazing", 5, (Button) x));
        v.findViewById(R.id.moodGoodHome).setOnClickListener(x ->
                saveMoodFromHome("good", 4, (Button) x));
        v.findViewById(R.id.moodMehHome).setOnClickListener(x ->
                saveMoodFromHome("meh", 3, (Button) x));
        v.findViewById(R.id.moodLowHome).setOnClickListener(x ->
                saveMoodFromHome("low", 2, (Button) x));
        v.findViewById(R.id.moodStressedHome).setOnClickListener(x ->
                saveMoodFromHome("stressed", 1, (Button) x));
        v.findViewById(R.id.buttonSeeActivities).setOnClickListener(x ->
                Navigation.findNavController(v).navigate(R.id.activitiesFragment));
        v.findViewById(R.id.buttonSeeRecipes).setOnClickListener(x ->
                Navigation.findNavController(v).navigate(R.id.recipesFragment));
        v.findViewById(R.id.cardBibo).setOnClickListener(x ->
                Navigation.findNavController(v).navigate(R.id.companionFragment));

        refresh();
    }

    private void refresh() {
        String name = rewards.getName();
        View v = getView();
        if (v == null) return;

        ((TextView) v.findViewById(R.id.textGreeting)).setText(getString(R.string.home_greeting_format, name));

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int timeRes = R.string.good_evening;
        if (hour < 12) timeRes = R.string.good_morning;
        else if (hour < 18) timeRes = R.string.good_afternoon;
        ((TextView) v.findViewById(R.id.textTimeGreeting)).setText(getString(timeRes));

        io.execute(() -> {
            String mood = BloomDatabase.getInstance(requireContext()).moodDao().latestMood();
            List<Suggestion> quests = AdaptiveEngine.activitiesFor(mood);
            List<Suggestion> foods = AdaptiveEngine.recipesFor(mood);
            
            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> {
                ((TextView) v.findViewById(R.id.textBiboHome)).setText(AdaptiveEngine.biboMessage(mood, name));
                buildQuests(quests);
                buildFoods(foods);
                
                textStats.setText(getString(R.string.home_level_format, rewards.getLevel(), rewards.getXp()));
                textStreak.setText(getString(R.string.home_streak_format, rewards.getStreak()));
                progressXp.setProgress(rewards.getProgress());
            });
        });
    }

    private void buildFoods(List<Suggestion> foods) {
        View v = getView();
        if (v == null || foods.size() < 2) return;
        
        LinearLayout container = v.findViewById(R.id.foodContainer);
        container.removeAllViews();
        
        for (int i = 0; i < 2; i++) {
            Suggestion s = foods.get(i);
            
            LinearLayout card = new LinearLayout(requireContext());
            card.setOrientation(LinearLayout.VERTICAL);
            card.setBackgroundResource(R.drawable.bg_card);
            card.setElevation(4f * getResources().getDisplayMetrics().density);
            card.setClipToOutline(true);
            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, -2, 1f);
            if (i > 0) {
                params.setMarginStart((int)(12 * getResources().getDisplayMetrics().density));
            }
            card.setLayoutParams(params);
            

            android.widget.RelativeLayout top = new android.widget.RelativeLayout(requireContext());
            top.setLayoutParams(new LinearLayout.LayoutParams(-1, (int)(100 * getResources().getDisplayMetrics().density)));
            boolean isWarm = s.intensity.equals("low") || s.intensity.equals("meh") || s.intensity.equals("stressed");
            top.setBackgroundResource(isWarm ? R.drawable.bg_gradient_orange : R.drawable.bg_gradient_mint);
            
            TextView emoji = new TextView(requireContext());
            emoji.setText(s.emoji);
            emoji.setTextSize(32);
            emoji.setBackgroundResource(R.drawable.bg_white_circle);
            emoji.setPadding(12, 12, 12, 12);
            emoji.setGravity(Gravity.CENTER);
            android.widget.RelativeLayout.LayoutParams emojiParams = new android.widget.RelativeLayout.LayoutParams(-2, -2);
            emojiParams.addRule(android.widget.RelativeLayout.CENTER_IN_PARENT);
            top.addView(emoji, emojiParams);
            card.addView(top);
            

            TextView title = new TextView(requireContext());
            title.setText(s.title);
            title.setPadding(16, 16, 16, 16);
            title.setGravity(Gravity.CENTER);
            title.setTextColor(getResources().getColor(R.color.bloom_text, null));
            title.setTextSize(14);
            title.setTypeface(null, android.graphics.Typeface.BOLD);
            card.addView(title);
            
            card.setOnClickListener(x -> showRecipe(s));
            container.addView(card);
        }
    }

    private void showRecipe(Suggestion s) {
        BloomDialogHelper.showRecipeDetails(requireContext(), s);
    }

    private void buildQuests(List<Suggestion> quests) {
        questContainer.removeAllViews();
        for (int i = 0; i < Math.min(quests.size(), 2); i++) {
            Suggestion s = quests.get(i);
            
            LinearLayout card = new LinearLayout(requireContext());
            card.setOrientation(LinearLayout.HORIZONTAL);
            card.setGravity(Gravity.CENTER_VERTICAL);
            card.setBackgroundResource(R.drawable.bg_card);
            card.setElevation(2f * getResources().getDisplayMetrics().density);
            card.setPadding(16, 16, 16, 16);
            card.setOnClickListener(x -> BloomDialogHelper.showActivityDetails(requireContext(), s, rewards, this::refresh));
            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, (int)(88 * getResources().getDisplayMetrics().density));
            params.bottomMargin = (int)(12 * getResources().getDisplayMetrics().density);
            card.setLayoutParams(params);
            
            TextView emoji = new TextView(requireContext());
            emoji.setText(s.emoji);
            emoji.setTextSize(24);
            emoji.setPadding(32, 32, 32, 32);
            emoji.setBackgroundResource(R.drawable.bg_mood_pill);
            card.addView(emoji);
            
            LinearLayout info = new LinearLayout(requireContext());
            info.setOrientation(LinearLayout.VERTICAL);
            info.setPadding(32, 0, 32, 0);
            LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(0, -2, 1f);
            info.setLayoutParams(infoParams);
            
            TextView title = new TextView(requireContext());
            title.setText(s.title);
            title.setTextColor(getResources().getColor(R.color.bloom_text, null));
            title.setTextSize(16);
            title.setTypeface(null, android.graphics.Typeface.BOLD);
            info.addView(title);
            
            TextView detail = new TextView(requireContext());
            detail.setText(getString(R.string.activity_detail_xp_format, s.detail, s.xp));
            detail.setTextColor(getResources().getColor(R.color.bloom_muted, null));
            detail.setTextSize(13);
            info.addView(detail);
            
            card.addView(info);
            
            Button add = new Button(requireContext());
            add.setText("+");
            add.setTextSize(20);
            add.setTextColor(Color.WHITE);
            add.setBackgroundResource(R.drawable.bg_gradient_card);
            int btnSize = (int)(48 * getResources().getDisplayMetrics().density);
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(btnSize, btnSize);
            add.setLayoutParams(btnParams);
            
            add.setEnabled(!rewards.isDoneToday(s.id));
            add.setOnClickListener(x -> {
                if (rewards.completeTask(s.id, s.xp)) {
                    add.setEnabled(false);
                    refresh();
                    BloomDialogHelper.showCelebrationDialog(requireContext(), "Quest Completed! 🎉", "Awesome job completing " + s.title + "!", s.xp, rewards);
                }
            });
            card.addView(add);
            
            questContainer.addView(card);
        }
    }

    private void saveMoodFromHome(String mood, int energy, Button b) {
        if (selectedMoodButton != null) {
            selectedMoodButton.setBackgroundResource(R.drawable.bg_mood_pill);
        }
        selectedMoodButton = b;
        selectedMoodButton.setBackgroundResource(R.drawable.bg_mood_pill_selected);

        io.execute(() -> {
            String today = RewardManager.today();
            String noteToSave = "";
            MoodEntry latest = BloomDatabase.getInstance(requireContext()).moodDao().getLatestEntry();
            if (latest != null && today.equals(latest.date)) {
                noteToSave = latest.note;
            }

            MoodEntry entry = new MoodEntry(mood, energy, noteToSave, today);
            BloomDatabase.getInstance(requireContext()).moodDao().deleteForDate(today);
            BloomDatabase.getInstance(requireContext()).moodDao().insert(entry);
            boolean first = rewards.completeTask("mood_checkin", 10);

            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> {
                refresh();
                if (first) {
                    BloomDialogHelper.showCelebrationDialog(requireContext(), "Daily Check-in Complete! 🌸", "Thank you for taking a moment to check in with yourself today!", 10, rewards);
                } else {
                    Toast.makeText(requireContext(), "Mood updated! ✨", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override public void onResume() { super.onResume(); refresh(); }
}
