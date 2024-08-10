package com.example.foodorder;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PaymentActivity extends BaseActivity {

    TextView totalPriceTextView;
    EditText addressEditText, phoneEditText, cardNumberEditText, cvvEditText, expiryDateEditText;
    Button completeOrderButton;
    double totalPrice;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        totalPrice = getIntent().getDoubleExtra("total_price", 0.0);
        userId = getIntent().getIntExtra("user_id", -1);

        totalPriceTextView = findViewById(R.id.total_price_text_view);
        addressEditText = findViewById(R.id.address_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        cardNumberEditText = findViewById(R.id.card_number_edit_text);
        cvvEditText = findViewById(R.id.cvv_edit_text);
        expiryDateEditText = findViewById(R.id.expiry_date_edit_text);
        completeOrderButton = findViewById(R.id.complete_order_button);

        totalPriceTextView.setText(String.format("Total Price: $%.2f", totalPrice));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNavigationView);

        completeOrderButton.setOnClickListener(v -> completeOrder());
    }

    private void completeOrder() {
        String address = addressEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String cardNumber = cardNumberEditText.getText().toString().trim();
        String cvv = cvvEditText.getText().toString().trim();
        String expiryDate = expiryDateEditText.getText().toString().trim();

        if (TextUtils.isEmpty(address) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(cardNumber) ||
                TextUtils.isEmpty(cvv) || TextUtils.isEmpty(expiryDate)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Burada sipariş tamamlama işlemleri yapılacak
        Toast.makeText(this, "Order Completed Successfully!", Toast.LENGTH_SHORT).show();

        // Sepeti temizle
        DatabaseHelper db = new DatabaseHelper(this);
        db.clearCart(userId);
    }


}
