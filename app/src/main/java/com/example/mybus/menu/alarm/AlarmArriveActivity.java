package com.example.mybus.menu.alarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybus.ActivityAnimate;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import dagger.hilt.android.AndroidEntryPoint;

// 도착 알람 설정
@AndroidEntryPoint
public class AlarmArriveActivity extends AppCompatActivity implements ActivityAnimate {
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
    private String stId;
    private String routeId;
    private int curFlag;
    private ArrAlarmPref arrAlarm;
    private Dialog dialog;
    private boolean isDupliAlarm;
    private FloatingActionButton floatingActionButton;
    private long mLastClickTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlarmArriveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        arrAlarmViewModel = new ViewModelProvider(this).get(ArrAlarmViewModel.class);
        initView();
        setAlarmImage();
        getDataFromIntent();
        setRefreshBtnListener();

        // 알람 삭제시 반영.
        ArrAlarmService.deleteFlag.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 1){
                    Log.d("AlarmArriveActivity", "detected changed!" + integer);
                    binding.addAlarm.setImageResource(R.drawable.ic_baseline_add_alarm_24);
                    binding.addAlarm2.setImageResource(R.drawable.ic_baseline_add_alarm_24);
                    arrAlarm = null;
                }
            }
        });

        binding.addAlarm.setOnClickListener(view -> {
            clickEvent();
        });

        binding.addAlarm2.setOnClickListener(view -> {
            clickEventBtn2();
        });
    }

    public void initView(){
        floatingActionButton = binding.arrAlarmRefreshBtn;
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
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;

            case android.R.id.home:
                if (getIntent().getAction() != null){
                    intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    this.finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getDataFromIntent(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            busSchList = bundle.getParcelable("busList");
            getArriveInfo();
        }
//        if (bundle != null && busSchList != null){
//
//        }
    }

    public void getArriveInfo(){
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

    public void getArrAlarm(){
        arrAlarmViewModel.getArrAlarm();
    }

    public void insertArrAlarm(String stid, String routeId, int flag){
        arrAlarmViewModel.insertArrAlarm(new ArrAlarmPref(stid, routeId, flag));
    }

    public void setAlarmImage(){
        arrAlarmViewModel.arrAlarmPrefMutableLiveData.observe(this, new Observer<ArrAlarmPref>() {
            @Override
            public void onChanged(ArrAlarmPref arrAlarmPref) {
                Log.d("AlarmArriveActivity", "changed detected!!");
                if (arrAlarmPref!= null){
                    arrAlarm = arrAlarmPref;
                    if (arrAlarmPref.getStId().equals(busSchList.getStId()) && arrAlarmPref.getRouteId().equals(busSchList.getBusRouteId())){
                        stId = arrAlarm.getStId();
                        routeId = arrAlarm.getRouteId();
                        curFlag = arrAlarm.getFlag();
                        if (arrAlarmPref.getFlag() == 1){
                            binding.addAlarm.setImageResource(R.drawable.ic_baseline_alarm_on_24);
                        }else{
                            binding.addAlarm2.setImageResource(R.drawable.ic_baseline_alarm_on_24);
                        }
                    }
                }else{
                    arrAlarm = null;
                    binding.addAlarm.setImageResource(R.drawable.ic_baseline_add_alarm_24);
                    binding.addAlarm2.setImageResource(R.drawable.ic_baseline_add_alarm_24);
                }
            }
        });
    }

    public void clickEvent(){
        if (arrAlarm != null){
            // 다이얼 로그
            initDialog(1);
        }else{
            if (arrInfoByRoute != null && !isEndF1){
                startService(1);
                insertArrAlarm(arrInfoByRoute.getStId(), arrInfoByRoute.getBusRouteId(), 1);
            }else if (gBusRouteArriveInfo != null && !isEndF1){
                startGBusService(1);
                insertArrAlarm(busSchList.getStId(), busSchList.getBusRouteId(), 1);
            }
        }
    }

    public void clickEventBtn2(){
        if (arrAlarm != null){
            // 다이얼 로그
            initDialog(2);
        }else{
            if (arrInfoByRoute != null && !isEndF2){
                startService(2);
                insertArrAlarm(arrInfoByRoute.getStId(), arrInfoByRoute.getBusRouteId(), 2);
            }else if (gBusRouteArriveInfo != null && !isEndF2){
                startGBusService(2);
                insertArrAlarm(busSchList.getStId(), busSchList.getBusRouteId(), 2);
            }
        }
    }

    public void initDialog(int flag) {
        Log.d("BusRouteDetailActivity", "initDialog!");
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.arr_alarm_dialog);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(params);

        if (stId.equals(busSchList.getStId()) && routeId.equals(busSchList.getBusRouteId()) && flag == curFlag){
            isDupliAlarm = true;
            TextView text  = dialog.findViewById(R.id.arr_alarm_text2);
            dialog.findViewById(R.id.arr_alarm_text1).setVisibility(View.INVISIBLE);
            text.setText("알람을 제거하시겠습니까?");
        }
        dialog.show();
        dialogListener(dialog, flag, isDupliAlarm);
//        dialog.findViewById(R.id.arr_alarm_dialog_yes_btn).setOnClickListener(view -> {
//            arrAlarmViewModel.deleteArrAlarm();
//            stopArrAlarmService();
//            if (flag == 1){
//                if (arrInfoByRoute != null && !isEndF1){
//                    startService(1);
//                    insertArrAlarm(arrInfoByRoute.getStId(), arrInfoByRoute.getBusRouteId(), 1);
//                }else if (gBusRouteArriveInfo != null && !isEndF1){
//                    startGBusService(1);
//                    insertArrAlarm(busSchList.getStId(), busSchList.getBusRouteId(), 1);
//                }
//            }else if (flag == 2){
//                if (arrInfoByRoute != null && !isEndF2){
//                    startService(2);
//                    insertArrAlarm(arrInfoByRoute.getStId(), arrInfoByRoute.getBusRouteId(), 2);
//                }else if (gBusRouteArriveInfo != null && !isEndF2){
//                    startGBusService(2);
//                    insertArrAlarm(busSchList.getStId(), busSchList.getBusRouteId(), 2);
//                }
//            }else{
//                stopArrAlarmServiceDup();
//            }
//            dialog.cancel();
//        });

        dialog.findViewById(R.id.arr_alarm_dialog_no_btn).setOnClickListener(view -> {
            dialog.cancel();
        });
    }

    public void dialogListener(Dialog dialog, int flag, boolean isDupliAlarm){
        dialog.findViewById(R.id.arr_alarm_dialog_yes_btn).setOnClickListener(view -> {
            arrAlarmViewModel.deleteArrAlarm();
            stopArrAlarmService();
            if (flag == 1 && !isDupliAlarm){
                if (arrInfoByRoute != null && !isEndF1){
                    startService(1);
                    insertArrAlarm(arrInfoByRoute.getStId(), arrInfoByRoute.getBusRouteId(), 1);
                }else if (gBusRouteArriveInfo != null && !isEndF1){
                    startGBusService(1);
                    insertArrAlarm(busSchList.getStId(), busSchList.getBusRouteId(), 1);
                }
            }else if (flag == 2 && !isDupliAlarm){
                if (arrInfoByRoute != null && !isEndF2){
                    startService(2);
                    insertArrAlarm(arrInfoByRoute.getStId(), arrInfoByRoute.getBusRouteId(), 2);
                }else if (gBusRouteArriveInfo != null && !isEndF2){
                    startGBusService(2);
                    insertArrAlarm(busSchList.getStId(), busSchList.getBusRouteId(), 2);
                }
            }else{
                stopArrAlarmServiceDup();
            }
            dialog.cancel();
        });

    }

    public void stopArrAlarmService(){
        isEndF2 = false;    isEndF1 = false;    isDupliAlarm = false;
        Intent stopService = new Intent(AlarmArriveActivity.this, ArrAlarmService.class);
        stopService.putExtra("stopApiCall", "-1");
        startService(stopService);
    }

    public void stopArrAlarmServiceDup(){
        isEndF2 = false;    isEndF1 = false;    isDupliAlarm = false;
        Intent stopService = new Intent(AlarmArriveActivity.this, ArrAlarmService.class);
        stopService.putExtra("stopService", "stop");
        startService(stopService);
    }

    public void setRefreshBtnListener(){
        floatingActionButton.setOnClickListener(view -> {
            try {
                if (SystemClock.elapsedRealtime() - mLastClickTime > 5000) {
                    getArriveInfo();
                }
                mLastClickTime = SystemClock.elapsedRealtime();
            }
            catch (Exception e){
                Log.d("MainActivity", "setRefreshBtnListener error : " + e.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().getAction() != null){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            exitAnimate();
        }else{
            finishAfterTransition();
        }
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