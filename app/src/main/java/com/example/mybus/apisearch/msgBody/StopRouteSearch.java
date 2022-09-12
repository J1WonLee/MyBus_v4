package com.example.mybus.apisearch.msgBody;

import com.example.mybus.apisearch.itemList.StopRouteList;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StopRouteSearch {
    @SerializedName("itemList")
    private List<StopRouteList> stopRouteList;

    public List<StopRouteList> getStopRouteList() {
        return stopRouteList;
    }

    public void setStopRouteList(List<StopRouteList> stopRouteList) {
        this.stopRouteList = stopRouteList;
    }
}
