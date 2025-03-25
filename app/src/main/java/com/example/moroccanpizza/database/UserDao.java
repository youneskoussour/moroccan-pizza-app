package com.example.moroccanpizza.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moroccanpizza.model.User;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUser(User user);

    @Update
    void updateUser(User user);

    @Query("SELECT * FROM users WHERE userId = :userId")
    LiveData<User> getUserById(String userId);

    @Query("SELECT * FROM users WHERE email = :email")
    LiveData<User> getUserByEmail(String email);

    @Query("UPDATE users SET address = :address WHERE userId = :userId")
    void updateUserAddress(String userId, String address);

    @Query("UPDATE users SET phone = :phone WHERE userId = :userId")
    void updateUserPhone(String userId, String phone);

    @Query("DELETE FROM users WHERE userId = :userId")
    void deleteUser(String userId);
}
