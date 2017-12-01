package com.cn.uca.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cn.uca.config.wechat.WeChatManager;
import com.cn.uca.util.ToastXutil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);
//    	api = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID);
//        api.handleIntent(getIntent(), this);
		Log.i("123","45678910123");
		WeChatManager.instance().handleIntent(getIntent(),this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		WeChatManager.instance().handleIntent(getIntent(),this);
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.i("123", "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (resp.errCode == 0){
//				ToastXutil.show("支付成功");
//				PayResp.Resp newResp = (SendAuth.Resp) resp;
//				String code = newResp.code;
//				//获取微信传回的code--------
//				QueryHttp.getWeChatAccessToken(code,this) ;
			}else {
				ToastXutil.show("支付失败");
			}
			finish();
		}
	}

	@Override
	public void onReq(BaseReq baseReq) {

	}
}