package com.cn.uca.util;

import java.io.File;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import com.cn.uca.config.MyApplication;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


public class SystemUtil
{
	
	private final static String[] dayNames = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
	private static int virtualKeyHeight=-1;//虚拟键高度
	/** 获取屏幕的宽度 */
	public final static int getWindowWidth(Activity activity) 
	{
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	/** 获取屏幕的高度 */
	public final static int getWindowHeight(Activity activity) 
	{
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
	
	/** 获取屏幕的显示尺寸 */
	public final static DisplayMetrics getDisplayMetrics(Activity activity) 
	{
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}
	/**获取状态栏的高度**/
	public static int getVirtualKeyHeight(Activity activity){
		if(virtualKeyHeight==-1){
			virtualKeyHeight=getDpi(activity)-getWindowHeight(activity);
			
		}
		return virtualKeyHeight;
		
	} 
	/** 获取原始屏幕的高度 **/
	private static int getDpi(Activity activity){
		 int dpi = 0;
		 Display display =activity.getWindowManager().getDefaultDisplay();
		 DisplayMetrics dm = new DisplayMetrics();
		 @SuppressWarnings("rawtypes")
		 Class c;
		try {
		c = Class.forName("android.view.Display");
		@SuppressWarnings("unchecked")
		java.lang.reflect.Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
		method.invoke(display, dm);
		dpi=dm.heightPixels;
		}catch(Exception e){
		e.printStackTrace();
		}
		return dpi;
	}
	/**
	 * 照片命名
	 * @return
	 */
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".png";
	}

	/**
	 * 根据string.xml资源格式化字符串
	 *
	 * @param context
	 * @param resource
	 * @param args
	 * @return
	 */
	public static String formatResourceString(Context context, int resource, Object... args) {
		String str = context.getResources().getString(resource);
		if (TextUtils.isEmpty(str)) {
			return null;
		}
		return String.format(str, args);
	}
    public static String getRealPathFromURI(Uri contentUri,Activity activity) {
        String res = null;
		try{
            if (contentUri != null){
                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
                if(cursor.moveToFirst()){;
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    res = cursor.getString(column_index);
                }
                cursor.close();
            }
		}catch (Exception e){
            return null;
		}
        return res;
    }

	/**
	 * 获取磁盘缓存文件
	 *
	 * @param context
	 * @param uniqueName
	 * @return
	 */
	public static File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}
	/**
	 * 获取应用程序版本号
	 *
	 * @param context
	 * @return
	 */
	public static int getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return info.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}
	public static String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}
	/**
	 * 获取拍照相片存储文件
	 *
	 * @param context
	 * @return
	 */
	public static File createFile(Context context) {
		File file;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String timeStamp = String.valueOf(new Date().getTime());
			file = new File(Environment.getExternalStorageDirectory() +
					File.separator + timeStamp + ".jpg");
		} else {
			File cacheDir = context.getCacheDir();
			String timeStamp = String.valueOf(new Date().getTime());
			file = new File(cacheDir, timeStamp + ".jpg");
		}
		return file;
	}
	private static String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
	/**
	 * 获取状态栏高度
	 * @return
	 */
	public static int getStatusHeight(Context activity) {
		int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
		return activity.getResources().getDimensionPixelSize(resourceId);

	}
	// 判断sd卡是否存在
	public static boolean hasSDCard() 
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	public static int getConnectedType(Context paramContext) {
		if (paramContext != null) {
			NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
			if ((localNetworkInfo != null) && (localNetworkInfo.isAvailable())) {
				return localNetworkInfo.getType();
			}
		}
		return -1;
	}

	/**
	 * 判断文件是否存在
	 * @param name
	 * @return
	 */
	public boolean fileIsExists(String name){
		try{
		File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ name);
		if(!f.exists()){
		return false;
		}

		}catch (Exception e) {
		return false;
		}
		return true;
		}
	
	@SuppressWarnings("deprecation")
	public static boolean isByTypeConnected(Context paramContext, int paramInt) {
		if (paramContext != null) {
			NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext
					.getSystemService("connectivity")).getNetworkInfo(paramInt);
			if (localNetworkInfo != null) {
				return localNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	// 检测网络连接
	/** 检查是否有网络 */
	public static boolean isNetworkAvailable(Context context) {
		NetworkInfo info = getNetworkInfo(context);
		if (info != null) {
			return info.isAvailable();
		}
		return false;
	}

	/** 检查是否是WIFI */
	public static boolean isWifi(Context context) {
		NetworkInfo info = getNetworkInfo(context);
		if (info != null) {
			if (info.getType() == ConnectivityManager.TYPE_WIFI)
				return true;
		}
		return false;
	}

	/** 检查是否是移动网络 */
	public static boolean isMobile(Context context) {
		NetworkInfo info = getNetworkInfo(context);
		if (info != null) {
			if (info.getType() == ConnectivityManager.TYPE_MOBILE)
				return true;
		}
		return false;
	}
	private static NetworkInfo getNetworkInfo(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo();
	}

	/**
	 * 比较两个时间相差的天数
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public static int getIntervalDays(Date fDate, Date oDate) {
		if (null == fDate || null == oDate) {
			return -1;
		}
		long intervalMilli = oDate.getTime() - fDate.getTime();
		return (int) (intervalMilli / (24 * 60 * 60 * 1000));
	}
	public static String getCurrentDate()
	{
		SimpleDateFormat formatter = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
		String str = formatter.format(curDate); 
		return str;
	}
	public static String getCurrentDate2()
	{
		SimpleDateFormat formatter = new  SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		String str = formatter.format(curDate);
		return str;
	}
	public static String getCurrentDateOnly()
	{
		SimpleDateFormat formatter = new  SimpleDateFormat("yyyy-MM-dd");       
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
		String str = formatter.format(curDate); 
		return str;
	}
	
	public static String getCurrentDateOnly2()
	{
		SimpleDateFormat formatter = new  SimpleDateFormat("yyyy.MM.dd");       
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
		String str = formatter.format(curDate); 
		return str;
	}


	/**
	 * 日期转星期
	 *
	 * @param datetime
	 * @return
	 */
	public static String dateToWeek(String datetime) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		Calendar cal = Calendar.getInstance(); // 获得一个日历
		Date datet = null;
		try {
			datet = f.parse(datetime);
			cal.setTime(datet);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
		if (w < 0)
			w = 0;
		return weekDays[w];
	}
	/**
	 * 获取当前年月
	 * @return
	 */
	public static String getCurrentDateOnly3()
	{
		SimpleDateFormat formatter = new  SimpleDateFormat("yyyy-MM");
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	/**
	 * 获取某月年月
	 * @param date
	 * @return
	 */
	public static String getCurrentDateOnly4(Date date)
	{
		SimpleDateFormat formatter = new  SimpleDateFormat("yyyy-MM");
		String str = formatter.format(date);
		return str;
	}

	/**
	 * 计算时间差
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static String countDate(Date d1,Date d2){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long l=d2.getTime()-d1.getTime();
		long day=l/(24*60*60*1000);
		long hour=(l/(60*60*1000)-day*24);
		long min=((l/(60*1000))-day*24*60-hour*60);
		long s=(l/1000-day*24*60*60-hour*60*60-min*60);
//		System.out.println(""+day+"天"+hour+"小时"+min+"分"+s+"秒");
		return "";
	}
	/**
	 * 获取未来 第 past 天的日期
	 * @param past
	 * @return
	 */
	public static String getFetureDate(int past) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
		Date today = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		String result = format.format(today);
		return result;
	}

	public static List<String> getBetweenDate(String date,int past){
		List<String> list = null;
		try{
			Date start = StringToUtilDate(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(start);
			cal.add(Calendar.DAY_OF_MONTH, past);
			Date end = cal.getTime();
			list = getBetweenDateString(start,end);
		}catch (Exception e){

		}
		return list;
	}
	/**
	 * 日期转换字符串
	 * @param DateTimeString
	 * @return
	 * @throws ParseException
	 */
	 public static Date StringToUtilDate(String DateTimeString) throws ParseException {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(DateTimeString);
			return date;
	 }
	public static Date StringToUtilDate2(String DateTimeString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(DateTimeString);
		return date;
	}
	public static Date StringToUtilDate3(String DateTimeString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = sdf.parse(DateTimeString);
		return date;
	}

	public static Date StringToUtilDate4(String DateTimeString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date date = sdf.parse(DateTimeString);
		return date;
	}
	/**
	 * 获取两个日期之间的日期
	 * @param start 开始日期
	 * @param end 结束日期
	 * @return 日期集合
	 */
	public static List<Date> getBetweenDates(Date start, Date end) {
		List<Date> result = new ArrayList<Date>();
		Calendar tempStart = Calendar.getInstance();
		tempStart.setTime(start);
		tempStart.add(Calendar.DAY_OF_YEAR, 1);

		Calendar tempEnd = Calendar.getInstance();
		tempEnd.setTime(end);
		result.add(start);
		while (tempStart.before(tempEnd)) {
			result.add(tempStart.getTime());
			tempStart.add(Calendar.DAY_OF_YEAR, 1);
		}
		result.add(end);
		return result;
	}

	public static List<String> getBetweenDateString(Date start, Date end) {
		List<String> result = new ArrayList<>();
		Calendar tempStart = Calendar.getInstance();
		tempStart.setTime(start);
		tempStart.add(Calendar.DAY_OF_YEAR, 1);

		Calendar tempEnd = Calendar.getInstance();
		tempEnd.setTime(end);
		result.add(UtilDateToString(start));
		while (tempStart.before(tempEnd)) {
			result.add(UtilDateToString(tempStart.getTime()));
			tempStart.add(Calendar.DAY_OF_YEAR, 1);
		}
		result.add(UtilDateToString(end));
		return result;
	}
	//由出生日期获得年龄
	public static  int getAge(Date birthDay) throws Exception {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			throw new IllegalArgumentException(
					"The birthDay is before Now.It's unbelievable!");
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) age--;
			}else{
				age--;
			}
		}
		return age;
	}
	public static String UtilDateToString(Date date) {
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = sdf.format(date);
			return dateString;
	}
	public static String UtilDateToString2(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		String dateString = sdf.format(date);
		return dateString;
	}
	public static String UtilDateToString3(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateString = sdf.format(date);
		return dateString;
	}
	/*时间戳转换成字符窜*/
    public static String getDateToString(long time) 
    {
        Date d = new Date(time);
        SimpleDateFormat  sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(d);
    }
    public static String formatDate(long time) 
    {
		return formatDateTime(time, 0);
	}
    /*时间戳转换成字符窜*/
    public static String getDateOnly(long time) 
    {
        Date d = new Date(time);
        SimpleDateFormat  sf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        return sf.format(d);
    }
    
    /*时间戳转换成字符窜*/
    public static String getDateOnly(String timeStr)
    {
    	if(TextUtils.isEmpty(timeStr)||timeStr.equals("null"))
    	{
    		SimpleDateFormat formatter = new  SimpleDateFormat("yyyy-MM-dd HH:mm");       
    		Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
    		String str = formatter.format(curDate); 
    		return str;
    	}
    	long time = Long.parseLong(timeStr);
        Date d = new Date(time);
        SimpleDateFormat  sf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        return sf.format(d);
    }

	public static String getDateOnly2(String timeStr)
	{
		if(TextUtils.isEmpty(timeStr)||timeStr.equals("null"))
		{
			SimpleDateFormat formatter = new  SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date curDate = new Date(System.currentTimeMillis());//获取当前时间
			String str = formatter.format(curDate);
			return str;
		}
		long time = Long.parseLong(timeStr);
		Date d = new Date(time);
		SimpleDateFormat  sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(d);
	}
    /**
     * 
     * @param time
     * @return
     */
	public static String formatDate(Date time)
	{
		return time == null ? "" : formatDateTime(time.getTime(), 0);
	}

	public static String formatTimestamp() 
	{
		return formatDateTime(System.currentTimeMillis(), 2);
	}

	/**
	 * 格式化成周几
	 * @param time
	 * @return
	 */
	public static String formatDateWeek(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return dayNames[dayOfWeek - 1];
	}
	
	public static String formatDateTime(long time, int type)
	{
		SimpleDateFormat df;
		switch (type) {
		case 0:
			df = new SimpleDateFormat("yyyy-MM-dd");
			break;
		case 1:
			df = new SimpleDateFormat("HH:mm:ss");
			break;
		case 2:
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			break;
		case 3:
			df = new SimpleDateFormat("MM-dd");
			break;
		case 4:
			df = new SimpleDateFormat("HH:mm");
			break;
		case 5:
			df = new SimpleDateFormat("MM-dd HH:mm:ss");
			break;
		case 6:
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			break;
		case 7:
			df = new SimpleDateFormat("hh");
		case 8:
			df = new SimpleDateFormat("HH");
			break;
		case 9:
			df = new SimpleDateFormat("yyyy年MM月dd日");
			break;
		case 10:
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			break;
		case 11:
			df = new SimpleDateFormat("MM月dd日    HH:mm");
			break;
		case 12:
			df = new SimpleDateFormat("MM月dd日");
			break;
		default:
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			break;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		return df.format(cal.getTime());
	}
	
	int GB= 1024 * 1024 * 1024 ; 
	// 定义GB的计算常量 2 
	int MB= 1024 * 1024 ; 
	// 定义MB的计算常量 3 
	int KB= 1024;
	
	public static String getDataSize(long bytes) 
	{  
        BigDecimal filesize = new BigDecimal(bytes);  
        BigDecimal megabyte = new BigDecimal(1024 * 1024);  
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)  .floatValue();  
        if (returnValue > 1)  
            return (returnValue + "MB");  
        BigDecimal kilobyte = new BigDecimal(1024);  
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)  
                .floatValue();  
        return (returnValue + "KB");  
    } 
	public static String getTimeBySecond(int duration)
	{
	   duration = duration/1000;
	   long hour=duration/3600;// !小时
	   long minute=duration%3600/60;// !分钟
	   long second=duration%60;// !秒
	   
	   String stamp = "";
	   
	   String b = "" ;
	   String c = "" ;
	   String d = "" ;
	   if(second< 10)
	   {
		   b = "0";
	   }
	   if(minute< 10)
	   {
		   c = "0";
	   }
	   if(hour< 10)
	   {
		   d = "0";
	   }
	   if(hour>0)
	   {
		   stamp+=d+hour + ":";
	   }
	   stamp += c+minute + ":" + b+second;
	   return stamp;
	}
	public static boolean isCloseEnough(String msgTime, String msgTime2)
	{
		return false;
	}
    /**
	 * @param context
	 * @param dpValue
	 */
	public static int dipTopx(Context context, float dpValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px  的单位 转成为 dp(像素)
	 */
	public static int pxTodip(Context context, float pxValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(float dpValue) 
	{
		Context context = MyApplication.getContext();
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) 
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	public static float toDp(float pxValue)
	{
		Context context = MyApplication.getInstance();
		return pxValue * context.getResources().getDisplayMetrics().density + 0.5f;
	} 
	
	public static String getVersionName(Activity activity)
	{
		// 获取packagemanager的实例
		PackageManager packageManager = activity.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try 
		{
			packInfo = packageManager.getPackageInfo(activity.getPackageName(),0);
		} 
		catch (NameNotFoundException e) 
		{
			e.printStackTrace();
		}
		return packInfo.versionName;
	}
	public static int getVersionCode(Activity activity)
	{
		// 获取packagemanager的实例
		PackageManager packageManager = activity.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try 
		{
			packInfo = packageManager.getPackageInfo(activity.getPackageName(),0);
		} 
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return packInfo.versionCode;
	}
	public static void hideKeyboard(Activity activity)
	{
		InputMethodManager im = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE); 
		im.hideSoftInputFromWindow(activity.getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	public static void displayKeyboard(final EditText view)//Activity activity)
	{
//		InputMethodManager im = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE); 
//		im.hideSoftInputFromWindow(activity.getCurrentFocus().getApplicationWindowToken(), InputMethodManager.SHOW_FORCED);
		
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		view.requestFocus();
		
		Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
        	@Override
			public void run()
			{
        		InputMethodManager inputManager = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE); 
                inputManager.showSoftInput(view, 0);
			}},200);
	}
}
