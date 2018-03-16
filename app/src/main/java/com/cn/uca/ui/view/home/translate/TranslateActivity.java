package com.cn.uca.ui.view.home.translate;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cn.uca.R;
import com.cn.uca.adapter.home.translate.HistoryAdapter;
import com.cn.uca.adapter.home.translate.LanguageAdapter;
import com.cn.uca.adapter.home.translate.TranslateResultAdapter;
import com.cn.uca.bean.home.translate.HistoryBean;
import com.cn.uca.bean.home.translate.LanguageBean;
import com.cn.uca.bean.home.translate.TranslateResultBean;
import com.cn.uca.config.MyApplication;
import com.cn.uca.db.DatabaseHelper;
import com.cn.uca.impl.CallBack;
import com.cn.uca.server.home.HomeHttp;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.SignUtil;
import com.cn.uca.util.StringXutil;
import com.cn.uca.util.SystemUtil;
import com.cn.uca.util.ToastXutil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslateActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back,clean,language1,exchange,language2,translate,hide_layoout;
    private EditText editText;
    private LinearLayout layout1;
    private RelativeLayout layout2;
    private ListView listView;
    private ListView resultList;
    private List<LanguageBean> list;
    private List<TranslateResultBean> listResult;
    private TranslateResultAdapter adapter;
    private String languageFrom = "";
    private String languageTo = "";
    private String exchangeStr = "";
    private DatabaseHelper helper;
    private HistoryAdapter historyAdapter;
    private List<HistoryBean> listHoistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        initView();
        getLanguages();
    }

    private void initView() {
        helper = new DatabaseHelper(this);
        back = (TextView)findViewById(R.id.back);
        language1 = (TextView)findViewById(R.id.language1);
        exchange = (TextView)findViewById(R.id.exchange);
        language2 = (TextView)findViewById(R.id.language2);
        clean = (TextView)findViewById(R.id.clean);
        editText = (EditText)findViewById(R.id.editText);
        translate = (TextView)findViewById(R.id.translate);
        listView = (ListView)findViewById(R.id.listView);

        layout1 = (LinearLayout)findViewById(R.id.layout1);
        hide_layoout = (TextView)findViewById(R.id.hide_layout);
        layout2 = (RelativeLayout)findViewById(R.id.layout2);
        resultList = (ListView)findViewById(R.id.resultList);

        back.setOnClickListener(this);
        language1.setOnClickListener(this);
        exchange.setOnClickListener(this);
        language2.setOnClickListener(this);
        clean.setOnClickListener(this);
        translate.setOnClickListener(this);
        hide_layoout.setOnClickListener(this);

        list = new ArrayList<>();
        listResult = new ArrayList<>();
        adapter = new TranslateResultAdapter(listResult,this);
        resultList.setAdapter(adapter);

        listHoistory = getHistory();
        historyAdapter = new HistoryAdapter(listHoistory,this);
        listView.setAdapter(historyAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                baiduTranslate(listHoistory.get(position).getFromType(),
                        listHoistory.get(position).getToType(),
                        listHoistory.get(position).getSrc());
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.language1:
                languageWindow(1,language1,this,list);
                break;
            case R.id.exchange:
                exchangeStr = language1.getText().toString();
                language1.setText(language2.getText().toString());
                language2.setText(exchangeStr);
                exchangeStr = languageFrom;
                languageFrom = languageTo;//1-3
                languageTo = exchangeStr;
                break;
            case R.id.language2:
                languageWindow(2,language2,this,list);
                break;
            case R.id.clean:
                editText.setText("");
                break;
            case R.id.translate:
                String text = editText.getText().toString().trim();
                if (StringXutil.isEmpty(text)){
                    ToastXutil.show("请输入要翻译的文字");
                }else{
                    baiduTranslate(languageFrom,languageTo,text);
                }
                break;
            case R.id.hide_layout:
                layout2.setVisibility(View.GONE);
                layout1.setVisibility(View.VISIBLE);
                break;
        }
    }

    //获取翻译语种
    private void getLanguages(){
        Map<String,Object> map = new HashMap<>();
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        String sign = SignUtil.sign(map);
        HomeHttp.getLanguages(sign, time_stamp, new CallBack() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int code = jsonObject.getInt("code");
                    switch (code){
                        case 0:
                            Gson gson = new Gson();
                            list = gson.fromJson(jsonObject.getJSONArray("data").toString(),new TypeToken<List<LanguageBean>>() {
                            }.getType());
                            break;
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onErrorMsg(String errorMsg) {

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    //语种弹窗
    private void languageWindow(final int type, View view, Context context, final List<LanguageBean> list){
        View inflate = LayoutInflater.from(context).inflate(R.layout.language_popupwindow, null);
        ListView listView = (ListView)inflate.findViewById(R.id.listView);
        LanguageAdapter adapter = new LanguageAdapter(list,context);
        listView.setAdapter(adapter);

        final PopupWindow popupWindow = new PopupWindow(inflate, MyApplication.width,
                MyApplication.height*3/7, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        popupWindow.showAsDropDown(view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (type){
                    case 1:
                        language1.setText(list.get(position).getName());
                        popupWindow.dismiss();
                        languageFrom = list.get(position).getShorthand();
                        break;
                    case 2:
                        language2.setText(list.get(position).getName());
                        popupWindow.dismiss();
                        languageTo = list.get(position).getShorthand();
                        break;
                }
            }
        });
    }

    //百度翻译
    private void baiduTranslate(final String from, String to, String q){
        Map<String,Object> map = new HashMap<>();
        String time_stamp = SystemUtil.getCurrentDate2();
        map.put("time_stamp",time_stamp);
        map.put("from",from);
        map.put("to",to);
        map.put("q",q);
        String sign = SignUtil.sign(map);
        HomeHttp.baiduTranslate(sign, time_stamp, from, to, q, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try{
                    if (i == 200) {
                        JSONObject jsonObject = new JSONObject(new String(bytes, "UTF-8"));
                        Log.e("456",jsonObject.toString());
                        int code = jsonObject.getInt("code");
                        switch (code){
                            case 0:
                                Gson gson = new Gson();
                                String fromS = jsonObject.getJSONObject("data").getString("from");
                                String toS = jsonObject.getJSONObject("data").getString("to");
                                List<TranslateResultBean> bean = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("trans_result").toString(),new TypeToken<List<TranslateResultBean>>() {
                                }.getType());
                                if (bean.size() > 0){
                                    layout1.setVisibility(View.GONE);
                                    layout2.setVisibility(View.VISIBLE);
                                    listResult.clear();
                                    listResult.addAll(bean);
                                    adapter.setList(listResult);
                                    List<HistoryBean> list = new ArrayList<HistoryBean>();
                                    HistoryBean historyBean;
                                    for (TranslateResultBean li : bean){
                                        historyBean = new HistoryBean();
                                        if (languageFrom == ""){
                                            historyBean.setFromType(fromS);
                                        }else{
                                            historyBean.setFromType(languageFrom);
                                        }
                                       if (languageTo == ""){
                                           historyBean.setToType(toS);
                                       }else{
                                           historyBean.setToType(languageTo);
                                       }
                                        historyBean.setSrc(li.getSrc());
                                        historyBean.setDst(li.getDst());
                                        list.add(historyBean);
                                    }
                                    for (HistoryBean bean1 : list){
                                        insertHistory(bean1);
                                    }
                                    listHoistory.clear();
                                    listHoistory = getHistory();
                                    historyAdapter.setList(listHoistory);
                                }
                                break;
                            default:
                                ToastXutil.show(jsonObject.getString("msg"));
                                break;

                        }
                    }
                }catch (Exception e){
            }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

    //获取搜索历史
    private List<HistoryBean> getHistory(){
        ArrayList<HistoryBean> list = new ArrayList<>();
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from translate", null);
            HistoryBean bean;
            while (cursor.moveToNext()) {
                bean = new HistoryBean();
                bean.setFromType(cursor.getString(cursor.getColumnIndex("fromType")));
                bean.setToType(cursor.getString(cursor.getColumnIndex("toType")));
                bean.setSrc(cursor.getString(cursor.getColumnIndex("src")));
                bean.setDst(cursor.getString(cursor.getColumnIndex("dst")));
                list.add(bean);
            }
            cursor.close();
            db.close();
        }catch (Exception e){
            Log.e("456",e.getMessage());
        }
        return list;
    }

    //插入历史记录
    private void insertHistory(HistoryBean bean){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from translate where src ='"
                + bean.getSrc() + "'", null);
        if (cursor.getCount() > 0) { //
            db.delete("translate", "src = ?", new String[] { bean.getSrc() });
        }
        StringBuffer buffer = new StringBuffer("insert into translate (fromType,toType,src,dst) values (");
        buffer.append("'" + bean.getFromType() + "'").append(",")
                .append("'" + bean.getToType()+ "'").append(",")
                .append("'" + bean.getSrc()+ "'").append(",")
                .append("'" + bean.getDst() + "'").append(")");
        db.execSQL(buffer.toString());
        db.close();
    }
}
