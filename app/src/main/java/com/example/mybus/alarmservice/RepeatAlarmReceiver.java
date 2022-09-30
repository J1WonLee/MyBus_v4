package com.example.mybus.alarmservice;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.mybus.apisearch.itemList.BusSchList;

import java.util.Calendar;

public class RepeatAlarmReceiver extends BroadcastReceiver {
    private BusSchList busSchList;
    private boolean[] week;
    private Bundle args;

    @Override
    public void onReceive(Context context, Intent intent) {
        week = intent.getBooleanArrayExtra("weekdays");
        Log.d("AlarmReceiver", "AlarmReceiver Repeat onReceive");
        if (week != null){
            // 데이터 잘 받아온 경우
            args = intent.getExtras();
            busSchList = args.getParcelable("busList");
            Calendar cal = Calendar.getInstance();
            if (busSchList == null) {
                Log.d("AlarmReceiver", "AlarmReceiver Repeat received! busschlist is null");
                return;
            }
            if(!week[cal.get(Calendar.DAY_OF_WEEK)] ){
                Log.d("AlarmReceiver", "not Repeat today!");
                return; // 선택한 요일이 아니면 넘긴다.
            }
            // 선택한 요일일 경우 서비스를 시작한다.
            Intent mServiceIntent = new Intent(context, AlarmService.class);
            args.putParcelable("busList", busSchList);
            mServiceIntent.putExtras(args);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                try{
                    context.startForegroundService(mServiceIntent);
                }catch (Exception e){
                    context.startService(mServiceIntent);
                }
            }else{
                context.startService(mServiceIntent);
            }
        }else{
            Log.d("AlarmReceiver", "week is null");
        }
    }
}