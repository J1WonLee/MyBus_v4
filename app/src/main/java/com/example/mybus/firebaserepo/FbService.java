package com.example.mybus.firebaserepo;


import com.example.mybus.vo.LocalFav;
import com.example.mybus.vo.User;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

// 파이어 베이스 서비스
public interface FbService {
    void insert(User user);

    void insertFbFav(LocalFav localFav, String logInId);

    void deleteFbFab(String lfId, String logInId);

}


