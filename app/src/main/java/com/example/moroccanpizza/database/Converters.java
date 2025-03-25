package com.example.moroccanpizza.database;

import androidx.room.TypeConverter;

import com.example.moroccanpizza.model.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static List<CartItem> fromCartItemJson(String value) {
        Type listType = new TypeToken<List<CartItem>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String cartItemToJson(List<CartItem> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
