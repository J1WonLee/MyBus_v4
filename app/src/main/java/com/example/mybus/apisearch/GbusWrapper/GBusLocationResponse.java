package com.example.mybus.apisearch.GbusWrapper;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

// http://apis.data.go.kr/6410000/buslocationservice/getBusLocationList
@Xml(name="response")
public class GBusLocationResponse {
    @Element
    ComMsgHeader comMsgHeader;
    @Element
    GBusRouteSearchMsgHeader header;
    @Element
    GBusLocationWrap gBusLocationWrap;

    public GBusLocationResponse(ComMsgHeader comMsgHeader, GBusRouteSearchMsgHeader header, GBusLocationWrap gBusLocationWrap) {
        this.comMsgHeader = comMsgHeader;
        this.header = header;
        this.gBusLocationWrap = gBusLocationWrap;
    }

    public GBusLocationResponse() {
    }

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

    public GBusLocationWrap getgBusLocationWrap() {
        return gBusLocationWrap;
    }

    public void setgBusLocationWrap(GBusLocationWrap gBusLocationWrap) {
        this.gBusLocationWrap = gBusLocationWrap;
    }
}
