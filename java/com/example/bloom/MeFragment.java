package com.example.bloom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.bloom.rewards.RewardManager;

public class MeFragment extends Fragment {

    private RewardManager rewards;
    private TextView textName, textAge, textHeight, textWeight, textUserAvatar;
    private TextView textStepsSubMe, textWaterSubMe;
    private ProgressBar progressStepsMe;
    private LinearLayout layoutWaterDotsMe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rewards = new RewardManager(requireContext());

        textName = view.findViewById(R.id.textNameVal);
        textAge = view.findViewById(R.id.textAgeVal);
        textHeight = view.findViewById(R.id.textHeightVal);
        textWeight = view.findViewById(R.id.textWeightVal);
        textUserAvatar = view.findViewById(R.id.textUserAvatar);

        if (textUserAvatar != null) {
            textUserAvatar.setOnClickListener(v -> BloomDialogHelper.showAvatarPicker(requireContext(), rewards, this::refreshProfileData));
        }

        textStepsSubMe = view.findViewById(R.id.textStepsSubMe);
        textWaterSubMe = view.findViewById(R.id.textWaterSubMe);
        progressStepsMe = view.findViewById(R.id.progressStepsMe);
        layoutWaterDotsMe = view.findViewById(R.id.layoutWaterDotsMe);

        refreshProfileData();

        view.findViewById(R.id.buttonSaveProfile).setOnClickListener(v -> showEditDialog());
        view.findViewById(R.id.buttonEditProfile).setOnClickListener(v -> showEditDialog());

        view.findViewById(R.id.buttonInviteFriends).setOnClickListener(v -> 
            Toast.makeText(requireContext(), "Invitation link copied! 🔗", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.buttonBrowseChallenges).setOnClickListener(v -> 
            Navigation.findNavController(view).navigate(R.id.friendsFragment));

        View addFriendBtn = view.findViewById(R.id.layoutAddFriendMe);
        if (addFriendBtn != null) {
            addFriendBtn.setOnClickListener(v -> BloomDialogHelper.showAddFriend(requireContext(), rewards, this::refreshProfileData));
        }

        View seeAllBtn = view.findViewById(R.id.layoutSeeAllFriendsMe);
        if (seeAllBtn != null) {
            seeAllBtn.setOnClickListener(v -> BloomDialogHelper.showFriendsList(requireContext(), rewards, this::refreshProfileData));
        }

        View statFriends = view.findViewById(R.id.statFriendsMe);
        if (statFriends != null) statFriends.setOnClickListener(v -> BloomDialogHelper.showFriendsList(requireContext(), rewards, this::refreshProfileData));

        View statChallenges = view.findViewById(R.id.statChallengesMe);
        if (statChallenges != null) statChallenges.setOnClickListener(v -> BloomDialogHelper.showChallenges(requireContext(), rewards));

        View statAchievements = view.findViewById(R.id.statAchievementsMe);
        if (statAchievements != null) statAchievements.setOnClickListener(v -> BloomDialogHelper.showAchievements(requireContext(), rewards));

        View cardSteps = view.findViewById(R.id.cardStepsMe);
        if (cardSteps != null) {
            cardSteps.setOnClickListener(v -> {
                rewards.addSteps(500);
                Toast.makeText(requireContext(), "+500 steps! 👟", Toast.LENGTH_SHORT).show();
                refreshProfileData();
            });
        }

        View cardWater = view.findViewById(R.id.cardWaterMe);
        if (cardWater != null) {
            cardWater.setOnClickListener(v -> {
                rewards.incrementWater();
                int current = rewards.getWater();
                if (current == 0) {
                    Toast.makeText(requireContext(), "Water count reset! 💧", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Glass added! (" + current + "/8) 💧", Toast.LENGTH_SHORT).show();
                }
                refreshProfileData();
            });
        }

        view.findViewById(R.id.buttonSignOut).setOnClickListener(v -> signOut());
        view.findViewById(R.id.buttonDeleteInfo).setOnClickListener(v -> confirmDeleteAll());
    }

    private void showEditDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_onboarding, null);
        EditText nameInput = dialogView.findViewById(R.id.onboardName);
        EditText ageInput = dialogView.findViewById(R.id.onboardAge);
        EditText heightInput = dialogView.findViewById(R.id.onboardHeight);
        EditText weightInput = dialogView.findViewById(R.id.onboardWeight);
        
        nameInput.setText(rewards.getName());
        ageInput.setText(String.valueOf(rewards.getAge()));
        heightInput.setText(String.valueOf(rewards.getHeight()));
        weightInput.setText(String.valueOf(rewards.getWeight()));

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Update Profile")
                .setView(dialogView)
                .setPositiveButton("Save", (d, w) -> {
                    try {
                        rewards.setName(nameInput.getText().toString());
                        rewards.setAge(Integer.parseInt(ageInput.getText().toString()));
                        rewards.setHeight(Integer.parseInt(heightInput.getText().toString()));
                        rewards.setWeight(Integer.parseInt(weightInput.getText().toString()));
                        refreshProfileData();
                        Toast.makeText(requireContext(), "Profile updated! ✨", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "Error updating profile", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void refreshProfileData() {
        textName.setText(rewards.getName());
        textAge.setText(String.valueOf(rewards.getAge()));
        textHeight.setText(getString(R.string.label_height_unit_format, rewards.getHeight()));
        textWeight.setText(getString(R.string.label_weight_unit_format, rewards.getWeight()));
        
        String currentAvatar = rewards.getSelectedAvatar();
        if (textUserAvatar != null) {
            textUserAvatar.setText(currentAvatar);
        }

        View root = getView();
        if (root != null) {
            TextView textSubtitle = root.findViewById(R.id.textMeSubtitle);
            if (textSubtitle != null) {
                textSubtitle.setText(currentAvatar + " " + rewards.getAvatarTitle(currentAvatar) + " · Level " + rewards.getLevel());
            }

            ((TextView) root.findViewById(R.id.textFriendsCount)).setText(String.valueOf(rewards.getFriendsCount()));
            ((TextView) root.findViewById(R.id.textChallengesCount)).setText(String.valueOf(rewards.getChallengesCompletedToday()));
            ((TextView) root.findViewById(R.id.textAchievementsCount)).setText(String.valueOf(rewards.getAchievementsCount()));
        }

        int steps = rewards.getSteps();
        if (textStepsSubMe != null) {
            textStepsSubMe.setText(getString(R.string.challenge_steps_format, steps));
        }
        if (progressStepsMe != null) {
            progressStepsMe.setProgress(steps);
        }

        int water = rewards.getWater();
        if (textWaterSubMe != null) {
            textWaterSubMe.setText(getString(R.string.challenge_water_format, water));
        }
        if (layoutWaterDotsMe != null) {
            for (int i = 0; i < layoutWaterDotsMe.getChildCount(); i++) {
                View dot = layoutWaterDotsMe.getChildAt(i);
                if (i < water) {
                    dot.setBackgroundResource(R.drawable.bg_circle_outline);
                    dot.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                            getResources().getColor(R.color.bloom_purple, null)));
                } else {
                    dot.setBackgroundResource(R.drawable.bg_circle_outline);
                    dot.setBackgroundTintList(null);
                }
            }
        }
    }

    private void signOut() {
        rewards.setLoggedIn(false);
        Navigation.findNavController(requireView()).navigate(R.id.loginFragment);
    }

    private void confirmDeleteAll() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle(R.string.confirm_delete_title)
                .setMessage(R.string.confirm_delete_message)
                .setPositiveButton(R.string.delete, (d, w) -> {
                    rewards.clearAll(requireContext());
                    Navigation.findNavController(requireView()).navigate(R.id.splashFragment);
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
