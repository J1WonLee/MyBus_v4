package com.example.mybus.apisearch.GbusWrapper;

import com.example.mybus.apisearch.itemList.BusRouteStationList;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml(name="msgBody")
public class GBusRouteStationWrap {
    @Element(name="busRouteStationList")
    List<BusRouteStationList> busRouteStationList = new ArrayList();

    public GBusRouteStationWrap(List<BusRouteStationList> busRouteStationList) {
        this.busRouteStationList = busRouteStationList;
    }

    public List<BusRouteStationList> getBusRouteStationList() {
        return busRouteStationList;
    }

    public void setBusRouteStationList(List<BusRouteStationList> busRouteStationList) {
        this.busRouteStationList = busRouteStationList;
    }
}
