package com.example.mybus.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.mybus.ActivityAnimate;
import com.example.mybus.MainActivity;
import com.example.mybus.R;
import com.example.mybus.databinding.ActivityFireBaseSyncBinding;
import com.example.mybus.viewmodel.FireBaseSyncViewModel;
import com.example.mybus.vo.LocalFav;
import com.example.mybus.vo.LocalFavStopBus;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FireBaseSyncActivity extends AppCompatActivity implements ActivityAnimate {
    private ActivityFireBaseSyncBinding binding;
    private FireBaseSyncViewModel fireBaseSyncViewModel;
    private SharedPreferences sharedPreferences;
    private String loginId;
    private List<LocalFav> localFavList = new ArrayList<>();
    private List<LocalFavStopBus> localFavStopBusList = new ArrayList<>();
    private int chkCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFireBaseSyncBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getLoginId();

        fireBaseSyncViewModel = new ViewModelProvider(this).get(FireBaseSyncViewModel.class);
        fireBaseSyncViewModel.clearLocalFavBus();
        fireBaseSyncViewModel.deleteFlag.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer flag) {
                if (flag>0){
                    Log.d("FireBaseSyncActivity", "clear all");
                    getFbFavList(loginId);
                    observeFsbList();
                    observeInsertFav();
                    observeInsertFavBus();
                }
            }
        });

    }

    public void getLoginId(){
        sharedPreferences = getSharedPreferences(LoginActivity.sharedId, MODE_PRIVATE);
        loginId = sharedPreferences.getString("loginId", null);
        if (loginId == null){
            finish();
            exitAnimate();
        }
    }

    public void getFbFavList(String loginId){
        fireBaseSyncViewModel.getFbList(loginId);
        fireBaseSyncViewModel.mutableLiveDataLocalFavList.observe(this, new Observer<List<LocalFav>>() {
            @Override
            public void onChanged(List<LocalFav> localFavLists) {
                if (localFavLists.size()>0){
                    Log.d("FireBaseSyncActivity", "FireBaseSyncActivity mutableLiveDataLocalFavList onChanged called :::::");
                    localFavList = localFavLists;
                    fireBaseSyncViewModel.insertFavAll(localFavList);
                }
            }
        });
    }

//    public void getFbFsbList(){
//        fireBaseSyncViewModel.getFavList(loginId, localFavList);
//    }

    public void observeFsbList(){
        fireBaseSyncViewModel.mutableLiveDataLocalFavBusList.observe(this, new Observer<List<LocalFavStopBus>>() {
            @Override
            public void onChanged(List<LocalFavStopBus> localFavStopBusLists) {
                if (localFavStopBusLists != null){
                    localFavStopBusList =localFavStopBusLists;
                    Log.d("FireBaseSyncActivity", "FireBaseSyncActivity mutableLiveDataLocalFavBusList onChanged called ::::: size : ");
                    chkCount++;
                    fireBaseSyncViewModel.insertFavBusAll(localFavStopBusList);
                }
            }
        });
    }

    public void observeInsertFav(){
        fireBaseSyncViewModel.insertLocalFav.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer chk) {
                if (chk > 0){
                    Log.d("FireBaseSyncActivity", "success!");
//                    getFbFsbList();
                    fireBaseSyncViewModel.getFavList(loginId, localFavList);
                }
            }
        });
    }

    public void observeInsertFavBus(){
        fireBaseSyncViewModel.insertLocalFavFab.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                Log.d("FireBaseSyncActivity", "count is   " + count);
                if (count == localFavList.size()){
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(FireBaseSyncActivity.this, "동기화 완료!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(FireBaseSyncActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 2000);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Toast.makeText(this, "동기화 중입니다 잠시만 기다려주세요", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveAnimate() {
        overridePendingTransition(R.anim.vertical_center, R.anim.none);
    }

    @Override
    public void exitAnimate() {
        overridePendingTransition(R.anim.none, R.anim.vertical_exit);
    }
}