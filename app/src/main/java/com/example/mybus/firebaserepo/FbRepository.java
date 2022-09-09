package com.example.mybus.firebaserepo;

import android.util.Log;

import com.example.mybus.vo.LocalFav;
import com.example.mybus.vo.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FbRepository implements FbService{
    private DatabaseReference databaseReference;

    public FbRepository(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    @Override
    public void insert(User user) {
        databaseReference.child("users").child(user.getUser_tk()).setValue(user);
    }

    @Override
    public void insertFbFav(LocalFav localFav, String loginId) {
        try{
            Log.d("FbRepository", "INSERT FBFAV!!");
            databaseReference.child("FavList").child(loginId).child(localFav.getLf_id()).setValue(localFav);
        }catch(Exception e){
            Log.d("FbRepository", "ERROR ON INSERTFBFAV " + e.getMessage());
        }
    }

    @Override
    public void deleteFbFab(String lfbId, String logInId) {
        try{
            Log.d("FbRepository", "deleteFbFab FBFAV!!");
            databaseReference.child("FavList").child(logInId).child(lfbId).removeValue();
        }catch(Exception e){
            Log.d("FbRepository", "ERROR ON INSERTFBFAV " + e.getMessage());
        }
    }
}
