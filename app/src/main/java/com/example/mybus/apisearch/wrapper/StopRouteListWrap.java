package com.example.mybus.apisearch.wrapper;

import com.example.mybus.apisearch.msgBody.StopRouteSearch;
import com.google.gson.annotations.SerializedName;

public class StopRouteListWrap {
    @SerializedName("msgBody")
    private StopRouteSearch stopRouteSearch;

    public StopRouteSearch getStopRouteList() {
        return stopRouteSearch;
    }

    public void setStopRouteList(StopRouteSearch stopRouteSearch) {
        this.stopRouteSearch = stopRouteSearch;
    }
}
