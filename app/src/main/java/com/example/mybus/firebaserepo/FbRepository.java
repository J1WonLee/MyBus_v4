package com.example.mybus.firebaserepo;

import android.util.Log;

import com.example.mybus.vo.LocalFav;
import com.example.mybus.vo.LocalFavStopBus;
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
    public void deleteFbFab(String lfId, String logInId) {
        try{
            Log.d("FbRepository", "deleteFbFab FBFAV!!");
            databaseReference.child("FavList").child(logInId).child(lfId).removeValue();
        }catch(Exception e){
            Log.d("FbRepository", "ERROR ON INSERTFBFAV " + e.getMessage());
        }
    }

    @Override
    public void insertFbStopFav(LocalFav localFav, LocalFavStopBus localFavStopBus, String loginId) {
        try{
            databaseReference.child("FavList").child(loginId).child(localFav.getLf_id()).setValue(localFav);
            databaseReference.child("FsbList").child(loginId).child(localFav.getLf_id()).child(localFavStopBus.getLfb_busId()).setValue(localFavStopBus);
        }catch(Exception e){
            Log.d("FbRepository", "ERROR ON insertFbStopFav " + e.getMessage());
        }
    }

    @Override
    public void deleteFbStopFav(String lfId, String lfbId, String loginId) {
        try{
            databaseReference.child("FsbList").child(loginId).child(lfId).child(lfbId).removeValue();
        }catch(Exception e){
            Log.d("FbRepository", "ERROR ON deleteFbStopFav " + e.getMessage());
        }
    }

    @Override
    public void deleteFbFabInStopDetail(String lfId, String loginId) {
        try{
            databaseReference.child("FavList").child(loginId).child(lfId).removeValue();
            databaseReference.child("FsbList").child(loginId).child(lfId).removeValue();
        }catch(Exception e){
            Log.d("FbRepository", "ERROR ON deleteFbFabInStopDetail " + e.getMessage());
        }
    }

    // 매인화면에서 정류장 경유 버스 즐겨찾기 저장
    @Override
    public void insertFbStopFavFromMain(LocalFavStopBus localFavStopBus, String loginId) {
        try{
            databaseReference.child("FsbList").child(loginId).child(localFavStopBus.getLfb_id()).child(localFavStopBus.getLfb_busId()).setValue(localFavStopBus);
        }catch(Exception e){
            Log.d("FbRepository", "ERROR ON insertFbStopFavFromMain " + e.getMessage());
        }
    }
}
