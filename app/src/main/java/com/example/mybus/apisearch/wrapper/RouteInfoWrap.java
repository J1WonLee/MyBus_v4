package com.example.mybus.apisearch.wrapper;

import com.example.mybus.apisearch.msgBody.RouteInfo;
import com.google.gson.annotations.SerializedName;

// http://ws.bus.go.kr/api/rest/busRouteInfo/getRouteInfo
public class RouteInfoWrap {
    @SerializedName("msgBody")
    RouteInfo routeInfo;

    public RouteInfo getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(RouteInfo routeInfo) {
        this.routeInfo = routeInfo;
    }
}
