package com.example.foodorder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends BaseActivity {

    TextView profileEmail, profileName, profilePassword;
    Button logoutButton;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileEmail = findViewById(R.id.profile_email);
        profileName = findViewById(R.id.profile_name);
        profilePassword = findViewById(R.id.profile_password);
        logoutButton = findViewById(R.id.logout_button);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNavigationView);

        db = new DatabaseHelper(this);

        // SharedPreferences kullanarak giriş yapan kullanıcının email'ini alın
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userEmail = prefs.getString("user_email", null);

        if (userEmail != null) {
            loadUserProfile(userEmail);
        }

        logoutButton.setOnClickListener(v -> {
            // Çıkış işlemleri (örneğin, shared preferences'tan kullanıcı bilgilerini silme)
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            // Ana sayfaya dönme
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void loadUserProfile(String email) {
        Cursor cursor = db.getUserByEmail(email);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("NAME_SURNAME"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("PASSWORD"));

            profileEmail.setText("Email: " + email);
            profileName.setText("Name: " + name);
            profilePassword.setText("Password: " + password);
        }
        cursor.close();
    }
}