package com.example.mybus.apisearch.itemList;

import com.google.gson.annotations.SerializedName;

// 광역 버스 잔여 좌석 및 현재 위치
public class GBusLocationList {
    @SerializedName("endBus")
    private Integer endBus;
    @SerializedName("routeId")
    private Integer routeId;
    @SerializedName("plateNo")
    private String plateNo;
    @SerializedName("plateType")
    private Integer plateType;
    @SerializedName("remainSeatCnt")
    private Integer remainSeatCnt;
    @SerializedName("stationSeq")
    private Integer stationSeq;
    @SerializedName("lowPlate")
    private Integer lowPlate;
    @SerializedName("stationId")
    private Integer stationId;

    public Integer getEndBus() {
        return endBus;
    }

    public void setEndBus(Integer endBus) {
        this.endBus = endBus;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public Integer getPlateType() {
        return plateType;
    }

    public void setPlateType(Integer plateType) {
        this.plateType = plateType;
    }

    public Integer getRemainSeatCnt() {
        return remainSeatCnt;
    }

    public void setRemainSeatCnt(Integer remainSeatCnt) {
        this.remainSeatCnt = remainSeatCnt;
    }

    public Integer getStationSeq() {
        return stationSeq;
    }

    public void setStationSeq(Integer stationSeq) {
        this.stationSeq = stationSeq;
    }

    public Integer getLowPlate() {
        return lowPlate;
    }

    public void setLowPlate(Integer lowPlate) {
        this.lowPlate = lowPlate;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }
}
