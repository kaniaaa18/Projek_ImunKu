package com.example.projek_imunku.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projek_imunku.R;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_EMAIL = "extra_email";
    private LinearLayout buttonContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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