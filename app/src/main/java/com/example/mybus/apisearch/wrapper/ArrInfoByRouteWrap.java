package com.example.mybus.apisearch.wrapper;

import com.example.mybus.apisearch.msgBody.ArrInfoByRoute;
import com.google.gson.annotations.SerializedName;

// 알림 서비스
public class ArrInfoByRouteWrap  {
    @SerializedName("msgBody")
    private ArrInfoByRoute arrInfoByRoute;

    public ArrInfoByRouteWrap(ArrInfoByRoute arrInfoByRoute) {
        this.arrInfoByRoute = arrInfoByRoute;
    }

    public ArrInfoByRoute getArrInfoByRoute() {
        return arrInfoByRoute;
    }

    public void setArrInfoByRoute(ArrInfoByRoute arrInfoByRoute) {
        this.arrInfoByRoute = arrInfoByRoute;
    }
}
