package com.example.mybus.apisearch.msgBody;

import com.example.mybus.apisearch.itemList.ArrInfoByRouteList;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ArrInfoByRoute {
    @SerializedName("itemList")
    List<ArrInfoByRouteList> arrInfoByRouteLists = new ArrayList<>();

    public List<ArrInfoByRouteList> getArrInfoByRouteLists() {
        return arrInfoByRouteLists;
    }

    public void setArrInfoByRouteLists(List<ArrInfoByRouteList> arrInfoByRouteLists) {
        this.arrInfoByRouteLists = arrInfoByRouteLists;
    }
}
