package com.example.mybus.apisearch.wrapper;

import com.example.mybus.apisearch.msgBody.BusRouteSearch;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RouteSearchWrap {
    @SerializedName("msgBody")
    @Expose
    private BusRouteSearch busRouteSearch;

    public BusRouteSearch getBusRouteSearch() {
        return busRouteSearch;
    }

    public void setBusRouteSearch(BusRouteSearch busRouteSearch) {
        this.busRouteSearch = busRouteSearch;
    }
}
