package com.example.moroccanpizza.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moroccanpizza.R;
import com.example.moroccanpizza.model.Pizza;

import java.util.List;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder> {

    private final Context context;
    private List<Pizza> pizzaList;
    private final OnPizzaClickListener listener;

    public interface OnPizzaClickListener {
        void onPizzaClick(Pizza pizza);
        void onAddToCartClick(Pizza pizza);
    }

    public PizzaAdapter(Context context, List<Pizza> pizzaList, OnPizzaClickListener listener) {
        this.context = context;
        this.pizzaList = pizzaList;
        this.listener = listener;
    }

    public void updatePizzaList(List<Pizza> newList) {
        this.pizzaList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pizza, parent, false);
        return new PizzaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PizzaViewHolder holder, int position) {
        Pizza pizza = pizzaList.get(position);
        
        holder.nameTextView.setText(pizza.getName());
        holder.nameArabicTextView.setText(pizza.getNameArabic());
        holder.descriptionTextView.setText(pizza.getDescription());
        holder.priceTextView.setText(String.format("%.2f MAD", pizza.getPrice()));
        
        // Load pizza image using Glide
        Glide.with(context)
                .load(pizza.getImageUrl())
                .placeholder(R.drawable.ic_menu)
                .error(R.drawable.ic_menu)
                .into(holder.pizzaImageView);
        
        // Show indicators for spicy and vegetarian pizzas
        if (pizza.isSpicy()) {
            holder.spicyIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.spicyIndicator.setVisibility(View.GONE);
        }
        
        if (pizza.isVegetarian()) {
            holder.vegetarianIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.vegetarianIndicator.setVisibility(View.GONE);
        }
        
        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPizzaClick(pizza);
            }
        });
        
        holder.addToCartButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddToCartClick(pizza);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pizzaList != null ? pizzaList.size() : 0;
    }

    static class PizzaViewHolder extends RecyclerView.ViewHolder {
        ImageView pizzaImageView;
        TextView nameTextView;
        TextView nameArabicTextView;
        TextView descriptionTextView;
        TextView priceTextView;
        ImageView spicyIndicator;
        ImageView vegetarianIndicator;
        Button addToCartButton;

        PizzaViewHolder(@NonNull View itemView) {
            super(itemView);
            pizzaImageView = itemView.findViewById(R.id.pizza_image);
            nameTextView = itemView.findViewById(R.id.pizza_name);
            nameArabicTextView = itemView.findViewById(R.id.pizza_name_arabic);
            descriptionTextView = itemView.findViewById(R.id.pizza_description);
            priceTextView = itemView.findViewById(R.id.pizza_price);
            spicyIndicator = itemView.findViewById(R.id.spicy_indicator);
            vegetarianIndicator = itemView.findViewById(R.id.vegetarian_indicator);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
        }
    }
}
