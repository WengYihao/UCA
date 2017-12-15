package com.cn.uca.ui.view.home.samecityka;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.cn.uca.R;
import com.cn.uca.adapter.home.samecityka.SearchResultAdapter;
import com.cn.uca.ui.view.home.travel.TourismActivity;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.view.MyEditText;
import com.cn.uca.view.dialog.LoadDialog;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends BaseBackActivity implements View.OnClickListener,PoiSearch.OnPoiSearchListener {

    private TextView back;
    private MyEditText keyword;
    private ListView listView;
    private String keyWord = "";// 要输入的poi搜索关键字
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private SearchResultAdapter adapter;
    private List<PoiItem> list;
    private String city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        getInfo();
        initView();
    }
    private void getInfo(){
        Intent intent = getIntent();
        if (intent != null){
            city = intent.getStringExtra("city");
        }
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        keyword = (MyEditText)findViewById(R.id.keyword);

        listView = (ListView)findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new SearchResultAdapter(list,this);
        listView.setAdapter(adapter);
        keyword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    //隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchResultActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    keyWord = StringXutil.checkEditText(keyword);
                    if (!"".equals(keyWord)) {
                        doSearchQuery("");
                    }
                }
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                double lat = list.get(position).getLatLonPoint().getLatitude();
                double lng = list.get(position).getLatLonPoint().getLongitude();
                Intent intent = new Intent();
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                setResult(0,intent);
                SearchResultActivity.this.finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
        }

    }

    protected void doSearchQuery(String city) {
        LoadDialog.show(this);
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        LoadDialog.dismiss(this);
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                poiResult = result;
                // 取得搜索到的poiitems有多少页
                list.clear();
                list = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                if (list.size() > 0){
                    adapter.setList(list);
                    for (int i = 0;i<= list.size();i++){
                        Log.i("123",list.get(i).getTitle()+"-"+list.get(i).getSnippet()+"-"+list.get(i).getLatLonPoint().getLatitude()+"-"+list.get(i).getLatLonPoint().getLongitude());
                    }
                }else{
                    doSearchQuery(city);
                }
            }
        } else {
            ToastXutil.show(rCode+"");
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
