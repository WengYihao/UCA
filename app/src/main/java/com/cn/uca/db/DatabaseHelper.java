package com.cn.uca.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	// 类没有实例化,是不能用作父类构造器的参数,必须声明为静态
	private static final String name = "city"; // 数据库名称
	private static final int version = 1; // 数据库版本

	public DatabaseHelper(Context context) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS recentcity (id integer primary key autoincrement, name varchar(40), date INTEGER)");
		db.execSQL("CREATE TABLE IF NOT EXISTS recentcounty (id integer primary key autoincrement, name varchar(40), date INTEGER)");
		db.execSQL("CREATE TABLE IF NOT EXISTS lookHistory(city_id integer primary key,city_name varchar(10),city_raiders_id integer,collection boolean,lock boolean ,pacture_url varchar(200),price double)");
		db.execSQL("CREATE TABLE IF NOT EXISTS translate (id integer primary key autoincrement, fromType varchar(40), toType varchar(40), src varchar(40),dst varchar(40))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
