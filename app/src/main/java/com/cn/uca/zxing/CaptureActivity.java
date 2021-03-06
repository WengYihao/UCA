package com.cn.uca.zxing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.ui.view.util.BaseBackActivity;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.zxing.camera.CameraManager;
import com.google.zxing.Result;

import java.io.IOException;
import java.lang.reflect.Field;

public final class CaptureActivity extends BaseBackActivity implements SurfaceHolder.Callback,View.OnClickListener{

  private static final String TAG = CaptureActivity.class.getSimpleName();

  private CameraManager cameraManager;
  private CaptureActivityHandler handler;
  private boolean hasSurface;
  private InactivityTimer inactivityTimer;
  private BeepManager beepManager;

  private RelativeLayout mRlScanContainer = null;
  private RelativeLayout mRlScanArea = null;
  private ImageView mIvScanLine = null;
  private Rect mScanRect;
  private TextView back;

  public Handler getHandler() {
    return handler;
  }

  CameraManager getCameraManager() {
    return cameraManager;
  }

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);

    Window window = getWindow();
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    setContentView(R.layout.activity_capture);
    initView();
    hasSurface = false;
    inactivityTimer = new InactivityTimer(this);
    beepManager = new BeepManager(this);
  }

  private void initView(){
     mRlScanContainer = (RelativeLayout) findViewById(R.id.rl_scan_container);
     mRlScanArea = (RelativeLayout) findViewById(R.id.rl_scan_area);
     mIvScanLine = (ImageView) findViewById(R.id.iv_scan_line);
     TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
            .RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
            0.9f);
     animation.setDuration(4500);
     animation.setRepeatCount(-1);
     animation.setRepeatMode(Animation.RESTART);
     mIvScanLine.startAnimation(animation);
    back = (TextView)findViewById(R.id.back);
    back.setOnClickListener(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    
    // CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
    // want to open the camera driver and measure the screen size if we're going to show the help on
    // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
    // off screen.
    cameraManager = new CameraManager(getApplication());

    handler = null;

    beepManager.updatePrefs();

    inactivityTimer.onResume();

    SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
    SurfaceHolder surfaceHolder = surfaceView.getHolder();
    if (hasSurface) {
      // The activity was paused but not stopped, so the surface still exists. Therefore
      // surfaceCreated() won't be called, so init the camera here.
      initCamera(surfaceHolder);
    } else {
      // Install the callback and wait for surfaceCreated() to init the camera.
      surfaceHolder.addCallback(this);
    }
  }

  @Override
  protected void onPause() {
    if (handler != null) {
      handler.quitSynchronously();
      handler = null;
    }
    inactivityTimer.onPause();
    beepManager.close();
    cameraManager.closeDriver();
    if (!hasSurface) {
      SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
      SurfaceHolder surfaceHolder = surfaceView.getHolder();
      surfaceHolder.removeCallback(this);
    }
    super.onPause();
  }

  @Override
  protected void onDestroy() {
    inactivityTimer.shutdown();
    super.onDestroy();
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    if (holder == null) {
      Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
    }
    if (!hasSurface) {
      hasSurface = true;
      initCamera(holder);
    }
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    hasSurface = false;
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

  }

  /**
   * A valid barcode has been found, so give an indication of success and show the results.
   *
   * @param rawResult The contents of the barcode.
   */
  public void handleDecode(Result rawResult) {
    inactivityTimer.onActivity();
    beepManager.playBeepSoundAndVibrate();

    Intent resultIntent = new Intent();
    resultIntent.putExtra("result", rawResult.getText());
    this.setResult(RESULT_OK, resultIntent);
    CaptureActivity.this.finish();
  }

  private void initCamera(SurfaceHolder surfaceHolder) {
    if (surfaceHolder == null) {
      throw new IllegalStateException("No SurfaceHolder provided");
    }
    if (cameraManager.isOpen()) {
      Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
      return;
    }
    try {
      cameraManager.openDriver(surfaceHolder);
      // Creating the handler starts the preview, which can also throw a RuntimeException.
      if (handler == null) {
        handler = new CaptureActivityHandler(this, null, null, null, cameraManager);
      }
      initScanArea();
    } catch (IOException ioe) {
      Log.w(TAG, ioe);
      displayFrameworkBugMessageAndExit();
    } catch (RuntimeException e) {
      // Barcode Scanner has seen crashes in the wild of this variety:
      // java.?lang.?RuntimeException: Fail to connect to camera service
      Log.w(TAG, "Unexpected error initializing camera", e);
      displayFrameworkBugMessageAndExit();
    }
  }

  private void displayFrameworkBugMessageAndExit() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("提示");
    builder.setMessage("初始化摄像头出错");
    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        finish();
      }

    });
    builder.show();
  }

  public Rect getScanArea(){
     return mScanRect;
  }

  /**
   * 初始化扫描的矩形区域
   */
  private void initScanArea() {
    int cameraWidth = cameraManager.getCameraResolution().y;
    int cameraHeight = cameraManager.getCameraResolution().x;

    /** 获取布局中扫描框的位置信息 */
    int[] location = new int[2];
    mRlScanArea.getLocationInWindow(location);

    int cropLeft = location[0];
    int cropTop = location[1] - getStatusBarHeight();

    int cropWidth = mRlScanArea.getWidth();
    int cropHeight = mRlScanArea.getHeight();

    /** 获取布局容器的宽高 */
    int containerWidth = mRlScanContainer.getWidth();
    int containerHeight = mRlScanContainer.getHeight();

    /** 计算最终截取的矩形的左上角顶点x坐标 */
    int x = cropLeft * cameraWidth / containerWidth;
    /** 计算最终截取的矩形的左上角顶点y坐标 */
    int y = cropTop * cameraHeight / containerHeight;

    /** 计算最终截取的矩形的宽度 */
    int width = cropWidth * cameraWidth / containerWidth;
    /** 计算最终截取的矩形的高度 */
    int height = cropHeight * cameraHeight / containerHeight;

    /** 生成最终的截取的矩形 */
    mScanRect = new Rect(x, y, width + x, height + y);
  }

  private int getStatusBarHeight() {
    try {
      Class<?> c = Class.forName("com.android.internal.R$dimen");
      Object obj = c.newInstance();
      Field field = c.getField("status_bar_height");
      int x = Integer.parseInt(field.get(obj).toString());
      return getResources().getDimensionPixelSize(x);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.back:
        this.finish();
        break;
    }
  }
}
