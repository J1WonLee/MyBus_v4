package com.example.mybus.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mybus.apisearch.itemList.BusPosList;
import com.example.mybus.apisearch.itemList.StationByRouteList;
import com.example.mybus.apisearch.wrapper.BusPositionSearchWrap;
import com.example.mybus.apisearch.wrapper.RouteStationWrap;
import com.example.mybus.retrofitrepo.RetrofitGbusRepository;
import com.example.mybus.retrofitrepo.RetrofitRepository;
import com.example.mybus.roomrepo.BusRoomRepository;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class BusRouteSearchDetailViewModel extends ViewModel {
    private BusRoomRepository busRoomRepository;
    private RetrofitRepository retrofitRepository;
    private RetrofitGbusRepository retrofitGbusRepository;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RouteStationWrap routeStationWrap = new RouteStationWrap();
    private BusPositionSearchWrap busPositionSearchWrap;


    public MutableLiveData<List<StationByRouteList>> stationRouteList = new MutableLiveData<>();
    public MutableLiveData<List<BusPosList>> busPosList = new MutableLiveData<>();

    private static String serviceKey = "";
    static {
        try {
            serviceKey = URLDecoder.decode("7xKgSgAhOl%2FF9gxIzB20lcht%2BtM6G4MKRuw3arXF57DoSZftgzWzLrvcJNQIKn8mvv4UnoGSI5EzgAoxPI02yg%3D%3D", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Inject
    public BusRouteSearchDetailViewModel(BusRoomRepository busRoomRepository, RetrofitRepository retrofitRepository, RetrofitGbusRepository retrofitGbusRepository) {
        this.busRoomRepository = busRoomRepository;
        this.retrofitRepository = retrofitRepository;
        this.retrofitGbusRepository = retrofitGbusRepository;
    }

    // 해당 노선에 속하는 정류장 목록을 불러온다.
    public void getStationRouteList(String routeId){
        compositeDisposable.add(
                retrofitRepository.getRouteStationList(serviceKey, routeId, "json")
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<RouteStationWrap>() {
                            @Override
                            public void onSuccess(@NonNull RouteStationWrap routeStationWraps) {
                                Log.d("kkang", "BusRouteSearchDetailViewModel getStationRouteList Success");
                                routeStationWrap = routeStationWraps;
                                stationRouteList.setValue(routeStationWrap.getRouteByStation().getStationByRouteLists());
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("kkang", "BusRouteSearchDetailViewModel getStationRouteList error : " + e.getMessage());
                            }
                        })
        );
    }

    // 해당 버스의 현재 위치 조회(서울)
    public void getBusPositionList(String routeId){
        compositeDisposable.add(
                retrofitRepository.getBusPositionList(serviceKey, routeId, "json")
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<BusPositionSearchWrap>() {
                            @Override
                            public void onSuccess(@NonNull BusPositionSearchWrap busPositionSearchWraps) {
                                Log.d("kkang", "BusRouteSearchDetailViewModel getBusPositionList onSuccess ");
                                busPositionSearchWrap = busPositionSearchWraps;
                                Collections.sort(busPositionSearchWrap.getBusPositionSearch().getItemList());
                                busPosList.setValue(busPositionSearchWrap.getBusPositionSearch().getItemList());
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("kkang", "BusRouteSearchDetailViewModel getBusPositionList error : " + e.getMessage());
                            }
                        })
        );
    }




}
