package com.example.mybus.roomdb;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.apisearch.itemList.StopSchList;
import com.example.mybus.vo.ArrAlarmPref;
import com.example.mybus.vo.DataWithFavStopBus;
import com.example.mybus.vo.LocalFav;
import com.example.mybus.vo.LocalFavStopBus;
import com.example.mybus.vo.SchAlarmInfo;
import com.example.mybus.vo.User;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface BusDao {

    @Query("select * from USER_INFO")
    Single<User> getUser();

    @Insert
    Completable register(User user);

    @Query("delete from USER_INFO")
    Completable delete();

    @Query("update USER_INFO set is_Logout = 0 where user_tk =:user_tk")
    Completable logOut(String user_tk);

    @Query("update USER_INFO set is_Logout = 1 where user_tk =:user_tk")
    Completable logIn(String user_tk);
    
    // 최근 버스 검색
    @Insert
    Completable regitRecentBusSch(BusSchList busSchList);
    
    // 최근 정류장 검색
    @Insert
    Completable regitRecentStopSch(StopSchList stopSchList);

    // 최근 버스 검색 목록
    @Query("select distinct * from BUS_SEARCH_LIST ORDER BY search_order desc limit 0, 5")
    Single<List<BusSchList>> getRecentBusSchList();

    // 최근 정류장 검색 목록
    @Query("select distinct * from STOP_SEARCH_LIST ORDER BY search_order desc limit 0, 5")
    Single<List<StopSchList>> getRecentStopSchList();

    // 최근 검색 중복검색 시 업데이트 쿼리
    @Query("update BUS_SEARCH_LIST set search_order = (select MAX(search_order) from BUS_SEARCH_LIST) +1 WHERE busRouteId =:busId")
    Completable updateRecentBusSch(String busId);

    @Query("update STOP_SEARCH_LIST set search_order = (select MAX(search_order) from STOP_SEARCH_LIST) +1 WHERE stId =:stId")
    Completable updateRecentStopSch(String stId);

    // 최근 버스 검색 삭제
    @Query("delete from BUS_SEARCH_LIST")
    Completable deleteRecentBusSch();

    // 최근 정류장 검색 삭제
    @Query("delete from STOP_SEARCH_LIST")
    Completable deleteRecentStopSch();

    @Insert
    Completable regitFav(LocalFav localFav);

    @Insert
    Completable regitFavStopBus(LocalFavStopBus localFavStopBus);

    // 정류장 버스 즐겨찾기 목록 삭제
    @Query("delete from LOCAL_FAV_STOP_BUS where lfb_id =:lfbId and lfb_busId =:lbBisOd")
    Completable deleteFavStopBus(String lfbId, String lbBisOd);

    // 정류장 버스 즐겨찾기 전체 삭제
    @Query("delete from LOCAL_FAV_STOP_BUS where lfb_id =:lfbId")
    Completable deleteFavStopBusAll(String lfbId);

    // 즐겨찾기 목록 조회(전체)
    @Query("SELECT * FROM LOCAL_FAV order by lf_order desc")
    Single<List<LocalFav>> getLocalFavList();

    // 즐겨찾기 목록 조회(특정)
    @Query("SELECT * FROM LOCAL_FAV where lf_id =:lfId")
    Single<List<LocalFav>> getLocalFavIsSaved(String lfId);

    // 즐겨찾기 삭제
    @Query("delete FROM LOCAL_FAV WHERE LF_ID =:lfId")
    Completable deleteLocalFav(String lfId);

    @Transaction
    @Query("SELECT * FROM LOCAL_FAV order by lf_order desc")
    Single<List<DataWithFavStopBus>> getFavStopBus();

    @Transaction
    @Query("SELECT * FROM LOCAL_FAV where lf_isBus ='1' order by lf_order desc")
    Single<List<DataWithFavStopBus>> getFavStopBusForAlarm();

    @Query("SELECT * FROM LOCAL_FAV_STOP_BUS where lfb_id =:lsbId  order by lfb_busId")
    Single<List<LocalFavStopBus>> getLocalFavStopBusLists(String lsbId);

    @Query("SELECT count(lfb_id) FROM LOCAL_FAV_STOP_BUS where lfb_busId =:routeId")
    Single<Integer> isLocalFavStopBusSaved(String routeId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable updateAll(List<LocalFav> LocalFav);

    @Delete
    Completable deleteFavList(List<LocalFav> localFavList);

    // 즐겨찾기에 저장
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertFavAll(List<LocalFav> localFavs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    Completable insertFabFabAll(LocalFavStopBus localFavStopBus);
    Completable insertFabFabAll(List<LocalFavStopBus> localFavStopBusLists);

    @Query("DELETE FROM LOCAL_FAV")
    Completable deleteLocalFavAll();

    @Query("DELETE FROM LOCAL_FAV_STOP_BUS")
    Completable deleteLocalBusAll();

    @Insert
    Completable insertArrAlarm(ArrAlarmPref arrAlarmPref);

    @Query("SELECT * FROM LOCAL_ARR_ALARM")
    Single<ArrAlarmPref> getArrAlarm();

    @Query("DELETE FROM LOCAL_ARR_ALARM")
    Completable deleteArrAlarm();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertSchAlarm(SchAlarmInfo schAlarmInfo);

    @Query("SELECT * FROM SCH_ALARM")
    Single<List<SchAlarmInfo>> getSchAlarmList();

    @Delete
    Completable deleteSchAlarm(SchAlarmInfo schAlarmInfo);

    // 알람 목록에서 ON/OFF 한 알람들을 일괄적으로 수정한다.
    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable updateSchAlarm(List<SchAlarmInfo> schAlarmInfos);

    // 알람 수정에서 하나의 알람 정보를 수정한다.
    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable updateSchAlarm(SchAlarmInfo schAlarmInfo);

}
