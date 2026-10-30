package com.example.bloom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bloom.rewards.RewardManager;
import java.util.List;

public class BloomDialogHelper {

    public static void showFriendsList(Context context, RewardManager rewards, Runnable onRefresh) {
        Dialog dialog = createBloomDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_bloom_custom, null);
        dialog.setContentView(view);

        ((TextView) view.findViewById(R.id.dialogIcon)).setText("👥");
        List<String> friends = rewards.getFriendsList();
        ((TextView) view.findViewById(R.id.dialogTitle)).setText("Your Friends (" + friends.size() + ")");
        ((TextView) view.findViewById(R.id.dialogSubtitle)).setText("Connected wellness buddies");

        LinearLayout listContainer = view.findViewById(R.id.dialogListContainer);
        listContainer.removeAllViews();

        for (String friend : friends) {
            View item = createFriendRow(context, friend);
            listContainer.addView(item);
        }



        Button btnPrimary = view.findViewById(R.id.buttonDialogPrimary);
        btnPrimary.setText("Close");
        btnPrimary.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public static void showAddFriend(Context context, RewardManager rewards, Runnable onRefresh) {
        Dialog dialog = createBloomDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_bloom_custom, null);
        dialog.setContentView(view);

        ((TextView) view.findViewById(R.id.dialogIcon)).setText("➕");
        ((TextView) view.findViewById(R.id.dialogTitle)).setText("Add a Friend");
        ((TextView) view.findViewById(R.id.dialogSubtitle)).setText("Type your friend's name to connect");

        LinearLayout listContainer = view.findViewById(R.id.dialogListContainer);
        listContainer.removeAllViews();

        EditText input = new EditText(context);
        input.setHint("Friend's name (e.g., Maya)");
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        input.setBackgroundResource(R.drawable.bg_input_field);
        int pad = dpToPx(context, 14);
        input.setPadding(pad, pad, pad, pad);
        input.setTextColor(context.getResources().getColor(R.color.bloom_text, null));
        input.setHintTextColor(context.getResources().getColor(R.color.bloom_muted, null));

        listContainer.addView(input, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Button btnSecondary = view.findViewById(R.id.buttonDialogSecondary);
        btnSecondary.setVisibility(View.VISIBLE);
        btnSecondary.setText("Cancel");
        btnSecondary.setTextColor(context.getResources().getColor(R.color.bloom_text, null));
        btnSecondary.setOnClickListener(v -> dialog.dismiss());

        Button btnPrimary = view.findViewById(R.id.buttonDialogPrimary);
        btnPrimary.setText("Add Friend");
        btnPrimary.setOnClickListener(v -> {
            String name = input.getText().toString().trim();
            if (!name.isEmpty()) {
                rewards.addFriend(name);
                Toast.makeText(context, "Added " + name + " to your friends! 🎉", Toast.LENGTH_SHORT).show();
                if (onRefresh != null) onRefresh.run();
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    public static void showChallenges(Context context, RewardManager rewards) {
        Dialog dialog = createBloomDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_bloom_custom, null);
        dialog.setContentView(view);

        ((TextView) view.findViewById(R.id.dialogIcon)).setText("🏆");
        ((TextView) view.findViewById(R.id.dialogTitle)).setText("Today's Challenges");
        ((TextView) view.findViewById(R.id.dialogSubtitle)).setText("Completed & active daily goals");

        LinearLayout listContainer = view.findViewById(R.id.dialogListContainer);
        listContainer.removeAllViews();

        List<String> completed = rewards.getCompletedChallengesList();
        for (String c : completed) {
            View item = createChallengeRow(context, c);
            listContainer.addView(item);
        }

        Button btnPrimary = view.findViewById(R.id.buttonDialogPrimary);
        btnPrimary.setText("Awesome!");
        btnPrimary.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public static void showAchievements(Context context, RewardManager rewards) {
        Dialog dialog = createBloomDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_bloom_custom, null);
        dialog.setContentView(view);

        ((TextView) view.findViewById(R.id.dialogIcon)).setText("⭐");
        ((TextView) view.findViewById(R.id.dialogTitle)).setText("Your Achievements (" + rewards.getAchievementsCount() + ")");
        ((TextView) view.findViewById(R.id.dialogSubtitle)).setText("Unlocked badges & milestones");

        LinearLayout listContainer = view.findViewById(R.id.dialogListContainer);
        listContainer.removeAllViews();

        List<String> achievements = rewards.getAchievementsList();
        for (String a : achievements) {
            View item = createAchievementRow(context, a);
            listContainer.addView(item);
        }

        Button btnPrimary = view.findViewById(R.id.buttonDialogPrimary);
        btnPrimary.setText("Keep Going!");
        btnPrimary.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public static void showRecipeDetails(Context context, com.example.bloom.engine.Suggestion recipe) {
        Dialog dialog = createBloomDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_recipe_detail, null);
        dialog.setContentView(view);

        ((TextView) view.findViewById(R.id.dialogRecipeIcon)).setText(recipe.emoji);
        ((TextView) view.findViewById(R.id.dialogRecipeTitle)).setText(recipe.title);

        TextView textPrep = view.findViewById(R.id.dialogRecipePrep);
        if (recipe.duration != null && !recipe.duration.isEmpty()) {
            textPrep.setText("⏱️ Prep: " + recipe.duration);
            textPrep.setVisibility(View.VISIBLE);
        } else {
            textPrep.setVisibility(View.GONE);
        }

        TextView textMacros = view.findViewById(R.id.dialogRecipeMacros);
        if (recipe.detail != null && !recipe.detail.isEmpty()) {
            textMacros.setText("🥗 " + recipe.detail);
            textMacros.setVisibility(View.VISIBLE);
        } else {
            textMacros.setVisibility(View.GONE);
        }

        LinearLayout ingredientsContainer = view.findViewById(R.id.dialogIngredientsContainer);
        ingredientsContainer.removeAllViews();
        if (recipe.ingredients != null && !recipe.ingredients.trim().isEmpty()) {
            String[] items = recipe.ingredients.split(",\\s*");
            for (String item : items) {
                if (!item.trim().isEmpty()) {
                    ingredientsContainer.addView(createIngredientRow(context, item.trim()));
                }
            }
        }

        LinearLayout instructionsContainer = view.findViewById(R.id.dialogInstructionsContainer);
        instructionsContainer.removeAllViews();
        if (recipe.instructions != null && !recipe.instructions.trim().isEmpty()) {
            String[] rawSteps = recipe.instructions.split("(?=\\b\\d+\\.\\s*)");
            int stepNum = 1;
            for (String raw : rawSteps) {
                String clean = raw.replaceAll("^\\d+\\.\\s*", "").trim();
                if (!clean.isEmpty()) {
                    instructionsContainer.addView(createInstructionRow(context, stepNum++, clean));
                }
            }
        }

        Button btnDone = view.findViewById(R.id.buttonRecipeDone);
        btnDone.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public static void showActivityDetails(Context context, com.example.bloom.engine.Suggestion activity) {
        showActivityDetails(context, activity, null, null);
    }

    public static void showActivityDetails(Context context, com.example.bloom.engine.Suggestion activity, RewardManager rewards, Runnable onComplete) {
        Dialog dialog = createBloomDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_activity_detail, null);
        dialog.setContentView(view);

        ((TextView) view.findViewById(R.id.dialogActivityIcon)).setText(activity.emoji);
        ((TextView) view.findViewById(R.id.dialogActivityTitle)).setText(activity.title);

        TextView textDuration = view.findViewById(R.id.dialogActivityDuration);
        if (activity.duration != null && !activity.duration.isEmpty()) {
            textDuration.setText("⏱️ " + activity.duration);
            textDuration.setVisibility(View.VISIBLE);
        } else {
            textDuration.setVisibility(View.GONE);
        }

        TextView textGoal = view.findViewById(R.id.dialogActivityGoal);
        String goalText = (activity.focus != null && !activity.focus.isEmpty()) ? activity.focus : activity.detail;
        if (goalText != null && !goalText.isEmpty()) {
            textGoal.setText("🎯 " + goalText);
            textGoal.setVisibility(View.VISIBLE);
        } else {
            textGoal.setVisibility(View.GONE);
        }

        TextView textXp = view.findViewById(R.id.dialogActivityXp);
        if (activity.xp > 0) {
            textXp.setText("⭐ +" + activity.xp + " XP");
            textXp.setVisibility(View.VISIBLE);
        } else {
            textXp.setVisibility(View.GONE);
        }

        LinearLayout movementsContainer = view.findViewById(R.id.dialogMovementsContainer);
        movementsContainer.removeAllViews();
        if (activity.movements != null && !activity.movements.trim().isEmpty()) {
            String[] items = activity.movements.split(",\\s*");
            int idx = 1;
            for (String item : items) {
                if (!item.trim().isEmpty()) {
                    movementsContainer.addView(createMovementRow(context, idx++, item.trim()));
                }
            }
        }

        Button btnPrimary = view.findViewById(R.id.buttonActivityPrimary);
        Button btnSecondary = view.findViewById(R.id.buttonActivitySecondary);

        if (rewards != null && !rewards.isDoneToday(activity.id)) {
            btnPrimary.setText("Complete Activity (+" + activity.xp + " XP) 🎉");
            btnSecondary.setVisibility(View.VISIBLE);
            btnSecondary.setOnClickListener(v -> dialog.dismiss());

            btnPrimary.setOnClickListener(v -> {
                if (rewards.completeTask(activity.id, activity.xp)) {
                    if (onComplete != null) onComplete.run();
                    dialog.dismiss();
                    showCelebrationDialog(context, "Activity Completed! 🎉", "Great job completing " + activity.title + "!", activity.xp, rewards);
                }
            });
        } else if (rewards != null && rewards.isDoneToday(activity.id)) {
            btnPrimary.setText("Completed Today ✓");
            btnPrimary.setEnabled(false);
            btnSecondary.setVisibility(View.VISIBLE);
            btnSecondary.setText("Close");
            btnSecondary.setOnClickListener(v -> dialog.dismiss());
        } else {
            btnPrimary.setText("Got it! ✨");
            btnPrimary.setOnClickListener(v -> dialog.dismiss());
            btnSecondary.setVisibility(View.GONE);
        }

        dialog.show();
    }

    public static void showCelebrationDialog(Context context, String title, String message, int xpEarned, RewardManager rewards) {
        Dialog dialog = createBloomDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_celebration, null);
        dialog.setContentView(view);

        TextView icon = view.findViewById(R.id.dialogCelebrationIcon);
        TextView tag = view.findViewById(R.id.dialogCelebrationTag);
        TextView textTitle = view.findViewById(R.id.dialogCelebrationTitle);
        TextView textMessage = view.findViewById(R.id.dialogCelebrationMessage);
        TextView textXp = view.findViewById(R.id.dialogCelebrationXpBadge);
        TextView textStreak = view.findViewById(R.id.dialogCelebrationStreakBadge);

        if (title != null) textTitle.setText(title);
        if (message != null) textMessage.setText(message);

        textXp.setText("⭐ +" + xpEarned + " XP");
        if (rewards != null) {
            textStreak.setText("🔥 " + rewards.getStreak() + "-Day Streak");
            if (rewards.getProgress() < xpEarned && rewards.getLevel() > 1) {
                icon.setText("👑");
                tag.setText("LEVEL UP UNLOCKED!");
                textTitle.setText("Level " + rewards.getLevel() + " Explorer! 🎉");
            }
        }

        Button btnDone = view.findViewById(R.id.buttonCelebrationDone);
        btnDone.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public static void showAvatarPicker(Context context, RewardManager rewards, Runnable onAvatarChanged) {
        Dialog dialog = createBloomDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_avatar_picker, null);
        dialog.setContentView(view);

        TextView currentIcon = view.findViewById(R.id.dialogAvatarCurrentIcon);
        currentIcon.setText(rewards.getSelectedAvatar());

        LinearLayout grid = view.findViewById(R.id.dialogAvatarGrid);
        grid.removeAllViews();

        String[] avatars = new String[]{"🌸", "🐻", "🌿", "🐱", "🥑", "🦋", "🚀", "🦄", "🌈", "🐲", "🌟", "☕"};
        String[] names = new String[]{"Cherry Blossom", "Bibo Bear", "Sprout", "Zen Kitty", "Avocado Toast", "Butterfly", "Cosmic Rocket", "Hydration Unicorn", "Rainbow Cloud", "Golden Dragon", "Star Master", "Cozy Boba"};

        for (int i = 0; i < avatars.length; i++) {
            final String emoji = avatars[i];
            String name = names[i];
            String titleTag = rewards.getAvatarTitle(emoji);
            boolean unlocked = rewards.isAvatarUnlocked(emoji);
            String condition = rewards.getAvatarUnlockCondition(emoji);

            LinearLayout row = new LinearLayout(context);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(android.view.Gravity.CENTER_VERTICAL);
            row.setBackgroundResource(R.drawable.bg_input_field);
            row.setPadding(dpToPx(context, 12), dpToPx(context, 10), dpToPx(context, 12), dpToPx(context, 10));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = dpToPx(context, 8);
            row.setLayoutParams(params);

            TextView avatarView = new TextView(context);
            avatarView.setText(emoji);
            avatarView.setTextSize(22);
            avatarView.setGravity(android.view.Gravity.CENTER);
            avatarView.setBackgroundResource(R.drawable.bg_white_circle);
            int size = dpToPx(context, 42);
            row.addView(avatarView, new LinearLayout.LayoutParams(size, size));

            LinearLayout info = new LinearLayout(context);
            info.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            infoParams.setMarginStart(dpToPx(context, 12));
            info.setLayoutParams(infoParams);

            TextView textName = new TextView(context);
            textName.setText(name + " (" + titleTag + ")");
            textName.setTextSize(13);
            textName.setTypeface(null, android.graphics.Typeface.BOLD);
            textName.setTextColor(context.getResources().getColor(R.color.bloom_text, null));
            info.addView(textName);

            TextView textCondition = new TextView(context);
            textCondition.setText(unlocked ? "✨ Unlocked" : "🔒 " + condition);
            textCondition.setTextSize(12);
            textCondition.setTextColor(unlocked ? context.getResources().getColor(R.color.bloom_pink, null) : context.getResources().getColor(R.color.bloom_muted, null));
            info.addView(textCondition);

            row.addView(info);

            if (unlocked) {
                boolean isSelected = emoji.equals(rewards.getSelectedAvatar());
                TextView badge = new TextView(context);
                badge.setText(isSelected ? "Selected ✓" : "Select");
                badge.setTextSize(11);
                badge.setTypeface(null, android.graphics.Typeface.BOLD);
                badge.setTextColor(context.getResources().getColor(isSelected ? R.color.bloom_purple : R.color.bloom_text, null));
                badge.setBackgroundResource(R.drawable.bg_white_pill);
                badge.setPadding(dpToPx(context, 10), dpToPx(context, 4), dpToPx(context, 10), dpToPx(context, 4));
                row.addView(badge);

                row.setOnClickListener(v -> {
                    rewards.setSelectedAvatar(emoji);
                    currentIcon.setText(emoji);
                    Toast.makeText(context, "Avatar updated to " + emoji + " (" + titleTag + ") ✨", Toast.LENGTH_SHORT).show();
                    if (onAvatarChanged != null) onAvatarChanged.run();
                    dialog.dismiss();
                });
            } else {
                row.setAlpha(0.6f);
                row.setOnClickListener(v -> Toast.makeText(context, "Locked: " + condition, Toast.LENGTH_SHORT).show());
            }

            grid.addView(row);
        }

        Button btnClose = view.findViewById(R.id.buttonAvatarClose);
        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private static View createIngredientRow(Context context, String ingredient) {
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(android.view.Gravity.CENTER_VERTICAL);
        row.setPadding(dpToPx(context, 6), dpToPx(context, 6), dpToPx(context, 6), dpToPx(context, 6));

        TextView dot = new TextView(context);
        dot.setText("🌿");
        dot.setTextSize(13);
        row.addView(dot);

        TextView text = new TextView(context);
        text.setText(ingredient);
        text.setTextSize(14);
        text.setTextColor(context.getResources().getColor(R.color.bloom_text, null));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        params.setMarginStart(dpToPx(context, 10));
        row.addView(text, params);

        return row;
    }

    private static View createInstructionRow(Context context, int stepNum, String stepText) {
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(android.view.Gravity.TOP);
        row.setPadding(dpToPx(context, 6), dpToPx(context, 6), dpToPx(context, 6), dpToPx(context, 6));

        TextView badge = new TextView(context);
        badge.setText(String.valueOf(stepNum));
        badge.setTextSize(12);
        badge.setTypeface(null, android.graphics.Typeface.BOLD);
        badge.setTextColor(context.getResources().getColor(R.color.bloom_pink, null));
        badge.setBackgroundResource(R.drawable.bg_icon_circle);
        badge.setGravity(android.view.Gravity.CENTER);
        int size = dpToPx(context, 24);
        row.addView(badge, new LinearLayout.LayoutParams(size, size));

        TextView text = new TextView(context);
        text.setText(stepText);
        text.setTextSize(14);
        text.setTextColor(context.getResources().getColor(R.color.bloom_text, null));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        params.setMarginStart(dpToPx(context, 10));
        row.addView(text, params);

        return row;
    }

    private static View createMovementRow(Context context, int index, String movement) {
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(android.view.Gravity.CENTER_VERTICAL);
        row.setPadding(dpToPx(context, 6), dpToPx(context, 6), dpToPx(context, 6), dpToPx(context, 6));

        TextView badge = new TextView(context);
        badge.setText(String.valueOf(index));
        badge.setTextSize(11);
        badge.setTypeface(null, android.graphics.Typeface.BOLD);
        badge.setTextColor(context.getResources().getColor(R.color.bloom_purple, null));
        badge.setBackgroundResource(R.drawable.bg_soft_pill);
        badge.setGravity(android.view.Gravity.CENTER);
        int size = dpToPx(context, 22);
        row.addView(badge, new LinearLayout.LayoutParams(size, size));

        TextView text = new TextView(context);
        text.setText(movement);
        text.setTextSize(14);
        text.setTextColor(context.getResources().getColor(R.color.bloom_text, null));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        params.setMarginStart(dpToPx(context, 10));
        row.addView(text, params);

        return row;
    }

    private static Dialog createBloomDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return dialog;
    }

    private static View createFriendRow(Context context, String name) {
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setBackgroundResource(R.drawable.bg_input_field);
        row.setPadding(dpToPx(context, 12), dpToPx(context, 10), dpToPx(context, 12), dpToPx(context, 10));
        row.setGravity(android.view.Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = dpToPx(context, 8);
        row.setLayoutParams(params);

        TextView avatar = new TextView(context);
        avatar.setText("🌸");
        avatar.setTextSize(18);
        avatar.setGravity(android.view.Gravity.CENTER);
        avatar.setBackgroundResource(R.drawable.bg_white_circle_friends);
        int size = dpToPx(context, 38);
        row.addView(avatar, new LinearLayout.LayoutParams(size, size));

        TextView textName = new TextView(context);
        textName.setText(name);
        textName.setTextSize(15);
        textName.setTypeface(null, android.graphics.Typeface.BOLD);
        textName.setTextColor(context.getResources().getColor(R.color.bloom_text, null));
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        textParams.setMarginStart(dpToPx(context, 12));
        row.addView(textName, textParams);

        TextView btnBadge = new TextView(context);
        btnBadge.setText("Active 💖");
        btnBadge.setTextSize(11);
        btnBadge.setTypeface(null, android.graphics.Typeface.BOLD);
        btnBadge.setTextColor(context.getResources().getColor(R.color.bloom_pink, null));
        btnBadge.setBackgroundResource(R.drawable.bg_white_pill);
        btnBadge.setPadding(dpToPx(context, 10), dpToPx(context, 4), dpToPx(context, 10), dpToPx(context, 4));
        row.addView(btnBadge);

        return row;
    }

    private static View createChallengeRow(Context context, String text) {
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setBackgroundResource(R.drawable.bg_input_field);
        row.setPadding(dpToPx(context, 12), dpToPx(context, 10), dpToPx(context, 12), dpToPx(context, 10));
        row.setGravity(android.view.Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = dpToPx(context, 8);
        row.setLayoutParams(params);

        TextView textContent = new TextView(context);
        textContent.setText(text);
        textContent.setTextSize(14);
        textContent.setTextColor(context.getResources().getColor(R.color.bloom_text, null));
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        row.addView(textContent, textParams);

        TextView badge = new TextView(context);
        badge.setText("✓ Done");
        badge.setTextSize(11);
        badge.setTypeface(null, android.graphics.Typeface.BOLD);
        badge.setTextColor(context.getResources().getColor(R.color.bloom_pink, null));
        badge.setBackgroundResource(R.drawable.bg_white_pill);
        badge.setPadding(dpToPx(context, 10), dpToPx(context, 4), dpToPx(context, 10), dpToPx(context, 4));
        row.addView(badge);

        return row;
    }

    private static View createAchievementRow(Context context, String text) {
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setBackgroundResource(R.drawable.bg_input_field);
        row.setPadding(dpToPx(context, 12), dpToPx(context, 10), dpToPx(context, 12), dpToPx(context, 10));
        row.setGravity(android.view.Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = dpToPx(context, 8);
        row.setLayoutParams(params);

        TextView textContent = new TextView(context);
        textContent.setText(text);
        textContent.setTextSize(14);
        textContent.setTypeface(null, android.graphics.Typeface.BOLD);
        textContent.setTextColor(context.getResources().getColor(R.color.bloom_text, null));
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        row.addView(textContent, textParams);

        TextView badge = new TextView(context);
        badge.setText("✨ Unlocked");
        badge.setTextSize(11);
        badge.setTypeface(null, android.graphics.Typeface.BOLD);
        badge.setTextColor(context.getResources().getColor(R.color.bloom_purple, null));
        badge.setBackgroundResource(R.drawable.bg_white_pill);
        badge.setPadding(dpToPx(context, 10), dpToPx(context, 4), dpToPx(context, 10), dpToPx(context, 4));
        row.addView(badge);

        return row;
    }

    private static int dpToPx(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}
