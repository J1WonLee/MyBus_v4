package com.example.mybus.apisearch.msgBody;

import com.example.mybus.apisearch.itemList.RouteInfoList;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RouteInfo {
    @SerializedName("itemList")
    List<RouteInfoList> routeInfoLists = new ArrayList<>();

    public List<RouteInfoList> getRouteInfoLists() {
        return routeInfoLists;
    }

    public void setRouteInfoLists(List<RouteInfoList> routeInfoLists) {
        this.routeInfoLists = routeInfoLists;
    }
}
