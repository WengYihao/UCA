package com.cn.uca.config;

import com.tencent.mm.opensdk.openapi.IWXAPI;

public class Constant {
	final public static String userIdKey = "UserInfo";       //保存用户信息的key
	final public static String TAG = "UCA";
	final public static  int PHOTO_REQUEST_TAKEPHOTO = 1;  // 拍照
	final public static int PHOTO_REQUEST_GALLERY = 2;     // 从相册中选择
	final public static  int PHOTO_REQUEST_CUT = 3;        // 结果
	final public static String WX_APP_ID = "wx350d62481e115db6";
	public static IWXAPI api;

}
