package com.example.mybus.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mybus.ActivityAnimate;
import com.example.mybus.MainActivity;
import com.example.mybus.R;
import com.example.mybus.alarmservice.AlarmReceiver;
import com.example.mybus.alarmservice.RepeatAlarmReceiver;
import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.databinding.ActivityMyAlarmBinding;
import com.example.mybus.menu.alarm.AddAlarmActivity;
import com.example.mybus.menu.alarm.AlarmListAdapter;
import com.example.mybus.menu.alarm.UpdateAlarmActivity;
import com.example.mybus.viewmodel.AddAlarmListViewModel;
import com.example.mybus.viewmodel.MyAlarmViewModel;
import com.example.mybus.vo.SchAlarmInfo;

import java.util.Calendar;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MyAlarmActivity extends AppCompatActivity implements ActivityAnimate {
    private ActivityMyAlarmBinding binding;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MyAlarmViewModel myAlarmViewModel;
    private AlarmListAdapter adapter;
    private boolean isScolled = false;
    private AlarmManager alarmManager;
    private List<SchAlarmInfo> schAlarmInfo;
    private boolean[] weeks = {false, false, false, false, false, false, false, false};
    private boolean[] cancelWeeks = {};
    private Bundle bundle;
    private LinearLayout emptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myAlarmViewModel = new ViewModelProvider(this).get(MyAlarmViewModel.class);
        initView();
        initRecycler();
        getAlarmList();
    }

    public void initView(){
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        emptyLayout = binding.myAlarmEmptyWrap;
       toolbar = binding.toolbar;
       setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayShowTitleEnabled(false);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }

    public void initRecycler(){
        recyclerView = binding.recyclerView2;
        adapter = new AlarmListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        setRecyclerListener();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_alarm_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        switch (item.getItemId()){
            case R.id.action_add:
                intent = new Intent(this, AddAlarmActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                break;
            case android.R.id.home:
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                exitAnimate();
                finishAfterTransition();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getAlarmList(){
        myAlarmViewModel.getSchAlarmList();
        myAlarmViewModel.schAlarmList.observe(this, new Observer<List<SchAlarmInfo>>() {
            @Override
            public void onChanged(List<SchAlarmInfo> schAlarmInfos) {
                if (schAlarmInfos != null && schAlarmInfos.size()>0 ){
                    Log.d("MyAlarmActivity", schAlarmInfos.size()+" is size");
                    schAlarmInfo = schAlarmInfos;
                    adapter.updateschAlarmInfoList(schAlarmInfo);
                    emptyLayout.setVisibility(View.GONE);
                }else{
                    adapter.updateschAlarmInfoList(null);
                    emptyLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void setRecyclerListener(){
        adapter.setOnItemClickListener(new AlarmListAdapter.OnItemClickListener() {
            @Override
            public void onScrollClick(CompoundButton v, int position, boolean isChecked) {
//                isScolled = true;
//                if (b && isScolled){
//                    Log.d("MyAlarmActivity", "is selected!");
//                    // ????????? ??????
//                    chkWeekDays(schAlarmInfo.get(position).getWeeks());
//                    setAlarmManager(schAlarmInfo.get(position));
//                    schAlarmInfo.get(position).setOn(true);
//                }else if (!b && isScolled){
//                    // ????????? ??????.
//                    delAlarm(schAlarmInfo.get(position));
//                    schAlarmInfo.get(position).setOn(false);
//                }
                if (v.isPressed()){
                    if (isChecked){
                        // ????????? ??????
                         chkWeekDays(schAlarmInfo.get(position).getWeeks());
                         setAlarmManager(schAlarmInfo.get(position));
                         schAlarmInfo.get(position).setOn(true);
                        adapter.updatesSchAlarmOnOff(schAlarmInfo, position);
                    }else{// ????????? ??????
                        delAlarm(schAlarmInfo.get(position));
                        schAlarmInfo.get(position).setOn(false);
                        adapter.updatesSchAlarmOnOff(schAlarmInfo, position);
                    }
                }
            }

            @Override
            public void onDelBtnClick(View v, int position) {
                // ?????? ?????? ??? ?????? ??????.
                delAlarm(schAlarmInfo.get(position));
                myAlarmViewModel.deleteSchAlarm(schAlarmInfo.get(position));
            }

            @Override
            public void onEditBtnClick(View v, int position) {
                // ?????? ?????? ?????????, ?????? ?????? ???????????? ??????
                if (schAlarmInfo.get(position).isOn()){
                    bundle = new Bundle();
                    bundle.putParcelable("updateAlarm", schAlarmInfo.get(position));
                    Intent updateAlarmIntent = new Intent(v.getContext(), UpdateAlarmActivity.class);
                    updateAlarmIntent.putExtras(bundle);
                    startActivity(updateAlarmIntent, ActivityOptions.makeSceneTransitionAnimation(MyAlarmActivity.this).toBundle());
                }
            }
        });
    }

    public void setAlarmManager(SchAlarmInfo schAlarmInfo){
        Bundle args = new Bundle();
        Calendar calendar = Calendar.getInstance();
        long intervalDay = 24*60*60*1000;       // 24??????
        long selectTime = schAlarmInfo.getAlarm_date();
        long current = System.currentTimeMillis();
        if (current > selectTime){
            selectTime +=intervalDay;
        }

        int alarmId = Integer.parseInt(schAlarmInfo.getAlarm_id().substring(0,3) + schAlarmInfo.getAlarm_busId().substring(0,3)+schAlarmInfo.getStOrder());

        BusSchList busSchList = new BusSchList();
        busSchList.setStId(schAlarmInfo.getAlarm_id());
        busSchList.setBusRouteId(schAlarmInfo.getAlarm_busId());
        busSchList.setBusRouteNm(schAlarmInfo.getAlarm_bus_nm());
        busSchList.setCorpNm(schAlarmInfo.getStOrder());
        busSchList.setStStationNm(schAlarmInfo.getAlarm_stop_nm());

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.setAction("start_alarm");
        alarmIntent.putExtra("weekDays", weeks);
        args.putParcelable("busList", busSchList);
        alarmIntent.putExtras(args);
        PendingIntent alarmPending = PendingIntent.getBroadcast(this, alarmId, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(selectTime, alarmPending);

        alarmManager.setAlarmClock(alarmClockInfo, alarmPending);
        Toast.makeText(this, "????????? ??????????????????", Toast.LENGTH_SHORT).show();
    }

    public void chkWeekDays(String dates){
        String[] sliceDates = dates.substring(1).split(",");
        for (int i=0; i< sliceDates.length; i++){
            switch(sliceDates[i]){
                case "2":
                    weeks[2] = true;
                    break;
                case "3":
                    weeks[3] = true;
                    break;
                case "4":
                    weeks[4] = true;
                    break;
                case "5":
                    weeks[5] = true;
                    break;
                case "6":
                    weeks[6] = true;
                    break;
                case "7":
                    weeks[7] = true;
                    break;
                case "1":
                    weeks[1] = true;
                    break;
                default:
                    break;
            }
        }
    }

    public void delAlarm(SchAlarmInfo schAlarmInfo){
        int alarmId = Integer.parseInt(schAlarmInfo.getAlarm_id().substring(0,3) + schAlarmInfo.getAlarm_busId().substring(0,3)+schAlarmInfo.getStOrder());
        int repeatId = Integer.parseInt(schAlarmInfo.getAlarm_id().substring(0,4) + schAlarmInfo.getAlarm_busId().substring(0,3)+schAlarmInfo.getStOrder());
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setAction("start_alarm");
        intent.putExtra("weekDays", cancelWeeks);
        intent.putExtra("selectTime", 0);
        intent.putExtras(new Bundle());
        PendingIntent pIntent = PendingIntent.getBroadcast(this, alarmId , intent, PendingIntent.FLAG_IMMUTABLE);
        Log.d("AddAlarmActivity", "cancel alarm call");
        Toast.makeText(this,"????????? ??????????????????", Toast.LENGTH_SHORT).show();
        alarmManager.cancel(pIntent);
        offRepeatingAlarm(repeatId);
    }

    public void offRepeatingAlarm(int id){
        Intent intent = new Intent(this, RepeatAlarmReceiver.class);
        intent.putExtra("weekdays", cancelWeeks);
        intent.putExtras(new Bundle());
        PendingIntent pIntent = PendingIntent.getBroadcast(this, id , intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pIntent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        exitAnimate();
        finishAfterTransition();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myAlarmViewModel.updateSchAlarm(schAlarmInfo);
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