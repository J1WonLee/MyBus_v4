package com.example.mybus.vo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "USER_INFO")
public class User {
    @NonNull
    @PrimaryKey
    String user_tk;

    String user_name;

    String user_thunbnail;

    int is_Logout;

    public User(String user_tk, String user_name, String user_thunbnail) {
        this.user_tk = user_tk;
        this.user_name = user_name;
        this.user_thunbnail = user_thunbnail;
        this.is_Logout = 1;
    }

    public String getUser_tk() {
        return user_tk;
    }

    public void setUser_tk(String user_tk) {
        this.user_tk = user_tk;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_thunbnail() {
        return user_thunbnail;
    }

    public void setUser_thunbnail(String user_thunbnail) {
        this.user_thunbnail = user_thunbnail;
    }

    public int getIs_Logout() {
        return is_Logout;
    }

    public void setIs_Logout(int is_Logout) {
        this.is_Logout = is_Logout;
    }
}
