package com.example.mybus.apisearch.itemList;

import com.google.gson.annotations.SerializedName;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

// 광역 버스 잔여 좌석 및 현재 위치
@Xml
public class GBusLocationList implements Comparable<GBusLocationList> {
    @PropertyElement(name = "endBus")
    private Integer endBus;
    @PropertyElement(name = "routeId")
    private String routeId;
    @PropertyElement(name = "plateNo")
    private String plateNo;
    @PropertyElement(name = "plateType")
    private String plateType;
    @PropertyElement(name = "remainSeatCnt")
    private Integer remainSeatCnt;
    @PropertyElement(name = "stationSeq")
    private Integer stationSeq;
    @PropertyElement(name = "lowPlate")
    private String lowPlate;
    @PropertyElement(name = "stationId")
    private String stationId;

    public GBusLocationList(Integer endBus, String routeId, String plateNo, String plateType, Integer remainSeatCnt, Integer stationSeq, String lowPlate, String stationId) {
        this.endBus = endBus;
        this.routeId = routeId;
        this.plateNo = plateNo;
        this.plateType = plateType;
        this.remainSeatCnt = remainSeatCnt;
        this.stationSeq = stationSeq;
        this.lowPlate = lowPlate;
        this.stationId = stationId;
    }

    @Override
    public int compareTo(GBusLocationList gBusLocationList) {
        return stationSeq.compareTo(gBusLocationList.getStationSeq());
    }

    public GBusLocationList() {
        this(null, null, null, null, null, null, null, null);
    }

    public Integer getEndBus() {
        return endBus;
    }

    public void setEndBus(Integer endBus) {
        this.endBus = endBus;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getPlateType() {
        return plateType;
    }

    public void setPlateType(String plateType) {
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

    public String getLowPlate() {
        return lowPlate;
    }

    public void setLowPlate(String lowPlate) {
        this.lowPlate = lowPlate;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
}
