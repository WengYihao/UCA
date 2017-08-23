package com.cn.uca.badger.impl;

import android.content.ComponentName;
import android.content.Context;

import com.cn.uca.badger.ShortcutBadgeException;

import java.util.List;

/**
 * Created by asus on 2017/8/7.
 */

public interface Badger {

    void executeBadge(Context context, ComponentName componentName, int badgeCount) throws ShortcutBadgeException;
    List<String> getSupportLaunchers();
}
