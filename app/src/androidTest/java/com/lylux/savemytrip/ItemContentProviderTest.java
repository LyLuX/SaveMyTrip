package com.lylux.savemytrip;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.lylux.savemytrip.database.SaveMyTripDatabase;
import com.lylux.savemytrip.provider.ItemContentProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ItemContentProviderTest {
    
    private static final long               USER_ID = 1;
    private              ContentResolver    mResolver;
    private              SaveMyTripDatabase mDatabase;
    
    @Before public void setup() {
        this.mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(), SaveMyTripDatabase.class)
                             .allowMainThreadQueries()
                             .build();
        
        mResolver = InstrumentationRegistry.getInstrumentation().getContext().getContentResolver();
    }
    
    @After public void closeDB() {
        this.mDatabase.close();
    }
    
    @Test public void getItemsWhenNoItemsInserted() {
        final Cursor cursor = mResolver.query(ContentUris.withAppendedId(ItemContentProvider.ITEM_URI, USER_ID), null, null, null, null);
        
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        
        cursor.close();
    }
    
    @Test public void insertAndGetItem() {
        final Uri    userUri = mResolver.insert(ItemContentProvider.ITEM_URI, this.generateItem());
        final Cursor cursor  = mResolver.query(ContentUris.withAppendedId(ItemContentProvider.ITEM_URI, USER_ID), null, null, null, null);
        
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("text")), is("Visiter cet endroit de rêve !"));
        
        cursor.close();
    }
    
    @NonNull private ContentValues generateItem() {
        final ContentValues values = new ContentValues();
        
        values.put("category", "0");
        values.put("text", "Visiter cet endroit de rêve !");
        values.put("isSelected", "false");
        values.put("userID", "1");
        
        return values;
    }
}
