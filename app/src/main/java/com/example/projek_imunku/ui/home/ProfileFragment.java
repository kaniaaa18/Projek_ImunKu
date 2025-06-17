package com.example.projek_imunku.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    public static final String PROFILE_PREFS = "ProfilePrefs";
    public static final String KEY_NAME = "userName";
    public static final String KEY_EMAIL = "userEmail";
    public static final String KEY_PHONE = "userPhone";
    public static final String KEY_IMAGE_URI = "userImageUri";

    private CircleImageView userIcon;
    private FrameLayout profileImageLayout;
    private TextInputEditText nameInput, emailInput, phoneInput;
    private TextInputEditText oldPasswordInput, newPasswordInput, confirmPasswordInput;
    private MaterialButton guideButton, logoutButton, saveButton;
    private static final int PICK_IMAGE = 1;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userIcon = view.findViewById(R.id.user_icon);
        profileImageLayout = view.findViewById(R.id.profile_image_layout);
        nameInput = view.findViewById(R.id.name_input);
        emailInput = view.findViewById(R.id.email_input);
        phoneInput = view.findViewById(R.id.phone_input);
        oldPasswordInput = view.findViewById(R.id.old_password_input);
        newPasswordInput = view.findViewById(R.id.new_password_input);
        confirmPasswordInput = view.findViewById(R.id.confirm_password_input);
        guideButton = view.findViewById(R.id.panduan_aplikasi);
        logoutButton = view.findViewById(R.id.logout_button);
        saveButton = view.findViewById(R.id.save_button);

        profileImageLayout.setOnClickListener(v -> openImagePicker());
        guideButton.setOnClickListener(v -> startActivity(new Intent(getActivity(), PanduanActivity.class)));
        logoutButton.setOnClickListener(v -> showLogoutConfirmationDialog());
        saveButton.setOnClickListener(v -> saveProfile());

        loadProfileData();

        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            userIcon.setImageURI(selectedImageUri);

            SharedPreferences prefs = requireActivity().getSharedPreferences(PROFILE_PREFS, Context.MODE_PRIVATE);
            prefs.edit().putString(KEY_IMAGE_URI, selectedImageUri.toString()).apply();
        }
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Log Out")
                .setMessage("Apakah Anda yakin untuk log out?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    Toast.makeText(getActivity(), "Anda telah log out", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void saveProfile() {
        SharedPreferences prefs = requireActivity().getSharedPreferences(PROFILE_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(KEY_NAME, nameInput.getText().toString());
        editor.putString(KEY_EMAIL, emailInput.getText().toString());
        editor.putString(KEY_PHONE, phoneInput.getText().toString());

        editor.apply();

        Toast.makeText(getActivity(), "Profil berhasil disimpan", Toast.LENGTH_SHORT).show();
    }

    private void loadProfileData() {
        SharedPreferences prefs = requireActivity().getSharedPreferences(PROFILE_PREFS, Context.MODE_PRIVATE);

        String name = prefs.getString(KEY_NAME, "");
        String email = prefs.getString(KEY_EMAIL, "");
        String phone = prefs.getString(KEY_PHONE, "");
        String imageUriString = prefs.getString(KEY_IMAGE_URI, null);

        nameInput.setText(name);
        emailInput.setText(email);
        phoneInput.setText(phone);

        if (imageUriString != null) {
            userIcon.setImageURI(Uri.parse(imageUriString));
        }
    }
}