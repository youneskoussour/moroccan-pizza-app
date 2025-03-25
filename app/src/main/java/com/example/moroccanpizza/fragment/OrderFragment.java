package com.example.moroccanpizza.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moroccanpizza.R;
import com.example.moroccanpizza.adapter.OrderAdapter;
import com.example.moroccanpizza.database.AppDatabase;
import com.example.moroccanpizza.model.Order;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment implements OrderAdapter.OrderAdapterListener {

    private TabLayout orderTabLayout;
    private RecyclerView ordersRecyclerView;
    private TextView emptyOrdersTextView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList = new ArrayList<>();
    private AppDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        
        // Initialize views
        orderTabLayout = view.findViewById(R.id.order_tab_layout);
        ordersRecyclerView = view.findViewById(R.id.orders_recycler_view);
        emptyOrdersTextView = view.findViewById(R.id.empty_orders_text);
        
        // Setup tab layout
        setupTabLayout();
        
        // Setup recycler view
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapter(getContext(), orderList, this);
        ordersRecyclerView.setAdapter(orderAdapter);
        
        // Get database instance
        database = AppDatabase.getInstance(requireContext());
        
        // Load active orders by default
        loadActiveOrders();
        
        return view;
    }

    private void setupTabLayout() {
        // Add tabs
        orderTabLayout.addTab(orderTabLayout.newTab().setText("Active"));
        orderTabLayout.addTab(orderTabLayout.newTab().setText("All Orders"));
        
        orderTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        loadActiveOrders();
                        break;
                    case 1:
                        loadAllOrders();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void loadActiveOrders() {
        database.orderDao().getActiveOrders().observe(getViewLifecycleOwner(), orders -> {
            updateOrdersList(orders);
        });
    }

    private void loadAllOrders() {
        database.orderDao().getAllOrders().observe(getViewLifecycleOwner(), orders -> {
            updateOrdersList(orders);
        });
    }

    private void updateOrdersList(List<Order> orders) {
        orderList.clear();
        if (orders != null) {
            orderList.addAll(orders);
        }
        orderAdapter.updateOrders(orderList);
        
        // Update UI based on order list
        if (orderList.isEmpty()) {
            emptyOrdersTextView.setVisibility(View.VISIBLE);
            ordersRecyclerView.setVisibility(View.GONE);
        } else {
            emptyOrdersTextView.setVisibility(View.GONE);
            ordersRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onOrderClick(Order order) {
        // Show order details
        Bundle bundle = new Bundle();
        bundle.putString("orderId", order.getOrderId());
        // Navigation.findNavController(requireView()).navigate(R.id.action_orders_to_order_details, bundle);
    }

    @Override
    public void onTrackOrderClick(Order order) {
        // Navigate to tracking fragment
        Bundle bundle = new Bundle();
        bundle.putString("orderId", order.getOrderId());
        Navigation.findNavController(requireView()).navigate(R.id.action_orders_to_tracking, bundle);
    }
}
