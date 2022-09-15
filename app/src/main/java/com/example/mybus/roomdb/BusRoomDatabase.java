package com.example.mybus.roomdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.apisearch.itemList.StopSchList;
import com.example.mybus.vo.Converters;
import com.example.mybus.vo.LocalFav;
import com.example.mybus.vo.LocalFavStopBus;
import com.example.mybus.vo.User;

@Database(entities = {User.class, BusSchList.class, StopSchList.class, LocalFav.class, LocalFavStopBus.class}, version = 16, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class BusRoomDatabase extends RoomDatabase {
    public abstract BusDao getDao();
}
