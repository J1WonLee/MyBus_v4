package com.example.mybus.roomrepo;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.apisearch.itemList.StopSchList;
import com.example.mybus.roomdb.BusDao;
import com.example.mybus.vo.DataWithFavStopBus;
import com.example.mybus.vo.LocalFav;
import com.example.mybus.vo.LocalFavStopBus;
import com.example.mybus.vo.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BusRoomRepository {
    private BusDao busDao;
    public MutableLiveData<Integer> localFavSize;


    public BusRoomRepository(BusDao busDao) {
        this.busDao = busDao;
    }

    // 회원 등록
    public void register(User user){
        Completable completable = busDao.register(user);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("BusRoomRepository", "insert success"),
                        error -> logIn(user.getUser_tk())
                );
    }

    // 로그아웃 시 삭제해줌
    public void delete(){
        Completable completable = busDao.delete();
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("BusRoomRepository", "delete success"),
                        error -> Log.d("BusRoomRepository", error.getMessage())
                );
    }

    public void logOut(String user_tk){
        Completable completable = busDao.logOut(user_tk);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("BusRoomRepository", "out success"),
                        error -> Log.d("BusRoomRepository", error.getMessage())
                );
    }

    // 로그인
    public void logIn(String user_tk){
        Completable completable = busDao.logIn(user_tk);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("BusRoomRepository", "again in success"),
                        error -> Log.d("BusRoomRepository", error.getMessage())
                );
    }

    public Single<User> getUser(){
        return busDao.getUser();
    }

    // 최근 검색 버스
    public void regitRecentBusSch(BusSchList busSchList){
        Completable completable = busDao.regitRecentBusSch(busSchList);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> updateRecentBusSchList(busSchList.getBusRouteId()),
                        error -> updateRecentBusSchList(busSchList.getBusRouteId())
                );
    }
    
    // 최근 검색 정류장
    public void regitRecentStopSch(StopSchList stopSchList){
        Completable completable = busDao.regitRecentStopSch(stopSchList);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> updateRecentStopSchList(stopSchList.getStId()),
                        error -> updateRecentStopSchList(stopSchList.getStId())
                );
    }
    
    // 최근 검색 목록 불러오기 버스
    public Single<List<BusSchList>> getRecentBusSchList(){
        return busDao.getRecentBusSchList();
    }

    // 최근 검색 목록 불러오기 정류장
    public Single<List<StopSchList>> getRecentStopSchList(){
        return busDao.getRecentStopSchList();
    }
    
    // 최근 검색어 수정 버스
    public void updateRecentBusSchList(String busId){
        Completable completable = busDao.updateRecentBusSch(busId);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("BusRoomRepository", "update recentbusschlist"),
                        error -> Log.d("BusRoomRepository", error.getMessage() +" error on update recentbusschlist")
                );
    }

    // 최근 검색어 수정 정류장
    public void updateRecentStopSchList(String stId){
        Completable completable = busDao.updateRecentStopSch(stId);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("BusRoomRepository", "update recentbusschlist"),
                        error -> Log.d("BusRoomRepository", error.getMessage() +" error on update recentbusschlist")
                );
    }

    // 즐겨찾기 추가 정류장
    public void regitFav(LocalFav localFav){
        Completable completable = busDao.regitFav(localFav);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("BusRoomRepository", "busroomrepository regitFav success"),
                        error -> Log.d("BusRoomRepository", "busroomrepository regitFav error message : " + error.getMessage())
                );
    }

    public void regitFavFromItemList(LocalFav localFav, LocalFavStopBus localFavStopBus){
        Completable completable = busDao.regitFav(localFav);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> regitFavStopBus(localFavStopBus),
                        error -> regitFavStopBus(localFavStopBus)
                );
    }

    public void regitFavStopBus(LocalFavStopBus localFavStopBus){
        Completable completable = busDao.regitFavStopBus(localFavStopBus);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("BusRoomRepository", "busroomrepository regitFavStopBus success"),
                        error -> deleteFavStopBus(localFavStopBus.getLfb_id(), localFavStopBus.getLfb_busId())
                );

    }

    public void deleteFavStopBus(String lfbId, String lfBusId){
        Completable completable = busDao.deleteFavStopBus(lfbId, lfBusId);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("BusRoomRepository", "deleteFavStopBus success!"),
                        error -> Log.d("BusRoomRepository", "delete failed!" + error.getMessage())
                );
    }

    public void deleteLocalFav(String lfId){
        Completable completable = busDao.deleteLocalFav(lfId);
        completable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("BusRoomRepository", "deletelocalFav success"),
                        error -> Log.d("BusRoomRepository", "delete localfav failed!"+error.getMessage())
                );
    }

    public Single<List<LocalFav>> getLocalFavIsSaved(String lfId){
        return busDao.getLocalFavIsSaved(lfId);
    }

    public Single<List<DataWithFavStopBus>> getFavStopBus(){
        return busDao.getFavStopBus();
    }

    public Single<List<LocalFavStopBus>> getFavStopBusLists(String lsbId){
        return busDao.getLocalFavStopBusLists(lsbId);
    }

    public Single<List<LocalFav>> getLocalFavList(){
        return busDao.getLocalFavList();
    }

}
