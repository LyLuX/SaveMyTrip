package com.lylux.savemytrip.repositories;

import androidx.lifecycle.LiveData;

import com.lylux.savemytrip.database.dao.UserDao;
import com.lylux.savemytrip.models.User;

public class UserDataRepository {
    
    private final UserDao mUserDao;
    
    public UserDataRepository(UserDao pUserDao) {
        mUserDao = pUserDao;
    }
    
    public LiveData<User> getUserByID(long pUserID) { return this.getUserDao().getUserByID(pUserID); }
    
    protected UserDao getUserDao() {
        return mUserDao;
    }
}
