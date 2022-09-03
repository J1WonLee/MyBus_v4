package com.example.mybus.apisearch.wrapper;

import com.example.mybus.apisearch.msgBody.StopSearch;
import com.google.gson.annotations.SerializedName;

public class StopSearchWrap{
    @SerializedName("msgBody")
    StopSearch stopSearch;

    public StopSearch getStopSearch() {
        return stopSearch;
    }

    public void setStopSearch(StopSearch stopSearch) {
        this.stopSearch = stopSearch;
    }
}
