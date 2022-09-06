package com.example.mybus.apisearch.itemList;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class GBusRouteList {
    @PropertyElement(name = "routeName")
    private String routeName;

    @PropertyElement(name = "routeId")
    private String routeId;

    @PropertyElement(name="startStationName")
    private String startStationName;

    @PropertyElement(name="endStationName")
    private String endStationName;

    @PropertyElement(name="upFirstTime")
    private String upFirstTime;

    @PropertyElement(name="upLastTime")
    private String upLastTime;

    @PropertyElement(name="downFirstTime")
    private String downFirstTime;

    @PropertyElement(name="downLastTime")
    private String downLastTime;

    @PropertyElement(name="peekAlloc")
    private String peekAlloc;

    @PropertyElement(name="npeekAlloc")
    private String npeekAlloc;

    @PropertyElement(name="companyName")
    private String companyName;


    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public GBusRouteList(String routeName, String routeId, String startStationName, String endStationName, String upFirstTime, String upLastTime, String downFirstTime, String downLastTime, String peekAlloc, String npeekAlloc, String companyName) {
        this.routeName = routeName;
        this.routeId = routeId;
        this.startStationName = startStationName;
        this.endStationName = endStationName;
        this.upFirstTime = upFirstTime;
        this.upLastTime = upLastTime;
        this.downFirstTime = downFirstTime;
        this.downLastTime = downLastTime;
        this.peekAlloc = peekAlloc;
        this.npeekAlloc = npeekAlloc;
        this.companyName = companyName;
    }

    public GBusRouteList(){
        this(null, null, null, null, null, null, null, null, null, null, null);
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public void setStartStationName(String startStationName) {
        this.startStationName = startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public void setEndStationName(String endStationName) {
        this.endStationName = endStationName;
    }

    public String getUpFirstTime() {
        return upFirstTime;
    }

    public void setUpFirstTime(String upFirstTime) {
        this.upFirstTime = upFirstTime;
    }

    public String getUpLastTime() {
        return upLastTime;
    }

    public void setUpLastTime(String upLastTime) {
        this.upLastTime = upLastTime;
    }

    public String getDownFirstTime() {
        return downFirstTime;
    }

    public void setDownFirstTime(String downFirstTime) {
        this.downFirstTime = downFirstTime;
    }

    public String getDownLastTime() {
        return downLastTime;
    }

    public void setDownLastTime(String downLastTime) {
        this.downLastTime = downLastTime;
    }

    public String getPeekAlloc() {
        return peekAlloc;
    }

    public void setPeekAlloc(String peekAlloc) {
        this.peekAlloc = peekAlloc;
    }

    public String getNpeekAlloc() {
        return npeekAlloc;
    }

    public void setNpeekAlloc(String npeekAlloc) {
        this.npeekAlloc = npeekAlloc;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
