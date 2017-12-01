package com.cn.uca.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

import com.cn.uca.ui.view.rongim.ChatListActivity;

public class ActivityCollector {  
    
    public static List<Activity> activities = new ArrayList<Activity>();
    public static List<Activity> loginActivity = new ArrayList<>();
    public static List<Activity> registerActivity = new ArrayList<>();
    public static List<Activity> forgetActivity = new ArrayList<>();

    public static void loginActivity(Activity activity) {
        loginActivity.add(activity);
    }
    public static void finishLogin() {
        for (Activity activity : loginActivity) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static void registerActivity(Activity activity){
        registerActivity.add(activity);
    }

    public static void finishRegister(){
        for (Activity activity : registerActivity) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static void forgetActivity(Activity activity){
        forgetActivity.add(activity);
    }

    public static void finishForget(){
        for (Activity activity : forgetActivity) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static void addActivity(Activity activity) {  
        activities.add(activity);  
    }  
    public static void removeActivity(Activity activity) {  
        activities.remove(activity);  
    }  
    public static void finishAll() {  
        for (Activity activity : activities) {  
            if (!activity.isFinishing()) {  
                activity.finish();  
            }  
        }  
    }
    private static ArrayList<Activity> mActivities = new ArrayList<Activity>();
    public static void popActivity(Activity activity) {
        if (mActivities.contains(activity)) {
            activity.finish();
            mActivities.remove(activity);
        }
    }
    public static void popAllActivity() {
        try {
            if (ChatListActivity.mViewPager != null) {
                ChatListActivity.mViewPager.setCurrentItem(0);
            }
            for (Activity activity : mActivities) {
                if (activity != null) {
                    activity.finish();
                }
            }
            mActivities.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void pushActivity(Activity activity) {
        mActivities.add(activity);
    }
}  