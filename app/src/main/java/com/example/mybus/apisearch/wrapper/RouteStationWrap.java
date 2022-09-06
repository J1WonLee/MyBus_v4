package com.example.mybus.apisearch.wrapper;

import com.example.mybus.apisearch.msgBody.RouteByStation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute
public class RouteStationWrap {
    @SerializedName("msgBody")
    @Expose
    RouteByStation routeByStation;

    public RouteByStation getRouteByStation() {
        return routeByStation;
    }

    public void setRouteByStation(RouteByStation routeByStation) {
        this.routeByStation = routeByStation;
    }
}
