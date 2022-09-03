package com.example.mybus.roomrepo;

import android.util.Log;

import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.apisearch.itemList.StopSchList;
import com.example.mybus.roomdb.BusDao;
import com.example.mybus.vo.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BusRepository {
    private BusDao busDao;


    public BusRepository(BusDao busDao) {
        this.busDao = busDao;
    }

    // 회원 등록
    public void register(User user){
        Completable completable = busDao.register(user);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("kkang", "insert success"),
                        error -> logIn(user.getUser_tk())
                );
    }

    // 로그아웃 시 삭제해줌
    public void delete(){
        Completable completable = busDao.delete();
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("kkang", "delete success"),
                        error -> Log.d("kkang", error.getMessage())
                );
    }

    public void logOut(String user_tk){
        Completable completable = busDao.logOut(user_tk);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("kkang", "out success"),
                        error -> Log.d("kkang", error.getMessage())
                );
    }

    public void logIn(String user_tk){
        Completable completable = busDao.logIn(user_tk);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("kkang", "again in success"),
                        error -> Log.d("kkang", error.getMessage())
                );
    }

    public Single<User> getUser(){
        return busDao.getUser();
    }

    public void regitRecentBusSch(BusSchList busSchList){
        Completable completable = busDao.regitRecentBusSch(busSchList);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> updateRecentBusSchList(busSchList.getBusRouteId()),
                        error -> updateRecentBusSchList(busSchList.getBusRouteId())
                );
    }

    public void regitRecentStopSch(StopSchList stopSchList){
        Completable completable = busDao.regitRecentStopSch(stopSchList);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> updateRecentStopSchList(stopSchList.getStId()),
                        error -> updateRecentStopSchList(stopSchList.getStId())
                );
    }

    public Single<List<BusSchList>> getRecentBusSchList(){
        return busDao.getRecentBusSchList();
    }

    public Single<List<StopSchList>> getRecentStopSchList(){
        return busDao.getRecentStopSchList();
    }

    public void updateRecentBusSchList(String busId){
        Completable completable = busDao.updateRecentBusSch(busId);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("kkang", "update recentbusschlist"),
                        error -> Log.d("kkang", error.getMessage() +" error on update recentbusschlist")
                );
    }

    public void updateRecentStopSchList(String stId){
        Completable completable = busDao.updateRecentStopSch(stId);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("kkang", "update recentbusschlist"),
                        error -> Log.d("kkang", error.getMessage() +" error on update recentbusschlist")
                );
    }

}
