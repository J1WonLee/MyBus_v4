package com.example.mybus.apisearch.itemList;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class GBusRouteArriveInfoList {
    @PropertyElement(name = "flag")
    private String flag;
    @PropertyElement(name = "predictTime1")
    private String predictTime1;
    @PropertyElement(name = "predictTime2")
    private String predictTime2;
    @PropertyElement(name = "plateNo1")
    private String plateNo1;
    @PropertyElement(name = "plateNo2")
    private String plateNo2;
    @PropertyElement(name="locationNo1")
    private String locationNo1;
    @PropertyElement(name="locationNo2")
    private String locationNo2;

    public GBusRouteArriveInfoList(String flag, String predictTime1, String predictTime2, String plateNo1, String plateNo2, String locationNo1, String locationNo2) {
        this.flag = flag;
        this.predictTime1 = predictTime1;
        this.predictTime2 = predictTime2;
        this.plateNo1 = plateNo1;
        this.plateNo2 = plateNo2;
        this.locationNo1 = locationNo1;
        this.locationNo2 = locationNo2;
    }

    public GBusRouteArriveInfoList() {
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
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
}
