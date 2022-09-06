package com.example.mybus.apisearch.wrapper;

import com.example.mybus.apisearch.msgBody.BusPositionSearch;
import com.google.gson.annotations.SerializedName;

public class BusPositionSearchWrap {
    @SerializedName("msgBody")
    BusPositionSearch busPositionSearch;

    public BusPositionSearch getBusPositionSearch() {
        return busPositionSearch;
    }

    public void setBusPositionSearch(BusPositionSearch busPositionSearch) {
        this.busPositionSearch = busPositionSearch;
    }
}
