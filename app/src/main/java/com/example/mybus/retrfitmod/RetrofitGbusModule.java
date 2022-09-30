package com.example.mybus.retrfitmod;

import com.example.mybus.BuildConfig;
import com.example.mybus.retrofitrepo.RetrofitGbusRepository;
import com.example.mybus.retrofitrepo.RetrofitGbusService;
import com.tickaroo.tikxml.TikXml;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import java.lang.annotation.Retention;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Qualifier;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import kotlin.annotation.AnnotationRetention;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class RetrofitGbusModule {

    // 경기도 공통 url
    @Provides
    String provideBaseUrlForGbus(){
        return "http://apis.data.go.kr/6410000/";
    }


    @Singleton
    @Provides
    @Named("http_gbus")
    OkHttpClient provideHttpClient() {
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            return new OkHttpClient.Builder().addInterceptor(loggingInterceptor)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
        } else {
            return new OkHttpClient.Builder().build();
        }
    }

    // 경기도 api retrofit
    @Singleton
    @Provides
    @Named("retrofit_gbus")
    Retrofit provideRetrofitGbus(@Named("http_gbus") OkHttpClient okHttpClient){
        TikXml tikXml = new TikXml.Builder().exceptionOnUnreadXml(false).build();
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(provideBaseUrlForGbus())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(TikXmlConverterFactory.create(tikXml))
                .build();
    }

    @Singleton
    @Provides
    RetrofitGbusService provideRetrofitGbusService(@Named("retrofit_gbus") Retrofit retrofit){
        return retrofit.create(RetrofitGbusService.class);
    }

    @Singleton
    @Provides
    RetrofitGbusRepository provideRetrofitGbusRepository(RetrofitGbusService retrofitService){
        return new RetrofitGbusRepository(retrofitService);
    }
}
