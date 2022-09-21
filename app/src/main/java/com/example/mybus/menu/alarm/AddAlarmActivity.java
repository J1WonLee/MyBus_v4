package com.example.mybus.menu.alarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mybus.MainActivity;
import com.example.mybus.R;
import com.example.mybus.alarmservice.AlarmReceiver;
import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.databinding.ActivityAddAlarmBinding;
import com.example.mybus.menu.MyAlarmActivity;
import com.example.mybus.viewmodel.AddAlarmViewModel;
import com.example.mybus.vo.AddAlarmBusSchCopy;
import com.example.mybus.vo.SchAlarmInfo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddAlarmActivity extends AppCompatActivity {
    private ActivityAddAlarmBinding binding;
    private BusSchList busSchList;
    private Toolbar toolbar;
    private TimePicker timePicker;
    private AppCompatButton datesBtn;
    private int hour, minute;
    private boolean[] weeks = {false, false, false, false, false, false, false, false};
    private AlarmManager alarmManager;
    private Bundle bundle;
    private Bundle args;
    private AddAlarmViewModel addAlarmViewModel;
    private String dates = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addAlarmViewModel = new ViewModelProvider(this).get(AddAlarmViewModel.class);
        initView();
        getDataFromIntent();
        getDateFromBtn();
        binding.alarmTitle.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddAlarmFavListActivity.class);
            startActivity(intent);
        });
        setComfirmBtn();
        delAlarm();
    }

    public void initView(){
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        timePicker = binding.timePicker;
        toolbar = binding.addAlarm;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" 스케줄 알람 목록 ");
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
        bundle = getIntent().getExtras();
        if(bundle!= null){
            busSchList = bundle.getParcelable("busList");
        }
        if (busSchList != null){
            binding.alarmTitle.setText(busSchList.getStStationNm() + " / " + busSchList.getBusRouteNm() +"번 버스");
        }
    }

    public void setComfirmBtn(){
        binding.confirmBtn.setOnClickListener(view -> {
            for (int i=1; i< weeks.length; i++){
                if (weeks[i] == true){
                    registerAlarm();
                    weeks[0] = true;
                    break;
                }
            }
            if (!weeks[0]){
                Toast.makeText(this, "요일을 선택해야 합니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getDateFromBtn(){
        View.OnClickListener dateClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AddAlarmActivity", "getdatefrombtn");
                switch(view.getId()){
                    case R.id.mon:
                        weeks[2] = !weeks[2];
                        if (weeks[2])   {
                            binding.mon.setBackgroundColor(Color.parseColor("#ff0000"));
                        }
                        else{
                            binding.mon.setBackgroundColor(Color.parseColor("#afe3ff"));
                        }
                        break;
                    case R.id.tue:
                        weeks[3] = !weeks[3];
                        if (weeks[3])   binding.tue.setBackgroundColor(Color.parseColor("#ff0000"));
                        else            binding.tue.setBackgroundColor(Color.parseColor("#afe3ff"));
                        break;
                    case R.id.wen:
                        weeks[4] = !weeks[4];
                        if (weeks[4])   binding.wen.setBackgroundColor(Color.parseColor("#ff0000"));
                        else            binding.wen.setBackgroundColor(Color.parseColor("#afe3ff"));
                        break;
                    case R.id.thr:
                        weeks[5] = !weeks[5];
                        if (weeks[5])   binding.thr.setBackgroundColor(Color.parseColor("#ff0000"));
                        else            binding.thr.setBackgroundColor(Color.parseColor("#afe3ff"));
                        break;
                    case R.id.fri:
                        weeks[6] = !weeks[6];
                        if (weeks[6])   binding.fri.setBackgroundColor(Color.parseColor("#ff0000"));
                        else            binding.fri.setBackgroundColor(Color.parseColor("#afe3ff"));
                        break;
                    case R.id.sat:
                        weeks[7] = !weeks[7];
                        if (weeks[7])   binding.sat.setBackgroundColor(Color.parseColor("#ff0000"));
                        else            binding.sat.setBackgroundColor(Color.parseColor("#afe3ff"));
                        break;
                    case R.id.sun:
                        weeks[1] = !weeks[1];
                        if (weeks[1])   binding.sun.setBackgroundColor(Color.parseColor("#ff0000"));
                        else            binding.sun.setBackgroundColor(Color.parseColor("#afe3ff"));
                        break;
                }
            }
        };
        binding.mon.setOnClickListener(dateClick);   binding.tue.setOnClickListener(dateClick);   binding.wen.setOnClickListener(dateClick);
        binding.thr.setOnClickListener(dateClick);   binding.fri.setOnClickListener(dateClick);   binding.sat.setOnClickListener(dateClick);
        binding.sun.setOnClickListener(dateClick);
    }

    public void registerAlarm(){
        args = new Bundle();
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
            selectTime +=intervalDay;
        }

        int alarmId = Integer.parseInt(busSchList.getStId().substring(0,3) + busSchList.getBusRouteId().substring(0,3)+busSchList.getCorpNm());

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.setAction("start_alarm");
        alarmIntent.putExtra("weekDays", weeks);
        args.putParcelable("busList", busSchList);
        alarmIntent.putExtras(args);
        PendingIntent alarmPending = PendingIntent.getBroadcast(this, alarmId, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(selectTime, alarmPending);

        alarmManager.setAlarmClock(alarmClockInfo, alarmPending);
        Toast.makeText(this, "알람을 생성했습니다", Toast.LENGTH_SHORT).show();
        for (int i=1; i< weeks.length; i++){
            if (weeks[i]){
                dates += ","+ i;
            }
        }
        addAlarmViewModel.insertSchAlarm(new SchAlarmInfo(busSchList.getStId(), busSchList.getBusRouteId(), busSchList.getStStationNm(), busSchList.getBusRouteNm(), dates, selectTime, busSchList.getCorpNm()));
        Intent intent = new Intent(this, MyAlarmActivity.class);
        startActivity(intent);
        finish();

    }

    public void delAlarm(){
        binding.imageView2.setOnClickListener(view -> {
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.setAction("stop_alarm");
            PendingIntent pIntent = PendingIntent.getBroadcast(this, 0 , intent, PendingIntent.FLAG_IMMUTABLE);
            Log.d("AddAlarmActivity", "cancel alarm call");
            alarmManager.cancel(pIntent);
            sendBroadcast(intent);
        });
    }
}