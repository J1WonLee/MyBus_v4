package com.example.mybus.alarmservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;

import androidx.core.app.NotificationCompat;

import com.example.mybus.R;
import com.example.mybus.apisearch.itemList.ArrInfoByRouteList;
import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.apisearch.wrapper.ArrInfoByRouteWrap;
import com.example.mybus.menu.alarm.AlarmArriveActivity;
import com.example.mybus.retrofitrepo.RetrofitGbusRepository;
import com.example.mybus.retrofitrepo.RetrofitRepository;
import com.example.mybus.viewmodel.ArrAlarmViewModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AlarmService extends Service {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private NotificationManager manager;
    private NotificationCompat.Builder builder;
    private Bundle bundle;
    private BusSchList busSchList;
    private PowerManager pm;
    public AlarmService() {}
    public static String WAKELOCK_TAG = "wakeTag";
    private PowerManager.WakeLock wl;

    @Override
    public void onCreate() {
        super.onCreate();
        setWakeLock();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("AlarmService", "onstartcommand called ::::::");
        if (intent.getStringExtra("stopAlarm") != null){
            stopForegroundService();
        }else{
            bundle = intent.getExtras();
            busSchList = bundle.getParcelable("busList");
            if (busSchList != null){
//                getArrInfoByRoute(busSchList.getStId(), busSchList.getBusRouteId(), busSchList.getCorpNm());
                generateNotification();
            }
        }
        return START_NOT_STICKY;
    }

    public void generateNotification(){
        Intent deleteIntent = new Intent(this, AlarmReceiver.class);
        deleteIntent.setAction("stop_alarm");
        deleteIntent.putExtra("EXTRA_NOTIFICATION_ID","del");
        PendingIntent deletePendingIntent = PendingIntent.getBroadcast(this, 0, deleteIntent, PendingIntent.FLAG_IMMUTABLE);

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = "ArrAlarm";
            String channelName = "ArrAlarmCh";
            String channelDescription = "ArrAlarmChDesc";
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            manager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(this, channelId);
        }else{
            builder = new NotificationCompat.Builder(this);
        }
        builder.setSmallIcon(R.drawable.ic_baseline_directions_bus_24);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle(busSchList.getBusRouteNm());
        builder.setContentText("버스 도착 알람이 도착하였습니다!!");
        builder.addAction(R.drawable.ic_baseline_directions_bus_24, "확인", deletePendingIntent);

        Intent notificationIntent = new Intent(this, AlarmArriveActivity.class);
        bundle = new Bundle();
        bundle.putParcelable("busList", busSchList);
        notificationIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        startForeground(123, notification);
    }

//    public void getArrInfoByRoute( String stId, String routeId, String ord){
//        compositeDisposable.add(
//                retrofitRepository.getArrInfoByRouteInit(serviceKey , stId, routeId, ord, "json")
//                        .subscribeOn(Schedulers.newThread())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeWith(new DisposableSingleObserver<ArrInfoByRouteWrap>() {
//                            @Override
//                            public void onSuccess(@NonNull ArrInfoByRouteWrap arrInfoByRouteWrap) {
//                                if (arrInfoByRouteWrap != null){
//                                    generateNotification(arrInfoByRouteWrap.getArrInfoByRoute().getArrInfoByRouteLists().get(0));
//                                    Log.d("AlarmService", "getArrinfobyroute!!");
//                                }
//                            }
//
//                            @Override
//                            public void onError(@NonNull Throwable e) {
//                                Log.d("AlarmService", "getArrInfoByRoute error!!" + e.getMessage());
//                            }
//                        })
//        );
//    }

    public void setWakeLock(){
        pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON , WAKELOCK_TAG);
        // PARTIAL_WAKE_LOCK
        wl.acquire(30000);
        Log.d("AlarmService", "setWakeLock service");
    }

    public void stopForegroundService(){
        stopForeground(true);
        stopSelf();
        wl.release();
        compositeDisposable.dispose();
        Log.d("AlarmService", "stop service");
    }

}