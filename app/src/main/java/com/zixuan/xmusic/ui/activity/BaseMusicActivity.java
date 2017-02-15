package com.zixuan.xmusic.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.zixuan.xmusic.music.AudioPlayerService;
import com.zixuan.xmusic.music.MediaPlayerHelper;

/**
 * Created by zixuan on 2016/11/30.
 */

public class BaseMusicActivity extends AppCompatActivity implements ServiceConnection{
    private static final String TAG = "BaseMusicActivity";

    protected MediaSessionCompat.Token mToken;
    protected MediaControllerCompat mMediaController;
    protected MediaPlayerHelper mediaPlayerHelper;
    protected PlaybackStateCompat mState;
    protected MediaMetadataCompat mMetadata;

    protected MediaControllerCompat.Callback mMediaControllerCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);
            mState = state;
            BaseMusicActivity.this.onPlaybackStateChanged(state);
        }


        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            super.onMetadataChanged(metadata);
            mMetadata = metadata;
            BaseMusicActivity.this.onMetaDataChanged(metadata);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this,AudioPlayerService.class);
        bindService(intent,this,BIND_AUTO_CREATE);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this,AudioPlayerService.class);
        startService(intent);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        AudioPlayerService.ServiceBinder mBinder = (AudioPlayerService.ServiceBinder) iBinder;
        mToken = mBinder.getService().getMediaSessionToken();
        mediaPlayerHelper = mBinder.getService().getMediaPlayerHelper();
        try {
            mMediaController = new MediaControllerCompat(this,mToken);
            mMediaController.registerCallback(mMediaControllerCallback);
            mState = mediaPlayerHelper.getPlaybackState();
            mMetadata = mMediaController.getMetadata();

            BaseMusicActivity.this.onServiceConnected();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    protected void onPlaybackStateChanged(PlaybackStateCompat state){

    }

    protected void onMetaDataChanged(MediaMetadataCompat metadata){

    }

    protected void onServiceConnected(){

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
        mMediaController.unregisterCallback(mMediaControllerCallback);
        mMediaController = null;
        mediaPlayerHelper = null;
    }

    protected void  toast(String msg){
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
}
