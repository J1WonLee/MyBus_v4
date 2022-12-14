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
import com.example.mybus.firebaserepo.FbRepository;
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
    private FbRepository fbRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    // ?????? ????????? ?????? ?????? ??????
    public MutableLiveData<List<StopUidSchList>> stopUidSchList = new MutableLiveData<>();
    // ????????? ????????? ?????? ?????? ??????
    public MutableLiveData<List<BusArrivalList>> gbusUidSchList = new MutableLiveData<>();
    private List<BusArrivalList> busArrivalLists = new ArrayList<>();
    private Map<String, String> busRouteMatchMap = new HashMap<>();
    // ??????????????? ???????????? ??????
    public MutableLiveData<List<LocalFavStopBus>> localFabStopBusList = new MutableLiveData<>();
    public List<LocalFavStopBus> localFavStopBusList = new ArrayList<>();
    public MutableLiveData<Integer> listsState = new MutableLiveData<>();
    public MutableLiveData<Integer> isFavSaved = new MutableLiveData<>();

    // ???????????? ?????????
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
    public SearchDetailViewModel(RetrofitRepository retrofitRepository, RetrofitGbusRepository retrofitGbusRepository, BusRoomRepository busRoomRepository, FbRepository fbRepository){
        this.retrofitRepository = retrofitRepository;
        this.retrofitGbusService = retrofitGbusRepository;
        this.busRoomRepository = busRoomRepository;
        this.fbRepository = fbRepository;
    }

    // ????????? ?????? ???????????? ?????? ?????? (arsId)
    public void getSeoulStopUidSchResult(String arsId){
        compositeDisposable.add(
                  retrofitRepository.schStopUidv2( serviceKey ,arsId,"json")
                          .subscribeOn(Schedulers.newThread())
                          .observeOn(AndroidSchedulers.mainThread())
                          .subscribeWith(new DisposableSingleObserver<StopSearchUidWrap>() {
                              @Override
                              public void onSuccess(@NonNull StopSearchUidWrap stopSearchUidWrap) {
                                  if (stopSearchUidWrap.getStopSearchUid().getItemLists() != null){
                                      Collections.sort(stopSearchUidWrap.getStopSearchUid().getItemLists());
                                      stopUidSchList.setValue(stopSearchUidWrap.getStopSearchUid().getItemLists());
                                  }else{
                                      stopUidSchList.setValue(null);
                                  }

                              }
                              @Override
                              public void onError(@NonNull Throwable e) {
                                  Log.d("kkang", "error on getSeoulStopUidSchResult : " + e.getMessage());
                              }
                          })
        );
    }

    // ????????? ?????? ???????????? ?????? ?????? (????????? id)
    public void getGBusStopUidSchResult(String stationId){
        compositeDisposable.add(
                retrofitGbusService.schStopUid(serviceKey, stationId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<GBusStopSearchResponse>() {
                            @Override
                            public void onSuccess(@NonNull GBusStopSearchResponse gBusStopSearchResponse) {
//                                gbusUidSchList.setValue(gBusStopSearchResponse.getgBusStopSearchUidWrap().getBusArrivalListList());
                                if (gBusStopSearchResponse.getgBusStopSearchUidWrap() == null){
//                                    busArrivalLists = gBusStopSearchResponse.getgBusStopSearchUidWrap().getBusArrivalListList();
//                                    Collections.sort(busArrivalLists);
//                                    getGBusRouteName(busArrivalLists);
                                    gbusUidSchList.setValue(null);
                                }else{
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
                                if (gBusRouteSearchResponse.getgBusStopSearchUidWrap() != null){
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
            b.setRouteNm(busRouteMatchMap.getOrDefault(b.getRouteId(), "????????? ????????????"));
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

    public void getLocalFavIsSaved(String lfId){
        compositeDisposable.add(
                busRoomRepository.getLocalFavIsSaved(lfId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<LocalFav>>() {
                            @Override
                            public void onSuccess(@NonNull List<LocalFav> localFavs) {
                                if (localFavs != null){
                                    isFavSaved.setValue(localFavs.size());
                                }else{
                                    isFavSaved.setValue(-1);
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("SearchDetailViewModel", "getLocalFavIsSaved error msg :" + e.getMessage());
                            }
                        })

        );
    }

    public void deleteLocalFav(String lfId){
        busRoomRepository.deleteLocalFav(lfId);
    }


    // fb db ???????????? ??????
    public void insertFbFav(LocalFav localFav, String loginId){
        fbRepository.insertFbFav(localFav, loginId);
    }

    // fb db ???????????? ??????
    public void deleteFbFab(String lbId, String loginId){
        fbRepository.deleteFbFab(lbId, loginId);
    }

    public void insertFbStopFav(LocalFav localFav, LocalFavStopBus localFavStopBus, String loginId){
        fbRepository.insertFbStopFav(localFav, localFavStopBus, loginId);
    }

    public void deleteFbFabInStopDetail(String lfId, String loginId){
        fbRepository.deleteFbFabInStopDetail(lfId, loginId);
    }

    public void deleteFbStopFav(String lfId, String lfbid, String loginId){
        fbRepository.deleteFbStopFav(lfId, lfbid, loginId);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
