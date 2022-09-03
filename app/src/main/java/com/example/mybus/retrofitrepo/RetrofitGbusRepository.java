package com.example.mybus.retrofitrepo;

import com.example.mybus.apisearch.msgBody.GbusBusLocation;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class RetrofitGbusRepository implements RetrofitGbusService {
    RetrofitGbusService retrofitGbusService;

    @Inject
    public RetrofitGbusRepository(RetrofitGbusService retrofitGbusService) {
        this.retrofitGbusService = retrofitGbusService;
    }

    @Override
    public Single<GbusBusLocation> getRemainSeat(String serviceKey, String routeId) {
        return null;
    }
}
