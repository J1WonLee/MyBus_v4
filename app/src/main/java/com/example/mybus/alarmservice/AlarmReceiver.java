package com.example.mybus.alarmservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.WindowManager;

import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.menu.LoginActivity;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    private BusSchList busSchList;
    private boolean[] week;
    private Bundle args;
    private AlarmManager alarmManager;
    private long selectedTime = 0;
    private int alarmId;
    private SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        week = intent.getBooleanArrayExtra("weekDays");
        selectedTime = intent.getLongExtra("selectTime", 0);
        sharedPreferences = context.getSharedPreferences(LoginActivity.sharedId, Context.MODE_PRIVATE);
        if (intent.getStringExtra("EXTRA_NOTIFICATION_ID") != null){
            Log.d("AlarmReceiver", "canceling alarm and service");
            Intent stopService = new Intent(context, AlarmService.class);
            stopService.putExtra("stopAlarm", "-1");
            context.startService(stopService);
            return;
        }
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (week == null && selectedTime == 0){
            chkDates(context);
            return;
        }
        if (week == null) {
            Log.d("AlarmReceiver", "canceling alarm and service");
            Intent stopService = new Intent(context, AlarmService.class);
            stopService.putExtra("stopAlarm", "-1");
            context.startService(stopService);
        } else{
            args = intent.getExtras();
            busSchList = args.getParcelable("busList");
            if (busSchList == null) {
                Log.d("AlarmReceiver", "AlarmReceiver received! busschlist is null");
                return;
            }
            Calendar cal = Calendar.getInstance();
            if (!week[cal.get(Calendar.DAY_OF_WEEK)])   {
                Log.d("AlarmReceiver", "not today! in !week if");
                makePendingIntent(context);
                return; // 선택한 요일이 아니면 넘긴다.
            }
            Intent mServiceIntent = new Intent(context, AlarmService.class);
            args = new Bundle();
            args.putParcelable("busList", busSchList);
            mServiceIntent.putExtras(args);
            context.startService(mServiceIntent);
            makePendingIntent(context);
            return;
        }
    }

    public void chkDates(Context context){
        Calendar cal = Calendar.getInstance();
        String dates = sharedPreferences.getString("dates", null);
        String today = Integer.toString(cal.get(Calendar.DAY_OF_WEEK));
        makeWeek(dates);
        setBusSchList();
        if (dates.contains(today)){
            Log.d("AlarmReceiver", "today in chkDates");
            Intent mServiceIntent = new Intent(context, AlarmService.class);
            args = new Bundle();
            args.putParcelable("busList", busSchList);
            mServiceIntent.putExtras(args);
            context.startService(mServiceIntent);
            makePendingIntent(context);
            return;
        }else{
            makePendingIntent(context);
            return;
        }
    }

    public void setBusSchList(){
        BusSchList busSchList = new BusSchList();
        busSchList.setStId(sharedPreferences.getString("stId", "0"));
        busSchList.setStStationNm(sharedPreferences.getString("stNm", "0"));
        busSchList.setBusRouteNm(sharedPreferences.getString("busName", "0"));
        busSchList.setBusRouteId(sharedPreferences.getString("busRoute", "0"));
        busSchList.setCorpNm(sharedPreferences.getString("stOrder", "0"));
        selectedTime = sharedPreferences.getLong("selectTime", 0L);
    }

    public void makePendingIntent(Context context){
        Log.d("AlarmReceiver", "makePendingIntent selectedTime is " + selectedTime);
        long intervalDay = 24*60*60*1000;
        long afterTime  = selectedTime + intervalDay;
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.setAction("start_alarm");
        alarmIntent.putExtra("weekDays", week);
        alarmIntent.putExtra("selectTime", afterTime);
        args.putParcelable("busList", busSchList);
        alarmIntent.putExtras(args);
        Log.d("AlarmReceiver", "makePendingIntent selectedTime after is " + afterTime);
        PendingIntent alarmPending = PendingIntent.getBroadcast(context, alarmId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(afterTime, alarmPending);
        alarmManager.setAlarmClock(alarmClockInfo, alarmPending);
    }

    public void makeWeek(String dates){
       String[] day =  dates.split(",");
       for (int i=0; i<day.length; i++){
           switch(day[i]){
               case "1":
                   week[1] = true;
                   break;
               case "2":
                   week[2] = true;
                   break;
               case "3":
                   week[3] = true;
                   break;
               case "4":
                   week[4] = true;
                   break;
               case "5":
                   week[5] = true;
                   break;
               case "6":
                   week[6] = true;
                   break;
               case "7":
                   week[7] = true;
                   break;
           }
       }
    }
}