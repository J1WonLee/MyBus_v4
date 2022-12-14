package com.example.mybus.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mybus.apisearch.GbusWrapper.GBusLocationResponse;
import com.example.mybus.apisearch.GbusWrapper.GBusLocationWrap;
import com.example.mybus.apisearch.GbusWrapper.GBusRouteSearchResponse;
import com.example.mybus.apisearch.GbusWrapper.GBusRouteStationResponse;
import com.example.mybus.apisearch.GbusWrapper.GBusRouteStationWrap;
import com.example.mybus.apisearch.itemList.BusPosList;
import com.example.mybus.apisearch.itemList.GBusLocationList;
import com.example.mybus.apisearch.itemList.GBusRouteList;
import com.example.mybus.apisearch.itemList.GBusRouteStationList;
import com.example.mybus.apisearch.itemList.RouteInfoList;
import com.example.mybus.apisearch.itemList.StationByRouteList;
import com.example.mybus.apisearch.wrapper.BusPositionSearchWrap;
import com.example.mybus.apisearch.wrapper.RouteInfoWrap;
import com.example.mybus.apisearch.wrapper.RouteStationWrap;
import com.example.mybus.firebaserepo.FbRepository;
import com.example.mybus.retrofitrepo.RetrofitGbusRepository;
import com.example.mybus.retrofitrepo.RetrofitRepository;
import com.example.mybus.roomrepo.BusRoomRepository;
import com.example.mybus.vo.LocalFav;
import com.example.mybus.vo.LocalFavStopBus;

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
    private FbRepository fbRepository;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RouteStationWrap routeStationWrap = new RouteStationWrap();
    private BusPositionSearchWrap busPositionSearchWrap;
    private GBusRouteStationWrap gBusRouteStationWrap;
    private GBusLocationWrap gBusLocationWrap;


    public MutableLiveData<List<StationByRouteList>> stationRouteList = new MutableLiveData<>();
    public MutableLiveData<List<BusPosList>> busPosList = new MutableLiveData<>();
    public MutableLiveData<List<GBusRouteStationList>> gBusStationList = new MutableLiveData<>();
    public MutableLiveData<List<GBusLocationList>> gBusLocationList = new MutableLiveData<>();
    public MutableLiveData<Integer> isFavSaved = new MutableLiveData<>();
    public MutableLiveData<List<RouteInfoList>> routeInfoList = new MutableLiveData<>();
    public MutableLiveData<List<GBusRouteList>> gBusRouteInfoList = new MutableLiveData<>();

    private static String serviceKey = "";
    static {
        try {
            serviceKey = URLDecoder.decode("7xKgSgAhOl%2FF9gxIzB20lcht%2BtM6G4MKRuw3arXF57DoSZftgzWzLrvcJNQIKn8mvv4UnoGSI5EzgAoxPI02yg%3D%3D", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Inject
    public BusRouteSearchDetailViewModel(BusRoomRepository busRoomRepository, RetrofitRepository retrofitRepository, RetrofitGbusRepository retrofitGbusRepository, FbRepository fbRepository) {
        this.busRoomRepository = busRoomRepository;
        this.retrofitRepository = retrofitRepository;
        this.retrofitGbusRepository = retrofitGbusRepository;
        this.fbRepository = fbRepository;
    }

    // ?????? ????????? ????????? ????????? ????????? ????????????.
    public void getStationRouteList(String routeId){
        compositeDisposable.add(
                retrofitRepository.getRouteStationList(serviceKey, routeId, "json")
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<RouteStationWrap>() {
                            @Override
                            public void onSuccess(@NonNull RouteStationWrap routeStationWraps) {
                                Log.d("kkang", "BusRouteSearchDetailViewModel getStationRouteList Success");
                                if (routeStationWraps != null){
                                    routeStationWrap = routeStationWraps;
                                    stationRouteList.setValue(routeStationWrap.getRouteByStation().getStationByRouteLists());
                                }else{
                                    stationRouteList.setValue(null);
                                }

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("kkang", "BusRouteSearchDetailViewModel getStationRouteList error : " + e.getMessage());
                            }
                        })
        );
    }

    // ?????? ????????? ?????? ?????? ??????(??????)
    public void getBusPositionList(String routeId){
        compositeDisposable.add(
                retrofitRepository.getBusPositionList(serviceKey, routeId, "json")
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<BusPositionSearchWrap>() {
                            @Override
                            public void onSuccess(@NonNull BusPositionSearchWrap busPositionSearchWraps) {
                                Log.d("BusRouteDetailActivity", "BusRouteSearchDetailViewModel getBusPositionList onSuccess ");
                                if (busPositionSearchWraps.getBusPositionSearch().getItemList() != null ){
                                    busPositionSearchWrap = busPositionSearchWraps;
                                    Collections.sort(busPositionSearchWrap.getBusPositionSearch().getItemList());
                                    busPosList.setValue(busPositionSearchWrap.getBusPositionSearch().getItemList());
                                }else{
                                    busPosList.setValue(null);
                                }

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("kkang", "BusRouteSearchDetailViewModel getBusPositionList error : " + e.getMessage());
                            }
                        })
        );
    }

    // ????????? ????????? ????????? ????????? ????????????.
    public void getGbusStopList(String routeId){
        compositeDisposable.add(
            retrofitGbusRepository.getGbusRouteStationList(serviceKey, routeId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<GBusRouteStationResponse>() {
                        @Override
                        public void onSuccess(@NonNull GBusRouteStationResponse gBusRouteStationResponse) {
                            Log.d("kkang", "BusRouteSearchDetailViewModel getGbusStopList onSuccess ");
                            if (gBusRouteStationResponse.getgBusRouteStationWrap() != null){
                                gBusRouteStationWrap = gBusRouteStationResponse.getgBusRouteStationWrap();
//                                Collections.sort(gBusRouteStationWrap.getBusRouteStationList());
                                gBusStationList.setValue( gBusRouteStationWrap.getBusRouteStationList());
                            }else{
                                gBusStationList.setValue(null);
                            }

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.d("kkang", "BusRouteSearchDetailViewModel getGbusStopList error : " + e.getMessage());
                            gBusStationList.setValue(null);
                        }
                    })
        );
    }

    // ????????? ????????? ??? ????????? ????????????.
    public void getGbusLocationList(String routeId){
        compositeDisposable.add(
            retrofitGbusRepository.getGbusPositionList(serviceKey, routeId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<GBusLocationResponse>() {
                        @Override
                        public void onSuccess(@NonNull GBusLocationResponse gBusLocationResponse) {
                            Log.d("kkang", "BusRouteSearchDetailViewModel getGbusLocationList onSuccess ");
                            if (gBusLocationResponse.getgBusLocationWrap() != null){
                                gBusLocationWrap = gBusLocationResponse.getgBusLocationWrap();
                                Collections.sort(gBusLocationWrap.getBusLocationList());
                                gBusLocationList.setValue(gBusLocationWrap.getBusLocationList());
                            }else{
                                gBusLocationList.setValue(null);
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.d("kkang", "BusRouteSearchDetailViewModel getGbusLocationList error : " + e.getMessage());
                            gBusLocationList.setValue(null);
                        }
                    })
        );
    }

    // ???????????? ??????
    public void regitFav(LocalFav localFav){
        busRoomRepository.regitFav(localFav);
    }

    // ???????????? ?????? ????????? ????????????.
    public void getLocalFavIsSaved(String lfId){
        compositeDisposable.add(
                busRoomRepository.getLocalFavIsSaved(lfId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<LocalFav>>() {
                            @Override
                            public void onSuccess(@NonNull List<LocalFav> localFavs) {
                                if (localFavs != null && localFavs.size() > 0){
                                    isFavSaved.setValue(localFavs.size());
                                }
//                                else{
//                                    getLocalFavStopBus(lfId);
//                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("BusRouteSearchViewModel", "getLocalFavIsSaved error msg :" + e.getMessage());
                            }
                        })

        );
    }

    // ???????????? ?????? ????????? ????????????.
    public void getLocalFavStopBus(String routeId){
        compositeDisposable.add(
                busRoomRepository.isLocalFavStopBusSaved(routeId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Integer>() {
                            @Override
                            public void onSuccess(@NonNull Integer integer) {
                                isFavSaved.setValue(integer);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                isFavSaved.setValue(-1);
                                Log.d("BusRouteSearchViewModel", "getLocalFavStopBus error msg :" + e.getMessage());
                            }
                        })
        );
    }

    // ???????????? ??????
    public void deleteLocalFav(String lfId){
        busRoomRepository.deleteLocalFav(lfId);
    }

    // ????????????????????? ????????? ?????? ??????(??????)
    public void getRouteInfo(String routeId){
        compositeDisposable.add(
                retrofitRepository.getRouteInfo(serviceKey, routeId, "json")
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<RouteInfoWrap>() {
                            @Override
                            public void onSuccess(@NonNull RouteInfoWrap routeInfoWrap) {
                                if (routeInfoWrap.getRouteInfo() != null){
                                    routeInfoList.setValue( routeInfoWrap.getRouteInfo().getRouteInfoLists());
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("BusRouteSearchViewModel", "getRouteInfo error : " + e.getMessage());
                            }
                        })
        );
    }

    // // ????????????????????? ????????? ?????? ??????(??????)
    public void getGbusRouteInfo(String routeId){
        compositeDisposable.add(
                retrofitGbusRepository.getGbusRouteInfo(serviceKey, routeId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<GBusRouteSearchResponse>() {
                            @Override
                            public void onSuccess(@NonNull GBusRouteSearchResponse gBusRouteSearchResponse) {
                                if (gBusRouteSearchResponse.getgBusStopSearchUidWrap() != null){
                                    gBusRouteInfoList.setValue(gBusRouteSearchResponse.getgBusStopSearchUidWrap().getgBusRouteList());
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("BusRouteSearchViewModel", "getGbusRouteInfo error : " + e.getMessage());
                            }
                        })
        );
    }

    public void insertFbFav(LocalFav localFav, String logInId){
        fbRepository.insertFbFav(localFav, logInId);
    }

    public void deleteFbFab(String lfbId, String logInId){
        fbRepository.deleteFbFab(lfbId, logInId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
