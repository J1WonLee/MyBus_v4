package com.example.mybus.apisearch.itemList;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class GBusRouteStationList implements Comparable<GBusRouteStationList> {
    @PropertyElement(name="centerYn")
    private String centerYn;
    @PropertyElement(name="districtCd")
    private String districtCd;
    @PropertyElement(name="mobileNo")
    private String mobileNo;
    @PropertyElement(name="regionName")
    private String regionName;
    @PropertyElement(name="stationId")
    private String stationId;
    @PropertyElement(name="stationName")
    private String stationName;
    @PropertyElement(name="stationSeq")
    private String stationSeq;
    @PropertyElement(name="turnYn")
    private String turnYn;

    public GBusRouteStationList(String centerYn, String districtCd, String mobileNo, String regionName, String stationId, String stationName, String stationSeq, String turnYn) {
        this.centerYn = centerYn;
        this.districtCd = districtCd;
        this.mobileNo = mobileNo;
        this.regionName = regionName;
        this.stationId = stationId;
        this.stationName = stationName;
        this.stationSeq = stationSeq;
        this.turnYn = turnYn;
    }

    @Override
    public int compareTo(GBusRouteStationList gBusRouteStationList) {
        return stationSeq.compareTo(gBusRouteStationList.getStationSeq());
    }

    public GBusRouteStationList() {
        this(null, null, null, null, null, null, null, null);
    }

    public String getCenterYn() {
        return centerYn;
    }

    public void setCenterYn(String centerYn) {
        this.centerYn = centerYn;
    }

    public String getDistrictCd() {
        return districtCd;
    }

    public void setDistrictCd(String districtCd) {
        this.districtCd = districtCd;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationSeq() {
        return stationSeq;
    }

    public void setStationSeq(String stationSeq) {
        this.stationSeq = stationSeq;
    }

    public String getTurnYn() {
        return turnYn;
    }

    public void setTurnYn(String turnYn) {
        this.turnYn = turnYn;
    }


}
