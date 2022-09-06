package com.example.mybus.retrofitrepo;

import com.example.mybus.apisearch.GbusWrapper.GBusRouteSearchResponse;
import com.example.mybus.apisearch.GbusWrapper.GBusRouteSearchWrap;
import com.example.mybus.apisearch.msgBody.GBusBusLocation;
import com.example.mybus.apisearch.GbusWrapper.GBusStopSearchResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class RetrofitGbusRepository implements RetrofitGbusService {
    RetrofitGbusService retrofitGbusService;

    @Inject
    public RetrofitGbusRepository(RetrofitGbusService retrofitGbusService) {
        this.retrofitGbusService = retrofitGbusService;
    }

    @Override
    public Single<GBusBusLocation> getRemainSeat(String serviceKey, String routeId) {
        return null;
    }

    @Override
    public Single<GBusStopSearchResponse> schStopUid(String serviceKey, String stationId) {
        return retrofitGbusService.schStopUid(serviceKey, stationId);
    }

    @Override
    public Single<GBusRouteSearchResponse> schRouteId(String serviceKey, String routeId) {
        return retrofitGbusService.schRouteId(serviceKey, routeId);
    }

    @Override
    public Observable<GBusRouteSearchResponse> schRouteIdv2(String serviceKey, String routeId) {
        return retrofitGbusService.schRouteIdv2(serviceKey, routeId);
    }
}
