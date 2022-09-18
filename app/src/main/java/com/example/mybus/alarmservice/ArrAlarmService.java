package com.example.mybus.alarmservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.mybus.R;
import com.example.mybus.apisearch.GbusWrapper.GBusRouteArriveInfoResponse;
import com.example.mybus.apisearch.GbusWrapper.GBusRouteArriveInfoWrap;
import com.example.mybus.apisearch.itemList.ArrInfoByRouteList;
import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.apisearch.itemList.GBusRouteArriveInfoList;
import com.example.mybus.apisearch.msgBody.ArrInfoByRoute;
import com.example.mybus.apisearch.wrapper.ArrInfoByRouteWrap;
import com.example.mybus.menu.alarm.AlarmArriveActivity;
import com.example.mybus.retrofitrepo.RetrofitGbusRepository;
import com.example.mybus.retrofitrepo.RetrofitRepository;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class ArrAlarmService extends Service {
    @Inject
    RetrofitRepository retrofitRepository;
    @Inject
    RetrofitGbusRepository retrofitGbusRepository;

    private BusSchList busSchList = new BusSchList();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String serviceKey;
    private NotificationManager manager;
    private NotificationCompat.Builder builder;
    private String ARR_ACTION = "com.example.mybus.alarmservice.ArrAlarmService.REMOVE";

    public ArrAlarmService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getStringExtra("stopService") != null){
            Log.d("ArrAlarmService", "onStartCommand if state");
            stopForegroundService();
        }else{
            Bundle bundle = intent.getExtras();
            busSchList = bundle.getParcelable("busSchList");
            serviceKey = intent.getStringExtra("serviceKey");
            generateNotification();
            if (busSchList.getStId().startsWith("1")){
                getArrDataInterval();
            }else if (busSchList.getStId().startsWith("2")){
                getGBusArrDataInterval();
            }

        }
        return START_NOT_STICKY;
    }

    public void generateNotification(){
        Intent deleteIntent = new Intent(this, ArrAlarmNotiReceiver.class);
        deleteIntent.setAction(ARR_ACTION);
        deleteIntent.putExtra("EXTRA_NOTIFICATION_ID","del");

        PendingIntent deletePendingIntent = PendingIntent.getBroadcast(this, 0, deleteIntent, PendingIntent.FLAG_IMMUTABLE);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = "ArrAlarm";
            String channelName = "ArrAlarmCh";
            String channelDescription = "ArrAlarmChDesc";
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            manager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(this, channelId);
        }else{
            builder = new NotificationCompat.Builder(this);
        }
        builder.setSmallIcon(R.drawable.ic_baseline_directions_bus_24);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle(busSchList.getBusRouteNm());
        builder.setContentText(busSchList.getFirstBusTm());
        builder.addAction(R.drawable.ic_baseline_directions_bus_24, "삭제", deletePendingIntent);
        Intent notificationIntent = new Intent(this, AlarmArriveActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        startForeground(123, notification);
    }

    public void updateNotification(ArrInfoByRouteList arrInfoByRouteList){
        Log.d("ArrAlarmService", "updatenotification called!");
        if (busSchList.getFirstLowTm().equals("1")){
            builder.setContentText(arrInfoByRouteList.getArrmsg1());
        }else{
            builder.setContentText(arrInfoByRouteList.getArrmsg2());
        }

        manager.notify(123, builder.build());
    }

    public void updateNotificationGbus(GBusRouteArriveInfoList gBusRouteArriveInfo){
        builder.setContentText(gBusRouteArriveInfo.getPredictTime1());
        if (busSchList.getFirstLowTm().equals("1")){
            builder.setContentText(gBusRouteArriveInfo.getPredictTime1() + "분 [" + gBusRouteArriveInfo.getLocationNo1()+"전]");
        }else{
            builder.setContentText(gBusRouteArriveInfo.getPredictTime2() + "분 [" + gBusRouteArriveInfo.getLocationNo2()+"전]");
        }
        manager.notify(123, builder.build());
    }

    public void getArrDataInterval(){
        if (busSchList != null){
            compositeDisposable.add(
                    Observable.interval(30, TimeUnit.SECONDS)
                            .flatMap(list -> retrofitRepository.getArrInfoByRoute(serviceKey, busSchList.getStId(), busSchList.getBusRouteId(), busSchList.getCorpNm(), "json"))
                            .repeat()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<ArrInfoByRouteWrap>() {
                                @Override
                                public void accept(ArrInfoByRouteWrap arrInfoByRouteWrap) throws Throwable {
                                    if (arrInfoByRouteWrap != null){
                                        Log.d("ArrAlarmViewModel", "getArrInfoByRoute sisze ::::::::" + arrInfoByRouteWrap.getArrInfoByRoute().getArrInfoByRouteLists().size());
                                        updateNotification(arrInfoByRouteWrap.getArrInfoByRoute().getArrInfoByRouteLists().get(0));
                                    }
                                }
                            }, error -> Log.d("ArrAlarmViewModel", "getArrInfoByRoute error!!" + error.getMessage()))
            );
        }
    }

    public void getGBusArrDataInterval(){
        compositeDisposable.add(
                Observable.interval(30, TimeUnit.SECONDS)
                        .flatMap(list -> retrofitGbusRepository.getGBusArrInfoByRoute(serviceKey, busSchList.getStId(), busSchList.getBusRouteId(), busSchList.getCorpNm()))
                        .repeat()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<GBusRouteArriveInfoResponse>() {
                            @Override
                            public void accept(GBusRouteArriveInfoResponse gBusRouteArriveInfoResponse) throws Throwable {
                                if (gBusRouteArriveInfoResponse.getgBusLocationWrap() != null){
                                    Log.d("ArrAlarmViewModel", "getGBusArrDataInterval sisze ::::::::" + gBusRouteArriveInfoResponse.getgBusLocationWrap().getBusArriveInfoLists().size());
                                    updateNotificationGbus(gBusRouteArriveInfoResponse.getgBusLocationWrap().getBusArriveInfoLists().get(0));
                                }
                            }
                        }, error -> Log.d("ArrAlarmViewModel", "getArrInfoByRoute error!!" + error.getMessage()))
        );
    }

    public void stopForegroundService(){
        stopForeground(true);
        stopSelf();
        compositeDisposable.dispose();
        Log.d("ArrAlarmService", "stop service");
    }
}