package com.example.mybus.apisearch.itemList;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class GBusRouteList {
    @PropertyElement(name = "routeName")
    private String routeName;

    @PropertyElement(name = "routeId")
    private String routeId;

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public GBusRouteList(String routeName, String routeId) {
        this.routeName = routeName;
        this.routeId = routeId;
    }

    public GBusRouteList(){
        this(null, null);
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
}
