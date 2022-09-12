package com.example.mybus.apisearch.GbusWrapper;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name="response")
public class GBusStopRouteResponse {
    @Element
    ComMsgHeader comMsgHeader;
    @Element
    GBusStopRouteMsgHeader header;
    @Element
    GBusStopRouteWrap gBusRouteStationWrap;

    public GBusStopRouteResponse() {
    }

    public GBusStopRouteResponse(ComMsgHeader comMsgHeader, GBusStopRouteMsgHeader header, GBusStopRouteWrap gBusRouteStationWrap) {
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

    public GBusStopRouteMsgHeader getHeader() {
        return header;
    }

    public void setHeader(GBusStopRouteMsgHeader header) {
        this.header = header;
    }

    public GBusStopRouteWrap getgBusRouteStationWrap() {
        return gBusRouteStationWrap;
    }

    public void setgBusRouteStationWrap(GBusStopRouteWrap gBusRouteStationWrap) {
        this.gBusRouteStationWrap = gBusRouteStationWrap;
    }
}
