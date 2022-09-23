package com.example.mybus.firebaserepo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mybus.vo.LocalFav;
import com.example.mybus.vo.LocalFavStopBus;
import com.example.mybus.vo.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
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
    private List<LocalFav> localFavList = new ArrayList<>();

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

    @Override
    public void updateFbFab(LocalFav localFav, String loginId) {
        try{
            databaseReference.child("FavList").child(loginId).child(localFav.getLf_id()).child("lf_order").setValue(localFav.getLf_order());
        }catch(Exception e){
            Log.d("FbRepository", "ERROR ON insertFbStopFav home edit " + e.getMessage());
        }
    }

    @Override
    public void deleteFbFab(LocalFav localFav, String loginId) {
        try{
            databaseReference.child("FavList").child(loginId).child(localFav.getLf_id()).removeValue();
        }catch(Exception e){
            Log.d("FbRepository", "ERROR ON deleteFbFab  home edit " + e.getMessage());
        }
    }

    @Override
    public List<LocalFav> getFbFavLists(String loginId) {
        databaseReference.child("FavList").child(loginId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (DataSnapshot localFavSnapShot : dataSnapshot.getChildren()){
                   LocalFav localFav = localFavSnapShot.getValue(LocalFav.class);
                   Log.d("FbRepository", localFav.getLf_id() + "::::" + localFav.getLf_name() +"::::");
                   localFavList.add(localFav);
               }
//                Log.d("FbRepository", localFav.getLf_id() + "::::" + localFav.getLf_name() +"::::");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("FbRepository", "ERROR ON getFbFavLists  home edit " + databaseError.getMessage());
            }
        });
        return localFavList;
    }


}
