package com.example.mybus.hiltapp;

import android.app.Application;

import com.example.mybus.R;
import com.kakao.sdk.common.KakaoSdk;

import dagger.hilt.android.HiltAndroidApp;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltAndroidApp
public class MyBusApplication extends Application {
    private static MyBusApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        KakaoSdk.init(this, this.getString(R.string.kakao_app_key));
    }
}
