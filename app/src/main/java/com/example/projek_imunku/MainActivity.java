package com.example.projek_imunku;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.projek_imunku.ui.home.HomeFragment;
import com.example.projek_imunku.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.projek_imunku.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static final String EXTRA_EMAIL = "extra_email";
    private LinearLayout buttonContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_daftarImun, R.id.navigation_lokasi, R.id.navigation_home, R.id.navigation_jadwalImun, R.id.navigation_profil)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        handleLoginData();

        Button btnToDashboard = findViewById(R.id.btn_to_dashboard);
        Button btnToProfile = findViewById(R.id.btn_to_profile);
        buttonContainer = findViewById(R.id.button_container);

        btnToDashboard.setOnClickListener(v -> {
            loadFragment(new HomeFragment());
        });

        btnToProfile.setOnClickListener(v -> {
            loadFragment(new ProfileFragment());
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                buttonContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    private void handleLoginData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_EMAIL)) {
            String emailFromLogin = intent.getStringExtra(EXTRA_EMAIL);

            SharedPreferences prefs = getSharedPreferences(ProfileFragment.PROFILE_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString(ProfileFragment.KEY_EMAIL, emailFromLogin);

            editor.apply();
        }
    }

    private void loadFragment(Fragment fragment) {
        buttonContainer.setVisibility(View.GONE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}