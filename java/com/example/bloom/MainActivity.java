package com.example.bloom;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.bloom.rewards.StepManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StepManager.getInstance(this).start(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host);
        if (host != null) {
            navController = host.getNavController();
            BottomNavigationView nav = findViewById(R.id.bottom_nav);
            
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.homeFragment,
                    R.id.moodFragment,
                    R.id.activitiesFragment,
                    R.id.recipesFragment,
                    R.id.meFragment)
                    .build();

            NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

            nav.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() == itemId) {
                    return true;
                }
                navController.navigate(itemId);
                return true;
            });

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                int id = destination.getId();
                if (id == R.id.splashFragment || id == R.id.loginFragment || 
                    id == R.id.onboardingFragment || id == R.id.signUpFragment) {
                    toolbar.setVisibility(View.GONE);
                    nav.setVisibility(View.GONE);
                } else {
                    toolbar.setVisibility(View.VISIBLE);
                    nav.setVisibility(View.VISIBLE);
                    if (nav.getMenu().findItem(id) != null) {
                        nav.getMenu().findItem(id).setChecked(true);
                    }
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            StepManager.getInstance(this).registerSensors();
        }
    }
}
