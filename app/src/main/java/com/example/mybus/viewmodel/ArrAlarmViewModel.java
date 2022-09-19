package com.example.mybus.viewmodel;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mybus.apisearch.GbusWrapper.GBusRouteArriveInfoResponse;
import com.example.mybus.apisearch.GbusWrapper.GBusRouteArriveInfoWrap;
import com.example.mybus.apisearch.itemList.ArrInfoByRouteList;
import com.example.mybus.apisearch.itemList.GBusRouteArriveInfoList;
import com.example.mybus.apisearch.wrapper.ArrInfoByRouteWrap;
import com.example.mybus.retrofitrepo.RetrofitGbusRepository;
import com.example.mybus.retrofitrepo.RetrofitRepository;
import com.example.mybus.roomrepo.BusRoomRepository;
import com.example.mybus.vo.ArrAlarmPref;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class ArrAlarmViewModel extends ViewModel {
    private RetrofitRepository retrofitRepository;
    private RetrofitGbusRepository retrofitGbusRepository;
    private BusRoomRepository busRoomRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ArrAlarmPref arrAlarm;

    public MutableLiveData<ArrInfoByRouteList> arrInfoData = new MutableLiveData<>();
    public MutableLiveData<GBusRouteArriveInfoList> arrGbusInfoData = new MutableLiveData<>();
    public MutableLiveData<ArrAlarmPref> arrAlarmPrefMutableLiveData = new MutableLiveData<>();

    private static String serviceKey = "";
    static {
        try {
            serviceKey = URLDecoder.decode("7xKgSgAhOl%2FF9gxIzB20lcht%2BtM6G4MKRuw3arXF57DoSZftgzWzLrvcJNQIKn8mvv4UnoGSI5EzgAoxPI02yg%3D%3D", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Inject
    public ArrAlarmViewModel(RetrofitRepository retrofitRepository, RetrofitGbusRepository retrofitGbusRepository, BusRoomRepository busRoomRepository) {
        this.retrofitRepository = retrofitRepository;
        this.retrofitGbusRepository = retrofitGbusRepository;
        this.busRoomRepository = busRoomRepository;
    }

    public String getServiceKey(){
        return serviceKey;
    }

    public void getArrInfoByRoute( String stId, String routeId, String ord){
        compositeDisposable.add(
                retrofitRepository.getArrInfoByRouteInit(serviceKey, stId, routeId,ord, "json")
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ArrInfoByRouteWrap>() {
                            @Override
                            public void onSuccess(@NonNull ArrInfoByRouteWrap arrInfoByRouteWrap) {
                                if (arrInfoByRouteWrap != null){
                                    arrInfoData.setValue(arrInfoByRouteWrap.getArrInfoByRoute().getArrInfoByRouteLists().get(0));
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("ArrAlarmViewModel", "getArrInfoByRoute error!!" + e.getMessage());
                            }
                        })
        );
    }

    public void getGBusArrInfoByRoute(String stid, String routeId, String ord){
        compositeDisposable.add(
                retrofitGbusRepository.getGBusArrInfoByRouteInit(serviceKey, stid, routeId, ord)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<GBusRouteArriveInfoResponse>() {
                            @Override
                            public void onSuccess(@NonNull GBusRouteArriveInfoResponse gBusRouteArriveInfoResponse) {
                               if (gBusRouteArriveInfoResponse.getgBusLocationWrap() != null){
                                   arrGbusInfoData.setValue(gBusRouteArriveInfoResponse.getgBusLocationWrap().getBusArriveInfoLists().get(0));
                               }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("ArrAlarmViewModel", "getGBusArrInfoByRoute error!!" + e.getMessage());
                            }
                        })
        );
    }

    public void insertArrAlarm(ArrAlarmPref arrAlarmPref){
        busRoomRepository.insertArrAlarm(arrAlarmPref);
        arrAlarmPrefMutableLiveData.setValue(arrAlarmPref);
    }

//    public void deleteArrAlarm(){
//        Completable completable = busRoomRepository.deleteArrAlarm();
//        completable.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        () -> arrAlarmPrefMutableLiveData.setValue(null),
//                        error -> Log.d("ArrAlarmViewModel", " deleteArrAlarm failed!"+error.getMessage())
//                );
//    }

    public void getArrAlarm(){
        compositeDisposable.add(
                busRoomRepository.getArrAlarm()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ArrAlarmPref>() {
                            @Override
                            public void onSuccess(@NonNull ArrAlarmPref arrAlarmPref) {
                                if (arrAlarmPref != null){
                                    arrAlarm = arrAlarmPref;
                                    arrAlarmPrefMutableLiveData.setValue(arrAlarm);
                                }else{
                                    arrAlarm = null;
                                    arrAlarmPrefMutableLiveData.setValue(arrAlarm);
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("ArrAlarmViewModel", "getArrAlarm error!!" + e.getMessage());
                            }
                        })
        );
    }

    public void deleteArrAlarm(){
        busRoomRepository.deleteArrAlarm();
        arrAlarmPrefMutableLiveData.setValue(null);
    }
//    public void getArrInfoByRoute( String stId, String routeId, String ord){
//        compositeDisposable.add(
//                Observable.interval(2, TimeUnit.SECONDS)
//                        .flatMap(list -> retrofitRepository.getArrInfoByRoute(serviceKey, stId, routeId, ord, "json"))
//                        .repeat()
//                        .subscribeOn(Schedulers.newThread())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<ArrInfoByRouteWrap>() {
//                            @Override
//                            public void accept(ArrInfoByRouteWrap arrInfoByRouteWrap) throws Throwable {
//                                if (arrInfoByRouteWrap != null){
//                                    Log.d("ArrAlarmViewModel", "getArrInfoByRoute sisze ::::::::" + arrInfoByRouteWrap.getArrInfoByRoute().getArrInfoByRouteLists().size());
//                                }
//                            }
//                        }, error -> Log.d("ArrAlarmViewModel", "getArrInfoByRoute error!!" + error.getMessage()))
//        );
}
