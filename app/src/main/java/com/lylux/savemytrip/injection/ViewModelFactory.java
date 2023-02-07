package com.lylux.savemytrip.injection;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lylux.savemytrip.database.SaveMyTripDatabase;
import com.lylux.savemytrip.repositories.ItemDataRepository;
import com.lylux.savemytrip.repositories.UserDataRepository;
import com.lylux.savemytrip.todolist.ItemViewModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ViewModelFactory implements ViewModelProvider.Factory {
    
    private static volatile ViewModelFactory   sFactory;
    private final           ItemDataRepository mItemDataRepository;
    private final           UserDataRepository mUserDataRepository;
    private final           Executor           mExecutor;
    
    private ViewModelFactory(Context pContext) {
        SaveMyTripDatabase database = SaveMyTripDatabase.getInstance(pContext);
        
        this.mItemDataRepository = new ItemDataRepository(database.itemDao());
        this.mUserDataRepository = new UserDataRepository(database.userDao());
        this.mExecutor           = Executors.newSingleThreadExecutor();
    }
    
    public static ViewModelFactory getInstance(Context pContext) {
        if (getFactory() == null) {
            synchronized (ViewModelFactory.class) {
                if (getFactory() == null) {
                    sFactory = new ViewModelFactory(pContext);
                }
            }
        }
        
        return getFactory();
    }
    
    private static ViewModelFactory getFactory() {
        return sFactory;
    }
    
    @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ItemViewModel.class)) return (T) new ItemViewModel(this.getItemDataRepository(),
                                                                                           this.getUserDataRepository(),
                                                                                           this.getExecutor());
        throw new IllegalArgumentException("Unknown ViewModel class...");
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
}
