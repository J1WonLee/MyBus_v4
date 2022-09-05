package com.example.mybus.apisearch.GbusWrapper;

import com.example.mybus.apisearch.itemList.BusArrivalList;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

// 경기도 정류장 도착 조회 웨퍼 클래스
@Xml(name="msgBody")
public class GBusStopSearchUidWrap {
    @Element(name="busArrivalList")
    List<BusArrivalList> busArrivalListList = new ArrayList<>();

    public GBusStopSearchUidWrap(List<BusArrivalList> busArrivalListList) {
        this.busArrivalListList = busArrivalListList;
    }

    public GBusStopSearchUidWrap() {
    }

    public List<BusArrivalList> getBusArrivalListList() {
        return busArrivalListList;
    }

    public void setBusArrivalListList(List<BusArrivalList> busArrivalListList) {
        this.busArrivalListList = busArrivalListList;
    }
}
