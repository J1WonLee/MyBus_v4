package com.example.mybus.retrofitrepo;

import android.util.Log;

import com.example.mybus.apisearch.itemList.StopSchList;
import com.example.mybus.apisearch.wrapper.RouteSearchWrap;
import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.apisearch.itemList.BusStopList;
import com.example.mybus.apisearch.wrapper.StopSearchUidWrap;
import com.example.mybus.apisearch.wrapper.StopSearchWrap;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class RetrofitRepository implements RetrofitService {
    RetrofitService retrofitService;

    @Inject
    public RetrofitRepository( RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
    }


    @Override
    public Single<RouteSearchWrap> schBusKeyword(String serviceKey, String keyword, String json) {
        return retrofitService.schBusKeyword(serviceKey, keyword, "json");
    }


    @Override
    public Single<BusStopList> schBusStopList(String serviceKey, String budId, String json) {
        return null;
    }

    @Override
    public Single<BusSchList> schBusPosition(String serviceKey, String budId, String json) {
        return null;
    }

    @Override
    public Single<StopSearchWrap> schStopKeywordv2(String servieKey, String keyword, String json) {
        return retrofitService.schStopKeywordv2(servieKey, keyword, json);
    }

    @Override
    public Single<StopSearchUidWrap> schStopUidv2(String servieKey, String ardId, String json) {
        return retrofitService.schStopUidv2(servieKey, ardId, json);
    }






    @Override
    public Observable<StopSearchUidWrap> schStopUid(String servieKey, String arsId, String json) {
        return retrofitService.schStopUid(servieKey, arsId, json);
    }



    @Override
    public Observable<StopSearchUidWrap> schStopKeyword(String servieKey, String keyword, String json) {
        return retrofitService.schStopKeyword(servieKey, keyword, json);
    }




    public Observable<StopSearchUidWrap> getStopLists(String serviceKey, String keyword, String arsId, String stId){
        if (stId.startsWith("1")){
            Log.d("kkang", "in getStopLists -> schStopUid and st Id : " + stId +" , keyword : " + keyword);
            return schStopUid(serviceKey, arsId, "json");
        }else if (stId.startsWith("2")){
            Log.d("kkang", "in getStopLists -> schStopKeyword and st Id : " + stId +" , keyword : " + keyword);
            return schStopKeyword(serviceKey, keyword, "json");
        }
        return null;
    }

}
