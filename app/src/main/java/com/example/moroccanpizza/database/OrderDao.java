package com.example.moroccanpizza.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moroccanpizza.model.Order;

import java.util.List;

@Dao
public interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOrder(Order order);

    @Update
    void updateOrder(Order order);

    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    LiveData<List<Order>> getAllOrders();

    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    LiveData<Order> getOrderById(String orderId);

    @Query("SELECT * FROM orders WHERE orderStatus != 'Delivered' AND orderStatus != 'Cancelled' ORDER BY orderDate DESC")
    LiveData<List<Order>> getActiveOrders();

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY orderDate DESC")
    LiveData<List<Order>> getOrdersByUserId(String userId);

    @Query("UPDATE orders SET orderStatus = :status WHERE orderId = :orderId")
    void updateOrderStatus(String orderId, String status);

    @Query("UPDATE orders SET paymentStatus = :status WHERE orderId = :orderId")
    void updatePaymentStatus(String orderId, String status);
}
