package com.cn.uca.zxing;

import android.os.Handler;
import android.os.Message;

import com.cn.uca.R;
import com.cn.uca.zxing.camera.CameraManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;

import java.util.Collection;
import java.util.Map;


public final class CaptureActivityHandler extends Handler {

  private static final String TAG = CaptureActivityHandler.class.getSimpleName();

  private final CaptureActivity activity;
  private final DecodeThread decodeThread;
  private State state;
  private final CameraManager cameraManager;

  private enum State {
    PREVIEW,
    SUCCESS,
    DONE
  }

  CaptureActivityHandler(CaptureActivity activity,
                         Collection<BarcodeFormat> decodeFormats,
                         Map<DecodeHintType,?> baseHints,
                         String characterSet,
                         CameraManager cameraManager) {
    this.activity = activity;
    decodeThread = new DecodeThread(activity, decodeFormats, baseHints, characterSet, null
        );//new ViewfinderResultPointCallback(activity.getViewfinderView())
    decodeThread.start();
    state = State.SUCCESS;

    // Start ourselves capturing previews and decoding.
    this.cameraManager = cameraManager;
    cameraManager.startPreview();
    restartPreviewAndDecode();
  }

  @Override
  public void handleMessage(Message message) {
    int what = message.what;
    if(what == R.id.restart_preview){
      restartPreviewAndDecode();
    }
    else if(what == R.id.decode_succeeded){
      state = State.SUCCESS;
//      Bundle bundle = message.getData();
//      Bitmap barcode = null;
//      float scaleFactor = 1.0f;
//      if (bundle != null) {
//        byte[] compressedBitmap = bundle.getByteArray(DecodeThread.BARCODE_BITMAP);
//        if (compressedBitmap != null) {
//          barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
//          // Mutable copy:
//          barcode = barcode.copy(Bitmap.Config.ARGB_8888, true);
//        }
//        scaleFactor = bundle.getFloat(DecodeThread.BARCODE_SCALED_FACTOR);
//      }
      activity.handleDecode((Result) message.obj);
    }
    else if(what == R.id.decode_failed){
      state = State.PREVIEW;
      cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
    }
  }

  public void quitSynchronously() {
    state = State.DONE;
    cameraManager.stopPreview();
    Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
    quit.sendToTarget();
    try {
      // Wait at most half a second; should be enough time, and onPause() will timeout quickly
      decodeThread.join(500L);
    } catch (InterruptedException e) {
      // continue
    }

    // Be absolutely sure we don't send any queued up messages
    removeMessages(R.id.decode_succeeded);
    removeMessages(R.id.decode_failed);
  }

  private void restartPreviewAndDecode() {
    if (state == State.SUCCESS) {
      state = State.PREVIEW;
      cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
    }
  }

}
