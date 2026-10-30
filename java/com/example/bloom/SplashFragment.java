package com.example.bloom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import com.example.bloom.rewards.RewardManager;

public class SplashFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return new View(requireContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RewardManager rewards = new RewardManager(requireContext());
        
        view.post(() -> {
            NavOptions options = new NavOptions.Builder()
                    .setPopUpTo(R.id.splashFragment, true)
                    .build();

            if (!rewards.isLoggedIn()) {
                Navigation.findNavController(view).navigate(R.id.loginFragment, null, options);
            } else if (!rewards.isOnboarded()) {
                Navigation.findNavController(view).navigate(R.id.onboardingFragment, null, options);
            } else {
                Navigation.findNavController(view).navigate(R.id.homeFragment, null, options);
            }
        });
    }
}
