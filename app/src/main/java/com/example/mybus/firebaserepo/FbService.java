package com.example.mybus.firebaserepo;


import com.example.mybus.vo.LocalFav;
import com.example.mybus.vo.LocalFavStopBus;
import com.example.mybus.vo.User;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

// 파이어 베이스 서비스
public interface FbService {
    void insert(User user);

    void insertFbFav(LocalFav localFav, String loginId);

    void deleteFbFab(String lfId, String loginId);

    void insertFbStopFav(LocalFav localFav, LocalFavStopBus localFavStopBus, String loginId);

    void deleteFbStopFav(String lfId, String lfbId, String loginId);

    void deleteFbFabInStopDetail(String lfId, String loginId);

}


