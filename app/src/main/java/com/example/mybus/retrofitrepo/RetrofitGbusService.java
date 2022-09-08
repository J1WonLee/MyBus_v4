package com.example.mybus.retrofitrepo;

import com.example.mybus.apisearch.GbusWrapper.GBusLocationResponse;
import com.example.mybus.apisearch.GbusWrapper.GBusRouteSearchResponse;
import com.example.mybus.apisearch.GbusWrapper.GBusRouteSearchWrap;
import com.example.mybus.apisearch.GbusWrapper.GBusRouteStationResponse;
import com.example.mybus.apisearch.msgBody.GBusBusLocation;
import com.example.mybus.apisearch.GbusWrapper.GBusStopSearchResponse;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitGbusService {

    @GET("buslocationservice/getBusLocationList")
    Single<GBusBusLocation> getRemainSeat(
            @Query("serviceKey")   String serviceKey,
            @Query("routeId")   String routeId
    );

    @GET("busarrivalservice/getBusArrivalList")
    Single<GBusStopSearchResponse> schStopUid(
            @Query("serviceKey") String serviceKey,
            @Query("stationId") String stationId
    );

    @GET("busrouteservice/getBusRouteInfoItem")
    Single<GBusRouteSearchResponse> schRouteId(
            @Query("serviceKey") String serviceKey,
            @Query("routeId") String routeId
    );

    @GET("busrouteservice/getBusRouteInfoItem")
    Observable<GBusRouteSearchResponse> schRouteIdv2(
            @Query("serviceKey") String serviceKey,
            @Query("routeId") String routeId
    );

    // 노선 정류장 목록 조회
    @GET("busrouteservice/getBusRouteStationList")
    Single<GBusRouteStationResponse> getGbusRouteStationList(
            @Query("serviceKey") String serviceKey,
            @Query("routeId") String routeId
    );

    // 노선 현재 위치 조회
    @GET("buslocationservice/getBusLocationList")
    Single<GBusLocationResponse> getGbusPositionList(
            @Query("serviceKey") String serviceKey,
            @Query("routeId") String routeId
    );


}
