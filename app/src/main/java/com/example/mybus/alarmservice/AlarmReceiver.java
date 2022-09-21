package com.example.mybus.alarmservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.vo.AddAlarmBusSchCopy;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    private BusSchList busSchList;
    private boolean[] week;
    private Bundle args;
    private PowerManager pm;
    private PowerManager.WakeLock wl;
    @Override
    public void onReceive(Context context, Intent intent) {
        week = intent.getBooleanArrayExtra("weekDays");
        if (week == null) {
            Log.d("AlarmReceiver", "canceling alarm and service");
            Intent stopService = new Intent(context, AlarmService.class);
            stopService.putExtra("stopAlarm", "-1");
            context.startService(stopService);
        } else{
//            setWakeLock(context);
            week = intent.getBooleanArrayExtra("weekDays");
            if (week.length == 0){
                Log.d("AlarmReceiver", "AlarmReceiver received! week is null"); return;
            }
            args = intent.getExtras();
            busSchList = args.getParcelable("busList");
            if (busSchList == null) {
                Log.d("AlarmReceiver", "AlarmReceiver received! busschlist is null");
                return;
            }
            Log.d("AlarmReceiver", "AlarmReceiver received!" + week.toString());
            Calendar cal = Calendar.getInstance();
            if (!week[cal.get(Calendar.DAY_OF_WEEK)])       return; // 선택한 요일이 아니면 넘긴다.

            Intent mServiceIntent = new Intent(context, AlarmService.class);
            args = new Bundle();
            args.putParcelable("busList", busSchList);
            mServiceIntent.putExtras(args);
            context.startService(mServiceIntent);
        }
    }

    public void setWakeLock(Context context){
        pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON , AlarmService.WAKELOCK_TAG);
        // FULL_WAKE_LOCK PARTIAL_WAKE_LOCK
        wl.acquire();
        Log.d("AlarmReceiver", "setWakeLock service");
    }
}