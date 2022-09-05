package com.example.mybus.apisearch.GbusWrapper;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "response")
public class GBusStopSearchResponse {
    @Element
    ComMsgHeader comMsgHeader;
    @Element
    GBusStopSearchMsgHeader header;
    @Element
    GBusStopSearchUidWrap gBusStopSearchUidWrap;

    public GBusStopSearchResponse() {
    }

    public GBusStopSearchMsgHeader getHeader() {
        return header;
    }

    public void setHeader(GBusStopSearchMsgHeader header) {
        this.header = header;
    }

    public GBusStopSearchUidWrap getgBusStopSearchUidWrap() {
        return gBusStopSearchUidWrap;
    }

    public void setgBusStopSearchUidWrap(GBusStopSearchUidWrap gBusStopSearchUidWrap) {
        this.gBusStopSearchUidWrap = gBusStopSearchUidWrap;
    }



}
