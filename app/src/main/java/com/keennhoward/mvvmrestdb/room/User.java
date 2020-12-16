package com.keennhoward.mvvmrestdb.room;

import androidx.room.Entity;

@Entity(tableName = "user_table")
public class User {

    private int id;

    private String email;

    private String first_name;

    private String last_name;

    private String avatar;

    public User(int id, String email, String first_name, String last_name, String avatar) {
        this.id = id;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getAvatar() {
        return avatar;
    }
}
