package com.example.bloom;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.example.bloom.data.BloomDatabase;
import com.example.bloom.engine.AdaptiveEngine;
import com.example.bloom.rewards.RewardManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompanionFragment extends Fragment {

    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private final StringBuilder chat = new StringBuilder();

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b) {
        return i.inflate(R.layout.fragment_companion, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle b) {
        RewardManager rewards = new RewardManager(requireContext());
        TextView textChat = v.findViewById(R.id.textChat);
        EditText input = v.findViewById(R.id.editMessage);
        ScrollView scroll = v.findViewById(R.id.scrollChat);
        TextView levelBadge = v.findViewById(R.id.textLevelBadge);
        String name = rewards.getName();

        levelBadge.setText(getString(R.string.bibo_level_badge_format, rewards.getLevel()));

        io.execute(() -> {
            String mood = BloomDatabase.getInstance(requireContext()).moodDao().latestMood();
            
            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> {
                chat.setLength(0);
                addMessage("Bibo 🐻", AdaptiveEngine.biboMessage(mood, name), textChat);

                v.findViewById(R.id.buttonSend).setOnClickListener(x -> {
                    sendMessage(input.getText().toString(), mood, name, textChat, input, scroll);
                });
                
                v.findViewById(R.id.chipPepTalk).setOnClickListener(x -> {
                    sendMessage("I need a pep talk", mood, name, textChat, input, scroll);
                });

                v.findViewById(R.id.chipEasy).setOnClickListener(x -> {
                    sendMessage("Suggest something easy", mood, name, textChat, input, scroll);
                });

                v.findViewById(R.id.chipStuck).setOnClickListener(x -> {
                    sendMessage("I'm feeling stuck", mood, name, textChat, input, scroll);
                });
            });
        });
    }

    private void sendMessage(String msg, String mood, String name, TextView textChat, EditText input, ScrollView scroll) {
        String t = msg == null ? "" : msg.trim();
        if (t.isEmpty()) return;
        addMessage("You", t, textChat);
        addMessage("Bibo 🐻", AdaptiveEngine.biboReply(t, mood, name), textChat);
        input.setText("");
        scroll.post(() -> scroll.fullScroll(View.FOCUS_DOWN));
    }

    private void addMessage(String sender, String text, TextView textChat) {
        chat.append(sender).append(": ").append(text).append("\n\n");
        textChat.setText(chat.toString());
    }
}
