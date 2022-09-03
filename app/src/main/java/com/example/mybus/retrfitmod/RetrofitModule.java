package com.example.mybus.retrfitmod;


import com.example.mybus.BuildConfig;
import com.example.mybus.retrofitrepo.RetrofitGbusRepository;
import com.example.mybus.retrofitrepo.RetrofitGbusService;
import com.example.mybus.retrofitrepo.RetrofitRepository;
import com.example.mybus.retrofitrepo.RetrofitService;
import com.tickaroo.tikxml.TikXml;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class RetrofitModule {

    @Provides
    String provideBaseUrl() {
        // 서울시 공통 url
        return "http://ws.bus.go.kr/api/rest/";
    }

    @Singleton
    @Provides
    @Named("http_sbus")
    OkHttpClient provideHttpClient() {
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            return new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
        } else {
            return new OkHttpClient.Builder().build();
        }
    }

    @Singleton
    @Provides
    @Named("retrofit_sbus")
    Retrofit provideRetrofit(@Named("http_sbus") OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(provideBaseUrl())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    RetrofitService provideRetrofitService(@Named("retrofit_sbus") Retrofit retrofit){
        return retrofit.create(RetrofitService.class);
    }

    @Singleton
    @Provides
    RetrofitRepository provideRetrofitRepository(RetrofitService retrofitService){
        return new RetrofitRepository(retrofitService);
    }




}
