 package com.cn.uca.zxing;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;

import com.cn.uca.R;

import java.io.Closeable;
import java.io.IOException;

 final class BeepManager implements MediaPlayer.OnErrorListener, Closeable {

   private static final String TAG = BeepManager.class.getSimpleName();

   private static final float BEEP_VOLUME = 1.0f;
   private static final long VIBRATE_DURATION = 200L;

   private final Activity activity;
   private MediaPlayer mediaPlayer;
   private boolean playBeep = true;
   private boolean vibrate = false;

   BeepManager(Activity activity) {
     this.activity = activity;
     this.mediaPlayer = null;
     updatePrefs();
   }

   synchronized void updatePrefs() {
     if (playBeep && mediaPlayer == null) {
       // The volume on STREAM_SYSTEM is not adjustable, and users found it too loud,
       // so we now play on the music stream.
       activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
       mediaPlayer = buildMediaPlayer(activity);
     }
   }

   synchronized void playBeepSoundAndVibrate() {
     if (playBeep && mediaPlayer != null) {
       mediaPlayer.start();
     }
     if (vibrate) {
       Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
       vibrator.vibrate(VIBRATE_DURATION);
     }
   }

   private MediaPlayer buildMediaPlayer(Context activity) {
     MediaPlayer mediaPlayer = new MediaPlayer();
     try {
       AssetFileDescriptor file = activity.getResources().openRawResourceFd(R.raw.scan_completed);
       try {
         mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
       } finally {
         file.close();
       }
       mediaPlayer.setOnErrorListener(this);
       mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
       mediaPlayer.setLooping(false);
       mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
       mediaPlayer.prepare();
       return mediaPlayer;
     } catch (IOException ioe) {
       Log.w(TAG, ioe);
       mediaPlayer.release();
       return null;
     }
   }

   @Override
   public synchronized boolean onError(MediaPlayer mp, int what, int extra) {
     if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
       // we are finished, so put up an appropriate error toast if required and finish
       activity.finish();
     } else {
       // possibly media player error, so release and recreate
       close();
       updatePrefs();
     }
     return true;
   }

   @Override
   public synchronized void close() {
     if (mediaPlayer != null) {
       mediaPlayer.release();
       mediaPlayer = null;
     }
   }

 }
