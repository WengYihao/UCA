package com.cn.uca.config;

public class MyConfig {
	// 总接口
	public static final String url = "http://www.szyouka.com/youkatravel/api/";
//	public static final String url = "http://192.168.1.102/youkatravel/api/";
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
	public static final String getEscortRecords = url+"escort/query/getEscortRecords.do";//获取伴游
	public static final String getEscortRecordInfo = url+"escort/query/getEscortRecordInfo.do";//获取伴游详情
	public static final String addLine = url+"escort/update/insertRoute.do";//添加伴游路线
	public static final String getAllLine = url+"escort/query/getAllRoute.do";//获取伴游路线
	public static final String deleteLine = url+"escort/update/deleteRoute.do";//删除路线
	public static final String addLinePoint = url+"escort/update/addPlaces.do";//添加路线点
	public static final String updateLineName = url+"escort/update/updateRoute.do";//修改路线名
    public static final String getWallet = url+"user/query/queryBalance.do";//获取用户余额
	public static final String getReleaseInfo = url+"escort/query/getReleaseInfo.do";//获取发布页面数据
	public static final String collectionEscortRecord = url+"escort/update/collectionEscortRecord.do";//收藏/取消伴游
	public static final String getCarouselFigures = url+ "home/query/getCarouselFigures.do";//获取轮播图
	public static final String releaseEscortRecord = url+"escort/update/releaseEscortRecord.do";//发布伴游
	public static final String newAppVersionAndroid = url+"home/query/newAppVersionAndroid.do";//版本更新
	public static final String getUserState = url+"user/query/getUserState.do";//获取用户状态
	public static final String fastLogin = url+"user/query/fastLogin.do";//快速登录
	public static final String getEscortInfo = url+"escort/query/getEscortInfo.do";//获取约咖用户信息
	public static final String photo = "http://www.szyouka.com/youkatravel/api/util/pictureCompression.do?pictureUrl=";//图片压缩
	public static final String getZhouBianTravel = url+"travel/query/getZhouBianTravel.do";//获取周边游
	public static final String getJuiDian = url+"travel/query/getJuiDian.do";//获取周边游酒店
	public static final String getGuoNeiYou = url+"travel/query/getGuoNeiYou.do";//获取国内游
	public static final String getMiYeuYou = url+"travel/query/getMiYeuYou.do";//获取蜜月游
	public static final String getChuJingYou = url+"travel/query/getChuJingYou.do";//获取出境游
	public static final String getTicket = url+"travel/query/getTicket.do";//获取景区门票
}