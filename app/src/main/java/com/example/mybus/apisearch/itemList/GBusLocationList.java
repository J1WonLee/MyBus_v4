package com.example.mybus.apisearch.itemList;

import com.google.gson.annotations.SerializedName;
import com.tickaroo.tikxml.annotation.PropertyElement;

// 광역 버스 잔여 좌석 및 현재 위치
public class GBusLocationList {
    @PropertyElement(name = "endBus")
    private Integer endBus;
    @PropertyElement(name = "routeId")
    private Integer routeId;
    @PropertyElement(name = "plateNo")
    private String plateNo;
    @PropertyElement(name = "plateType")
    private Integer plateType;
    @PropertyElement(name = "remainSeatCnt")
    private Integer remainSeatCnt;
    @PropertyElement(name = "stationSeq")
    private Integer stationSeq;
    @PropertyElement(name = "lowPlate")
    private Integer lowPlate;
    @PropertyElement(name = "stationId")
    private Integer stationId;

    public GBusLocationList(Integer endBus, Integer routeId, String plateNo, Integer plateType, Integer remainSeatCnt, Integer stationSeq, Integer lowPlate, Integer stationId) {
        this.endBus = endBus;
        this.routeId = routeId;
        this.plateNo = plateNo;
        this.plateType = plateType;
        this.remainSeatCnt = remainSeatCnt;
        this.stationSeq = stationSeq;
        this.lowPlate = lowPlate;
        this.stationId = stationId;
    }

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
