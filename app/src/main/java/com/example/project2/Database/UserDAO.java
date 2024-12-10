package com.example.project2.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.project2.Database.entities.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + DexDatabase.USER_TABLE + " ORDER BY username")
    LiveData<List<User>> getAllUsers();

    @Query("DELETE FROM "+ DexDatabase.USER_TABLE)
    void deleteAll();

    @Query("SELECT * FROM " + DexDatabase.USER_TABLE + " WHERE username == :username" )
    LiveData<User> getUserByUserName(String username);

    @Query("SELECT * FROM " + DexDatabase.USER_TABLE + " WHERE id == :userId" )
    LiveData<User> getUserByUserId(int userId);

    // Fetch a user by ID synchronously
    @Query("SELECT * FROM usertable WHERE id = :userId")
    User getUserByIdSync(int userId);

    // Update a user
    @Update
    void update(User user);
}
