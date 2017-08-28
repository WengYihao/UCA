package com.cn.uca.util;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import com.cn.uca.config.MyApplication;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
	/**获取状态栏的高度**/
	public static int getStatusBarHeight() 
	{
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		Context context = MyApplication.getInstance();
		try 
		{
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} 
		catch (Exception e1) 
		{
			e1.printStackTrace();
		}
		return statusBarHeight;
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
		// TODO: handle exception
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

	public static String getCurrentDate()
	{
		SimpleDateFormat formatter = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       
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
	 public static Date StringToUtilDate(String DateTimeString) throws ParseException {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(DateTimeString);
			return date;
		}
    public static String UtilDateToString(Date date) {
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
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
		Context context = MyApplication.getInstance();
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
