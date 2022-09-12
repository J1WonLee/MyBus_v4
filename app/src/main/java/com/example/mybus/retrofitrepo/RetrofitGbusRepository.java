package com.example.mybus.retrofitrepo;

import com.example.mybus.apisearch.GbusWrapper.GBusLocationResponse;
import com.example.mybus.apisearch.GbusWrapper.GBusRouteSearchResponse;
import com.example.mybus.apisearch.GbusWrapper.GBusRouteStationResponse;
import com.example.mybus.apisearch.GbusWrapper.GBusStopRouteResponse;
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

    // 경기도 버스에 해당하는 정류장 목록
    @Override
    public Single<GBusRouteStationResponse> getGbusRouteStationList(String serviceKey, String routeId) {
        return retrofitGbusService.getGbusRouteStationList(serviceKey,routeId);
    }

    // 경기도 버스 현재 위치
    @Override
    public Single<GBusLocationResponse> getGbusPositionList(String serviceKey, String routeId) {
        return retrofitGbusService.getGbusPositionList(serviceKey, routeId);
    }

    // 해당 노선의 상세정보를 받아온다
    @Override
    public Single<GBusRouteSearchResponse> getGbusRouteInfo(String serviceKey, String routeId) {
        return retrofitGbusService.getGbusRouteInfo(serviceKey, routeId);
    }

    @Override
    public Observable<GBusStopSearchResponse> schStopUidv2(String serviceKey, String stationId) {
        return retrofitGbusService.schStopUidv2(serviceKey, stationId);
    }

    // 특정 정류장에 정차하는 버스 목록
    @Override
    public Single<GBusStopRouteResponse> getGStopRouteList(String serviceKey, String stationId) {
        return  retrofitGbusService.getGStopRouteList(serviceKey, stationId);
    }
}
