package com.example.mybus.apisearch.GbusWrapper;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "response")
public class GBusRouteSearchResponse {
    @Element
    ComMsgHeader comMsgHeader;
    @Element
    GBusRouteSearchMsgHeader header;
    @Element
    GBusRouteSearchWrap gBusStopSearchUidWrap;

    public ComMsgHeader getComMsgHeader() {
        return comMsgHeader;
    }

    public void setComMsgHeader(ComMsgHeader comMsgHeader) {
        this.comMsgHeader = comMsgHeader;
    }

    public GBusRouteSearchMsgHeader getHeader() {
        return header;
    }

    public void setHeader(GBusRouteSearchMsgHeader header) {
        this.header = header;
    }

    public GBusRouteSearchWrap getgBusStopSearchUidWrap() {
        return gBusStopSearchUidWrap;
    }

    public void setgBusStopSearchUidWrap(GBusRouteSearchWrap gBusStopSearchUidWrap) {
        this.gBusStopSearchUidWrap = gBusStopSearchUidWrap;
    }
}
