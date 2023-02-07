package com.lylux.savemytrip.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    
    @PrimaryKey
    private long   id;
    private String username;
    private String avatar;
    
    public User(long id, String username, String avatar) {
        this.id       = id;
        this.username = username;
        this.avatar   = avatar;
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long pId) {
        id = pId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String pUsername) {
        username = pUsername;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String pAvatar) {
        avatar = pAvatar;
    }
}
