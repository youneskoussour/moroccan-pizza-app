package com.example.moroccanpizza.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.moroccanpizza.R;
import com.example.moroccanpizza.adapter.PizzaAdapter;
import com.example.moroccanpizza.model.Pizza;
import com.example.moroccanpizza.util.PizzaData;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements PizzaAdapter.OnPizzaClickListener {

    private ViewPager2 bannerViewPager;
    private RecyclerView featuredPizzasRecyclerView;
    private TextView viewAllPizzasButton;
    private Button viewCartButton;
    private Button trackOrderButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        // Initialize views
        bannerViewPager = view.findViewById(R.id.banner_view_pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        featuredPizzasRecyclerView = view.findViewById(R.id.featured_pizzas_recycler_view);
        viewAllPizzasButton = view.findViewById(R.id.view_all_pizzas_button);
        viewCartButton = view.findViewById(R.id.view_cart_button);
        trackOrderButton = view.findViewById(R.id.track_order_button);
        
        // Setup banner view pager
        setupBannerViewPager();
        new TabLayoutMediator(tabLayout, bannerViewPager, (tab, position) -> {
            // No text for the tabs, just indicators
        }).attach();
        
        // Setup featured pizzas
        setupFeaturedPizzas();
        
        // Set click listeners
        viewAllPizzasButton.setOnClickListener(v -> {
            // Navigate to menu fragment
            Navigation.findNavController(view).navigate(R.id.navigation_menu);
        });
        
        viewCartButton.setOnClickListener(v -> {
            // Navigate to cart fragment
            Navigation.findNavController(view).navigate(R.id.navigation_cart);
        });
        
        trackOrderButton.setOnClickListener(v -> {
            // Navigate to orders fragment
            Navigation.findNavController(view).navigate(R.id.navigation_orders);
        });
        
        return view;
    }

    private void setupBannerViewPager() {
        // Create banner adapter and set to ViewPager2
        BannerAdapter bannerAdapter = new BannerAdapter();
        bannerViewPager.setAdapter(bannerAdapter);
        
        // Auto slide the banners
        bannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            private final long DELAY_MS = 3000; // Delay in milliseconds
            private final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    int currentPosition = bannerViewPager.getCurrentItem();
                    int totalCount = bannerAdapter.getItemCount();
                    int nextPosition = (currentPosition + 1) % totalCount;
                    bannerViewPager.setCurrentItem(nextPosition, true);
                    bannerViewPager.postDelayed(this, DELAY_MS);
                }
            };
            
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bannerViewPager.removeCallbacks(runnable);
                bannerViewPager.postDelayed(runnable, DELAY_MS);
            }
        });
    }

    private void setupFeaturedPizzas() {
        // Get featured pizzas
        List<Pizza> featuredPizzas = PizzaData.getFeaturedPizzas();
        
        // Setup recycler view
        featuredPizzasRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        PizzaAdapter adapter = new PizzaAdapter(getContext(), featuredPizzas, this);
        featuredPizzasRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onPizzaClick(Pizza pizza) {
        // Show pizza detail dialog or navigate to detail fragment
        Bundle bundle = new Bundle();
        bundle.putInt("pizzaId", pizza.getId());
        Navigation.findNavController(requireView()).navigate(R.id.action_home_to_pizza_detail, bundle);
    }

    @Override
    public void onAddToCartClick(Pizza pizza) {
        // Add pizza to cart with quantity 1
        // Implementation will depend on your cart management strategy
        
        // Show confirmation message
        // Toast.makeText(getContext(), pizza.getName() + " added to cart", Toast.LENGTH_SHORT).show();
    }

    // Banner adapter inner class
    private class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

        private final int[] bannerImages = {
                R.drawable.moroccan_background,
                R.drawable.moroccan_background,
                R.drawable.moroccan_background
        };

        @NonNull
        @Override
        public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_banner, parent, false);
            return new BannerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
            holder.bannerImageView.setImageResource(bannerImages[position]);
        }

        @Override
        public int getItemCount() {
            return bannerImages.length;
        }

        class BannerViewHolder extends RecyclerView.ViewHolder {
            ImageView bannerImageView;

            BannerViewHolder(@NonNull View itemView) {
                super(itemView);
                bannerImageView = itemView.findViewById(R.id.banner_image);
            }
        }
    }
}
