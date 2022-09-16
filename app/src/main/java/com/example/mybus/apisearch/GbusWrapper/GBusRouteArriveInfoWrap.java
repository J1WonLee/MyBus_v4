package com.example.mybus.apisearch.GbusWrapper;

import com.example.mybus.apisearch.itemList.GBusLocationList;
import com.example.mybus.apisearch.itemList.GBusRouteArriveInfoList;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml(name="msgBody")
public class GBusRouteArriveInfoWrap {
    @Element(name="busArrivalItem")
    List<GBusRouteArriveInfoList> busArriveInfoLists = new ArrayList<>();

    public GBusRouteArriveInfoWrap() {
    }

    public GBusRouteArriveInfoWrap(List<GBusRouteArriveInfoList> busArriveInfoLists) {
        this.busArriveInfoLists = busArriveInfoLists;
    }

    public List<GBusRouteArriveInfoList> getBusArriveInfoLists() {
        return busArriveInfoLists;
    }

    public void setBusArriveInfoLists(List<GBusRouteArriveInfoList> busArriveInfoLists) {
        this.busArriveInfoLists = busArriveInfoLists;
    }
}
