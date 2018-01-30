package com.cn.uca.util;

import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringXutil {
	/**
	 * 判断edittext是否null
	 */
	public static String checkEditText(EditText editText) {
		if (editText != null && editText.getText() != null
				&& !(editText.getText().toString().trim().equals(""))) {
			return editText.getText().toString().trim();
		} else {
			return "";
		}
	}
	/**
	 * 判断String是否为空或null
	 * 
	 * @param msg
	 * @return true:为空或null
	 */
	public static boolean isEmpty(String msg) {
		if (msg == null || msg.equals("")) {
			return true;
		}
		return false;
	}

	public static String ListtoString(List<String> list){
		StringBuilder builder = new StringBuilder();
		for (int i = 0;i<list.size();i++){
			if (i+1 == list.size()){
				builder.append(list.get(i));
			}else{
				builder.append(list.get(i)+",");
			}
		}
		return builder.toString();
	}

	public static List<String> DateToDay(List<String> list){
		List<String> list1 = new ArrayList<>();
		for (int i = 0;i < list.size();i++){
			list1.add(list.get(i).substring(8,10));
		}
		return list1;
	}
	/**
	 * 正则表达式验证密码
	 *
	 * @param input
	 * @return
	 */
	public static boolean rexCheckPassword(String input) {
		// 6-20 位，字母、数字、字符
		// String reg =
		// "^([A-Z]|[a-z]|[0-9]|[`-=[];,./~!@#$%^*()_+}{:?]){6,20}$";
		String regStr = "^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]){6,20}$";
		return input.matches(regStr);
	}
	/**
	 * 判断字符串是否是邮箱格式
	 * 
	 * @param strEmail
	 * @return true:字符串是邮箱格式
	 */
	public static boolean isEmail(String strEmail) {
		String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}
	public static byte[] toByteArray(InputStream input){
		try{
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int n = 0;
			while (-1 != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
			}
			return output.toByteArray();
		}catch (Exception e){

		}
		return null;
	}
	/**
	 * 验证身份证
	 *
	 * @param identity_number
	 * @return
	 */
	public static boolean isIDCard(String identity_number) {
		String iDCard = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
		return identity_number.matches(iDCard);
	}
	/**
	 * 判断手机号码是否正确
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;

		String expression = "((^(13|14|15|17|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
		CharSequence inputStr = phoneNumber;

		Pattern pattern = Pattern.compile(expression);

		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

}
