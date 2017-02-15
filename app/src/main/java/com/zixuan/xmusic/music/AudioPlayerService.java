package com.zixuan.xmusic.music;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;

import static com.zixuan.xmusic.constant.Constant.ACTION_NEXT;
import static com.zixuan.xmusic.constant.Constant.ACTION_PAUSE;
import static com.zixuan.xmusic.constant.Constant.ACTION_PLAY;
import static com.zixuan.xmusic.constant.Constant.ACTION_PREV;


public class AudioPlayerService extends Service {

    private MediaPlayerHelper mediaPlayerHelper;
    private Binder mBinder = new ServiceBinder();

    public class ServiceBinder extends Binder {

        public AudioPlayerService getService() {
            return AudioPlayerService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayerHelper = new MediaPlayerHelper(getApplicationContext());

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_PLAY:
                    mediaPlayerHelper.getMediaController().getTransportControls().play();
                    break;
                case ACTION_NEXT:
                    mediaPlayerHelper.getMediaController().getTransportControls().skipToNext();
                    break;
                case ACTION_PREV:
                    mediaPlayerHelper.getMediaController().getTransportControls().skipToPrevious();
                    break;
                case ACTION_PAUSE:
                    mediaPlayerHelper.getMediaController().getTransportControls().pause();
                    break;
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayerHelper.destoryService();
    }

    public MediaSessionCompat.Token getMediaSessionToken() {
        return mediaPlayerHelper.getMediaSessionToken();
    }

    public MediaPlayerHelper getMediaPlayerHelper() {
        return mediaPlayerHelper;
    }
}
