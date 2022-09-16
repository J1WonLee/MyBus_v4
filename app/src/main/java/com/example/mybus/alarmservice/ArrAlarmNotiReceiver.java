package com.example.mybus.alarmservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ArrAlarmNotiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d("ArrAlarmNotiReceiver", intent.getAction()+"");
        if (intent.getAction().equals("com.example.mybus.alarmservice.ArrAlarmService.REMOVE")){
            Toast.makeText(context, "알람을 종료합니다", Toast.LENGTH_SHORT).show();
            Intent stopService = new Intent(context, ArrAlarmService.class);
            stopService.putExtra("stopService", "-1");
            context.startService(stopService);
        }
    }
}