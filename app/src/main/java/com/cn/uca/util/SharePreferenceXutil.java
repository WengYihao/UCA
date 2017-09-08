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
		boolean isAutoLogin = getSp().getBoolean("isSuccess", false);
		return isAutoLogin;
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
		boolean isAutoLogin = getSp().getBoolean("isExit", false);
		return isAutoLogin;
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

}
