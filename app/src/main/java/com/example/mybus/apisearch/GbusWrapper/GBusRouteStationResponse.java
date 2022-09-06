package com.example.mybus.apisearch.GbusWrapper;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

// http://apis.data.go.kr/6410000/busrouteservice/getBusRouteStationList
@Xml(name="response")
public class GBusRouteStationResponse {
    @Element
    ComMsgHeader comMsgHeader;
    @Element
    GBusRouteStationMsgHeader header;
    @Element
    GBusRouteStationWrap gBusRouteStationWrap;

    public GBusRouteStationResponse(ComMsgHeader comMsgHeader, GBusRouteStationMsgHeader header, GBusRouteStationWrap gBusRouteStationWrap) {
        this.comMsgHeader = comMsgHeader;
        this.header = header;
        this.gBusRouteStationWrap = gBusRouteStationWrap;
    }

    public ComMsgHeader getComMsgHeader() {
        return comMsgHeader;
    }

    public void setComMsgHeader(ComMsgHeader comMsgHeader) {
        this.comMsgHeader = comMsgHeader;
    }

    public GBusRouteStationMsgHeader getHeader() {
        return header;
    }

    public void setHeader(GBusRouteStationMsgHeader header) {
        this.header = header;
    }

    public GBusRouteStationWrap getgBusRouteStationWrap() {
        return gBusRouteStationWrap;
    }

    public void setgBusRouteStationWrap(GBusRouteStationWrap gBusRouteStationWrap) {
        this.gBusRouteStationWrap = gBusRouteStationWrap;
    }
}
