package com.example.mybus.menu.alarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.mybus.MainActivity;
import com.example.mybus.R;
import com.example.mybus.alarmservice.ArrAlarmService;
import com.example.mybus.apisearch.itemList.ArrInfoByRouteList;
import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.databinding.ActivityAlarmArriveBinding;
import com.example.mybus.searchDetail.SearchDetailAdapter;
import com.example.mybus.viewmodel.ArrAlarmViewModel;

import dagger.hilt.android.AndroidEntryPoint;

// 도착 알람 설정
@AndroidEntryPoint
public class AlarmArriveActivity extends AppCompatActivity {
    private ActivityAlarmArriveBinding binding;
    private Toolbar toolbar;
    private BusSchList busSchList;
    private ArrAlarmViewModel arrAlarmViewModel;
    private Intent serviceIntent;
    private CountDownTimer countDownTimer;
    private Bundle args;
    private ArrInfoByRouteList arrInfoByRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlarmArriveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        arrAlarmViewModel = new ViewModelProvider(this).get(ArrAlarmViewModel.class);
        initView();
        getDataFromIntent();

        binding.addAlarm.setOnClickListener(view -> {
            if (arrInfoByRoute != null){
                startService(1);
            }
        });

        binding.addAlarm2.setOnClickListener(view -> {
            if (arrInfoByRoute != null){
                startService(2);
            }
        });
    }

    public void initView(){
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle(" 도착 알람 ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add_arrive_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.action_home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getDataFromIntent(){
        Bundle bundle = getIntent().getExtras();
        busSchList = bundle.getParcelable("busList");
        if (busSchList != null){
            if (busSchList.getCorpNm().equals("-1")){
                // 메인화면에서 온 경우
            }else{
                // 정류장 상세보기에서 온 경우
                arrAlarmViewModel.getArrInfoByRoute(busSchList.getStId(), busSchList.getBusRouteId(), busSchList.getCorpNm());
                observeArrInfoData();
            }
        }
    }

    public void observeArrInfoData(){
        arrAlarmViewModel.arrInfoData.observe(this, new Observer<ArrInfoByRouteList>() {
            @Override
            public void onChanged(ArrInfoByRouteList arrInfoByRouteList) {
                if (arrInfoByRouteList != null){
                    arrInfoByRoute = arrInfoByRouteList;
                    binding.busRouteName.setText(arrInfoByRouteList.getRtNm());
                    setTimeTexts(arrInfoByRouteList.getArrmsg1(), 1);
                    setTimeTexts(arrInfoByRouteList.getArrmsg2(), 2);
                }
            }
        });
    }

    public void setTimeTexts(String time, int flag){
        try{
            long conversionTime = 0;
            String remainStops = time.substring(time.indexOf('[')+1, time.indexOf(']'));
            String getMinute = time.substring(0, time.indexOf("분"));
            String getSeconds = time.substring(time.indexOf("분")+1, time.indexOf("초"));

            conversionTime = Long.valueOf(getMinute) * 60 * 1000 + Long.valueOf(getSeconds) * 1000;

            countDownTimer = new CountDownTimer(conversionTime, 3000) {
                // fab 통해서 주기적으로 새로고침 시켜주기
                @Override
                public void onTick(long milliUntilFinished) {
                    long getMin = milliUntilFinished - (milliUntilFinished / (60 * 60 * 1000));
                    String min = String.valueOf(getMin / (60 * 1000));      // 몫
                    String second = String.valueOf((getMin % (60 * 1000)) / 1000);

                    if (flag == 1){
                       binding.firstRemainTime.setText(min +" 분 " + second +" 초 ");
                       binding.firstRemainSeat.setText(remainStops);
                    }else{
                        binding.secondRemainTime.setText(min +" 분 " + second +" 초 ");
                        binding.secondRemainSeat.setText(remainStops);
                    }

                }

                @Override
                public void onFinish() {
                    if (flag == 1){
                        binding.firstRemainTime.setText("시간 초과");
                    }else{
                        binding.firstRemainTime.setText("시간 초과");
                    }

                }
            }.start();

        }catch(Exception e){
            Log.d("kkang", "Exception in searchdetailadapter setremaintime method msg : " + e.getMessage());
            if (flag == 1){
                binding.firstRemainTime.setText(time);
            }else {
                binding.secondRemainTime.setText(time);
            }


        }
    }

    public void startService(int flag){
        if (flag == 1){
            busSchList.setFirstBusTm(arrInfoByRoute.getArrmsg1());
        }else{
            busSchList.setFirstBusTm(arrInfoByRoute.getArrmsg2());
        }
        serviceIntent = new Intent(this, ArrAlarmService.class);
        args = new Bundle();
        args.putParcelable("busSchList", busSchList);
        serviceIntent.putExtras(args);
        serviceIntent.putExtra("serviceKey", arrAlarmViewModel.getServiceKey());
        startService(serviceIntent);
    }
}