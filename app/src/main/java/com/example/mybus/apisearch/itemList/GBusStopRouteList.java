package com.example.mybus.apisearch.itemList;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class GBusStopRouteList {
    @PropertyElement(name = "districtCd")
    private String districtCd;
    @PropertyElement(name = "regionName")
    private String regionName;
    @PropertyElement(name = "routeId")
    private String routeId;
    @PropertyElement(name = "routeName")
    private String routeName;
    @PropertyElement(name = "routeTypeName")
    private String routeTypeName;
    @PropertyElement(name = "staOrder")
    private String staOrder;

    private boolean flag = false;



    public GBusStopRouteList() {
    }

    public GBusStopRouteList(String districtCd, String regionName, String routeId, String routeName, String routeTypeName, String staOrder) {
        this.districtCd = districtCd;
        this.regionName = regionName;
        this.routeId = routeId;
        this.routeName = routeName;
        this.routeTypeName = routeTypeName;
        this.staOrder = staOrder;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getDistrictCd() {
        return districtCd;
    }

    public void setDistrictCd(String districtCd) {
        this.districtCd = districtCd;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteTypeName() {
        return routeTypeName;
    }

    public void setRouteTypeName(String routeTypeName) {
        this.routeTypeName = routeTypeName;
    }

    public String getStaOrder() {
        return staOrder;
    }

    public void setStaOrder(String staOrder) {
        this.staOrder = staOrder;
    }
}
