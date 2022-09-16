package com.example.mybus.apisearch.GbusWrapper;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name="msgHeader")
public class GBusRouteArriveInfoMsgHeader {
    @PropertyElement(name = "resultCode")
    private String resultCode;
    @PropertyElement(name = "queryTime")
    private String queryTime;
    @PropertyElement(name="resultMessage")
    private String resultMessage;

    public GBusRouteArriveInfoMsgHeader() {
    }

    public GBusRouteArriveInfoMsgHeader(String resultCode, String queryTime, String resultMessage) {
        this.resultCode = resultCode;
        this.queryTime = queryTime;
        this.resultMessage = resultMessage;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }
}
