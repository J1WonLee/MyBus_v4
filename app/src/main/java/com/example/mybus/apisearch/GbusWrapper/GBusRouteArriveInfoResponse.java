package com.example.mybus.apisearch.GbusWrapper;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name="response")
public class GBusRouteArriveInfoResponse {
    @Element
    ComMsgHeader comMsgHeader;
    @Element
    GBusRouteArriveInfoMsgHeader header;
    @Element
    GBusRouteArriveInfoWrap gBusLocationWrap;

    public GBusRouteArriveInfoResponse() {
    }

    public GBusRouteArriveInfoResponse(ComMsgHeader comMsgHeader, GBusRouteArriveInfoMsgHeader header, GBusRouteArriveInfoWrap gBusLocationWrap) {
        this.comMsgHeader = comMsgHeader;
        this.header = header;
        this.gBusLocationWrap = gBusLocationWrap;
    }

    public ComMsgHeader getComMsgHeader() {
        return comMsgHeader;
    }

    public void setComMsgHeader(ComMsgHeader comMsgHeader) {
        this.comMsgHeader = comMsgHeader;
    }

    public GBusRouteArriveInfoMsgHeader getHeader() {
        return header;
    }

    public void setHeader(GBusRouteArriveInfoMsgHeader header) {
        this.header = header;
    }

    public GBusRouteArriveInfoWrap getgBusLocationWrap() {
        return gBusLocationWrap;
    }

    public void setgBusLocationWrap(GBusRouteArriveInfoWrap gBusLocationWrap) {
        this.gBusLocationWrap = gBusLocationWrap;
    }
}
