package com.example.mybus.apisearch.wrapper;

import com.example.mybus.apisearch.msgBody.StopSearchUid;
import com.google.gson.annotations.SerializedName;

public class StopSearchUidWrap {
    @SerializedName("msgBody")
    private StopSearchUid stopSearchUid;

    public StopSearchUid getStopSearchUid() {
        return stopSearchUid;
    }

    public void setStopSearchUid(StopSearchUid stopSearchUid) {
        this.stopSearchUid = stopSearchUid;
    }


}
