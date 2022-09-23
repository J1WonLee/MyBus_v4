package com.example.mybus.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mybus.roomrepo.BusRoomRepository;
import com.example.mybus.vo.SchAlarmInfo;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class UpdateAlarmViewModel extends ViewModel {
    private BusRoomRepository busRoomRepository;
    private Completable completable;
    public MutableLiveData<Integer> isUpdatedLiveData = new MutableLiveData<>(0);

    @Inject
    public UpdateAlarmViewModel(BusRoomRepository busRoomRepository) {
        this.busRoomRepository = busRoomRepository;
    }

    public void updateAlarm(SchAlarmInfo schAlarmInfo){
            completable = busRoomRepository.updateSchAlarm(schAlarmInfo);
            completable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> isUpdatedLiveData.setValue(1),
                            error -> Log.d("updateAlarm", "error on updateAlarm")
                    );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

}
