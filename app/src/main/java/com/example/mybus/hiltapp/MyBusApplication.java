package com.example.mybus.hiltapp;

import android.app.Application;
import android.util.Log;

import com.example.mybus.R;
import com.kakao.sdk.common.KakaoSdk;

import java.io.IOException;
import java.net.SocketException;

import dagger.hilt.android.HiltAndroidApp;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.exceptions.UndeliverableException;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;

@HiltAndroidApp
public class MyBusApplication extends Application {

    private static MyBusApplication instance;




    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        KakaoSdk.init(this, this.getString(R.string.kakao_app_key));




        RxJavaPlugins.setErrorHandler(throwable -> {
                    if (throwable instanceof UndeliverableException) {
                        Log.d("kkang", "UndeliverableException" + throwable.getMessage() + "cause is : " + throwable.getCause());
                    }
                    if ((throwable instanceof IOException) || (throwable instanceof SocketException)){
                        Log.d("kkang", "IOException || SocketException" + throwable.getMessage()+ "cause is : " + throwable.getCause());
                        return;
                    }
                    if (throwable instanceof  InterruptedException){
                        return;
                    }
                    if ((throwable instanceof NullPointerException) || (throwable instanceof IllegalArgumentException)){
                        Thread.currentThread().getUncaughtExceptionHandler()
                                .uncaughtException(Thread.currentThread(), throwable);
                        return;
                    }
                    if (throwable instanceof IllegalStateException){
                        Thread.currentThread().getUncaughtExceptionHandler()
                                .uncaughtException(Thread.currentThread(), throwable);
                        return;
                    }

                    Log.d("kkang", "Undeliverable exception received, not sure what to do", throwable);

        });
    }



}
