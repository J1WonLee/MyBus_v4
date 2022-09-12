package com.example.mybus.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mybus.apisearch.GbusWrapper.GBusStopRouteResponse;
import com.example.mybus.apisearch.GbusWrapper.GBusStopSearchResponse;
import com.example.mybus.apisearch.itemList.BusArrivalList;
import com.example.mybus.apisearch.itemList.StopUidSchList;
import com.example.mybus.apisearch.wrapper.StopRouteListWrap;
import com.example.mybus.apisearch.wrapper.StopSearchUidWrap;
import com.example.mybus.firebaserepo.FbRepository;
import com.example.mybus.retrofitrepo.RetrofitGbusRepository;
import com.example.mybus.retrofitrepo.RetrofitRepository;
import com.example.mybus.roomrepo.BusRoomRepository;
import com.example.mybus.vo.DataWithFavStopBus;
import com.example.mybus.vo.LocalFav;
import com.example.mybus.vo.LocalFavStopBus;
import com.example.mybus.vo.User;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

// 로그아웃 시 회원 정보 날린다. 로그인 할 때 다시 정보를받아오는걸로 (동기화의 의미)
@HiltViewModel
public class MainViewModel extends ViewModel {
    private BusRoomRepository busRoomRepository;
    private FbRepository fbRepository;
    private RetrofitRepository retrofitRepository;
    private RetrofitGbusRepository retrofitGbusRepository;

    private List<LocalFav> localFavArrayList = new ArrayList<>();
    private List<DataWithFavStopBus> dataWithFavStopBuseArrayList = new ArrayList<>();
    private List<StopUidSchList> stopUidSchLists = new ArrayList<>();
    private List<BusArrivalList> busArrivalLists = new ArrayList<>();

    public MutableLiveData<User> mUser = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MutableLiveData<List<StopUidSchList>> stopUidSchList = new MutableLiveData<>();
    public MutableLiveData<List<LocalFav>> localFavLists = new MutableLiveData<>();
    public MutableLiveData<List<DataWithFavStopBus>> localFavStopBusLists = new MutableLiveData<>();
    public MutableLiveData<List<BusArrivalList>> busArrivalList = new MutableLiveData<>();

    private static String serviceKey = "";
    static {
        try {
            serviceKey = URLDecoder.decode("7xKgSgAhOl%2FF9gxIzB20lcht%2BtM6G4MKRuw3arXF57DoSZftgzWzLrvcJNQIKn8mvv4UnoGSI5EzgAoxPI02yg%3D%3D", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }




    @Inject
    public MainViewModel(BusRoomRepository busRoomRepository, FbRepository fbRepository, RetrofitRepository retrofitRepository, RetrofitGbusRepository retrofitGbusRepository) {
        this.busRoomRepository = busRoomRepository;
        this.fbRepository = fbRepository;
        this.retrofitRepository = retrofitRepository;
        this.retrofitGbusRepository = retrofitGbusRepository;
    }

    public void getUser(){
        compositeDisposable.add(
          busRoomRepository.getUser()
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
        busRoomRepository.delete();
    }

    public void getFavList(){
        compositeDisposable.add(
                busRoomRepository.getLocalFavList()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<LocalFav>>() {
                            @Override
                            public void onSuccess(@NonNull List<LocalFav> localFavs) {
                                if (localFavs != null){
                                    localFavArrayList = localFavs;
                                    localFavLists.setValue(localFavArrayList);
                                }else{
                                    localFavLists.setValue(null);
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                    Log.d("MainViewModel", "error on getFavList : " + e.getMessage());
                            }
                        })
        );
    }

    public void getFavStopBus(){
        compositeDisposable.add(
            busRoomRepository.getFavStopBus()
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

    public void getFavArrTime(List<DataWithFavStopBus> dataWithFavStopBusList){
        compositeDisposable.add(
            Observable.fromIterable(dataWithFavStopBusList)
                    .flatMap(item -> retrofitRepository.schStopUid(serviceKey, item.localFav.getLf_id(), "json"))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<StopSearchUidWrap>() {
                        @Override
                        public void onNext(@NonNull StopSearchUidWrap stopSearchUidWrap) {
                            if (stopSearchUidWrap.getStopSearchUid().getItemLists() != null){
                                Log.d("MainViewModel", "getFavArrTime onNext!!");
                                stopUidSchLists.addAll(stopSearchUidWrap.getStopSearchUid().getItemLists());
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.d("MainViewModel", "getFavArrTime error!!" + e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            Log.d("MainViewModel", "getFavArrTime onComplete!!");
                            stopUidSchList.setValue(stopUidSchLists);
                        }
                    })
        );
    }

    public void getGbusFavArrTime(List<DataWithFavStopBus> dataWithFavStopBusList){
        compositeDisposable.add(
            Observable.fromIterable(dataWithFavStopBusList)
                    .flatMap(item -> retrofitGbusRepository.schStopUidv2(serviceKey, item.localFav.getLf_id()))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<GBusStopSearchResponse>() {
                        @Override
                        public void onNext(@NonNull GBusStopSearchResponse gBusStopSearchResponse) {
                            if (gBusStopSearchResponse.getgBusStopSearchUidWrap() != null){
                                Log.d("MainViewModel", "getGbusFavArrTime onNext!!");
                                busArrivalLists.addAll(gBusStopSearchResponse.getgBusStopSearchUidWrap().getBusArrivalListList());
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.d("MainViewModel", "getGbusFavArrTime error!!" + e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            Log.d("MainViewModel", "getGbusFavArrTime onComplete!!");
                            busArrivalList.setValue(busArrivalLists);
                        }
                    })
        );

    }

    public void getStopRouteList(String arsId){
        compositeDisposable.add(
            retrofitRepository.getStopRouteList(serviceKey, arsId, "json")
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<StopRouteListWrap>() {
                        @Override
                        public void onSuccess(@NonNull StopRouteListWrap stopRouteListWrap) {
                            if (stopRouteListWrap.getStopRouteList() != null){
                                Log.d("MainViewModel", "getStopRouteList success");
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.d("MainViewModel", "getStopRouteList onError");
                        }
                    })

        );
    }

    public void getGBusStopRouteList(String stId){
        compositeDisposable.add(
            retrofitGbusRepository.getGStopRouteList(serviceKey, stId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<GBusStopRouteResponse>() {
                    @Override
                    public void onSuccess(@NonNull GBusStopRouteResponse gBusStopRouteResponse) {
                        if (gBusStopRouteResponse.getgBusRouteStationWrap() != null){
                            Log.d("MainViewModel", "getGBusStopRouteList success");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("MainViewModel", "getGBusStopRouteList onError");
                    }
                })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
