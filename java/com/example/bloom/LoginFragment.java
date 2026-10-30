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

public class LoginFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RewardManager rewards = new RewardManager(requireContext());
        EditText editEmail = view.findViewById(R.id.editEmail);
        EditText editPassword = view.findViewById(R.id.editPassword);


        if (rewards.getEmail() != null) {
            editEmail.setText(rewards.getEmail());
        }
        if (rewards.getPassword() != null) {
            editPassword.setText(rewards.getPassword());
        }

        view.findViewById(R.id.buttonLogin).setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
            } else {
                String storedEmail = rewards.getEmail();
                String storedPassword = rewards.getPassword();

                if (storedEmail == null) {
                    rewards.createNewAccount(requireContext(), email, password);
                    Navigation.findNavController(view).navigate(R.id.onboardingFragment);
                } else if (!storedEmail.equalsIgnoreCase(email)) {
                    rewards.createNewAccount(requireContext(), email, password);
                    Toast.makeText(requireContext(), "Welcome! Please set up your profile ✨", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.onboardingFragment);
                } else if (!storedPassword.equals(password)) {
                    Toast.makeText(requireContext(), "Invalid password", Toast.LENGTH_SHORT).show();
                } else {
                    rewards.setLoggedIn(true);
                    if (rewards.isOnboarded()) {
                        Navigation.findNavController(view).navigate(R.id.homeFragment);
                    } else {
                        Navigation.findNavController(view).navigate(R.id.onboardingFragment);
                    }
                }
            }
        });

        view.findViewById(R.id.textForgotPassword).setOnClickListener(v -> 
            Toast.makeText(requireContext(), "Password reset link sent to your email! 📧", Toast.LENGTH_LONG).show());

        view.findViewById(R.id.textSignUp).setOnClickListener(v -> 
            Navigation.findNavController(view).navigate(R.id.signUpFragment));
    }
}
