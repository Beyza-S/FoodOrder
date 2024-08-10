package com.example.foodorder;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class CartActivity extends BaseActivity implements CartAdapter.OnItemDeleteListener {

    DatabaseHelper db;
    RecyclerView cartRecyclerView;
    ArrayList<CartItem> cartItemList;
    CartAdapter adapter;
    TextView totalPriceTextView;
    Button proceedToPaymentButton;
    double totalPrice = 0.0;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        userId = getIntent().getIntExtra("user_id", -1); // Kullanıcı ID'sini al

        db = new DatabaseHelper(this);
        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        totalPriceTextView = findViewById(R.id.total_price_text_view);
        proceedToPaymentButton = findViewById(R.id.proceed_to_payment_button);
        cartItemList = new ArrayList<>();

        loadCartItems();

        adapter = new CartAdapter(this, cartItemList, this);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(adapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNavigationView);

        updateTotalPrice();

        proceedToPaymentButton.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
            intent.putExtra("total_price", totalPrice);
            intent.putExtra("user_id", userId);
            startActivity(intent);
        });
    }

    private void loadCartItems() {
        Cursor cursor = db.getCartItems(userId); // Kullanıcı ID'si ile sepet ürünlerini al
        if (cursor.getCount() == 0) {
            return;
        }

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int quantity = cursor.getInt(1);
            String name = cursor.getString(2);
            double price = cursor.getDouble(3);
            String description = cursor.getString(4);
            byte[] image = cursor.getBlob(5);

            cartItemList.add(new CartItem(id, quantity, name, price, description, image));
            totalPrice += price * quantity;
        }
    }

    private void updateTotalPrice() {
        totalPriceTextView.setText(String.format("Total Price: $%.2f", totalPrice));
    }

    @Override
    public void onItemDelete(int position) {
        CartItem item = cartItemList.get(position);
        db.deleteCartItem(item.getId(), userId); // İki parametre ile çağır
        totalPrice -= item.getPrice() * item.getQuantity();
        cartItemList.remove(position);
        adapter.notifyItemRemoved(position);
        updateTotalPrice();
    }
}