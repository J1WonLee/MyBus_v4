package com.example.mybus.vo;

import androidx.room.TypeConverter;

import java.util.Date;

// room db에 date 정보 담을 컨버터 클래스
public class Converters {

    @TypeConverter
    public static Date toDate(Long dateLong){
        return dateLong == null ? null : new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(Date date){
        return date == null ? null : date.getTime();
    }
}
