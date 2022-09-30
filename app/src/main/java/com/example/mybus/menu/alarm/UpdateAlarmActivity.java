package com.example.mybus.menu.alarm;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mybus.ActivityAnimate;
import com.example.mybus.MainActivity;
import com.example.mybus.R;
import com.example.mybus.alarmservice.AlarmReceiver;
import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.databinding.ActivityAddAlarmBinding;
import com.example.mybus.menu.MyAlarmActivity;
import com.example.mybus.viewmodel.UpdateAlarmViewModel;
import com.example.mybus.vo.SchAlarmInfo;

import java.util.Calendar;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UpdateAlarmActivity extends AppCompatActivity implements ActivityAnimate {
    private ActivityAddAlarmBinding binding;
    private AlarmManager alarmManager;
    private TimePicker timePicker;
    private Toolbar toolbar;
    private int hour, minute;
    private boolean[] weeks = {false, false, false, false, false, false, false, false};
    private Bundle bundle;
    private Bundle args;
    private SchAlarmInfo schAlarmInfo;
    private SchAlarmInfo prevSchAlarmInfo;
    private UpdateAlarmViewModel updateAlarmViewModel;
    private int alarmId;
    private String dates = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        updateAlarmViewModel = new ViewModelProvider(this).get(UpdateAlarmViewModel.class);
        initView();
        getDataFromIntent();
        getDateFromBtn();

        binding.confirmBtn.setText("수정 하기");
        setBtnClick();
        binding.alarmTitle.setOnClickListener(view -> {
            args = new Bundle();
            args.putParcelable("updateAlarm", schAlarmInfo);
            Intent favListIntent = new Intent(this, AddAlarmFavListActivity.class);
            favListIntent.putExtras(args);
            startActivity(favListIntent);
        });

        updateAlarmViewModel.isUpdatedLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer>0){
                    Toast.makeText(UpdateAlarmActivity.this, "알람을 수정했습니다", Toast.LENGTH_SHORT).show();
                    Intent myAlarmIntent = new Intent(UpdateAlarmActivity.this, MyAlarmActivity.class);
                    startActivity(myAlarmIntent);
                    finish();
                    exitAnimate();
                }
            }
        });
    }

    public void initView(){
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        timePicker = binding.timePicker;
        toolbar = binding.addAlarm;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" 스케줄 알람 수정 ");
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
            case android.R.id.home:
                intent = new Intent(this, MyAlarmActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                this.finishAfterTransition();
                break;

            case R.id.action_home:
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                this.finishAfterTransition();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getDataFromIntent(){
        bundle = getIntent().getExtras();
        if(bundle!= null){
            schAlarmInfo = bundle.getParcelable("updateAlarm");
            prevSchAlarmInfo = bundle.getParcelable("prevUpdatedAlarm");
        }else{
            finish();
            exitAnimate();
        }
        if (schAlarmInfo != null){
            binding.alarmTitle.setText(schAlarmInfo.getAlarm_stop_nm() + " / " + schAlarmInfo.getAlarm_bus_nm() +"번 버스");
            setViews();
        }
    }

    public void setViews(){
        // TimePicker 시간 설정
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(schAlarmInfo.getAlarm_date());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(c.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(c.get(Calendar.MINUTE));
        }
        // dates 버튼 활성화
        String[] dates = schAlarmInfo.getWeeks().substring(1).split(",");
        for (int i=0; i<dates.length; i++){
            switch (dates[i]){
                case "2":
                    weeks[2] = true;
                    binding.mon.setBackgroundResource(R.drawable.button_click_deco);
                    break;
                case "3":
                    weeks[3] = true;
                    binding.tue.setBackgroundResource(R.drawable.button_click_deco);
                    break;
                case "4":
                    weeks[4] = true;
                    binding.wen.setBackgroundResource(R.drawable.button_click_deco);
                    break;
                case "5":
                    weeks[5] = true;
                    binding.thr.setBackgroundResource(R.drawable.button_click_deco);
                    break;
                case "6":
                    weeks[6] = true;
                    binding.fri.setBackgroundResource(R.drawable.button_click_deco);
                    break;
                case "7":
                    weeks[7] = true;
                    binding.sat.setBackgroundResource(R.drawable.button_click_deco);
                    break;
                case "1":
                    weeks[1] = true;
                    binding.sun.setBackgroundResource(R.drawable.button_click_deco);
                    break;
                default:
                    break;
            }
        }
    }

    public void getDateFromBtn(){
        View.OnClickListener dateClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.mon:
                        weeks[2] = !weeks[2];
                        if (weeks[2])   binding.mon.setBackgroundResource(R.drawable.button_click_deco);
                        else            binding.mon.setBackgroundResource(R.drawable.days_button_deco);
                        break;
                    case R.id.tue:
                        weeks[3] = !weeks[3];
                        if (weeks[3])   binding.tue.setBackgroundResource(R.drawable.button_click_deco);
                        else            binding.tue.setBackgroundResource(R.drawable.days_button_deco);
                        break;
                    case R.id.wen:
                        weeks[4] = !weeks[4];
                        if (weeks[4])   binding.wen.setBackgroundResource(R.drawable.button_click_deco);
                        else            binding.wen.setBackgroundResource(R.drawable.days_button_deco);
                        break;
                    case R.id.thr:
                        weeks[5] = !weeks[5];
                        if (weeks[5])   binding.thr.setBackgroundResource(R.drawable.button_click_deco);
                        else            binding.thr.setBackgroundResource(R.drawable.days_button_deco);
                        break;
                    case R.id.fri:
                        weeks[6] = !weeks[6];
                        if (weeks[6])   binding.fri.setBackgroundResource(R.drawable.button_click_deco);
                        else            binding.fri.setBackgroundResource(R.drawable.days_button_deco);
                        break;
                    case R.id.sat:
                        weeks[7] = !weeks[7];
                        if (weeks[7])   binding.sat.setBackgroundResource(R.drawable.button_click_deco);
                        else            binding.sat.setBackgroundResource(R.drawable.days_button_deco);
                        break;
                    case R.id.sun:
                        weeks[1] = !weeks[1];
                        if (weeks[1])   binding.sun.setBackgroundResource(R.drawable.button_click_deco);
                        else            binding.sun.setBackgroundResource(R.drawable.days_button_deco);
                        break;
                }
            }
        };
        binding.mon.setOnClickListener(dateClick);   binding.tue.setOnClickListener(dateClick);   binding.wen.setOnClickListener(dateClick);
        binding.thr.setOnClickListener(dateClick);   binding.fri.setOnClickListener(dateClick);   binding.sat.setOnClickListener(dateClick);
        binding.sun.setOnClickListener(dateClick);
    }

    public void setBtnClick(){
        binding.confirmBtn.setOnClickListener(view -> {
            // 알람 수정
            editAlarm();
        });
    }

    public void editAlarm(){
        args = new Bundle();
        BusSchList busSchList = new BusSchList();
        busSchList.setStId(schAlarmInfo.getAlarm_id());
        busSchList.setStStationNm(schAlarmInfo.getAlarm_stop_nm());
        busSchList.setBusRouteId(schAlarmInfo.getAlarm_busId());
        busSchList.setBusRouteNm(schAlarmInfo.getAlarm_bus_nm());
        busSchList.setCorpNm(schAlarmInfo.getStOrder());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        }else{
            Toast.makeText(this, "버전을 확인해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long intervalDay = 24*60*60*1000;       // 24시간
        long selectTime = calendar.getTimeInMillis();
        long current = System.currentTimeMillis();
        if (current > selectTime){
            selectTime +=intervalDay;       // 알람 시간
        }

        if (prevSchAlarmInfo != null){
            alarmId = Integer.parseInt(prevSchAlarmInfo.getAlarm_id().substring(0,3) + prevSchAlarmInfo.getAlarm_busId().substring(0,3)+prevSchAlarmInfo.getStOrder());   // PendingIntent 고유 iD
        }else{
            alarmId = Integer.parseInt(busSchList.getStId().substring(0,3) + busSchList.getBusRouteId().substring(0,3)+busSchList.getCorpNm()); // PendingIntent 고유 iD
        }


        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.setAction("start_alarm");
        alarmIntent.putExtra("weekDays", weeks);
        alarmIntent.putExtra("selectTime", selectTime);
        args.putParcelable("busList", busSchList);
        alarmIntent.putExtras(args);
        PendingIntent alarmPending = PendingIntent.getBroadcast(this, alarmId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(selectTime, alarmPending);

        alarmManager.setAlarmClock(alarmClockInfo, alarmPending);
        // 변경된 시간 및 요일 정보를 반영 후 update쿼리 실행.
        for (int i=1; i< weeks.length; i++){
            if (weeks[i]){
                dates += ","+ i;
            }
        }
        schAlarmInfo.setAlarm_date(selectTime);
        schAlarmInfo.setWeeks(dates);
        updateAlarmViewModel.updateAlarm(schAlarmInfo);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAfterTransition();
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