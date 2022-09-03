package com.example.mybus.roomdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.apisearch.itemList.StopSchList;
import com.example.mybus.vo.User;

@Database(entities = {User.class, BusSchList.class, StopSchList.class}, version = 10, exportSchema = false)
public abstract class BusRoomDatabase extends RoomDatabase {
    public abstract BusDao getDao();
}
