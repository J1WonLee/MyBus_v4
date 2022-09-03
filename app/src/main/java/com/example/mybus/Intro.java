package com.example.mybus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.mybus.menu.LoginActivity;
import com.kakao.sdk.user.UserApiClient;



public class Intro extends AppCompatActivity {
    private ConnectivityManager connManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (isNetwrokAvailable()){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    chkLogin();
                }
            }, 3000);
        }else{
            Toast.makeText(this, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
            finish();
        }



    }

    public boolean isNetwrokAvailable(){
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
           Network network = connManager.getActiveNetwork();
           if (network != null){
               return true;
           }else{
               return false;
           }
       }else{
           NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
           if (networkInfo != null){
               return true;
           }else{
               return false;
           }
       }
    }

    public void chkLogin(){
        UserApiClient.getInstance().me((user, error) -> {
            if (error != null){
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(this, Main.class);
                startActivity(intent);
                finish();
            }
            return null;
        });

    }



}