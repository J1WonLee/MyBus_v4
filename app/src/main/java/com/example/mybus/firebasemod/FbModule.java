package com.example.mybus.firebasemod;

import android.content.Context;

import com.example.mybus.firebaserepo.FbRepository;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class FbModule {

    @Provides
    @Singleton
    DatabaseReference provideDatabaseReference(@ApplicationContext Context context) {
        return FirebaseDatabase.getInstance().getReference();
    }

    @Provides
    @Singleton
    FbRepository provideFbRepository(DatabaseReference databaseReference){
        return new FbRepository(databaseReference);
    }
}
