package com.example.mybus.viewmodel;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
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
    // 서비스키 인코딩
    private static String serviceKey = "";
    private int total;
    static {
        try {
            serviceKey = URLDecoder.decode("7xKgSgAhOl%2FF9gxIzB20lcht%2BtM6G4MKRuw3arXF57DoSZftgzWzLrvcJNQIKn8mvv4UnoGSI5EzgAoxPI02yg%3D%3D", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Inject
    public SearchDetailViewModel(RetrofitRepository retrofitRepository, RetrofitGbusRepository retrofitGbusRepository){
        this.retrofitRepository = retrofitRepository;
        this.retrofitGbusService = retrofitGbusRepository;
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
                                    total  = busArrivalLists.size()-1;
                                    for (int i =0; i< busArrivalLists.size(); i++){
                                        getGBusRouteName(busArrivalLists.get(i).getRouteId());
                                    }
                                }
                                Collections.sort(busArrivalLists);

                            }
                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("kkang", "error on getGBusStopUidSchResult : " + e.getMessage());
                            }
                        })
        );
    }

    public void getGBusRouteName(String routeId){
        compositeDisposable.add(
                retrofitGbusService.schRouteId(serviceKey, routeId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<GBusRouteSearchResponse>() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onSuccess(@NonNull GBusRouteSearchResponse gBusRouteSearchResponse) {
                                total--;
                                if (gBusRouteSearchResponse != null){
                                    Log.d("kkang", " getroutename : " +  gBusRouteSearchResponse.getgBusStopSearchUidWrap().getgBusRouteList().get(0).getRouteName());
                                    busRouteMatchMap.put(routeId,  gBusRouteSearchResponse.getgBusStopSearchUidWrap().getgBusRouteList().get(0).getRouteName());
                                }

                                if (total == -1){
                                    setOrderList();
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("kkang", "error on getGBusRouteName : " + e.getMessage());
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
}
