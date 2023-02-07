package com.lylux.savemytrip.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.lylux.savemytrip.database.dao.ItemDao;
import com.lylux.savemytrip.models.Item;

import java.util.List;

public class ItemDataRepository {
    
    private final ItemDao mItemDao;
    
    public ItemDataRepository(ItemDao pItemDao) {
        mItemDao = pItemDao;
    }
    
    public LiveData<List<Item>> getItemsByUserID(long pUserID) {
        return this.mItemDao.getItemsByUserID(pUserID);
    }
    
    public void createItem(Item pItem) {
        this.mItemDao.insertItem(pItem);
    }
    
    public void updateItem(Item pItem) {
        this.mItemDao.updateItem(pItem);
    }
    
    public void deleteItem(@NonNull Item pItem) {
        this.mItemDao.deleteItem(pItem.getId());
    }
    
    public ItemDao getItemDao() {
        return mItemDao;
    }
}
