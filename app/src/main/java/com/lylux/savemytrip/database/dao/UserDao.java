package com.lylux.savemytrip.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.lylux.savemytrip.models.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User WHERE id = :pUserID") LiveData<User> getUserByID(long pUserID);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE) void createUser(User pUser);
}
