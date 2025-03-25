package com.example.moroccanpizza.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moroccanpizza.R;
import com.example.moroccanpizza.model.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final Context context;
    private List<CartItem> cartItems;
    private final CartItemListener listener;

    public interface CartItemListener {
        void onIncreaseQuantity(CartItem cartItem);
        void onDecreaseQuantity(CartItem cartItem);
        void onRemoveItem(CartItem cartItem);
        void onCartItemClick(CartItem cartItem);
    }

    public CartAdapter(Context context, List<CartItem> cartItems, CartItemListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    public void updateCartItems(List<CartItem> newItems) {
        this.cartItems = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        
        holder.nameTextView.setText(cartItem.getPizzaName());
        holder.priceTextView.setText(String.format("%.2f MAD", cartItem.getPizzaPrice()));
        holder.quantityTextView.setText(String.valueOf(cartItem.getQuantity()));
        holder.totalTextView.setText(String.format("%.2f MAD", cartItem.getTotalPrice()));
        
        // Load pizza image
        Glide.with(context)
                .load(cartItem.getPizzaImageUrl())
                .placeholder(R.drawable.ic_menu)
                .error(R.drawable.ic_menu)
                .into(holder.pizzaImageView);
        
        // Set click listeners
        holder.increaseButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onIncreaseQuantity(cartItem);
            }
        });
        
        holder.decreaseButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDecreaseQuantity(cartItem);
            }
        });
        
        holder.removeButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemoveItem(cartItem);
            }
        });
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCartItemClick(cartItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView pizzaImageView;
        TextView nameTextView;
        TextView priceTextView;
        TextView quantityTextView;
        TextView totalTextView;
        ImageButton increaseButton;
        ImageButton decreaseButton;
        ImageButton removeButton;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            pizzaImageView = itemView.findViewById(R.id.cart_item_image);
            nameTextView = itemView.findViewById(R.id.cart_item_name);
            priceTextView = itemView.findViewById(R.id.cart_item_price);
            quantityTextView = itemView.findViewById(R.id.cart_item_quantity);
            totalTextView = itemView.findViewById(R.id.cart_item_total);
            increaseButton = itemView.findViewById(R.id.increase_quantity_button);
            decreaseButton = itemView.findViewById(R.id.decrease_quantity_button);
            removeButton = itemView.findViewById(R.id.remove_item_button);
        }
    }
}
