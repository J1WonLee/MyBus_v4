package com.example.mybus.retrofitrepo;

import com.example.mybus.apisearch.wrapper.ArrInfoByRouteWrap;
import com.example.mybus.apisearch.wrapper.BusPositionSearchWrap;
import com.example.mybus.apisearch.wrapper.RouteInfoWrap;
import com.example.mybus.apisearch.wrapper.RouteSearchWrap;
import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.apisearch.itemList.BusStopList;
import com.example.mybus.apisearch.wrapper.RouteStationWrap;
import com.example.mybus.apisearch.wrapper.StopRouteListWrap;
import com.example.mybus.apisearch.wrapper.StopSearchUidWrap;
import com.example.mybus.apisearch.wrapper.StopSearchWrap;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
// retrofit 서비스 인터페이스
public interface RetrofitService {

    // 노선 번호 검색(키워드는 노선명)
    @GET("busRouteInfo/getBusRouteList")
    Single<RouteSearchWrap> schBusKeyword(
            @Query("ServiceKey") String serviceKey,
            @Query("strSrch") String keyword,
            @Query("resultType") String json
    );


    // 해당 버스가 정차하는 정류장 , 고유번호 알아낸다.
    @GET("busRouteInfo/getStaionByRoute")
    Single<BusStopList> schBusStopList(
            @Query("serviceKey") String serviceKey,
            @Query("busRouteId") String budId,
            @Query("resultType") String json
    );
    
    // 현재 버스들의 위치 조회(키워드는 노선 id)
    @GET("buspos/getBusPosByRtid")
    Single<BusSchList> schBusPosition(
            @Query("serviceKey") String serviceKey,
            @Query("busRouteId") String budId,
            @Query("resultType") String json
    );

    // 키워드로 정류장을 검색한다
    @GET("stationinfo/getStationByName")
    Observable<StopSearchUidWrap> schStopKeyword(
            @Query("ServiceKey") String servieKey,
            @Query("stSrch") String keyword,
            @Query("resultType") String json
    );

    @GET("stationinfo/getStationByUid")
    Observable<StopSearchUidWrap> schStopUid(
            @Query("ServiceKey") String servieKey,
            @Query("arsId") String ardId,
            @Query("resultType") String json
    );


    // 키워드로 정류장을 검색한다
    @GET("stationinfo/getStationByName")
    Single<StopSearchWrap> schStopKeywordv2(
            @Query("ServiceKey") String servieKey,
            @Query("stSrch") String keyword,
            @Query("resultType") String json
    );
    
    // id로 정류장 상세를 검색한다
    @GET("stationinfo/getStationByUid")
    Single<StopSearchUidWrap> schStopUidv2(
            @Query("ServiceKey") String servieKey,
            @Query("arsId") String ardId,
            @Query("resultType") String json
    );

    // 해당 버스 번호에 해당하는 정류장 목록
    @GET("busRouteInfo/getStaionByRoute")
    Single<RouteStationWrap> getRouteStationList(
            @Query("serviceKey") String serviceKey,
            @Query("busRouteId") String busRouteId,
            @Query("resultType") String json
    );

    // 서울버스 현재 위치
    @GET("buspos/getBusPosByRtid")
    Single<BusPositionSearchWrap> getBusPositionList(
            @Query("serviceKey") String serviceKey,
            @Query("busRouteId") String busRouteId,
            @Query("resultType") String json
    );

    @GET("busRouteInfo/getRouteInfo")
    Single<RouteInfoWrap> getRouteInfo(
            @Query("serviceKey") String serviceKey,
            @Query("busRouteId") String busRouteId,
            @Query("resultType") String json
    );

    // 해당 정류소를 지나는 버스 목록
    @GET("stationinfo/getRouteByStation")
    Single<StopRouteListWrap> getStopRouteList(
            @Query("serviceKey") String serviceKey,
            @Query("arsId") String ardId,
            @Query("resultType") String json
    );

    // 특정 정류소 버스의 도착 예정을 구한다
    @GET("arrive/getArrInfoByRoute")
    Observable<ArrInfoByRouteWrap> getArrInfoByRoute(
            @Query("serviceKey") String serviceKey,
            @Query("stId") String stid,
            @Query("busRouteId") String busRouteId,
            @Query("ord") String ord,
            @Query("resultType") String json
    );

    @GET("arrive/getArrInfoByRoute")
    Single<ArrInfoByRouteWrap> getArrInfoByRouteInit(
            @Query("serviceKey") String serviceKey,
            @Query("stId") String stid,
            @Query("busRouteId") String busRouteId,
            @Query("ord") String ord,
            @Query("resultType") String json
    );
}
