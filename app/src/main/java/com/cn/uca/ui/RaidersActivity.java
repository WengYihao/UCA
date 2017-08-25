package com.cn.uca.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.cn.uca.R;
import com.cn.uca.adapter.LetterAdapter;
import com.cn.uca.adapter.RaidersAdapter;

import java.util.ArrayList;
import java.util.List;

public class RaidersActivity extends AppCompatActivity {

    private ListView letter,bbb;
    private LetterAdapter letterAdapter;
    private String [] data;
    private List<String> list;
    private RaidersAdapter raidersAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raiders);

        initView();
    }

    private void initView() {
        letter = (ListView)findViewById(R.id.letter);
        bbb = (ListView)findViewById(R.id.bbb);
        data = getResources().getStringArray(R.array.letter_list);
        letterAdapter = new LetterAdapter(data,this);
        letter.setAdapter(letterAdapter);

        list = new ArrayList<>();
        list.add("深圳");
        list.add("广州");
        list.add("上海");
        list.add("北京");

        raidersAdapter = new RaidersAdapter(list,this);
        bbb.setAdapter(raidersAdapter);

    }
}
