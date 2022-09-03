package com.example.mybus.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mybus.firebaserepo.FbRepository;
import com.example.mybus.roomrepo.BusRepository;
import com.example.mybus.vo.User;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

// 로그아웃 시 회원 정보 날린다. 로그인 할 때 다시 정보를받아오는걸로 (동기화의 의미)
@HiltViewModel
public class MainViewModel extends ViewModel {
    private BusRepository busRepository;
    private FbRepository fbRepository;
    public MutableLiveData<User> mUser = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public MainViewModel(BusRepository busRepository, FbRepository fbRepository) {
        this.busRepository = busRepository;
        this.fbRepository = fbRepository;
    }

    public void getUser(){
        disposable.add(
          busRepository.getUser()
                  .subscribeOn(Schedulers.newThread())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribeWith(new DisposableSingleObserver<User>() {
                      @Override
                      public void onSuccess(@NonNull User user) {
                          Log.d("kkang", "getuser success");
                          mUser.setValue(user);
                      }
                      @Override
                      public void onError(@NonNull Throwable e) {
                        Log.d("kkang", e.getMessage() + " is error");
                        mUser.setValue(null);
                      }
                  })
        );
    }

    public void delete(){
        busRepository.delete();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
