package com.cn.uca.util;

import com.cn.uca.config.Constant;
import com.cn.uca.config.MyApplication;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 本地保存工具类
 */
public class SharePreferenceXutil {
	private static SharedPreferences sp;

	private static SharedPreferences getSp() {
		if (sp == null) {
			sp = MyApplication.getContext().getSharedPreferences(Constant.userIdKey, Context.MODE_PRIVATE);
		}
		return sp;
	}
	/**
	 * 保存ChannelId
	 * @param channelId
	 */
	public static void saveChannelId(String channelId) {
		getSp().edit().putString("channelId", channelId).commit();
	}

	/**
	 * 获取ChannelId
	 * @return
	 */
	public static String getChannelId() {
		return getSp().getString("channelId", "");
	}
	/**
	 * 设置是否登录成功
	 * @param isSuccess ：true:登录成功
	 */
	public static void setSuccess(boolean isSuccess) {
		getSp().edit().putBoolean("isSuccess", isSuccess).commit();
	}

	/**
	 * 获取是否登录成功
	 * @return
	 * 		true:登录成功
	 */
	public static boolean isSuccess() {
		return getSp().getBoolean("isSuccess", false);
	}
	/**
	 * 设置是否成为领咖
	 * @param isCollar ：true:是
	 */
	public static void setCollar(boolean isCollar) {
		getSp().edit().putBoolean("isCollar", isCollar).commit();
	}

	/**
	 * 获取是否成为领咖
	 * @return
	 * 		true:是
	 */
	public static boolean isCollar() {
		return getSp().getBoolean("isCollar", false);
	}
	/**
	 * 保存极光推送id
	 * @param registrationId
	 */
	public static void saveRegistrationId(String registrationId) {
		getSp().edit().putString("registrationId", registrationId).commit();
	}

	/**
	 * 设置是否退出
	 * @param isExit ：true:确认退出
	 */
	public static void setExit(boolean isExit) {
		getSp().edit().putBoolean("isExit", isExit).commit();
	}

	/**
	 * 获取是否退出
	 * @return
	 */
	public static boolean isExit() {
		boolean isExit = getSp().getBoolean("isExit", false);
		return isExit;
	}
	/**
	 * 保存是否认证
	 * @param isAuthentication ：true:已认证
	 */
	public static void setAuthentication(boolean isAuthentication) {
		getSp().edit().putBoolean("isAuthentication", isAuthentication).commit();
	}

	/**
	 * 获取是否认证
	 * @return
	 */
	public static boolean isAuthentication() {
		boolean isAuthentication = getSp().getBoolean("isAuthentication", false);
		return isAuthentication;
	}

	/**
	 * 保存是否签到
	 * @param isClock ：true:已签到
	 */
	public static void setClock(boolean isClock) {
		getSp().edit().putBoolean("isClock", isClock).commit();
	}

	/**
	 * 获取是否签到
	 * @return
	 */
	public static boolean isClock() {
		boolean isClock = getSp().getBoolean("isClock", false);
		return isClock;
	}

	/**
	 * 保存是否绑定微信
	 * @param isBindWeChat ：true:已绑定
	 */
	public static void setBindWeCaht(boolean isBindWeChat) {
		getSp().edit().putBoolean("isBindWeChat", isBindWeChat).commit();
	}

	/**
	 * 获取是否绑定微信
	 * @return
	 */
	public static boolean isBindWeCaht() {
		boolean isBindWeChat = getSp().getBoolean("isBindWeChat", false);
		return isBindWeChat;
	}

	/**
	 * 保存是否绑定支付宝
	 * @param isBindPay ：true:已绑定
	 */
	public static void setBindPay(boolean isBindPay) {
		getSp().edit().putBoolean("isBindPay", isBindPay).commit();
	}

	/**
	 * 获取是否绑定支付宝
	 * @return
	 */
	public static boolean isBindPay() {
		boolean isBindPay = getSp().getBoolean("isBindPay", false);
		return isBindPay;
	}

	/**
	 * 保存是否商家入驻
	 * @param isEnter ：true:已入驻
	 */
	public static void setEnter(boolean isEnter) {
		getSp().edit().putBoolean("isEnter", isEnter).commit();
	}

	/**
	 * 获取是否已入住
	 * @return
	 */
	public static boolean isEnter() {
		boolean isEnter = getSp().getBoolean("isEnter", false);
		return isEnter;
	}

	/**
	 * 保存是否成为领咖
	 * @param isLingKa ：true:领咖
	 */
	public static void setLingKa(boolean isLingKa) {
		getSp().edit().putBoolean("isLingKa", isLingKa).commit();
	}

	/**
	 * 获取是否成为领咖
	 * @return
	 */
	public static boolean isLingKa() {
		boolean isLingKa = getSp().getBoolean("isLingKa", false);
		return isLingKa;
	}
	/**
	 * 保存是否开启余生
	 * @param isOpenYS ：true:已开启
	 */
	public static void setOpenYS(boolean isOpenYS) {
		getSp().edit().putBoolean("isOpenYS", isOpenYS).commit();
	}

	/**
	 * 获取是否是否开启余生
	 * @return
	 */
	public static boolean isOpenYS() {
		boolean isOpenYS = getSp().getBoolean("isOpenYS", false);
		return isOpenYS;
	}
	/**
	 * 获取极光推送id
	 * @return
	 */
	public static String getRegistrationId() {
		return getSp().getString("registrationId", "");
	}
	/**
	 * 保存微信AccessToken
	 * @param accessToken
	 */
	public static void saveAccessToken(String accessToken) {
		getSp().edit().putString("accessToken", accessToken).commit();
	}

	/**
	 * 获取微信AccessToken
	 * @return
	 */
	public static String getAccessToken() {
		return getSp().getString("accessToken", "");
	}

	/**
	 * 保存融云用户RongToken
	 * @param rongToken
	 */
	public static void saveRongToken(String rongToken) {
		getSp().edit().putString("rongToken", rongToken).commit();
	}

	/**
	 * 获取融云用户RongToken
	 * @return
	 */
	public static String getRongToken() {
		return getSp().getString("rongToken", "");
	}

	/**
	 * 保存融云用户rongUserId
	 * @param rongUserId
	 */
	public static void saveRongUserId(String rongUserId) {
		getSp().edit().putString("rongUserId", rongUserId).commit();
	}

	/**
	 * 获取融云用户rongUserId
	 * @return
	 */
	public static String getRongUserId() {
		return getSp().getString("rongUserId", "");
	}
	/**
	 * 保存融云用户rongUserName
	 * @param rongUserName
	 */
	public static void saveRongUserName(String rongUserName) {
		getSp().edit().putString("rongUserName", rongUserName).commit();
	}

	/**
	 * 获取融云用户rongUserName
	 * @return
	 */
	public static String getRongUserName() {
		return getSp().getString("rongUserName", "");
	}
	/**
	 * 保存融云用户rongUserPortrait
	 * @param rongUserPortrait
	 */
	public static void saveRongUserPortrait(String rongUserPortrait) {
		getSp().edit().putString("rongUserPortrait", rongUserPortrait).commit();
	}

	/**
	 * 获取融云用户rongUserPortrait
	 * @return
	 */
	public static String getRongUserPortrait() {
		return getSp().getString("rongUserPortrait", "");
	}

	/**
	 * 保存微信OpenId
	 * @param openId
	 */
	public static void saveOpenId(String openId) {
		getSp().edit().putString("openId", openId).commit();
	}

	/**
	 * 获取微信OpenId
	 * @return
	 */
	public static String getOpenId() {
		return getSp().getString("openId", "");
	}

	/**
	 * 保存用户accountToken
	 * @param accountToken
	 */
	public static void saveAccountToken(String accountToken) {
		getSp().edit().putString("accountToken", accountToken).commit();
	}

	/**
	 * 获取用户accountToken
	 * @return
	 */
	public static String getAccountToken() {
		return getSp().getString("accountToken", "");
	}

	/**
	 * 保存用户绑定的手机号
	 * @param phoneNumber
	 */
	public static void savePhoneNumber(String phoneNumber) {
		getSp().edit().putString("phoneNumber", phoneNumber).commit();
	}

	/**
	 * 获取用户绑定的手机号
	 * @return
	 */
	public static String getPhoneNumber() {
		return getSp().getString("phoneNumber", "");
	}

	/**
	 * 保存用户名和密码
	 * @param userName
	 */
	public static void saveUserName(String userName) {
		getSp().edit().putString("userName", userName).commit();

	}

	/**
	 * 保存密码
	 * @param password
	 */
	public static void savePassword(String password){
		getSp().edit().putString("password", password).commit();
	}

	/**
	 * 获取用户名
	 * @return
	 */
	public static String getUserName() {
		return getSp().getString("userName", "");
	}

	/**
	 * 获取密码
	 * @return
	 */
	public static String getPassword() {
		return getSp().getString("password", "");
	}

	/**
	 * 设置是否退出
	 * @param isSign ：true:已签到
	 */
	public static void setSign(boolean isSign) {
		getSp().edit().putBoolean("isSign", isSign).commit();
	}

	/**
	 * 获取是否退出
	 * @return
	 */
	public static boolean isSign() {
		boolean isSign = getSp().getBoolean("isSign", false);
		return isSign;
	}

    /**
     * 设置是否微信登录
     * @param isWeChatLogin ：true
     */
    public static void setWeChatLogin(boolean isWeChatLogin) {
        getSp().edit().putBoolean("isWeChatLogin", isWeChatLogin).commit();
    }

    /**
     * 获取是否微信登录
     * @return
     */
    public static boolean isWeChatLogin() {
        boolean isWeChatLogin = getSp().getBoolean("isWeChatLogin", false);
        return isWeChatLogin;
    }


	/**
	 * 设置是否入驻
	 * @param isSettled ：true
	 */
	public static void setSettled(boolean isSettled) {
		getSp().edit().putBoolean("isSettled", isSettled).commit();
	}

	/**
	 * 获取是否入驻
	 * @return
	 */
	public static boolean isSettled() {
		boolean isSettled = getSp().getBoolean("isSettled", false);
		return isSettled;
	}


}
