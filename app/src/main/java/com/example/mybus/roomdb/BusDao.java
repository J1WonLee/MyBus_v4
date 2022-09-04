package com.example.mybus.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.apisearch.itemList.StopSchList;
import com.example.mybus.vo.User;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
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

    // 최금 검색 중복검색 시 업데이트 쿼리
    @Query("update BUS_SEARCH_LIST set search_order = (select MAX(search_order) from BUS_SEARCH_LIST) +1 WHERE busRouteId =:busId")
    Completable updateRecentBusSch(String busId);

    @Query("update STOP_SEARCH_LIST set search_order = (select MAX(search_order) from STOP_SEARCH_LIST) +1 WHERE stId =:stId")
    Completable updateRecentStopSch(String stId);
}
