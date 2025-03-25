package com.example.moroccanpizza.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moroccanpizza.model.CartItem;

import java.util.List;

@Dao
public interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCartItem(CartItem cartItem);

    @Update
    void updateCartItem(CartItem cartItem);

    @Delete
    void deleteCartItem(CartItem cartItem);

    @Query("SELECT * FROM cart_items")
    LiveData<List<CartItem>> getAllCartItems();

    @Query("SELECT * FROM cart_items WHERE pizzaId = :pizzaId")
    LiveData<CartItem> getCartItemByPizzaId(int pizzaId);

    @Query("SELECT SUM(pizzaPrice * quantity) FROM cart_items")
    LiveData<Double> getCartTotal();

    @Query("SELECT COUNT(*) FROM cart_items")
    LiveData<Integer> getCartItemCount();

    @Query("DELETE FROM cart_items")
    void clearCart();
}
