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

public class SignUpFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RewardManager rewards = new RewardManager(requireContext());
        EditText editEmail = view.findViewById(R.id.editEmailSignUp);
        EditText editPassword = view.findViewById(R.id.editPasswordSignUp);

        if (rewards.getEmail() != null) {
            editEmail.setText(rewards.getEmail());
        }
        if (rewards.getPassword() != null) {
            editPassword.setText(rewards.getPassword());
        }

        view.findViewById(R.id.buttonSignUp).setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                rewards.createNewAccount(requireContext(), email, password);
                Toast.makeText(requireContext(), "Account created! Please set up your profile ✨", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.onboardingFragment);
            }
        });
    }
}
