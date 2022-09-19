package com.example.mybus.menu.alarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.example.mybus.MainActivity;
import com.example.mybus.R;
import com.example.mybus.alarmservice.ArrAlarmService;
import com.example.mybus.apisearch.itemList.ArrInfoByRouteList;
import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.apisearch.itemList.GBusRouteArriveInfoList;
import com.example.mybus.databinding.ActivityAlarmArriveBinding;
import com.example.mybus.menu.LoginActivity;
import com.example.mybus.searchDetail.SearchDetailAdapter;
import com.example.mybus.viewmodel.ArrAlarmViewModel;
import com.example.mybus.vo.ArrAlarmPref;
import com.google.gson.Gson;

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
    private boolean isEndF1;
    private boolean isEndF2;
    private GBusRouteArriveInfoList gBusRouteArriveInfo;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private ArrAlarmPref arrAlarm;
    private MutableLiveData<ArrAlarmPref> arrAlarmPrefMutableLiveData = new MutableLiveData<>();
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlarmArriveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        arrAlarmViewModel = new ViewModelProvider(this).get(ArrAlarmViewModel.class);
        initView();
        setAlarmImage();
        getDataFromIntent();

        // 알람 삭제시 반영.
        ArrAlarmService.deleteFlag.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 1){
                    Log.d("AlarmArriveActivity", "detected changed!" + integer);
                    binding.addAlarm.setImageResource(R.drawable.ic_baseline_add_alarm_24);
                    binding.addAlarm2.setImageResource(R.drawable.ic_baseline_add_alarm_24);
                }
            }
        });

        binding.addAlarm.setOnClickListener(view -> {
            if (arrAlarm != null){
                // 다이얼 로그
                initDialog();
            }else{
                if (arrInfoByRoute != null && !isEndF1){
                    startService(1);
                    insertArrAlarm(arrInfoByRoute.getStId(), arrInfoByRoute.getBusRouteId(), 1);
                }else if (gBusRouteArriveInfo != null && !isEndF1){
                    startGBusService(1);
                    insertArrAlarm(busSchList.getStId(), busSchList.getBusRouteId(), 1);
                }
            }
        });

        binding.addAlarm2.setOnClickListener(view -> {
            if (arrAlarm != null){
                // 다이얼 로그
                initDialog();
            }else{
                if (arrInfoByRoute != null && !isEndF2){
                    startService(2);
                    insertArrAlarm(arrInfoByRoute.getStId(), arrInfoByRoute.getBusRouteId(), 2);
                }else if (gBusRouteArriveInfo != null && !isEndF2){
                    startGBusService(2);
                    insertArrAlarm(busSchList.getStId(), busSchList.getBusRouteId(), 2);
                }
            }

        });
    }

    public void initView(){
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" 도착 알람 ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getArrAlarm();
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
            if (busSchList.getStId().startsWith("2")){
                // 경기도 버스인 경우
                arrAlarmViewModel.getGBusArrInfoByRoute(busSchList.getStId(), busSchList.getBusRouteId(), busSchList.getCorpNm());
                observeGbusArrInfoData();
            }else{
                // 서울인 경우
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

    public void observeGbusArrInfoData(){
        arrAlarmViewModel.arrGbusInfoData.observe(this, new Observer<GBusRouteArriveInfoList>() {
            @Override
            public void onChanged(GBusRouteArriveInfoList gBusRouteArriveInfoList) {
                if (gBusRouteArriveInfoList != null){
                    gBusRouteArriveInfo = gBusRouteArriveInfoList;
                    binding.busRouteName.setText(busSchList.getBusRouteNm());
                    gBusSetRemainTime(gBusRouteArriveInfo.getPredictTime1(), 1, gBusRouteArriveInfo.getLocationNo1());
                    gBusSetRemainTime(gBusRouteArriveInfo.getPredictTime2(), 2, gBusRouteArriveInfo.getLocationNo2());
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
            Log.d("AlarmArriveActivity", "Exception in AlarmArriveActivity setremaintime method msg : " + e.getMessage());
            if (flag == 1){
                binding.firstRemainTime.setText(time);
                binding.firstRemainSeat.setVisibility(View.INVISIBLE);
                isEndF1 = true;
            }else {
                binding.secondRemainTime.setText(time);
                binding.secondRemainSeat.setVisibility(View.INVISIBLE);
                isEndF2 = true;
            }


        }
    }

    public void gBusSetRemainTime( String time, int flag, String remainStops){
        try{
            long conversionTime = 0;
            String getMinute = time;
            int getSeconds = 0;

            conversionTime = Long.valueOf(getMinute) * 60 * 1000 + (getSeconds * 1000);

            countDownTimer = new CountDownTimer(conversionTime, 3000) {
                @Override
                public void onTick(long milliUntilFinished) {
                    long getMin = milliUntilFinished - (milliUntilFinished / (60 * 60 * 1000));
                    String min = String.valueOf(getMin / (60 * 1000));      // 몫
                    String second = String.valueOf((getMin % (60 * 1000)) / 1000);

                    if (flag == 1){
                        binding.firstRemainTime.setText(min +" 분 " + second +" 초 ");
                        binding.firstRemainSeat.setText(remainStops + "전");
                    }else{
                        binding.secondRemainTime.setText(min +" 분 " + second +" 초 ");
                        binding.secondRemainSeat.setText(remainStops + "전");
                    }
                }

                @Override
                public void onFinish() {
                    if (flag == 1){
                        binding.firstRemainTime.setText("시간 초과");
                        binding.firstRemainSeat.setText("");
                    }else{
                        binding.firstRemainTime.setText("시간 초과");
                        binding.firstRemainSeat.setText("");
                    }
                }
            }.start();
        }catch(Exception e){
            Log.d("AlarmArriveActivity", "Exception in AlarmArriveActivity gbussetRemainTime method msg : " + e.getMessage());
            if (flag == 1){
                binding.firstRemainTime.setText("운행 중인 버스가 없습니다");
                binding.firstRemainSeat.setVisibility(View.INVISIBLE);
                isEndF1 = true;
            }else {
                binding.secondRemainTime.setText("운행 중인 버스가 없습니다");
                binding.secondRemainSeat.setVisibility(View.INVISIBLE);
                isEndF2 = true;
            }
        }
    }

    public void startService(int flag){
        if (flag == 1){
            busSchList.setFirstBusTm(arrInfoByRoute.getArrmsg1());
            busSchList.setFirstLowTm("1");
        }else{
            busSchList.setFirstBusTm(arrInfoByRoute.getArrmsg2());
            busSchList.setFirstLowTm("2");
        }
        serviceIntent = new Intent(this, ArrAlarmService.class);
        args = new Bundle();
        args.putParcelable("busSchList", busSchList);
        serviceIntent.putExtras(args);
        serviceIntent.putExtra("serviceKey", arrAlarmViewModel.getServiceKey());
        startService(serviceIntent);
    }

    public void startGBusService(int flag){
        if (flag == 1){
            busSchList.setFirstBusTm(gBusRouteArriveInfo.getPredictTime1() + "분 [" + gBusRouteArriveInfo.getLocationNo1()+"전]");
            busSchList.setFirstLowTm("1");
        }else{
            busSchList.setFirstBusTm(gBusRouteArriveInfo.getPredictTime2() + "분 [" + gBusRouteArriveInfo.getLocationNo2()+"전]");
            busSchList.setFirstLowTm("2");
        }
        serviceIntent = new Intent(this, ArrAlarmService.class);
        args = new Bundle();
        args.putParcelable("busSchList", busSchList);
        serviceIntent.putExtras(args);
        serviceIntent.putExtra("serviceKey", arrAlarmViewModel.getServiceKey());
        startService(serviceIntent);
    }

    // 알람 저장 여부를 확인한다
//    public void getArrAlarm(){
//        sharedPreferences = getSharedPreferences(LoginActivity.sharedId, MODE_PRIVATE);
//        gson = new Gson();
//        arrAlarmPref = gson.fromJson(sharedPreferences.getString("ArrAlarm", ""), ArrAlarmPref.class);
//        if (arrAlarmPref != null){
//            arrAlarmPrefMutableLiveData.setValue(arrAlarmPref);
//        }
//    }

//    public void setArrAlarm(String stid, String routeId, int flag){
//        arrAlarmPref = new ArrAlarmPref(stid, routeId, flag);
//        gson = new Gson();
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("ArrAlarm", gson.toJson(arrAlarmPref));
//        editor.apply();
//    }

//    public void setAddAlarmImage(){
//        if (arrAlarmPref!= null){
//            if (arrAlarmPref.getStId().equals(busSchList.getStId()) && arrAlarmPref.getRouteId().equals(busSchList.getBusRouteId())){
//                if (arrAlarmPref.getFlag() == 1){
//                    binding.addAlarm.setImageResource(R.drawable.ic_baseline_alarm_on_24);
//                }else{
//                    binding.addAlarm2.setImageResource(R.drawable.ic_baseline_alarm_on_24);
//                }
//            }
//        }
//    }

    public void getArrAlarm(){
        arrAlarmViewModel.getArrAlarm();
    }

    public void insertArrAlarm(String stid, String routeId, int flag){
        arrAlarmViewModel.insertArrAlarm(new ArrAlarmPref(stid, routeId, flag));
        if (flag == 1){
//            binding.addAlarm.setImageResource(R.drawable.ic_baseline_alarm_on_24);
        }else{
//            binding.addAlarm2.setImageResource(R.drawable.ic_baseline_alarm_on_24);
        }
    }

    public void setAlarmImage(){
        arrAlarmViewModel.arrAlarmPrefMutableLiveData.observe(this, new Observer<ArrAlarmPref>() {
            @Override
            public void onChanged(ArrAlarmPref arrAlarmPref) {
                Log.d("AlarmArriveActivity", "changed detected!!");
                if (arrAlarmPref!= null){
                    arrAlarm = arrAlarmPref;
                    if (arrAlarmPref.getStId().equals(busSchList.getStId()) && arrAlarmPref.getRouteId().equals(busSchList.getBusRouteId())){
                        if (arrAlarmPref.getFlag() == 1){
                            binding.addAlarm.setImageResource(R.drawable.ic_baseline_alarm_on_24);
                        }else{
                            binding.addAlarm2.setImageResource(R.drawable.ic_baseline_alarm_on_24);
                        }
                    }
                }else{
                    binding.addAlarm.setImageResource(R.drawable.ic_baseline_add_alarm_24);
                    binding.addAlarm2.setImageResource(R.drawable.ic_baseline_add_alarm_24);
                }
            }
        });
    }

    public void initDialog() {
        Log.d("BusRouteDetailActivity", "initDialog!");
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.arr_alarm_dialog);
        dialog.show();
    }
}