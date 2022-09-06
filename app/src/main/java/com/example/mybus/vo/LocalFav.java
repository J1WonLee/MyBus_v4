package com.example.mybus.vo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

// 즐겨찾기 테이블(정류장 버스 웹퍼)
@Entity(tableName = "LOCAL_FAV")
public class LocalFav {
    @NonNull
    @PrimaryKey
    private String lf_id;   // 즐겨찾기 id로 버스, 정류장 id를 의미함. 정류장에 저장되는 버스들은 별도에 테이블에서 관리

    private String lf_name; // 정류장 혹은 버스의 이름

    private String lf_desc; // 정류장 버스의 설명으로 방면을 저장함

    private int lf_isBus;    // 0 -> 버스 , 1 -> 정류장

    private Date lf_order;   // 순서

    public LocalFav() {
    }

    @Ignore
    public LocalFav(@NonNull String lf_id, String lf_name, String lf_desc, int lf_isBus, Date lf_order) {
        this.lf_id = lf_id;
        this.lf_name = lf_name;
        this.lf_desc = lf_desc;
        this.lf_isBus = lf_isBus;
        this.lf_order = lf_order;
    }

    @NonNull
    public String getLf_id() {
        return lf_id;
    }

    public void setLf_id(@NonNull String lf_id) {
        this.lf_id = lf_id;
    }

    public String getLf_name() {
        return lf_name;
    }

    public void setLf_name(String lf_name) {
        this.lf_name = lf_name;
    }

    public String getLf_desc() {
        return lf_desc;
    }

    public void setLf_desc(String lf_desc) {
        this.lf_desc = lf_desc;
    }

    public int getLf_isBus() {
        return lf_isBus;
    }

    public void setLf_isBus(int lf_isBus) {
        this.lf_isBus = lf_isBus;
    }

    public Date getLf_order() {
        return lf_order;
    }

    public void setLf_order(Date lf_order) {
        this.lf_order = lf_order;
    }
}
