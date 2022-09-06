package com.example.mybus.roommod;

import android.content.Context;


import androidx.room.Room;

import com.example.mybus.roomdb.BusDao;
import com.example.mybus.roomdb.BusRoomDatabase;
import com.example.mybus.roomrepo.BusRoomRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RoomModule {

    @Singleton
    @Provides
    BusRoomDatabase provideBusDatabase(@ApplicationContext Context context){
        return Room.databaseBuilder(context, BusRoomDatabase.class, "bus_room")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    BusDao provideBusDao(BusRoomDatabase db){ return db.getDao(); }

    @Singleton
    @Provides
    BusRoomRepository provideBusRepository(BusDao busDao) {
        return new BusRoomRepository(busDao);
    }



}
