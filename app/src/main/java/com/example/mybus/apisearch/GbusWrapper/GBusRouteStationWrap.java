package com.example.mybus.apisearch.GbusWrapper;

import com.example.mybus.apisearch.itemList.GBusRouteStationList;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml(name="msgBody")
public class GBusRouteStationWrap {
    @Element(name="busRouteStationList")
    List<GBusRouteStationList> GBusRouteStationList = new ArrayList();

    public GBusRouteStationWrap(List<GBusRouteStationList> GBusRouteStationList) {
        this.GBusRouteStationList = GBusRouteStationList;
    }

    public GBusRouteStationWrap() {
    }

    public List<GBusRouteStationList> getBusRouteStationList() {
        return GBusRouteStationList;
    }

    public void setBusRouteStationList(List<GBusRouteStationList> GBusRouteStationList) {
        this.GBusRouteStationList = GBusRouteStationList;
    }
}
