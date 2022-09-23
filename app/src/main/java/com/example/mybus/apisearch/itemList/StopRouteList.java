package com.example.mybus.apisearch.itemList;

import com.google.gson.annotations.SerializedName;

public class StopRouteList {
    @SerializedName("busRouteId")
    private String busRouteId;
    @SerializedName("busRouteNm")
    private String busRouteNm;
    @SerializedName("busRouteAbrv")
    private String busRouteAbrv;
    @SerializedName("length")
    private String length;
    @SerializedName("busRouteType")
    private String busRouteType;
    @SerializedName("stBegin")
    private String stBegin;
    @SerializedName("stEnd")
    private String stEnd;
    @SerializedName("term")
    private String term;
    @SerializedName("nextBus")
    private String nextBus;
    @SerializedName("firstBusTm")
    private String firstBusTm;
    @SerializedName("lastBusTm")
    private String lastBusTm;
    @SerializedName("firstBusTmLow")
    private String firstBusTmLow;
    @SerializedName("lastBusTmLow")
    private String lastBusTmLow;

    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getBusRouteId() {
        return busRouteId;
    }

    public void setBusRouteId(String busRouteId) {
        this.busRouteId = busRouteId;
    }

    public String getBusRouteNm() {
        return busRouteNm;
    }

    public void setBusRouteNm(String busRouteNm) {
        this.busRouteNm = busRouteNm;
    }

    public String getBusRouteAbrv() {
        return busRouteAbrv;
    }

    public void setBusRouteAbrv(String busRouteAbrv) {
        this.busRouteAbrv = busRouteAbrv;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getBusRouteType() {
        return busRouteType;
    }

    public void setBusRouteType(String busRouteType) {
        this.busRouteType = busRouteType;
    }

    public String getStBegin() {
        return stBegin;
    }

    public void setStBegin(String stBegin) {
        this.stBegin = stBegin;
    }

    public String getStEnd() {
        return stEnd;
    }

    public void setStEnd(String stEnd) {
        this.stEnd = stEnd;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getNextBus() {
        return nextBus;
    }

    public void setNextBus(String nextBus) {
        this.nextBus = nextBus;
    }

    public String getFirstBusTm() {
        return firstBusTm;
    }

    public void setFirstBusTm(String firstBusTm) {
        this.firstBusTm = firstBusTm;
    }

    public String getLastBusTm() {
        return lastBusTm;
    }

    public void setLastBusTm(String lastBusTm) {
        this.lastBusTm = lastBusTm;
    }

    public String getFirstBusTmLow() {
        return firstBusTmLow;
    }

    public void setFirstBusTmLow(String firstBusTmLow) {
        this.firstBusTmLow = firstBusTmLow;
    }

    public String getLastBusTmLow() {
        return lastBusTmLow;
    }

    public void setLastBusTmLow(String lastBusTmLow) {
        this.lastBusTmLow = lastBusTmLow;
    }
}
