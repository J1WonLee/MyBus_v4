package com.example.mybus.apisearch.msgBody;

import com.example.mybus.apisearch.itemList.StationByRouteList;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RouteByStation {
    @SerializedName("itemList")
    List<StationByRouteList> stationByRouteLists = new ArrayList<>();

    public List<StationByRouteList> getStationByRouteLists() {
        return stationByRouteLists;
    }

    public void setStationByRouteLists(List<StationByRouteList> stationByRouteLists) {
        this.stationByRouteLists = stationByRouteLists;
    }
}
