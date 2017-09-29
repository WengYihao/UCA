package com.cn.uca.ui.register;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.util.BaseBackActivity;
import com.cn.uca.util.FitStateUI;

public class RegisterWebActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView back;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitStateUI.setImmersionStateMode(this);
        setContentView(R.layout.activity_register_web);

        initView();
    }

    private void initView() {
        back = (TextView)findViewById(R.id.back);
        webView = (WebView)findViewById(R.id.webView);
        back.setOnClickListener(this);

        String url = "http://www.szyouka.com/youkatravel/agreement/appUseProtocol.html";
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
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
            case R.id.back:
                this.finish();
                break;
        }
    }
}
