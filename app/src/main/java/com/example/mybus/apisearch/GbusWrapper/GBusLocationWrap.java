package com.example.mybus.apisearch.GbusWrapper;

import com.example.mybus.apisearch.itemList.GBusLocationList;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml(name="msgBody")
public class GBusLocationWrap {
    @Element(name="busLocationList")
    List<GBusLocationList> busLocationList = new ArrayList<>();

    public GBusLocationWrap(List<GBusLocationList> busLocationList) {
        this.busLocationList = busLocationList;
    }

    public List<GBusLocationList> getBusLocationList() {
        return busLocationList;
    }

    public void setBusLocationList(List<GBusLocationList> busLocationList) {
        this.busLocationList = busLocationList;
    }
}
