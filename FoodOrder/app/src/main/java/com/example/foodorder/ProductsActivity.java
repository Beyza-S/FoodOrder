package com.example.foodorder;

import android.database.Cursor;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class ProductsActivity extends BaseActivity {

    DatabaseHelper db;
    RecyclerView productsRecyclerView;
    ArrayList<Product> productList;
    ProductsAdapter adapter;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        db = new DatabaseHelper(this);
        productsRecyclerView = findViewById(R.id.products_recycler_view);
        productList = new ArrayList<>();

        userId = getIntent().getIntExtra("user_id", -1); // Kullanıcı ID'sini al

        loadProducts();

        adapter = new ProductsAdapter(this, productList, userId); // Kullanıcı ID'sini ilet
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productsRecyclerView.setAdapter(adapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNavigationView);
    }

    private void loadProducts() {
        Cursor cursor = db.getAllProducts();
        if (cursor.getCount() == 0) {
            return;
        }

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            double price = cursor.getDouble(2);
            String description = cursor.getString(3);
            byte[] image = cursor.getBlob(4);

            // Görsel kaynak ID'sini belirleyin
            String drawableName = name.toLowerCase().replace(" ", "_");
            int imageResource = getResources().getIdentifier(drawableName, "drawable", getPackageName());

            productList.add(new Product(id, name, price, description, imageResource));
        }
    }
}
