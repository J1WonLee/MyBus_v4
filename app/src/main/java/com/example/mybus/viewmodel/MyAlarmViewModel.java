package com.example.mybus.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mybus.roomrepo.BusRoomRepository;
import com.example.mybus.vo.SchAlarmInfo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class MyAlarmViewModel extends ViewModel {
    private BusRoomRepository busRoomRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private List<SchAlarmInfo> schAlarmInfoList = new ArrayList<>();
    public MutableLiveData<List<SchAlarmInfo>> schAlarmList = new MutableLiveData<>();

    @Inject
    public MyAlarmViewModel(BusRoomRepository busRoomRepository) {
        this.busRoomRepository = busRoomRepository;
    }

    public void getSchAlarmList(){
        compositeDisposable.add(
                busRoomRepository.getSchAlarmList()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<SchAlarmInfo>>() {
                            @Override
                            public void onSuccess(@NonNull List<SchAlarmInfo> schAlarmInfos) {
                                if (schAlarmInfos != null){
                                    schAlarmInfoList.addAll(schAlarmInfos);
                                    schAlarmList.setValue(schAlarmInfoList);
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("AddAlarmViewModel", "getSchAlarmList error!!" + e.getMessage());
                            }
                        })
        );
    }

    public void  deleteSchAlarm(SchAlarmInfo schAlarmInfo){
        Completable completable = busRoomRepository.deleteSchAlarm(schAlarmInfo);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d("MyAlarmViewModel", "deleteSchAlarm , disposed");
//                        d.dispose();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("MyAlarmViewModel", "deleteSchAlarm , completed");
                        schAlarmInfoList.remove(schAlarmInfo);
                        schAlarmList.setValue(schAlarmInfoList);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("MyAlarmViewModel", "deleteSchAlarm error!!" + e.getMessage());
                    }
                });
    }

    public void updateSchAlarm(List<SchAlarmInfo> schAlarmInfos){
        busRoomRepository.updateSchAlarm(schAlarmInfos);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
