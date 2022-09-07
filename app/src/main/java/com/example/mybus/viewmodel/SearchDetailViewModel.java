package com.example.mybus.viewmodel;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.mybus.apisearch.GbusWrapper.GBusRouteSearchResponse;
import com.example.mybus.apisearch.GbusWrapper.GBusRouteSearchWrap;
import com.example.mybus.apisearch.itemList.BusArrivalList;
import com.example.mybus.apisearch.itemList.StopUidSchList;
import com.example.mybus.apisearch.GbusWrapper.GBusStopSearchResponse;
import com.example.mybus.apisearch.wrapper.StopSearchUidWrap;
import com.example.mybus.retrofitrepo.RetrofitGbusRepository;
import com.example.mybus.retrofitrepo.RetrofitGbusService;
import com.example.mybus.retrofitrepo.RetrofitRepository;
import com.example.mybus.roomrepo.BusRoomRepository;
import com.example.mybus.vo.DataWithFavStopBus;
import com.example.mybus.vo.LocalFav;
import com.example.mybus.vo.LocalFavStopBus;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class SearchDetailViewModel extends ViewModel {
    private RetrofitRepository retrofitRepository;
    private RetrofitGbusService retrofitGbusService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    // 서울 정류장 버스 도착 조회
    public MutableLiveData<List<StopUidSchList>> stopUidSchList = new MutableLiveData<>();
    // 경기도 정류장 버스 도착 조회
    public MutableLiveData<List<BusArrivalList>> gbusUidSchList = new MutableLiveData<>();
    private List<BusArrivalList> busArrivalLists = new ArrayList<>();
    private Map<String, String> busRouteMatchMap = new HashMap<>();
    // 즐겨찾기한 정류장들 목록
    public MutableLiveData<List<LocalFavStopBus>> localFabStopBusList = new MutableLiveData<>();

    public List<LocalFavStopBus> localFavStopBusList = new ArrayList<>();

    public MutableLiveData<Integer> listsState = new MutableLiveData<>();

    // 서비스키 인코딩
    private static String serviceKey = "";
    private BusRoomRepository busRoomRepository;
    static {
        try {
            serviceKey = URLDecoder.decode("7xKgSgAhOl%2FF9gxIzB20lcht%2BtM6G4MKRuw3arXF57DoSZftgzWzLrvcJNQIKn8mvv4UnoGSI5EzgAoxPI02yg%3D%3D", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Inject
    public SearchDetailViewModel(RetrofitRepository retrofitRepository, RetrofitGbusRepository retrofitGbusRepository, BusRoomRepository busRoomRepository){
        this.retrofitRepository = retrofitRepository;
        this.retrofitGbusService = retrofitGbusRepository;
        this.busRoomRepository = busRoomRepository;
    }

    // 서울시 버스 정류장인 경우 검색 (arsId)
    public void getSeoulStopUidSchResult(String arsId){
        compositeDisposable.add(
                  retrofitRepository.schStopUidv2( serviceKey ,arsId,"json")
                          .subscribeOn(Schedulers.newThread())
                          .observeOn(AndroidSchedulers.mainThread())
                          .subscribeWith(new DisposableSingleObserver<StopSearchUidWrap>() {
                              @Override
                              public void onSuccess(@NonNull StopSearchUidWrap stopSearchUidWrap) {
                                  Collections.sort(stopSearchUidWrap.getStopSearchUid().getItemLists());
                                  stopUidSchList.setValue(stopSearchUidWrap.getStopSearchUid().getItemLists());
                              }
                              @Override
                              public void onError(@NonNull Throwable e) {
                                  Log.d("kkang", "error on getSeoulStopUidSchResult : " + e.getMessage());
                              }
                          })
        );
    }

    // 경기도 버스 정류장인 경우 검색 (정류소 id)
    public void getGBusStopUidSchResult(String stationId){
        compositeDisposable.add(
                retrofitGbusService.schStopUid(serviceKey, stationId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<GBusStopSearchResponse>() {
                            @Override
                            public void onSuccess(@NonNull GBusStopSearchResponse gBusStopSearchResponse) {
//                                gbusUidSchList.setValue(gBusStopSearchResponse.getgBusStopSearchUidWrap().getBusArrivalListList());
                                if (gBusStopSearchResponse != null){
                                    busArrivalLists = gBusStopSearchResponse.getgBusStopSearchUidWrap().getBusArrivalListList();
                                    Collections.sort(busArrivalLists);
                                    getGBusRouteName(busArrivalLists);
                                }
                            }
                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("kkang", "error on getGBusStopUidSchResult : " + e.getMessage());
                            }
                        })
        );
    }

    public void getGBusRouteName(List<BusArrivalList> busArrivalLists){
        compositeDisposable.add(
                Observable.fromIterable(busArrivalLists)
                        .flatMap(item -> retrofitGbusService.schRouteIdv2(serviceKey, item.getRouteId()))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<GBusRouteSearchResponse>() {
                            @Override
                            public void onNext(@NonNull GBusRouteSearchResponse gBusRouteSearchResponse) {
                                Log.d("kkang", "onNext on serchdetaivm getGBusRouteName" );
                                if (gBusRouteSearchResponse != null){
                                    busRouteMatchMap.put(gBusRouteSearchResponse.getgBusStopSearchUidWrap().getgBusRouteList().get(0).getRouteId(), gBusRouteSearchResponse.getgBusStopSearchUidWrap().getgBusRouteList().get(0).getRouteName());
                                }
                            }

                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("kkang", "error on serchdetaivm getGBusRouteName method error : " + e.getMessage());
                                setOrderList();
                            }

                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onComplete() {
                                setOrderList();
                            }
                        })
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setOrderList(){
        for(BusArrivalList b :busArrivalLists ){
            b.setRouteNm(busRouteMatchMap.getOrDefault(b.getRouteId(), "정보가 없습니다"));
        }
        gbusUidSchList.setValue(busArrivalLists);
    }

    public void regitFav(LocalFav localFav){
        busRoomRepository.regitFav(localFav);
    }

    public void regitFavList(LocalFav localFav, LocalFavStopBus localFavStopBus){
        busRoomRepository.regitFavFromItemList(localFav, localFavStopBus);
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
                                    try {
                                        Log.d("kkang", "datawithtiems : size : "  + dataWithFavStopBuses.get(0).localFavStopBusList.size());
                                    }catch(Exception e){
                                        Log.d("kkang", "searchdetailvm getfavstopbus get lists error : " + e.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("kkang", "error on searchdetailvm getfavstopbus : " + e.getMessage());
                            }
                        })
        );
    }

    public void getFavStopBusList(String lsbId){
        compositeDisposable.add(
                busRoomRepository.getFavStopBusLists(lsbId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<LocalFavStopBus>>() {
                            @Override
                            public void onSuccess(@NonNull List<LocalFavStopBus> localFavStopBuses) {
                                if (localFavStopBuses != null){
                                    localFabStopBusList.setValue(localFavStopBuses);
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("kkang", "searchdetail vm error on getFavStopBusList : " + e.getMessage());
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
