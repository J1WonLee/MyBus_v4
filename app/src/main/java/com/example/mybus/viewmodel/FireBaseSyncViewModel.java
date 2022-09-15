package com.example.mybus.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mybus.firebaserepo.FbRepository;
import com.example.mybus.roomrepo.BusRoomRepository;
import com.example.mybus.vo.LocalFav;
import com.example.mybus.vo.LocalFavStopBus;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class FireBaseSyncViewModel extends ViewModel {
    private BusRoomRepository busRoomRepository;
    private DatabaseReference databaseReference;
    private List<LocalFav> localFavList = new ArrayList<>();
    private List<LocalFavStopBus> localFavStopBusList = new ArrayList<>();
    public int count = 0;

    public MutableLiveData<List<LocalFav>> mutableLiveDataLocalFavList = new MutableLiveData<>();
    public MutableLiveData<List<LocalFavStopBus>> mutableLiveDataLocalFavBusList = new MutableLiveData<>();
    public MutableLiveData<Integer> insertLocalFav = new MutableLiveData<Integer>(0);
    public MutableLiveData<Integer> deleteFlag = new MutableLiveData<>(0);
    public MutableLiveData<Integer> insertLocalFavFab = new MutableLiveData<>(0);

    @Inject
    public FireBaseSyncViewModel(BusRoomRepository busRoomRepository, FbRepository fbRepository, DatabaseReference databaseReference) {
        this.busRoomRepository = busRoomRepository;
        this.databaseReference = databaseReference;
    }

    public void getFbList(String loginId){
        databaseReference.child("FavList").child(loginId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot fbFavSnapShot : dataSnapshot.getChildren()){
                    LocalFav localFav = fbFavSnapShot.getValue(LocalFav.class);
//                    Log.d("FbRepository", localFav.getLf_id() + "::::" + localFav.getLf_name() +"::::");
                    localFavList.add(localFav);
                }
                mutableLiveDataLocalFavList.setValue(localFavList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("FireBaseSyncViewModel", "ERROR ON getFbFavLists  home edit " + databaseError.getMessage());
            }
        });
    }

    public void getFavList(String loginId, String lfId){
        databaseReference.child("FsbList").child(loginId).child(lfId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot fbFavFabSanpShot : dataSnapshot.getChildren()){
                    LocalFavStopBus localFavStopBus = fbFavFabSanpShot.getValue(LocalFavStopBus.class);
                    Log.d("FireBaseSyncViewModel", localFavStopBus.getLfb_busId() + "::::" + localFavStopBus.getLfb_busName() +"::::");
                    localFavStopBusList.add(localFavStopBus);
                }
                mutableLiveDataLocalFavBusList.setValue(localFavStopBusList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("FireBaseSyncViewModel", "ERROR ON getFavList  home edit " + databaseError.getMessage());
            }
        });
    }

    public void getFavList(String loginId, List<LocalFav> localFavList){
        for(LocalFav lists : localFavList){
            databaseReference.child("FsbList").child(loginId).child(lists.getLf_id()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot fbFavFabSanpShot : dataSnapshot.getChildren()){
                        LocalFavStopBus localFavStopBus = fbFavFabSanpShot.getValue(LocalFavStopBus.class);
                        Log.d("FireBaseSyncViewModel", localFavStopBus.getLfb_busId() + "::::" + localFavStopBus.getLfb_busName() +"::::");
                        localFavStopBusList.add(localFavStopBus);
                    }
                    mutableLiveDataLocalFavBusList.setValue(localFavStopBusList);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("FireBaseSyncViewModel", "ERROR ON getFavList  home edit " + databaseError.getMessage());
                }
            });
        }
    }



    public void insertFavAll(List<LocalFav> localFavList){
        Completable completable = busRoomRepository.insertFavAll(localFavList);
        completable.subscribeOn(Schedulers.newThread())
                     .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        () -> insertLocalFav.setValue(1),
                        error -> Log.d("FireBaseSyncViewModel", " deleteFavList failed!"+error.getMessage())
                );
    }

//    public void insertFavBusAll(LocalFavStopBus localFavStopBus){
//        Completable completable = busRoomRepository.insertFabFabAll(localFavStopBus);
//        completable.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        () -> insertLocalFavFab.setValue(insertLocalFavFab.getValue()+1),
//                        error -> Log.d("FireBaseSyncViewModel", " insertFavBusAll failed!"+error.getMessage())
//                );
//    }

    public void insertFavBusAll(List<LocalFavStopBus> localFavStopBusLists){
        Completable completable = busRoomRepository.insertFabFabAll(localFavStopBusLists);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> insertLocalFavFab.setValue(insertLocalFavFab.getValue()+1),
                        error -> Log.d("FireBaseSyncViewModel", " insertFavBusAll failed!"+error.getMessage())
                );
    }

    public void clearLocalFavBus(){
        Completable completable = busRoomRepository.deleteLocalFavBusAll();
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> clearLocalFav(),
                        error -> Log.d("FireBaseSyncViewModel", " clearLocalFav failed!"+error.getMessage())
                );
    }

    public void clearLocalFav(){
        Completable completable = busRoomRepository.deleteLocalFavAll();
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> deleteFlag.setValue(1),
                        error -> Log.d("FireBaseSyncViewModel", " clearLocalFav failed!"+error.getMessage())
                );
    }
}
