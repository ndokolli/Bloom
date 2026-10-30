package com.example.bloom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.bloom.rewards.RewardManager;

public class OnboardingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RewardManager rewards = new RewardManager(requireContext());
        EditText editName = view.findViewById(R.id.onboardName);
        EditText editAge = view.findViewById(R.id.onboardAge);
        EditText editHeight = view.findViewById(R.id.onboardHeight);
        EditText editWeight = view.findViewById(R.id.onboardWeight);

        view.findViewById(R.id.buttonFinishOnboarding).setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String ageStr = editAge.getText().toString().trim();
            String heightStr = editHeight.getText().toString().trim();
            String weightStr = editWeight.getText().toString().trim();

            if (name.isEmpty() || ageStr.isEmpty() || heightStr.isEmpty() || weightStr.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    rewards.setName(name);
                    rewards.setAge(Integer.parseInt(ageStr));
                    rewards.setHeight(Integer.parseInt(heightStr));
                    rewards.setWeight(Integer.parseInt(weightStr));
                    rewards.setOnboarded(true);
                    Navigation.findNavController(view).navigate(R.id.homeFragment);
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Invalid number format", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
