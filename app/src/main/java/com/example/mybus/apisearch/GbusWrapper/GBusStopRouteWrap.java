package com.example.mybus.apisearch.GbusWrapper;

import com.example.mybus.apisearch.itemList.GBusStopRouteList;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml(name="msgBody")
public class GBusStopRouteWrap {
    @Element(name="busRouteList")
    List<GBusStopRouteList> gBusStopRouteList = new ArrayList();;

    public GBusStopRouteWrap() {
    }

    public GBusStopRouteWrap(List<GBusStopRouteList> gBusStopRouteList) {
        this.gBusStopRouteList = gBusStopRouteList;
    }

    public List<GBusStopRouteList> getgBusStopRouteList() {
        return gBusStopRouteList;
    }

    public void setgBusStopRouteList(List<GBusStopRouteList> gBusStopRouteList) {
        this.gBusStopRouteList = gBusStopRouteList;
    }


}
