package com.cn.uca.ui.fragment.yueka;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cn.uca.R;
import com.cn.uca.adapter.yueka.ContentAdapter;
import com.cn.uca.adapter.yueka.LineAdapter;
import com.cn.uca.adapter.yueka.RecommendAdapter;
import com.cn.uca.bean.RecommendBean;
import com.cn.uca.bean.yueka.PlacesBean;
import com.cn.uca.util.SetListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/9/6.
 */

public class YueDetailsFragment extends Fragment implements View.OnClickListener{

    private View view;
    private ListView line,content;
    private RelativeLayout layout1,layout2;
    private LinearLayout notes;//退款须知
    private GridView recommend;
    private List<String> listContent;
    private LineAdapter lineAdapter;
    private ContentAdapter contentAdapter;
    private boolean isShow = true;
    private ImageView icon;
    private List<RecommendBean> listRecomment;
    private RecommendAdapter recommendAdapter;
    private WebView webView;
    private LinearLayout listLayout;
    private ArrayList<PlacesBean> list;
    private String url;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_details_yue,null);

        getInfo();
        initView();

        return view;
    }

    public static YueDetailsFragment newInstance(ArrayList<PlacesBean> list, String url) {
        YueDetailsFragment fragment = new YueDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("list",list);
        args.putString("url",url);
        fragment.setArguments(args);
        return fragment;
    }

    private void getInfo(){
        if (getArguments() != null) {
            list = getArguments().getParcelableArrayList("list");
            url = getArguments().getString("url");
        }
    }

    private void initView() {
        line = (ListView)view.findViewById(R.id.line);
        lineAdapter = new LineAdapter(list,getActivity());
        line.setAdapter(lineAdapter);
        SetListView.setListViewHeightBasedOnChildren(line);

        layout1 = (RelativeLayout)view.findViewById(R.id.layout1);
        layout2 = (RelativeLayout)view.findViewById(R.id.layout2);
        notes = (LinearLayout)view.findViewById(R.id.notes);

        icon = (ImageView)view.findViewById(R.id.icon);

        recommend = (GridView)view.findViewById(R.id.recommend);

        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);

        listRecomment = new ArrayList<>();
        RecommendBean bean = new RecommendBean();
        bean.setPrice("￥230-330");
        bean.setPlace("【北京】");
        bean.setTitle("包你爽翻天");
        listRecomment.add(bean);

        RecommendBean bean2 = new RecommendBean();
        bean2.setPrice("￥350-450");
        bean2.setPlace("【上海】");
        bean2.setTitle("不爽不嗨我倒贴");
        listRecomment.add(bean2);

        RecommendBean bean3 = new RecommendBean();
        bean3.setPrice("￥380-500");
        bean3.setPlace("【广州】");
        bean3.setTitle("老司机带路");
        listRecomment.add(bean3);

        RecommendBean bean4 = new RecommendBean();
        bean4.setPrice("￥400-530");
        bean4.setPlace("【深圳】");
        bean4.setTitle("快上我的车");
        listRecomment.add(bean4);

        recommendAdapter = new RecommendAdapter(listRecomment,getActivity());
        recommend.setAdapter(recommendAdapter);
        SetListView.setGridViewHeightBasedOnChildren(recommend);

        listLayout = (LinearLayout)view.findViewById(R.id.list);
        final LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) listLayout.getLayoutParams(); //取控件textView当前的布局参数

        //获取title高度
        ViewTreeObserver viewTreeObserver1 = line.getViewTreeObserver();
        viewTreeObserver1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                linearParams.height = line.getHeight();// 控件的宽强制设成30
            }
        });

        listLayout.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        webView = (WebView)view.findViewById(R.id.webView);

        // 启用支持javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        // WebView加载web资源
        webView.loadUrl(url);
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout1:

                break;
            case R.id.layout2:
                if (isShow){
                    notes.setVisibility(View.VISIBLE);
                    isShow = false;
                    icon.setImageResource(R.mipmap.down);
                }else {
                    notes.setVisibility(View.GONE);
                    icon.setImageResource(R.mipmap.right_gray);
                    isShow = true;
                }
                break;
        }
    }
}
