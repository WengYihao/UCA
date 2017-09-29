package com.cn.uca.receiver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.cn.uca.R;
import com.cn.uca.ui.MainActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 * 更新服务Service类
 * @author weng
 */
public class UpdateService extends Service {
	// 标题
	private int titleId = 0;
	private String loadUrl;
	// 文件存储
	private File updateFile = null;
	// 下载文件的存放路径
	private File updateDir;
	// 通知栏
	private NotificationManager updateNotificationManager = null;
	private Notification updateNotification = null;
	// 通知栏跳转Intent
	private Intent updateIntent = null;
	private PendingIntent updatePendingIntent = null;
	// 下载完成标记常量
	private final static int DOWNLOAD_COMPLETE = 0;
	private final static int DOWNLOAD_FAIL = 1;
	private Thread downThread;

	private Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 下载成功
			case DOWNLOAD_COMPLETE:
				updateNotification = new Notification.Builder(UpdateService.this)
						.setContentTitle("下载成功")
						.setContentText("已完成")
						.setDefaults(Notification.DEFAULT_ALL)
						.setSmallIcon(R.mipmap.logo).build();
				updateNotificationManager.notify(0, updateNotification);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setDataAndType(Uri.fromFile(updateFile), "application/vnd.android.package-archive");
				startActivity(intent);
				stopSelf();// 停止服务
				break;
			// 下载失败
			case DOWNLOAD_FAIL:
				// 下载失败,这里是看要提示重新下载还是另外做其他操作
				updateNotification = new Notification.Builder(UpdateService.this)
				.setDefaults(Notification.DEFAULT_ALL)
				.setContentTitle("游咖旅行")
				.setContentText("下载失败")
				.build();
				updateNotificationManager.notify(0, updateNotification);
				break;
			default:
				stopSelf();
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 获取传值
		titleId = intent.getIntExtra("titleId", 0);
		loadUrl = intent.getStringExtra("loadUrl");
		Log.i("123",titleId+"-"+loadUrl);
		// 创建文件
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			// 组合下载地址
			updateDir = new File(Environment.getExternalStorageDirectory(), "UCA/app");
		} else {
			// files目录
			updateDir = getFilesDir();
		}
		// 拼凑下载文件文件名称
		updateFile = new File(updateDir.getPath(), getResources().getString(titleId) + ".apk");
		updateNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		updateNotification = new Notification();
		// 设置下载过程中，点击通知栏，回到主界面
//		updateIntent = new Intent(this, MainActivity.class);
//		updatePendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);
		// 设置通知栏显示内容
		updateNotification.tickerText = "开始下载";
		updateNotification = new Notification.Builder(UpdateService.this)
				.setContentTitle("游咖旅行")
				.setContentText("0%")
				.setDefaults(Notification.DEFAULT_ALL)
				.setSmallIcon(R.mipmap.logo)
				.build();
		// 发出通知
		updateNotificationManager.notify(0, updateNotification);
		// 开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
		downThread = new Thread(new updateRunnable());// 这个是下载的重点，是下载的过程
		downThread.start();
		return super.onStartCommand(intent, flags, startId);
	}

/**
 * 下载线程类
 * @author weng
 *
 */
	class updateRunnable implements Runnable {

		Message message = updateHandler.obtainMessage();

		@Override
		public void run() {
			message.what = DOWNLOAD_COMPLETE;
			try {
				// 文件目录是否存在
				if (!updateDir.exists()) {
					updateDir.mkdirs();
				}
				// 文件是否存在
				if (!updateFile.exists()) {
					updateFile.createNewFile();
				}
				// 下载函数
				// 增加权限
				long downloadSize = downloadUpdateFile(loadUrl, updateFile);
				if (downloadSize > 0) {
					// 下载成功,发送消息
					updateHandler.sendMessage(message);
				}
			} catch (Exception e) {
				e.printStackTrace();
				// 设置失败标识
				message.what = DOWNLOAD_FAIL;
				// 下载失败，发送消息
				updateHandler.sendMessage(message);
			}
		}

		/**
		 * 下载文件
		 * @param saveFile 文件名称
		 * @return
		 * @throws Exception
		 */
		private long downloadUpdateFile(String downloadUrl, File saveFile) throws Exception {
			int downloadCount = 0;
			int currentSize = 0;
			long totalSize = 0;
			int updateTotalSize = 0;

			HttpURLConnection httpConnection = null;
			// 输入流
			InputStream is = null;
			// 文件输出流
			FileOutputStream fos = null;
			try {
				URL url = new URL(downloadUrl);
				httpConnection = (HttpURLConnection) url.openConnection();
				httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");
				if (currentSize > 0) {
					httpConnection.setRequestProperty("RANGE", "bytes=" + currentSize + "-");
				}
				httpConnection.setConnectTimeout(10000);
				httpConnection.setReadTimeout(20000);
				updateTotalSize = httpConnection.getContentLength();
				if (httpConnection.getResponseCode() == 404) {
					throw new Exception("fail!");
				}
				is = httpConnection.getInputStream();
				fos = new FileOutputStream(saveFile, false);
				byte buffer[] = new byte[4096];
				int readsize = 0;
				while ((readsize = is.read(buffer)) > 0) {
					fos.write(buffer, 0, readsize);
					totalSize += readsize;
					// 为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次
					if ((downloadCount == 0) || (int) (totalSize * 100 / updateTotalSize) - 10 > downloadCount) {
						downloadCount += 10;
						updateNotification = new Notification.Builder(UpdateService.this)
								.setContentTitle("游咖旅行")
								.setContentText("正在下载       " + (int) totalSize * 100 / updateTotalSize + "%")
								.setContentIntent(updatePendingIntent)
								.setSmallIcon(R.mipmap.logo).build();
						updateNotificationManager.notify(0, updateNotification);
					}
				}
			} finally {
				if (httpConnection != null) {
					httpConnection.disconnect();
				}
				if (is != null) {
					is.close();
				}
				if (fos != null) {
					fos.close();
				}
			}
			return totalSize;
		}
	}

	@Override
	public void onDestroy() {
		downThread = null;
		super.onDestroy();
	}
}
