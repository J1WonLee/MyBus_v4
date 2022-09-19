package com.example.mybus.vo;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

// 도착 알람 정보를 임시로 담고있을 클래스.
@Entity(tableName = "LOCAL_ARR_ALARM", primaryKeys = {"stId", "routeId"} )
public class ArrAlarmPref {
    @SerializedName("stId")
    @NonNull
    private String stId;
    @SerializedName("routeId")
    @NonNull
    private String routeId;
    @SerializedName("flag")
    private int flag;

    public ArrAlarmPref(String stId, String routeId, int flag) {
        this.stId = stId;
        this.routeId = routeId;
        this.flag = flag;
    }

    public String getStId() {
        return stId;
    }

    public void setStId(String stId) {
        this.stId = stId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
