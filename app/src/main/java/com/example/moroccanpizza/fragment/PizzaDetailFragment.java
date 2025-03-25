package com.example.moroccanpizza.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.moroccanpizza.R;
import com.example.moroccanpizza.database.AppDatabase;
import com.example.moroccanpizza.model.CartItem;
import com.example.moroccanpizza.model.Pizza;
import com.example.moroccanpizza.util.PizzaData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PizzaDetailFragment extends Fragment {

    private ImageView pizzaImageView;
    private TextView nameTextView;
    private TextView nameArabicTextView;
    private TextView descriptionTextView;
    private TextView priceTextView;
    private TextView ingredientsTextView;
    private ImageView spicyIndicator;
    private ImageView vegetarianIndicator;
    private TextView quantityTextView;
    private ImageButton decreaseButton;
    private ImageButton increaseButton;
    private Button addToCartButton;
    private FloatingActionButton backButton;

    private int pizzaId;
    private Pizza pizza;
    private int quantity = 1;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pizza_detail, container, false);
        
        // Initialize views
        pizzaImageView = view.findViewById(R.id.detail_pizza_image);
        nameTextView = view.findViewById(R.id.detail_pizza_name);
        nameArabicTextView = view.findViewById(R.id.detail_pizza_name_arabic);
        descriptionTextView = view.findViewById(R.id.detail_pizza_description);
        priceTextView = view.findViewById(R.id.detail_pizza_price);
        ingredientsTextView = view.findViewById(R.id.detail_pizza_ingredients);
        spicyIndicator = view.findViewById(R.id.detail_spicy_indicator);
        vegetarianIndicator = view.findViewById(R.id.detail_vegetarian_indicator);
        quantityTextView = view.findViewById(R.id.quantity_text);
        decreaseButton = view.findViewById(R.id.decrease_quantity_button);
        increaseButton = view.findViewById(R.id.increase_quantity_button);
        addToCartButton = view.findViewById(R.id.add_to_cart_button);
        backButton = view.findViewById(R.id.back_button);
        
        // Get pizza ID from arguments
        Bundle args = getArguments();
        if (args != null) {
            pizzaId = args.getInt("pizzaId", 0);
            loadPizzaDetails();
        }
        
        // Set up click listeners
        setupClickListeners();
        
        return view;
    }

    private void loadPizzaDetails() {
        pizza = PizzaData.getPizzaById(pizzaId);
        if (pizza != null) {
            updateUI();
        }
    }

    private void updateUI() {
        nameTextView.setText(pizza.getName());
        nameArabicTextView.setText(pizza.getNameArabic());
        descriptionTextView.setText(pizza.getDescription());
        priceTextView.setText(String.format("%.2f MAD", pizza.getPrice()));
        ingredientsTextView.setText(pizza.getIngredients());
        
        // Show indicators for spicy and vegetarian pizzas
        spicyIndicator.setVisibility(pizza.isSpicy() ? View.VISIBLE : View.GONE);
        vegetarianIndicator.setVisibility(pizza.isVegetarian() ? View.VISIBLE : View.GONE);
        
        // Load pizza image using Glide
        Glide.with(requireContext())
                .load(pizza.getImageUrl())
                .placeholder(R.drawable.ic_menu)
                .error(R.drawable.ic_menu)
                .into(pizzaImageView);
                
        // Set initial quantity
        quantityTextView.setText(String.valueOf(quantity));
    }

    private void setupClickListeners() {
        increaseButton.setOnClickListener(v -> {
            quantity++;
            quantityTextView.setText(String.valueOf(quantity));
        });
        
        decreaseButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityTextView.setText(String.valueOf(quantity));
            }
        });
        
        addToCartButton.setOnClickListener(v -> {
            if (pizza != null) {
                addToCart();
            }
        });
        
        backButton.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }

    private void addToCart() {
        CartItem cartItem = new CartItem(
                pizza.getId(),
                pizza.getName(),
                pizza.getImageUrl(),
                pizza.getPrice(),
                quantity,
                ""
        );
        
        // Add to database
        executor.execute(() -> {
            AppDatabase.getInstance(requireContext()).cartDao().insertCartItem(cartItem);
            
            // Show toast on main thread
            requireActivity().runOnUiThread(() -> {
                String message = quantity > 1 
                        ? String.format("%d %s added to cart", quantity, pizza.getName())
                        : String.format("%s added to cart", pizza.getName());
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            });
        });
    }
}
