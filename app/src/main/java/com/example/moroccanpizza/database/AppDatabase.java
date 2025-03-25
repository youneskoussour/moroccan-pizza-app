package com.example.moroccanpizza.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.moroccanpizza.model.CartItem;
import com.example.moroccanpizza.model.Order;
import com.example.moroccanpizza.model.Pizza;
import com.example.moroccanpizza.model.User;

@Database(entities = {Pizza.class, CartItem.class, Order.class, User.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "moroccan_pizza_db";
    private static AppDatabase instance;

    public abstract CartDao cartDao();
    public abstract OrderDao orderDao();
    public abstract UserDao userDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
