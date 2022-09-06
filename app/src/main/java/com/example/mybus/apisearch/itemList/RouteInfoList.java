package com.example.mybus.apisearch.itemList;

import com.google.gson.annotations.SerializedName;

public class RouteInfoList {
    @SerializedName("busRouteId")
    public String busRouteId;
    @SerializedName("busRouteNm")
    public String busRouteNm;
    @SerializedName("busRouteAbrv")
    public String busRouteAbrv;
    @SerializedName("length")
    public String length;
    @SerializedName("routeType")
    public String routeType;
    @SerializedName("stStationNm")
    public String stStationNm;
    @SerializedName("edStationNm")
    public String edStationNm;
    @SerializedName("term")
    public String term;
    @SerializedName("lastBusYn")
    public String lastBusYn;
    @SerializedName("lastBusTm")
    public String lastBusTm;
    @SerializedName("firstBusTm")
    public String firstBusTm;
    @SerializedName("lastLowTm")
    public String lastLowTm;
    @SerializedName("firstLowTm")
    public String firstLowTm;
    @SerializedName("corpNm")
    public String corpNm;

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

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getStStationNm() {
        return stStationNm;
    }

    public void setStStationNm(String stStationNm) {
        this.stStationNm = stStationNm;
    }

    public String getEdStationNm() {
        return edStationNm;
    }

    public void setEdStationNm(String edStationNm) {
        this.edStationNm = edStationNm;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getLastBusYn() {
        return lastBusYn;
    }

    public void setLastBusYn(String lastBusYn) {
        this.lastBusYn = lastBusYn;
    }

    public String getLastBusTm() {
        return lastBusTm;
    }

    public void setLastBusTm(String lastBusTm) {
        this.lastBusTm = lastBusTm;
    }

    public String getFirstBusTm() {
        return firstBusTm;
    }

    public void setFirstBusTm(String firstBusTm) {
        this.firstBusTm = firstBusTm;
    }

    public String getLastLowTm() {
        return lastLowTm;
    }

    public void setLastLowTm(String lastLowTm) {
        this.lastLowTm = lastLowTm;
    }

    public String getFirstLowTm() {
        return firstLowTm;
    }

    public void setFirstLowTm(String firstLowTm) {
        this.firstLowTm = firstLowTm;
    }

    public String getCorpNm() {
        return corpNm;
    }

    public void setCorpNm(String corpNm) {
        this.corpNm = corpNm;
    }
}
