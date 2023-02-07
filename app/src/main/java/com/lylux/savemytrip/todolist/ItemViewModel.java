package com.lylux.savemytrip.todolist;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.lylux.savemytrip.models.Item;
import com.lylux.savemytrip.models.User;
import com.lylux.savemytrip.repositories.ItemDataRepository;
import com.lylux.savemytrip.repositories.UserDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class ItemViewModel extends ViewModel {
    
    private final     ItemDataRepository mItemDataRepository;
    private final     UserDataRepository mUserDataRepository;
    private final     Executor           mExecutor;
    @Nullable private LiveData<User>     mUser;
    
    /**
     * Construct a new ViewModel instance.
     * <p>
     * You should <strong>never</strong> manually construct a ViewModel outside of a {@link androidx.lifecycle.ViewModelProvider.Factory}.
     */
    public ItemViewModel(ItemDataRepository pItemDataRepository, UserDataRepository pUserDataRepository, Executor pExecutor) {
        this.mItemDataRepository = pItemDataRepository;
        this.mUserDataRepository = pUserDataRepository;
        this.mExecutor           = pExecutor;
    }
    
    public void initUser(long pUserID) {
        if (this.getUser() != null) return;
        
        this.setUser(this.getUserDataRepository().getUserByID(pUserID));
    }
    
    public LiveData<List<Item>> getItemsByUserID(long pUserID) {
        return this.getItemDataRepository().getItemsByUserID(pUserID);
    }
    
    public void createItem(int pCategory, String pText, long pUserID) {
        this.getExecutor().execute(() -> this.getItemDataRepository().createItem(new Item(pCategory, pText, pUserID)));
    }
    
    public void updateItem(Item pItem) {
        this.getExecutor().execute(() -> this.getItemDataRepository().updateItem(pItem));
    }
    
    public void deleteItem(Item pItem) {
        this.getExecutor().execute(() -> this.getItemDataRepository().deleteItem(pItem));
    }
    
    public ItemDataRepository getItemDataRepository() {
        return mItemDataRepository;
    }
    
    public UserDataRepository getUserDataRepository() {
        return mUserDataRepository;
    }
    
    public Executor getExecutor() {
        return mExecutor;
    }
    
    @Nullable public LiveData<User> getUser() {
        return mUser;
    }
    
    protected void setUser(@Nullable LiveData<User> pUser) {
        mUser = pUser;
    }
}
