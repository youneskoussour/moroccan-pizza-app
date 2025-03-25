package com.example.moroccanpizza.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moroccanpizza.R;
import com.example.moroccanpizza.adapter.PizzaAdapter;
import com.example.moroccanpizza.database.AppDatabase;
import com.example.moroccanpizza.model.CartItem;
import com.example.moroccanpizza.model.Pizza;
import com.example.moroccanpizza.util.PizzaData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MenuFragment extends Fragment implements PizzaAdapter.OnPizzaClickListener {

    private RecyclerView pizzaRecyclerView;
    private SearchView searchView;
    private ChipGroup filterChipGroup;
    private PizzaAdapter pizzaAdapter;
    private List<Pizza> allPizzas;
    private TextView emptyStateTextView;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        
        // Initialize views
        pizzaRecyclerView = view.findViewById(R.id.pizza_recycler_view);
        searchView = view.findViewById(R.id.search_view);
        filterChipGroup = view.findViewById(R.id.filter_chip_group);
        emptyStateTextView = view.findViewById(R.id.empty_state_text);
        
        // Setup RecyclerView
        pizzaRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        
        // Load all pizzas
        allPizzas = PizzaData.getAllPizzas();
        
        // Setup adapter
        pizzaAdapter = new PizzaAdapter(getContext(), allPizzas, this);
        pizzaRecyclerView.setAdapter(pizzaAdapter);
        
        // Setup search functionality
        setupSearch();
        
        // Setup filter chips
        setupFilterChips();
        
        return view;
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterPizzas(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterPizzas(newText);
                return true;
            }
        });
    }

    private void setupFilterChips() {
        // Category filters
        String[] categories = {"All", "Spicy", "Vegetarian", "Seafood", "Meat Lovers"};
        
        for (String category : categories) {
            Chip chip = new Chip(requireContext());
            chip.setText(category);
            chip.setCheckable(true);
            chip.setClickable(true);
            
            // Set "All" as default selected
            if (category.equals("All")) {
                chip.setChecked(true);
            }
            
            filterChipGroup.addView(chip);
            
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    // Uncheck other chips
                    for (int i = 0; i < filterChipGroup.getChildCount(); i++) {
                        Chip otherChip = (Chip) filterChipGroup.getChildAt(i);
                        if (otherChip != buttonView) {
                            otherChip.setChecked(false);
                        }
                    }
                    
                    // Apply filter
                    applyFilter(category);
                }
            });
        }
    }

    private void filterPizzas(String query) {
        List<Pizza> filteredList = new ArrayList<>();
        
        if (query == null || query.isEmpty()) {
            filteredList.addAll(allPizzas);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            
            for (Pizza pizza : allPizzas) {
                if (pizza.getName().toLowerCase().contains(lowerCaseQuery) ||
                        pizza.getNameArabic().toLowerCase().contains(lowerCaseQuery) ||
                        pizza.getDescription().toLowerCase().contains(lowerCaseQuery) ||
                        pizza.getIngredients().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(pizza);
                }
            }
        }
        
        updatePizzaList(filteredList);
    }

    private void applyFilter(String category) {
        List<Pizza> filteredList = new ArrayList<>();
        
        if (category.equals("All")) {
            filteredList.addAll(allPizzas);
        } else if (category.equals("Spicy")) {
            for (Pizza pizza : allPizzas) {
                if (pizza.isSpicy()) {
                    filteredList.add(pizza);
                }
            }
        } else if (category.equals("Vegetarian")) {
            for (Pizza pizza : allPizzas) {
                if (pizza.isVegetarian()) {
                    filteredList.add(pizza);
                }
            }
        } else if (category.equals("Seafood")) {
            for (Pizza pizza : allPizzas) {
                if (pizza.getIngredients().toLowerCase().contains("seafood") ||
                        pizza.getIngredients().toLowerCase().contains("shrimp") ||
                        pizza.getIngredients().toLowerCase().contains("fish") ||
                        pizza.getIngredients().toLowerCase().contains("tuna")) {
                    filteredList.add(pizza);
                }
            }
        } else if (category.equals("Meat Lovers")) {
            for (Pizza pizza : allPizzas) {
                if (pizza.getIngredients().toLowerCase().contains("beef") ||
                        pizza.getIngredients().toLowerCase().contains("chicken") ||
                        pizza.getIngredients().toLowerCase().contains("lamb") ||
                        pizza.getIngredients().toLowerCase().contains("meat")) {
                    filteredList.add(pizza);
                }
            }
        }
        
        updatePizzaList(filteredList);
    }

    private void updatePizzaList(List<Pizza> filteredList) {
        pizzaAdapter.updatePizzaList(filteredList);
        
        // Show empty state if no pizzas match
        if (filteredList.isEmpty()) {
            emptyStateTextView.setVisibility(View.VISIBLE);
            pizzaRecyclerView.setVisibility(View.GONE);
        } else {
            emptyStateTextView.setVisibility(View.GONE);
            pizzaRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPizzaClick(Pizza pizza) {
        // Navigate to pizza details
        Bundle bundle = new Bundle();
        bundle.putInt("pizzaId", pizza.getId());
        // Navigation.findNavController(requireView()).navigate(R.id.action_menu_to_pizza_detail, bundle);
    }

    @Override
    public void onAddToCartClick(Pizza pizza) {
        // Add to cart
        CartItem cartItem = new CartItem(
                pizza.getId(),
                pizza.getName(),
                pizza.getImageUrl(),
                pizza.getPrice(),
                1,
                ""
        );
        
        // Add to database
        executor.execute(() -> {
            AppDatabase.getInstance(requireContext()).cartDao().insertCartItem(cartItem);
            
            // Show toast on main thread
            requireActivity().runOnUiThread(() -> 
                Toast.makeText(getContext(), pizza.getName() + " added to cart", Toast.LENGTH_SHORT).show()
            );
        });
    }
}
