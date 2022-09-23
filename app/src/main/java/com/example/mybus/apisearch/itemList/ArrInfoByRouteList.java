package com.example.mybus.apisearch.itemList;

import com.google.gson.annotations.SerializedName;

public class ArrInfoByRouteList {
    @SerializedName("stId")
    public String stId;
    @SerializedName("stNm")
    public String stNm;
    @SerializedName("arsId")
    public String arsId;
    @SerializedName("busRouteId")
    public String busRouteId;
    @SerializedName("rtNm")
    public String rtNm;
    @SerializedName("stationNm1")
    public String stationNm1;
    @SerializedName("stationNm2")
    public String stationNm2;
    @SerializedName("arrmsg1")
    public String arrmsg1;
    @SerializedName("arrmsg2")
    public String arrmsg2;

    public String getStId() {
        return stId;
    }

    public void setStId(String stId) {
        this.stId = stId;
    }

    public String getStNm() {
        return stNm;
    }

    public void setStNm(String stNm) {
        this.stNm = stNm;
    }

    public String getArsId() {
        return arsId;
    }

    public void setArsId(String arsId) {
        this.arsId = arsId;
    }

    public String getBusRouteId() {
        return busRouteId;
    }

    public void setBusRouteId(String busRouteId) {
        this.busRouteId = busRouteId;
    }

    public String getRtNm() {
        return rtNm;
    }

    public void setRtNm(String rtNm) {
        this.rtNm = rtNm;
    }

    public String getStationNm1() {
        return stationNm1;
    }

    public void setStationNm1(String stationNm1) {
        this.stationNm1 = stationNm1;
    }

    public String getStationNm2() {
        return stationNm2;
    }

    public void setStationNm2(String stationNm2) {
        this.stationNm2 = stationNm2;
    }

    public String getArrmsg1() {
        return arrmsg1;
    }

    public void setArrmsg1(String arrmsg1) {
        this.arrmsg1 = arrmsg1;
    }

    public String getArrmsg2() {
        return arrmsg2;
    }

    public void setArrmsg2(String arrmsg2) {
        this.arrmsg2 = arrmsg2;
    }
}
