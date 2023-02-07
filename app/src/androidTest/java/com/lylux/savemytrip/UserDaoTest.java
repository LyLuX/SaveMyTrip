package com.lylux.savemytrip;

import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.lylux.savemytrip.database.SaveMyTripDatabase;
import com.lylux.savemytrip.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserDaoTest {
    
    private static final long USER_ID   = 1;
    private static final User TEST_USER = new User(USER_ID, "Paul", "2c19473a6bb69f5b75e769cb215d42ff");
    
    @Rule public InstantTaskExecutorRule mRule = new InstantTaskExecutorRule();
    private      SaveMyTripDatabase      mDatabase;
    
    @Before public void initDB() {
        this.mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(), SaveMyTripDatabase.class)
                             .allowMainThreadQueries()
                             .build();
    }
    
    @After public void closeDB() {
        this.mDatabase.close();
    }
    
    @Test public void insertAndGetUser() throws Exception {
        this.mDatabase.userDao().createUser(TEST_USER);
        
        User user = LiveDataTestUtil.getValue(this.mDatabase.userDao().getUserByID(USER_ID));
        
        assertTrue(user.getUsername().equals(TEST_USER.getUsername()) && user.getId() == USER_ID);
    }
}
