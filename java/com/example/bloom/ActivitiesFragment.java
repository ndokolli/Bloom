package com.example.bloom;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.graphics.Color;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.example.bloom.data.BloomDatabase;
import com.example.bloom.engine.*;
import com.example.bloom.rewards.RewardManager;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActivitiesFragment extends Fragment {

    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private String currentFilter = null;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b) {
        return i.inflate(R.layout.fragment_activities, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle b) {
        v.findViewById(R.id.filterForYou).setOnClickListener(x -> { currentFilter = null; refresh(); });
        v.findViewById(R.id.filterAmazing).setOnClickListener(x -> { currentFilter = "amazing"; refresh(); });
        v.findViewById(R.id.filterGood).setOnClickListener(x -> { currentFilter = "good"; refresh(); });
        v.findViewById(R.id.filterMeh).setOnClickListener(x -> { currentFilter = "meh"; refresh(); });
        v.findViewById(R.id.filterLow).setOnClickListener(x -> { currentFilter = "low"; refresh(); });
        v.findViewById(R.id.filterStressed).setOnClickListener(x -> { currentFilter = "stressed"; refresh(); });

        refresh();
    }

    private void refresh() {
        View v = getView();
        if (v == null) return;
        
        RewardManager rewards = new RewardManager(requireContext());
        
        io.execute(() -> {
            String mood = BloomDatabase.getInstance(requireContext()).moodDao().latestMood();
            List<Suggestion> activities = currentFilter == null
                    ? AdaptiveEngine.activitiesFor(mood)
                    : AdaptiveEngine.activitiesForIntensity(currentFilter);
            
            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> {
                updateFilterButtons(v);

                ((TextView) v.findViewById(R.id.textListTitle)).setText(R.string.activities_heading);

                LinearLayout list = v.findViewById(R.id.listContainer);
                list.removeAllViews();
                for (Suggestion s : activities) {
                    LinearLayout card = new LinearLayout(requireContext());
                    card.setOrientation(LinearLayout.HORIZONTAL);
                    card.setGravity(Gravity.CENTER_VERTICAL);
                    card.setBackgroundResource(R.drawable.bg_card);
                    card.setElevation(4f * getResources().getDisplayMetrics().density);
                    card.setPadding(16, 16, 16, 16);
                    card.setOnClickListener(x -> showActivityDetails(s));
                    
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, (int)(96 * getResources().getDisplayMetrics().density));
                    params.bottomMargin = (int)(14 * getResources().getDisplayMetrics().density);
                    card.setLayoutParams(params);
                    
                    TextView emoji = new TextView(requireContext());
                    emoji.setText(s.emoji);
                    emoji.setTextSize(26);
                    emoji.setPadding(10, 10, 10, 10);
                    emoji.setBackgroundResource(R.drawable.bg_mood_pill);
                    card.addView(emoji);
                    
                    LinearLayout info = new LinearLayout(requireContext());
                    info.setOrientation(LinearLayout.VERTICAL);
                    info.setPadding(18, 0, 18, 0);
                    LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(0, -2, 1f);
                    info.setLayoutParams(infoParams);
                    
                    TextView title = new TextView(requireContext());
                    title.setText(s.title);
                    title.setTextColor(getResources().getColor(R.color.bloom_text, null));
                    title.setTextSize(17);
                    title.setTypeface(null, android.graphics.Typeface.BOLD);
                    info.addView(title);
                    
                    TextView detail = new TextView(requireContext());
                    detail.setText(getString(R.string.activity_detail_format, s.detail, s.duration, s.xp));
                    detail.setTextColor(getResources().getColor(R.color.bloom_pink, null));
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
                            BloomDialogHelper.showCelebrationDialog(requireContext(), "Activity Completed! 🎉", "Great job completing " + s.title + "!", s.xp, rewards);
                        }
                    });
                    card.addView(add);
                    
                    list.addView(card);
                }
            });
        });
    }

    private void updateFilterButtons(View v) {
        int activeBg = R.drawable.bg_gradient_card;
        int inactiveBg = R.drawable.bg_mood_pill;
        int activeText = Color.WHITE;
        int inactiveText = getResources().getColor(R.color.bloom_text, null);

        Button b1 = v.findViewById(R.id.filterForYou);
        Button b2 = v.findViewById(R.id.filterAmazing);
        Button b3 = v.findViewById(R.id.filterGood);
        Button b4 = v.findViewById(R.id.filterMeh);
        Button b5 = v.findViewById(R.id.filterLow);
        Button b6 = v.findViewById(R.id.filterStressed);

        b1.setBackgroundResource(currentFilter == null ? activeBg : inactiveBg);
        b1.setTextColor(currentFilter == null ? activeText : inactiveText);

        b2.setBackgroundResource("amazing".equals(currentFilter) ? activeBg : inactiveBg);
        b2.setTextColor("amazing".equals(currentFilter) ? activeText : inactiveText);

        b3.setBackgroundResource("good".equals(currentFilter) ? activeBg : inactiveBg);
        b3.setTextColor("good".equals(currentFilter) ? activeText : inactiveText);

        b4.setBackgroundResource("meh".equals(currentFilter) ? activeBg : inactiveBg);
        b4.setTextColor("meh".equals(currentFilter) ? activeText : inactiveText);

        b5.setBackgroundResource("low".equals(currentFilter) ? activeBg : inactiveBg);
        b5.setTextColor("low".equals(currentFilter) ? activeText : inactiveText);

        b6.setBackgroundResource("stressed".equals(currentFilter) ? activeBg : inactiveBg);
        b6.setTextColor("stressed".equals(currentFilter) ? activeText : inactiveText);
    }

    private void showActivityDetails(Suggestion s) {
        RewardManager rewards = new RewardManager(requireContext());
        BloomDialogHelper.showActivityDetails(requireContext(), s, rewards, this::refresh);
    }
}
