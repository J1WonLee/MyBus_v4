package com.example.mybus.apisearch.GbusWrapper;

import com.example.mybus.apisearch.itemList.BusArrivalList;
import com.example.mybus.apisearch.itemList.GBusRouteList;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml(name="msgBody")
public class GBusRouteSearchWrap {
    @Element(name="busRouteInfoItem")
    List<GBusRouteList> gBusRouteList= new ArrayList<>();

    public List<GBusRouteList> getgBusRouteList() {
        return gBusRouteList;
    }

    public void setgBusRouteList(List<GBusRouteList> gBusRouteList) {
        this.gBusRouteList = gBusRouteList;
    }
}
