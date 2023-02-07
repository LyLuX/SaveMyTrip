package com.lylux.savemytrip.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.lylux.savemytrip.models.Item;

import java.util.List;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM Item WHERE userID = :userID") LiveData<List<Item>> getItemsByUserID(long userID);
    
    @Query("SELECT * FROM Item WHERE userID = :userID") Cursor getItemsWithCursor(long userID);
    
    @Insert long insertItem(Item pItem);
    
    @Update int updateItem(Item pItem);
    
    @Query("DELETE FROM Item WHERE id = :itemID") int deleteItem(long itemID);
}
