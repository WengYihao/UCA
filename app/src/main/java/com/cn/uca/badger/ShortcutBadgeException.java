package com.cn.uca.badger;

/**
 * Created by asus on 2017/8/7.
 */

public class ShortcutBadgeException extends Exception {
    public ShortcutBadgeException(String message) {
        super(message);
    }

    public ShortcutBadgeException(String message, Exception e) {
        super(message, e);
    }

}

