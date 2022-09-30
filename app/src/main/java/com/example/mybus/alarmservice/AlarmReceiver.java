package com.example.mybus.alarmservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.WindowManager;

import com.example.mybus.apisearch.itemList.BusSchList;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    private BusSchList busSchList;
    private boolean[] week;
    private Bundle args;
    private AlarmManager alarmManager;
    private long selectedTime = 0;
    private int alarmId;
    @Override
    public void onReceive(Context context, Intent intent) {
        week = intent.getBooleanArrayExtra("weekDays");
        selectedTime = intent.getLongExtra("selectTime", 0);
        if (week == null) {
            Log.d("AlarmReceiver", "canceling alarm and service");
            Intent stopService = new Intent(context, AlarmService.class);
            stopService.putExtra("stopAlarm", "-1");
            context.startService(stopService);
        } else{
            Log.d("AlarmReceiver", "AlarmReceiver received!");
            args = intent.getExtras();
            busSchList = args.getParcelable("busList");
            if (busSchList == null) {
                Log.d("AlarmReceiver", "AlarmReceiver received! busschlist is null");
                return;
            }
            Log.d("AlarmReceiver", "AlarmReceiver received!");
            Calendar cal = Calendar.getInstance();
            if (!week[cal.get(Calendar.DAY_OF_WEEK)])   {
                Log.d("AlarmReceiver", "not today!");
                return; // 선택한 요일이 아니면 넘긴다.
            }
            Intent mServiceIntent = new Intent(context, AlarmService.class);
            args = new Bundle();
            args.putParcelable("busList", busSchList);
            mServiceIntent.putExtras(args);
            context.startService(mServiceIntent);
        }
    }
}