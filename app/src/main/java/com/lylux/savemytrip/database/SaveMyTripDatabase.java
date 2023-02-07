package com.lylux.savemytrip.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.lylux.savemytrip.database.dao.ItemDao;
import com.lylux.savemytrip.database.dao.UserDao;
import com.lylux.savemytrip.models.Item;
import com.lylux.savemytrip.models.User;

import org.jetbrains.annotations.Contract;

import java.util.concurrent.Executors;

@Database(entities = { Item.class, User.class }, version = 1, exportSchema = false)
public abstract class SaveMyTripDatabase extends RoomDatabase {
    
    private static final    String             DB_NAME = "SaveMyTripDB.db";
    private static volatile SaveMyTripDatabase INSTANCE;
    
    public static SaveMyTripDatabase getInstance(Context pContext) {
        if (INSTANCE == null) {
            synchronized (SaveMyTripDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(pContext.getApplicationContext(), SaveMyTripDatabase.class, DB_NAME)
                                   .addCallback(prepopulateDB())
                                   .build();
                }
            }
        }
        
        return INSTANCE;
    }
    
    @NonNull @Contract(value = " -> new", pure = true) private static Callback prepopulateDB() {
        return new Callback() {
            @Override public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                
                Executors.newSingleThreadExecutor().execute(() -> {
                    INSTANCE.userDao().createUser(new User(1, "Paul", "2c19473a6bb69f5b75e769cb215d42ff"));
                });
            }
        };
    }
    
    public abstract ItemDao itemDao();
    
    public abstract UserDao userDao();
}
