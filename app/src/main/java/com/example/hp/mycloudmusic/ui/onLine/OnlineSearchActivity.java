package com.example.hp.mycloudmusic.ui.onLine;

import android.os.Bundle;
import android.util.SparseArray;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.ui.BaseActivity;

import butterknife.Bind;

public class OnlineSearchActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.search_bar_text)
    TextView tvBarText;
    @Bind(R.id.radio_group)
    RadioGroup radioGroup;

    int lastCheckedId = -1;
    SparseArray<RadioButton> radioButtons = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initView() {
        radioGroup.setOnCheckedChangeListener(this);
        tvBarText.setSelected(true);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_online_search;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (lastCheckedId != -1){

            RadioButton last = radioButtons.get(lastCheckedId);
            last.setChecked(false);
            lastCheckedId = checkedId;
        }
        RadioButton now = radioButtons.get(checkedId);
        if(now ==null){
            now = group.findViewById(checkedId);
            radioButtons.put(checkedId,now);
            now.setChecked(true);
        }else{
            now.setChecked(true);
        }
    }
}
