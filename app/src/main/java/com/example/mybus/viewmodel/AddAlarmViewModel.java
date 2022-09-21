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
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class AddAlarmViewModel extends ViewModel {
    private BusRoomRepository busRoomRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private List<SchAlarmInfo> schAlarmInfoList = new ArrayList<>();

    public MutableLiveData<List<SchAlarmInfo>> schAlarmList = new MutableLiveData<>();

    @Inject
    public AddAlarmViewModel(BusRoomRepository busRoomRepository) {
        this.busRoomRepository = busRoomRepository;
    }

    public void insertSchAlarm(SchAlarmInfo schAlarmInfo){
        busRoomRepository.insertSchAlarm(schAlarmInfo);
    }




}
