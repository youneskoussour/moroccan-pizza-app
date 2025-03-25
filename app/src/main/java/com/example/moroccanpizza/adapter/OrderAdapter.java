package com.example.moroccanpizza.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moroccanpizza.R;
import com.example.moroccanpizza.model.Order;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final Context context;
    private List<Order> orderList;
    private final OrderAdapterListener listener;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public interface OrderAdapterListener {
        void onOrderClick(Order order);
        void onTrackOrderClick(Order order);
    }

    public OrderAdapter(Context context, List<Order> orderList, OrderAdapterListener listener) {
        this.context = context;
        this.orderList = orderList;
        this.listener = listener;
    }

    public void updateOrders(List<Order> newOrders) {
        this.orderList = newOrders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        
        holder.orderIdTextView.setText(String.format("Order #%s", order.getOrderId()));
        holder.dateTextView.setText(dateFormat.format(order.getOrderDate()));
        holder.statusTextView.setText(order.getOrderStatus());
        holder.amountTextView.setText(String.format("%.2f MAD", order.getTotalAmount()));
        holder.itemCountTextView.setText(String.format("%d items", order.getItems().size()));
        
        // Set status text color based on order status
        switch (order.getOrderStatus()) {
            case "Pending":
                holder.statusTextView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_orange_dark));
                break;
            case "Preparing":
                holder.statusTextView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark));
                break;
            case "Out for Delivery":
                holder.statusTextView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_purple));
                break;
            case "Delivered":
                holder.statusTextView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
                break;
            case "Cancelled":
                holder.statusTextView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
                break;
        }
        
        // Show tracking button only for orders that are not delivered or cancelled
        if (!"Delivered".equals(order.getOrderStatus()) && !"Cancelled".equals(order.getOrderStatus())) {
            holder.trackOrderButton.setVisibility(View.VISIBLE);
        } else {
            holder.trackOrderButton.setVisibility(View.GONE);
        }
        
        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOrderClick(order);
            }
        });
        
        holder.trackOrderButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTrackOrderClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView;
        TextView dateTextView;
        TextView statusTextView;
        TextView amountTextView;
        TextView itemCountTextView;
        Button trackOrderButton;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.order_id);
            dateTextView = itemView.findViewById(R.id.order_date);
            statusTextView = itemView.findViewById(R.id.order_status);
            amountTextView = itemView.findViewById(R.id.order_amount);
            itemCountTextView = itemView.findViewById(R.id.order_item_count);
            trackOrderButton = itemView.findViewById(R.id.track_order_button);
        }
    }
}
