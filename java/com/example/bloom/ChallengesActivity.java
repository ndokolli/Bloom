package com.example.bloom;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bloom.rewards.RewardManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

public class ChallengesActivity extends AppCompatActivity {

    private RewardManager rewards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        rewards = new RewardManager(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbarChallenges);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        LinearLayout container = findViewById(R.id.challengesContainer);
        addChallenge(container, "steps_5k", getString(R.string.challenge_steps_title), 20);
        addChallenge(container, "steps_8k", getString(R.string.challenge_steps_8k), 30);
        addChallenge(container, "water_2l", getString(R.string.challenge_water_title), 20);
        addChallenge(container, "workout_10min", getString(R.string.challenge_workout), 25);
        addChallenge(container, "walk_20min", getString(R.string.challenge_walk), 20);
        addChallenge(container, "stretch_5min", getString(R.string.challenge_stretch), 15);
        addChallenge(container, "recommended", getString(R.string.challenge_recommended), 20);
        addChallenge(container, "veggies", getString(R.string.challenge_veggies), 15);
        addChallenge(container, "breakfast", getString(R.string.challenge_breakfast), 15);
        addChallenge(container, "relax_5min", getString(R.string.challenge_relax), 15);
        addChallenge(container, "mindfulness_5min", getString(R.string.challenge_mindfulness_title), 15);
        addChallenge(container, "sleep_7h", getString(R.string.challenge_sleep), 25);
        addChallenge(container, "sugar_free", getString(R.string.challenge_sugar), 30);
        addChallenge(container, "meals_3h", getString(R.string.challenge_meals), 30);
    }

    private void addChallenge(LinearLayout container, String taskId, String title, int points) {
        MaterialCardView card = new MaterialCardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.bottomMargin = (int)(12 * getResources().getDisplayMetrics().density);
        card.setLayoutParams(params);
        card.setRadius(24 * getResources().getDisplayMetrics().density);
        card.setCardElevation(2 * getResources().getDisplayMetrics().density);
        card.setCardBackgroundColor(getResources().getColor(R.color.bloom_card, null));

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(20, 20, 20, 20);
        row.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout textInfo = new LinearLayout(this);
        textInfo.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(0, -2, 1f);
        textInfo.setLayoutParams(infoParams);

        TextView tView = new TextView(this);
        tView.setText(title);
        tView.setTextColor(getResources().getColor(R.color.bloom_text, null));
        tView.setTextSize(16);
        tView.setTypeface(null, android.graphics.Typeface.BOLD);
        textInfo.addView(tView);

        TextView pView = new TextView(this);
        pView.setText(getString(R.string.points_format, points));
        pView.setTextColor(getResources().getColor(R.color.bloom_pink, null));
        pView.setTextSize(13);
        textInfo.addView(pView);

        row.addView(textInfo);

        TextView plus = new TextView(this);
        boolean done = rewards.isDoneToday(taskId);
        plus.setText(done ? "✓" : "+");
        plus.setTextSize(22);
        plus.setTextColor(getResources().getColor(R.color.bloom_pink, null));
        plus.setPadding(20, 20, 20, 20);
        plus.setClickable(!done);
        plus.setFocusable(!done);
        
        plus.setOnClickListener(v -> {
            if (rewards.completeTask(taskId, points)) {
                plus.setText("✓");
                plus.setClickable(false);
                plus.setFocusable(false);
                BloomDialogHelper.showCelebrationDialog(this, "Challenge Complete! 🏆", "Awesome effort completing " + title + "!", points, rewards);
            }
        });

        row.addView(plus);

        card.addView(row);
        container.addView(card);
    }
}
