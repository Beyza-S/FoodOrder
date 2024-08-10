package com.example.foodorder;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new Intent(BaseActivity.this, MainActivity.class));
                    return true;
                } else if (id == R.id.nav_cart) {
                    startActivity(new Intent(BaseActivity.this, CartActivity.class));
                    return true;
                } else if (id == R.id.nav_profile) {
                    startActivity(new Intent(BaseActivity.this, ProfileActivity.class));
                    return true;
                } else if (id == R.id.nav_product) {
                    startActivity(new Intent(BaseActivity.this, ProductsActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
}
