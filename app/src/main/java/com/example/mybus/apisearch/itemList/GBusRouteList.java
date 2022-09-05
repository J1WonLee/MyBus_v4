package com.example.mybus.apisearch.itemList;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class GBusRouteList {
    @PropertyElement(name = "routeName")
    private String routeName;

    public GBusRouteList(String routeName) {
        this.routeName = routeName;
    }

    public GBusRouteList(){
        this(null);
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
}
