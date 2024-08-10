package com.example.foodorder;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        // Sign In butonu tıklanabilir ve LoginActivity'e yönlendirme işlemi
        Button signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Alt metin tıklanabilir ve RegisterActivity'e yönlendirme işlemi
        TextView bottomText = findViewById(R.id.bottomText);
        SpannableString content = new SpannableString("Don't Have an Account? Sign Up");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        bottomText.setText(content);
        bottomText.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        // Gizli butonun tıklama olayını dinleyerek RegisterActivity'e yönlendirme işlemi
        findViewById(R.id.signUpButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
