package com.cn.uca.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class ActivityCollector {  
    
    public static List<Activity> activities = new ArrayList<Activity>();
    public static List<Activity> loginActivity = new ArrayList<>();
    public static List<Activity> registerActivity = new ArrayList<>();

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
  
}  