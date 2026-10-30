package com.example.bloom;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.bloom.rewards.RewardManager;

public class FriendsFragment extends Fragment {

    private RewardManager rewards;
    private TextView textSteps, textWater;
    private ProgressBar progressSteps;
    private LinearLayout layoutWaterDots, layoutMindfulnessStatus;

    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private final Runnable uiRunnable = new Runnable() {
        @Override
        public void run() {
            updateUI();
            uiHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        uiHandler.post(uiRunnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHandler.removeCallbacks(uiRunnable);
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b) {
        return i.inflate(R.layout.fragment_friends, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rewards = new RewardManager(requireContext());

        textSteps = view.findViewById(R.id.textStepsProgress);
        progressSteps = view.findViewById(R.id.progressSteps);
        textWater = view.findViewById(R.id.textWaterProgress);
        layoutWaterDots = view.findViewById(R.id.layoutWaterDots);
        layoutMindfulnessStatus = view.findViewById(R.id.layoutMindfulnessStatus);

        view.findViewById(R.id.buttonInvite).setOnClickListener(v -> 
            Toast.makeText(requireContext(), "Invitation link copied! 🔗", Toast.LENGTH_SHORT).show());
        
        view.findViewById(R.id.layoutAddFriend).setOnClickListener(v -> 
            BloomDialogHelper.showAddFriend(requireContext(), rewards, this::updateUI));

        view.findViewById(R.id.textSeeAllFriends).setOnClickListener(v -> 
            BloomDialogHelper.showFriendsList(requireContext(), rewards, this::updateUI));

        view.findViewById(R.id.cardSteps).setOnClickListener(v -> {
            rewards.addSteps(500);
            Toast.makeText(requireContext(), "+500 steps! 👟", Toast.LENGTH_SHORT).show();
            updateUI();
        });

        view.findViewById(R.id.cardWater).setOnClickListener(v -> {
            rewards.incrementWater();
            int current = rewards.getWater();
            if (current == 0) {
                Toast.makeText(requireContext(), "Water count reset! 💧", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Glass added! (" + current + "/8) 💧", Toast.LENGTH_SHORT).show();
            }
            updateUI();
        });

        view.findViewById(R.id.cardMindfulness).setOnClickListener(v -> {
            if (rewards.completeTask("mindfulness_5min", 15)) {
                Toast.makeText(requireContext(), "Mindfulness complete! +15 XP ✨", Toast.LENGTH_SHORT).show();
                updateUI();
            }
        });

        view.findViewById(R.id.buttonBrowseChallenges).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ChallengesActivity.class);
            startActivity(intent);
        });

        updateUI();
    }

    private void updateUI() {
        int steps = rewards.getSteps();
        textSteps.setText(getString(R.string.challenge_steps_format, steps));
        progressSteps.setProgress(steps);

        int water = rewards.getWater();
        textWater.setText(getString(R.string.challenge_water_format, water));
        for (int i = 0; i < layoutWaterDots.getChildCount(); i++) {
            View dot = layoutWaterDots.getChildAt(i);
            if (i < water) {
                dot.setBackgroundResource(R.drawable.bg_circle_outline);
                dot.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                    getResources().getColor(R.color.bloom_pink, null)));
            } else {
                dot.setBackgroundResource(R.drawable.bg_circle_outline);
                dot.setBackgroundTintList(null);
            }
        }

        if (rewards.isDoneToday("mindfulness_5min")) {
            layoutMindfulnessStatus.setVisibility(View.VISIBLE);
        }
    }
}
