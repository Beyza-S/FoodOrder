package com.example.foodorder;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProductDetailsActivity extends BaseActivity {

    ImageView productImage;
    TextView productName, productPrice, productDescription, productQuantity;
    Button increaseQuantity, decreaseQuantity, addToCartButton;
    DatabaseHelper db;
    int quantity = 1;
    int productId;
    int userId; // Kullanıcı ID'si

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productDescription = findViewById(R.id.product_description);
        productQuantity = findViewById(R.id.product_quantity);
        increaseQuantity = findViewById(R.id.increase_quantity);
        decreaseQuantity = findViewById(R.id.decrease_quantity);
        addToCartButton = findViewById(R.id.add_to_cart_button);

        db = new DatabaseHelper(this);

        Intent intent = getIntent();
        productId = intent.getIntExtra("product_id", -1);
        userId = intent.getIntExtra("user_id", -1); // Kullanıcı ID'sini al

        if (productId != -1) {
            loadProductDetails(productId);
        }

        increaseQuantity.setOnClickListener(v -> {
            quantity++;
            productQuantity.setText(String.valueOf(quantity));
        });

        decreaseQuantity.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                productQuantity.setText(String.valueOf(quantity));
            }
        });

        addToCartButton.setOnClickListener(v -> {
            addToCart();
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNavigationView);
    }

    private void loadProductDetails(int productId) {
        Cursor cursor = db.getProductById(productId);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION"));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("IMAGE"));

            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            productImage.setImageBitmap(bitmap);
            productName.setText(name);
            productPrice.setText(String.format("$%.2f", price));
            productDescription.setText(description);
        }
        cursor.close();
    }

    private void addToCart() {
        boolean result = db.addToCart(productId, userId, quantity); // Kullanıcı ID'sini de geç
        if (result) {
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
        }
    }
}
