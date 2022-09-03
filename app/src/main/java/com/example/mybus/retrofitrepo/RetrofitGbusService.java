package com.example.mybus.retrofitrepo;

import com.example.mybus.apisearch.msgBody.GbusBusLocation;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitGbusService {

    @GET("buslocationservice/getBusLocationList")
    Single<GbusBusLocation> getRemainSeat(
            @Query("serviceKey")   String serviceKey,
            @Query("routeId")   String routeId
    );


}
