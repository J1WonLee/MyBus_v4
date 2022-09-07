package com.example.mybus.apisearch.itemList;

import com.google.gson.annotations.SerializedName;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class BusArrivalList implements Comparable<BusArrivalList>{
    @PropertyElement(name = "flag")
    private String flag;
    @PropertyElement(name = "locationNo1")
    private String locationNo1;
    @PropertyElement(name ="locationNo2")
    private String locationNo2;
    @PropertyElement(name ="plateNo1")
    private String plateNo1;
    @PropertyElement(name ="plateNo2")
    private String plateNo2;
    @PropertyElement(name ="predictTime1")
    private String predictTime1;
    @PropertyElement(name ="predictTime2")
    private String predictTime2;
    @PropertyElement(name ="remainSeatCnt1")
    private String remainSeatCnt1;
    @PropertyElement(name ="remainSeatCnt2")
    private String remainSeatCnt2;
    @PropertyElement(name ="routeId")
    private String routeId;
    @PropertyElement(name ="staOrder")
    private String staOrder;
    @PropertyElement(name ="stationId")
    private String stationId;

    private boolean chkFlag = false;

    public boolean isChkFlag() {
        return chkFlag;
    }

    public void setChkFlag(boolean chkFlag) {
        this.chkFlag = chkFlag;
    }

    private String routeNm;

    public String getRouteNm() {
        return routeNm;
    }

    public void setRouteNm(String routeNm) {
        this.routeNm = routeNm;
    }

    public BusArrivalList(String flag, String locationNo1, String locationNo2, String plateNo1, String plateNo2, String predictTime1, String predictTime2, String remainSeatCnt1, String remainSeatCnt2, String routeId, String staOrder, String stationId) {
        this.flag = flag;
        this.locationNo1 = locationNo1;
        this.locationNo2 = locationNo2;
        this.plateNo1 = plateNo1;
        this.plateNo2 = plateNo2;
        this.predictTime1 = predictTime1;
        this.predictTime2 = predictTime2;
        this.remainSeatCnt1 = remainSeatCnt1;
        this.remainSeatCnt2 = remainSeatCnt2;
        this.routeId = routeId;
        this.staOrder = staOrder;
        this.stationId = stationId;
    }

    public BusArrivalList(){
        this(null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getLocationNo1() {
        return locationNo1;
    }

    public void setLocationNo1(String locationNo1) {
        this.locationNo1 = locationNo1;
    }

    public String getLocationNo2() {
        return locationNo2;
    }

    public void setLocationNo2(String locationNo2) {
        this.locationNo2 = locationNo2;
    }

    public String getPlateNo1() {
        return plateNo1;
    }

    public void setPlateNo1(String plateNo1) {
        this.plateNo1 = plateNo1;
    }

    public String getPlateNo2() {
        return plateNo2;
    }

    public void setPlateNo2(String plateNo2) {
        this.plateNo2 = plateNo2;
    }

    public String getPredictTime1() {
        return predictTime1;
    }

    public void setPredictTime1(String predictTime1) {
        this.predictTime1 = predictTime1;
    }

    public String getPredictTime2() {
        return predictTime2;
    }

    public void setPredictTime2(String predictTime2) {
        this.predictTime2 = predictTime2;
    }

    public String getRemainSeatCnt1() {
        return remainSeatCnt1;
    }

    public void setRemainSeatCnt1(String remainSeatCnt1) {
        this.remainSeatCnt1 = remainSeatCnt1;
    }

    public String getRemainSeatCnt2() {
        return remainSeatCnt2;
    }

    public void setRemainSeatCnt2(String remainSeatCnt2) {
        this.remainSeatCnt2 = remainSeatCnt2;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getStaOrder() {
        return staOrder;
    }

    public void setStaOrder(String staOrder) {
        this.staOrder = staOrder;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    @Override
    public int compareTo(BusArrivalList busArrivalList) {
        return routeId.compareTo(busArrivalList.getRouteId());
    }
}
