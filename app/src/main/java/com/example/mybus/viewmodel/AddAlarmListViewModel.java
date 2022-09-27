package com.example.mybus.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mybus.roomrepo.BusRoomRepository;
import com.example.mybus.vo.DataWithFavStopBus;
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
public class AddAlarmListViewModel extends ViewModel {
    private BusRoomRepository busRoomRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private List<DataWithFavStopBus> dataWithFavStopBuseArrayList = new ArrayList<>();
    public MutableLiveData<List<DataWithFavStopBus>> localFavStopBusLists = new MutableLiveData();


    @Inject
    public AddAlarmListViewModel(BusRoomRepository busRoomRepository) {
        this.busRoomRepository = busRoomRepository;
    }

    public void getFavStopBus(){
        compositeDisposable.add(
                busRoomRepository.getFavStopBusForAlarm()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<DataWithFavStopBus>>() {
                            @Override
                            public void onSuccess(@NonNull List<DataWithFavStopBus> dataWithFavStopBuses) {
                                if (dataWithFavStopBuses != null){
                                    dataWithFavStopBuseArrayList = dataWithFavStopBuses;
                                    localFavStopBusLists.setValue(dataWithFavStopBuseArrayList);
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("MainViewModel", "error on getFavStopBus : " + e.getMessage());
                            }
                        })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
