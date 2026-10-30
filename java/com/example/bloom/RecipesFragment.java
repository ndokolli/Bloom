package com.example.bloom;

import android.os.Bundle;
import android.graphics.Color;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.example.bloom.data.BloomDatabase;
import com.example.bloom.engine.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipesFragment extends Fragment {

    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private String currentFilter = null;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b) {
        return i.inflate(R.layout.fragment_recipes, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle b) {
        v.findViewById(R.id.filterForYou).setOnClickListener(x -> { currentFilter = null; refresh(); });
        v.findViewById(R.id.filterComfort).setOnClickListener(x -> { currentFilter = "low"; refresh(); });
        v.findViewById(R.id.filterBoost).setOnClickListener(x -> { currentFilter = "amazing"; refresh(); });
        v.findViewById(R.id.filterBalanced).setOnClickListener(x -> { currentFilter = "good"; refresh(); });
        
        refresh();
    }

    private void refresh() {
        io.execute(() -> {
            String mood = BloomDatabase.getInstance(requireContext()).moodDao().latestMood();
            List<Suggestion> recipes = currentFilter == null 
                    ? AdaptiveEngine.recipesFor(mood)
                    : AdaptiveEngine.recipesForIntensity(currentFilter);
            
            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> {
                View v = getView();
                if (v == null) return;
                
                updateFilterButtons(v);
                
                ((TextView) v.findViewById(R.id.textListTitle)).setText(R.string.recipes_heading);

                GridLayout grid = v.findViewById(R.id.gridContainer);
                grid.removeAllViews();
                
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                int cardWidth = (screenWidth - (int)(64 * getResources().getDisplayMetrics().density)) / 2;

                for (Suggestion r : recipes) {
                    LinearLayout card = new LinearLayout(requireContext());
                    card.setOrientation(LinearLayout.VERTICAL);
                    card.setBackgroundResource(R.drawable.bg_card);
                    card.setElevation(6f * getResources().getDisplayMetrics().density);
                    card.setClipToOutline(true);
                    card.setOnClickListener(x -> showRecipe(r));
                    
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = cardWidth;
                    params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                    params.setMargins(8, 8, 8, 24);
                    card.setLayoutParams(params);
                    

                    RelativeLayout header = new RelativeLayout(requireContext());
                    header.setLayoutParams(new LinearLayout.LayoutParams(-1, (int)(120 * getResources().getDisplayMetrics().density)));
                    boolean isWarm = r.intensity.equals("low") || r.intensity.equals("meh");
                    header.setBackgroundResource(isWarm ? R.drawable.bg_gradient_orange : R.drawable.bg_gradient_mint);
                    
                    TextView emoji = new TextView(requireContext());
                    emoji.setText(r.emoji);
                    emoji.setTextSize(32);
                    emoji.setBackgroundResource(R.drawable.bg_streak_badge);
                    emoji.setPadding(12, 12, 12, 12);
                    emoji.setGravity(android.view.Gravity.CENTER);
                    RelativeLayout.LayoutParams emojiParams = new RelativeLayout.LayoutParams(-2, -2);
                    emojiParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    header.addView(emoji, emojiParams);
                    
                    card.addView(header);
                    

                    LinearLayout content = new LinearLayout(requireContext());
                    content.setOrientation(LinearLayout.VERTICAL);
                    content.setPadding(16, 16, 16, 16);
                    
                    TextView title = new TextView(requireContext());
                    title.setText(r.title);
                    title.setTextColor(getResources().getColor(R.color.bloom_text, null));
                    title.setTextSize(17);
                    title.setTypeface(null, android.graphics.Typeface.BOLD);
                    content.addView(title);
                    
                    TextView detail = new TextView(requireContext());
                    detail.setText(r.detail);
                    detail.setTextColor(getResources().getColor(R.color.bloom_muted, null));
                    detail.setTextSize(13);
                    detail.setPadding(0, 4, 0, 8);
                    content.addView(detail);
                    
                    TextView duration = new TextView(requireContext());
                    duration.setText(getString(R.string.recipe_duration_format, r.duration));
                    duration.setTextColor(getResources().getColor(R.color.bloom_pink, null));
                    duration.setTextSize(13);
                    duration.setTypeface(null, android.graphics.Typeface.BOLD);
                    content.addView(duration);
                    
                    card.addView(content);
                    grid.addView(card);
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
        Button b2 = v.findViewById(R.id.filterComfort);
        Button b3 = v.findViewById(R.id.filterBoost);
        Button b4 = v.findViewById(R.id.filterBalanced);

        b1.setBackgroundResource(currentFilter == null ? activeBg : inactiveBg);
        b1.setTextColor(currentFilter == null ? activeText : inactiveText);
        
        b2.setBackgroundResource("low".equals(currentFilter) ? activeBg : inactiveBg);
        b2.setTextColor("low".equals(currentFilter) ? activeText : inactiveText);
        
        b3.setBackgroundResource("amazing".equals(currentFilter) ? activeBg : inactiveBg);
        b3.setTextColor("amazing".equals(currentFilter) ? activeText : inactiveText);
        
        b4.setBackgroundResource("good".equals(currentFilter) ? activeBg : inactiveBg);
        b4.setTextColor("good".equals(currentFilter) ? activeText : inactiveText);
    }

    private void showRecipe(Suggestion s) {
        BloomDialogHelper.showRecipeDetails(requireContext(), s);
    }
}
