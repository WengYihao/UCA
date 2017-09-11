package com.cn.uca.config;

public class MyConfig {
	// 总接口
	public static final String url = "http://119.23.210.252/youkatravel/api/";
//	public static final String url = "http://192.168.1.105/youkatravel/api/";
	public static final String text = "http://192.168.1.100/youkatravel/api/abnormal/getAbnormal.do";    //测试
	public static final String sendCode = url+"user/query/sendSMS.do";//发送短信验证码
	public static final String getWeChatToken = "https://api.weixin.qq.com/sns/oauth2/access_token";     //获取微信token
	public static final String weChatLogin = url+"/user/query/userLogin_weixin.do";//微信登录
	public static final String uplodePic = url+"user/update/uploadHeadPortrait.do";//上传用户头像
	public static final String getUserBriefInfo = url+"user/query/getUserBriefInfo.do";//获取简单用户信息
	public static final String setUserInfo = url+"user/update/setUserInfo.do";//修改用户信息
	public static final String phoneLogin = url+"user/query/phone_login.do"; //手机用户登录
	public static final String phoneRegister = url+"user/update/userRegister.do";//手机号注册
	public static final String getUserInfo = url+"user/query/getUserInfo.do";//获取用户信息
	public static final String getUserPush = url+"user/query/getUserSetting.do";//获取用户推送设置
	public static final String setUserPush = url+"user/update/updateUserSetting.do";//用户是否推送
	public static final String updatePassword = url+"user/update/modifyPassword.do";//修改用户密码
	public static final String forgetPassword = url+"user/update/forgotPassword.do";//忘记密码
	public static final String submitIdentity = url+"user/update/submitIdentityExamine.do";//用户身份认证
}