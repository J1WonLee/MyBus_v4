package com.example.mybus.vo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

// 즐겨찾기 테이블(정류장 버스 웹퍼)
@Entity(tableName = "LOCAL_FAV")
public class LocalFav implements Serializable {
    @NonNull
    @PrimaryKey
    private String lf_id;   // 즐겨찾기 id로 버스, 정류장 id를 의미함. 정류장에 저장되는 버스들은 별도에 테이블에서 관리

    private String st_id;   // 정류장 번호(특정 노선 도착 정보에 필요해서 넣어줌)

    private String lf_ord;  // 정류소 순번

    private String lf_name; // 정류장 혹은 버스의 이름

    private String lf_desc; // 정류장 버스의 설명으로 방면을 저장함

    private int lf_isBus;    // 0 -> 버스 , 1 -> 정류장

    private Date lf_order;   // 순서

    public LocalFav() {
    }

    @Ignore
    public LocalFav(@NonNull String lf_id, String st_id, String lf_ord, String lf_name, String lf_desc, int lf_isBus, Date lf_order) {
        this.lf_id = lf_id;
        this.st_id = st_id;
        this.lf_ord = lf_ord;
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

    public String getSt_id() {
        return st_id;
    }

    public void setSt_id(String st_id) {
        this.st_id = st_id;
    }

    public String getLf_ord() {
        return lf_ord;
    }

    public void setLf_ord(String lf_ord) {
        this.lf_ord = lf_ord;
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
