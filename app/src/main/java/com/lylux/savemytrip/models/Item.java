package com.lylux.savemytrip.models;

import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = { "id" }, childColumns = { "userID" }),
        indices = { @Index(value = { "userID" }) })
public class Item {
    
    @PrimaryKey(autoGenerate = true)
    private long    id;
    private int     category;
    private String  text;
    private boolean isSelected;
    private long    userID;
    
    public Item() { }
    
    public Item(int pCategory, String pText, long pUserID) {
        this.text       = pText;
        this.category   = pCategory;
        this.userID     = pUserID;
        this.isSelected = false;
    }
    
    @NonNull public static Item itemFromContentValues(@NonNull ContentValues pContentValues) {
        final Item item = new Item();
        
        if (pContentValues.containsKey("category")) item.setCategory(pContentValues.getAsInteger("category"));
        if (pContentValues.containsKey("text")) item.setText(pContentValues.getAsString("text"));
        if (pContentValues.containsKey("isSelected")) item.setIsSelected(pContentValues.getAsBoolean("isSelected"));
        if (pContentValues.containsKey("userID")) item.setUserID(pContentValues.getAsLong("userID"));
        
        return item;
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long pId) {
        id = pId;
    }
    
    public int getCategory() {
        return category;
    }
    
    public void setCategory(int pCategory) {
        category = pCategory;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String pText) {
        text = pText;
    }
    
    public boolean isSelected() {
        return isSelected;
    }
    
    public void setIsSelected(boolean pSelected) {
        isSelected = pSelected;
    }
    
    public long getUserID() {
        return userID;
    }
    
    public void setUserID(long pUserID) {
        userID = pUserID;
    }
}
