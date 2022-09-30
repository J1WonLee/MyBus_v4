package com.example.mybus.search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybus.ActivityAnimate;
import com.example.mybus.MainActivity;
import com.example.mybus.R;
import com.example.mybus.apisearch.itemList.StopSchList;
import com.example.mybus.apisearch.wrapper.StopSearchUidWrap;
import com.example.mybus.databinding.FragmentStopListsBinding;
import com.example.mybus.menu.LoginActivity;
import com.example.mybus.searchDetail.StopDetailActivity;
import com.example.mybus.viewmodel.SearchViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

public class StopListsFragment extends Fragment implements ActivityAnimate {
    private SearchViewModel searchViewModel;
    private ViewPager2 viewPager2;
    private FragmentStopListsBinding binding;
    private String getText;
    private RecyclerView recyclerView;
    private StopSearchListAdapter stopListAdapter;
    private TextView emptyText;
    private List<StopSchList> stopLists;
    private FloatingActionButton floatingActionButton;
    private SharedPreferences sharedPreferences;
    private boolean isRecentChk = true;
    private EditText inputText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stop_lists, container, false);
        getEditFocus();
        setFabClick();
        getRecentChk();
        setEditTextFilter();
        searchViewModel = new ViewModelProvider(getActivity()).get(SearchViewModel.class);
        initRecycler();

//        searchViewModel.mutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<StopSearchUidWrap>>() {
//            @Override
//            public void onChanged(List<StopSearchUidWrap> stopSearchUidWraps) {
//                if (stopSearchUidWraps != null){
//                    for (int i=0; i< stopSearchUidWraps.size(); i++){
//                        Log.d("kkang stopwraps  ",  i + " st " + stopSearchUidWraps.get(i).getStopSearchUid().getItemLists().size() +" ");
//                        for (int j=0; j<stopSearchUidWraps.get(i).getStopSearchUid().getItemLists().size(); j++ ){
//                            Log.d("kkang stopwraps  ",  i + " st " + stopSearchUidWraps.get(i).getStopSearchUid().getItemLists().get(j).getStId() +" ");
//                        }
//                    }
//                }
//            }
//        });

        searchViewModel.searchorderLists.observe(getViewLifecycleOwner(), new Observer<List<StopSchList>>() {
            @Override
            public void onChanged(List<StopSchList> stopSchLists) {
                if (stopSchLists != null){
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyText.setVisibility(View.GONE);
                    stopListAdapter.updateLists(stopSchLists);
                    stopLists = stopSchLists;
                }else{
                    recyclerView.setVisibility(View.GONE);
                    emptyText.setVisibility(View.VISIBLE);
                }
            }
        });
        if (isRecentChk)    setInitContents();
        setAutoResult();
        return binding.getRoot();
    }

    public void getEditFocus(){
        inputText = binding.searchStopInput;
        inputText.isFocusableInTouchMode();
        inputText.setFocusable(true);
        inputText.requestFocus();
    }

    public void initRecycler(){
        recyclerView = binding.searchStopLists;
        stopListAdapter = new StopSearchListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(stopListAdapter);
        emptyText = binding.searchBusNoResults;
        setListener();
    }


    public void setListener(){
        stopListAdapter.setOnItemClickListener(new StopSearchListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
//                Log.d("kkang", "position : " + stopLists.get(position).getArsId());
                if (!(stopLists.get(position).getArsId().equals("0"))){
                    searchViewModel.insertRecentStopSch(stopLists.get(position));
                    Intent intent = new Intent(getActivity(), StopDetailActivity.class);
                    Bundle args = new Bundle();
                    args.putParcelable("stopList", stopLists.get(position));
                    intent.putExtras(args);
                    startActivity(intent);
                    moveAnimate();
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.searchStopInput.requestFocus();
    }

    @Override
    public void onPause() {
        searchViewModel.setSharedData(binding.searchStopInput.getText().toString());
        super.onPause();
    }

    public void setInitContents(){
        if (binding.searchStopInput.getText().toString().length() <= 0){
            searchViewModel.getRecentStopSchList();
        }
    }

    // EDIT TEXT 특수문자 막기
    public void setEditTextFilter(){
        inputText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$");
                if (source.equals("") || ps.matcher(source).matches()) {
                    return source;
                }
                Toast.makeText(getContext(), "한글, 영문, 숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                return "";
            }
        },new InputFilter.LengthFilter(9)});
    }
    
    // 자동검색 기능 삭제 고려
    public void setAutoResult(){
        binding.searchStopInput.addTextChangedListener(
                new TextWatcher() {
                    private Timer timer = new Timer();
                    private final long DELAY = 1000;
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
                                        if (binding.searchStopInput.getText().toString().length() > 0){
                                            searchViewModel.stopListsKeyword(binding.searchStopInput.getText().toString());
                                        }else{
                                            if (isRecentChk)        searchViewModel.getRecentStopSchList();
                                        }
                                    }
                                },DELAY
                        );
                    }
                }
        );

    }

    public void getRecentChk(){
        sharedPreferences = getContext().getSharedPreferences(LoginActivity.sharedId, Context.MODE_PRIVATE);
        isRecentChk  = sharedPreferences.getBoolean("recentSch", true);
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

    @Override
    public void moveAnimate() {
        requireActivity().overridePendingTransition(R.anim.vertical_center, R.anim.none);
    }

    @Override
    public void exitAnimate() {
        requireActivity().overridePendingTransition(R.anim.none, R.anim.vertical_exit);
    }
}