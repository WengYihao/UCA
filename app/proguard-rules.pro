# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in G:\weng\ll\studioandroid/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html
#
# Add any project specific keep options here:
#
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
#
# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile
-keepattributes EnclosingMetho
#3D 地图

-keepclass com.amap.api.mapcore.**{*;}

-keepclass com.amap.api.maps.**{*;}

-keepclass com.autonavi.amap.mapcore.*{*;}

#定位

-keepclass com.amap.api.location.**{*;}

-keepclass com.loc.**{*;}

-keepclass com.amap.api.fence.**{*;}

-keepclass com.autonavi.aps.amapapi.model.**{*;}

# 搜索

-keepclass com.amap.api.services.**{*;}

# 2D地图

-keepclass com.amap.api.maps2d.**{*;}

-keepclass com.amap.api.mapcore2d.**{*;}

# 导航

-keepclass com.amap.api.navi.**{*;}

-keepclass com.autonavi.**{*;}
#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
#优化  不优化输入的类文件
-dontoptimize
#预校验
-dontpreverify
#混淆时是否记录日志
-verbose
# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护注解
-keepattributes *Annotation*
# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
#忽略警告
-ignorewarning

-keep class com.youth.banner.** {
    *;
 }
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;


