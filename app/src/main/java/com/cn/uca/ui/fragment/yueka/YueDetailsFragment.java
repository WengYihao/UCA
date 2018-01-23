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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.adapter.yueka.LineAdapter;
import com.cn.uca.bean.yueka.PlacesBean;
import com.cn.uca.config.Constant;
import com.cn.uca.util.SetListView;
import com.cn.uca.util.StatusMargin;
import com.cn.uca.util.SystemUtil;

import java.util.ArrayList;

/**
 * Created by asus on 2017/9/6.
 */

public class YueDetailsFragment extends Fragment implements View.OnClickListener{

    private View view;
    private RelativeLayout layout1,layout2;
    private LinearLayout notice,notes;//预定须知/退款须知
    private boolean isShow = false;
    private boolean isShow2 = false;
    private ImageView icon,icon1;
    private WebView webView;
    private LinearLayout listLayout;
    private ArrayList<PlacesBean> list;
    private String url;
    private TextView Refund_Note,Reservation_Notice;
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
        layout1 = (RelativeLayout)view.findViewById(R.id.layout1);
        layout2 = (RelativeLayout)view.findViewById(R.id.layout2);
        notice = (LinearLayout)view.findViewById(R.id.notice);
        notes = (LinearLayout)view.findViewById(R.id.notes);
        Refund_Note = (TextView)view.findViewById(R.id.Refund_Note);
        Refund_Note.setText(Constant.Refund_Note);
        Reservation_Notice = (TextView)view.findViewById(R.id.Reservation_Notice);
        Reservation_Notice.setText(Constant.Reservation_Notice);
        icon = (ImageView)view.findViewById(R.id.icon);
        icon1 = (ImageView)view.findViewById(R.id.icon1);


        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);


        listLayout = (LinearLayout)view.findViewById(R.id.list);
        final LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) listLayout.getLayoutParams(); //取控件textView当前的布局参数

        listLayout.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        webView = (WebView)view.findViewById(R.id.webView);

        // 启用支持javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        // WebView加载web资源
        webView.loadUrl(url);
//        webView.loadUrl("http:www.baidu.com");
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
                if (isShow2){
                    notice.setVisibility(View.VISIBLE);
                    isShow2 = false;
                    icon1.setImageResource(R.mipmap.down);
                }else {
                    notice.setVisibility(View.GONE);
                    icon1.setImageResource(R.mipmap.right_gray);
                    isShow2 = true;
                }
                break;
            case R.id.layout2:
                if (isShow){
                    notes.setVisibility(View.VISIBLE);
                    isShow = false;
                    icon.setImageResource(R.mipmap.down);
                    StatusMargin.setLinearLayoutBottom(getActivity(),layout2, 0);
                }else {
                    notes.setVisibility(View.GONE);
                    icon.setImageResource(R.mipmap.right_gray);
                    StatusMargin.setLinearLayoutBottom(getActivity(),layout2, SystemUtil.dip2px(60));
                    isShow = true;
                }
                break;
        }
    }
}
