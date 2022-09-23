package com.example.mybus.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.mybus.firebaserepo.FbRepository;
import com.example.mybus.roomrepo.BusRoomRepository;
import com.example.mybus.vo.DataWithFavStopBus;
import com.example.mybus.vo.LocalFav;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class HomeEditViewModel extends ViewModel {
    private BusRoomRepository busRoomRepository;
    private FbRepository fbRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public HomeEditViewModel(BusRoomRepository busRoomRepository, FbRepository fbRepository) {
        this.busRoomRepository = busRoomRepository;
        this.fbRepository = fbRepository;
    }

    public void updateAll(List<LocalFav> localFavList){
        busRoomRepository.updateFavAll(localFavList);
    }

    public void deleteFavList(List<LocalFav> localFavList){
        busRoomRepository.deleteFavList(localFavList);
    }

    public void updateFbFav(List<LocalFav> localFavList, String loginId){
        for (LocalFav lists : localFavList){
            fbRepository.updateFbFab(lists, loginId);
        }
    }

    public void deleteFbFav(List<LocalFav> localFavList, String loginId){
        for (LocalFav lists : localFavList){
            fbRepository.deleteFbFab(lists, loginId);
        }
    }

    public void deleteRecentSch(){
        busRoomRepository.deleteRecentBusSch();
        busRoomRepository.deleteRecentStopSch();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
