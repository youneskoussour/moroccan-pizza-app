package com.example.moroccanpizza.api;

import com.example.moroccanpizza.model.Order;
import com.example.moroccanpizza.model.Pizza;
import com.example.moroccanpizza.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @GET("pizzas")
    Call<List<Pizza>> getPizzas();

    @GET("pizzas/featured")
    Call<List<Pizza>> getFeaturedPizzas();

    @GET("orders/{userId}")
    Call<List<Order>> getOrdersByUser(@Path("userId") String userId);

    @GET("orders/track/{orderId}")
    Call<Order> trackOrder(@Path("orderId") String orderId);

    @POST("orders")
    Call<Order> createOrder(@Body Order order);

    @PUT("orders/{orderId}")
    Call<Order> updateOrder(@Path("orderId") String orderId, @Body Order order);

    @POST("users")
    Call<User> createUser(@Body User user);

    @PUT("users/{userId}")
    Call<User> updateUser(@Path("userId") String userId, @Body User user);

    @POST("support/message")
    Call<Void> sendSupportMessage(@Body String message);
}
