package com.example.foodorder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private ArrayList<CartItem> cartItemList;
    private OnItemDeleteListener onItemDeleteListener;

    public CartAdapter(Context context, ArrayList<CartItem> cartItemList, OnItemDeleteListener onItemDeleteListener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.onItemDeleteListener = onItemDeleteListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        holder.productName.setText(cartItem.getName());
        holder.productPrice.setText(String.format("$%.2f", cartItem.getPrice()));
        holder.productQuantity.setText(String.valueOf(cartItem.getQuantity()));
        Bitmap bitmap = BitmapFactory.decodeByteArray(cartItem.getImage(), 0, cartItem.getImage().length);
        holder.productImage.setImageBitmap(bitmap);

        holder.deleteButton.setOnClickListener(v -> {
            if (onItemDeleteListener != null) {
                onItemDeleteListener.onItemDelete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice, productQuantity;
        ImageView productImage;
        ImageButton deleteButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            productImage = itemView.findViewById(R.id.product_image);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    public interface OnItemDeleteListener {
        void onItemDelete(int position);
    }
}
