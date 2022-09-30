package com.example.mybus.search;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mybus.ActivityAnimate;
import com.example.mybus.MainActivity;
import com.example.mybus.R;
import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.databinding.FragmentBusListsBinding;
import com.example.mybus.menu.LoginActivity;
import com.example.mybus.searchDetail.BusRouteDetailActivity;
import com.example.mybus.searchDetail.StopDetailActivity;
import com.example.mybus.viewmodel.SearchViewModel;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class BusListsFragment extends Fragment implements ActivityAnimate {
    private SearchViewModel searchViewModel;
    private FragmentBusListsBinding binding;
    private BusSearchListAdapter busSearchListAdapter;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private List<BusSchList> busLists;
    private FloatingActionButton floatingActionButton;
    private SharedPreferences sharedPreferences;
    private boolean isRecentChk = true;
    private EditText inputText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bus_lists, container, false);
        inputText = binding.searchBusInput;
        getEditFocus();
        getRecentChk();
        initRecycler();
        setFabClick();
        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        // 버스 목록 옵저버로 탐지
        searchViewModel.busLists.observe(getViewLifecycleOwner(), new Observer<List<BusSchList>>() {
            @Override
            public void onChanged(List<BusSchList> busSchLists) {
                if (busSchLists != null){
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyText.setVisibility(View.GONE);
                    busSearchListAdapter.updateLists(busSchLists);
                    busLists = busSchLists;
                }else{
                    Log.d("kkang", "busschlists is null");
                    recyclerView.setVisibility(View.GONE);
                    emptyText.setVisibility(View.VISIBLE);
                }
            }
        });

        if (isRecentChk)                setInitContents();
        setAutoResult();
        return binding.getRoot();
    }

    public void getEditFocus(){
        inputText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                inputText.requestFocus();
            }
        });
    }

    public void initRecycler(){
        recyclerView = binding.searchBusLists;
        busSearchListAdapter = new BusSearchListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(busSearchListAdapter);
        emptyText = binding.searchBusNoResults;
        setListener();
    }

    // 리스트 클릭 이벤트
    public void setListener(){
        busSearchListAdapter.setOnItemClickListener(new BusSearchListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (isRecentChk)            searchViewModel.insertRecentBusSch(busLists.get(position));
                Intent intent = new Intent(getActivity(), BusRouteDetailActivity.class);
                Bundle args = new Bundle();
                args.putParcelable("busList", busLists.get(position));
                intent.putExtras(args);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
//                moveAnimate();
            }
        });
    }

    // 초기 기본 값으로 최근 검색어 보여주기
    public void setInitContents(){
        if (binding.searchBusInput.getText().toString().length() <= 0){
            searchViewModel.getRecentBusSchList();
        }
    }

    // 자동 검색 기능
    public void setAutoResult(){
        binding.searchBusInput.addTextChangedListener(
                new TextWatcher() {
                    private Timer timer = new Timer();
                    private final long DELAY = 800;
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }


                    @Override
                    public void afterTextChanged(Editable editable) {
                        timer.cancel();
                        searchViewModel.dispose();
                        timer = new Timer();
                        timer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        searchViewModel.newDispose();
                                        if (binding.searchBusInput.getText().toString().length() > 0){
                                            searchViewModel.getBusLists(binding.searchBusInput.getText().toString());
                                        }else{
                                            if (isRecentChk){
                                                searchViewModel.getRecentBusSchList();
                                            }

                                        }

                                    }
                                },DELAY
                        );
                    }
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.searchBusInput.requestFocus();
    }

    @Override
    public void onPause() {
        searchViewModel.setSharedData(binding.searchBusInput.getText().toString());
        super.onPause();
    }

    public void setFabClick(){
        floatingActionButton = binding.floatingActionButton;
        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
            exitAnimate();
        });
    }

    public void getRecentChk(){
        sharedPreferences = getContext().getSharedPreferences(LoginActivity.sharedId, Context.MODE_PRIVATE);
        isRecentChk  = sharedPreferences.getBoolean("recentSch", true);
    }

    @Override
    public void moveAnimate() {
        requireActivity().overridePendingTransition(R.anim.vertical_center, R.anim.none);
    }

    @Override
    public void exitAnimate() {
        requireActivity().overridePendingTransition(R.anim.none, R.anim.vertical_exit);
    }
}