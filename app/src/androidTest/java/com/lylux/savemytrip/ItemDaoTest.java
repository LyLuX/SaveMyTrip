package com.lylux.savemytrip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.lylux.savemytrip.database.SaveMyTripDatabase;
import com.lylux.savemytrip.models.Item;
import com.lylux.savemytrip.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ItemDaoTest {
    
    private static final long USER_ID                 = 1;
    private static final User TEST_USER               = new User(USER_ID, "Paul", "2c19473a6bb69f5b75e769cb215d42ff");
    private static final Item NEW_ITEM_PLACE_TO_VISIT = new Item(0, "Visite cet endroit de rêve !", USER_ID);
    private static final Item NEW_ITEM_IDEA           = new Item(1, "On pourrait faire du chien de traîneau ?", USER_ID);
    private static final Item NEW_ITEM_RESTAURANTS    = new Item(2, "Ce restaurant à l'air sympa", USER_ID);
    
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
    
    @Test public void getItemsWhenNoItems() throws Exception {
        List<Item> items = LiveDataTestUtil.getValue(this.mDatabase.itemDao().getItemsByUserID(USER_ID));
        
        assertTrue(items.isEmpty());
    }
    
    @Test public void insertAndGetItems() throws Exception {
        this.mDatabase.userDao().createUser(TEST_USER);
        this.mDatabase.itemDao().insertItem(NEW_ITEM_PLACE_TO_VISIT);
        this.mDatabase.itemDao().insertItem(NEW_ITEM_IDEA);
        this.mDatabase.itemDao().insertItem(NEW_ITEM_RESTAURANTS);
        
        List<Item> items = LiveDataTestUtil.getValue(this.mDatabase.itemDao().getItemsByUserID(USER_ID));
        
        assertEquals(3, items.size());
    }
    
    @Test public void InsertAndUpdateItem() throws Exception {
        this.mDatabase.userDao().createUser(TEST_USER);
        this.mDatabase.itemDao().insertItem(NEW_ITEM_PLACE_TO_VISIT);
        
        Item itemAdded = LiveDataTestUtil.getValue(this.mDatabase.itemDao().getItemsByUserID(USER_ID)).get(0);
        itemAdded.setIsSelected(true);
        
        this.mDatabase.itemDao().updateItem(itemAdded);
        
        List<Item> items = LiveDataTestUtil.getValue(this.mDatabase.itemDao().getItemsByUserID(USER_ID));
        
        assertTrue(items.size() == 1 && items.get(0).isSelected());
    }
    
    @Test public void insertAndDeleteItem() throws Exception {
        this.mDatabase.userDao().createUser(TEST_USER);
        this.mDatabase.itemDao().insertItem(NEW_ITEM_PLACE_TO_VISIT);
        
        Item itemAdded = LiveDataTestUtil.getValue(this.mDatabase.itemDao().getItemsByUserID(USER_ID)).get(0);
        this.mDatabase.itemDao().deleteItem(itemAdded.getId());
        
        List<Item> items = LiveDataTestUtil.getValue(this.mDatabase.itemDao().getItemsByUserID(USER_ID));
        
        assertTrue(items.isEmpty());
    }
}
