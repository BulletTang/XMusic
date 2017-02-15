package com.zixuan.xmusic.ui.activity;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zixuan.xmusic.R;
import com.zixuan.xmusic.handler.SeekbarHandler;
import com.zixuan.xmusic.music.MediaPlayerHelper;
import com.zixuan.xmusic.utils.LogHelper;
import com.zixuan.xmusic.utils.TimeUtils;

import java.io.File;
import java.lang.ref.WeakReference;

import cn.zhaiyifan.lyric.LyricUtils;
import cn.zhaiyifan.lyric.model.Lyric;
import okhttp3.Call;

public class SongPlayingActivity extends BaseMusicActivity implements View.OnClickListener {
    private static final String TAG = "SongPlayingActivity";

    private ImageView iv_song_art;
    private FloatingActionButton btn_play;
    private SeekBar seekBar;
    private SeekbarHandler handler;
    private TextView tv_currentPos;
    private TextView tv_duration;
    private ImageView btn_prev;
    private ImageView btn_next;
    private TextView tv_lrc;
    private Lyric lyric;
    private TextView tv_title;
    private TextView tv_song_author;
    private ImageView iv_playtype;
    private int mCurrentPlaytype;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_playing);
        initToolbar();
        initView();
        initListener();

    }
    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }
    private void initView(){
        iv_song_art = (ImageView) findViewById(R.id.iv_song_art);

        btn_play = (FloatingActionButton) findViewById(R.id.btn_play);
        btn_prev = (ImageView) findViewById(R.id.btn_prev);
        btn_next = (ImageView) findViewById(R.id.btn_play_next);

        seekBar = (SeekBar) findViewById(R.id.seekbar);
        tv_currentPos = (TextView) findViewById(R.id.tv_currentPos);
        tv_duration = (TextView) findViewById(R.id.tv_duration);

        tv_lrc = (TextView) findViewById(R.id.tv_lrc);
        tv_title = (TextView) findViewById(R.id.tv_songtitle);
        tv_song_author = (TextView) findViewById(R.id.tv_song_author);

        iv_playtype = (ImageView) findViewById(R.id.toolbar_playmode);

    }
    private void initListener(){


        btn_play.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_prev.setOnClickListener(this);

        iv_playtype.setOnClickListener(this);

    }
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        super.onServiceConnected(componentName, iBinder);
        handler = new SeekbarHandler(new WeakReference<>(seekBar), new WeakReference<>(tv_currentPos)
                , new WeakReference<>(tv_lrc));

        updateUiByMetadata();
        updateUiByState();

    }

    @Override
    protected void onPlaybackStateChanged(PlaybackStateCompat state) {
        super.onPlaybackStateChanged(state);
        updateUiByState();
    }

    @Override
    protected void onMetaDataChanged(MediaMetadataCompat metadata) {
        super.onMetaDataChanged(metadata);
        updateUiByMetadata();
    }

    private void updateUiByMetadata(){

        Glide.with(this).load(mMetadata.getString(MediaMetadataCompat.METADATA_KEY_ART_URI))
                .placeholder(R.drawable.placeholder_music).dontAnimate()
                .into(iv_song_art);
        updatePlaymodebtn(mediaPlayerHelper.getCurrentPlaymode());
        tv_title.setText(mMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
        tv_song_author.setText(mMetadata.getString(MediaMetadataCompat.METADATA_KEY_AUTHOR));
        tv_duration.setText(TimeUtils.turnTime((int)mMetadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)));
        tv_currentPos.setText(R.string.progress_zero);
        tv_lrc.setText("");
        searchLrc();

    }
    private void updateUiByState(){
        switch (mState.getState()){
            case PlaybackStateCompat.STATE_CONNECTING:
                LogHelper.d(TAG, "updateUiByState : connecting");
                btn_play.setImageResource(R.drawable.btn_pause);
                stopSeekbar();
                seekBar.setProgress(0);
                toast("正在连接");
                break;
            case PlaybackStateCompat.STATE_PLAYING:
                LogHelper.d(TAG, "updateUiByState : playing");
                btn_play.setImageResource(R.drawable.btn_pause);
                startSeekbar();
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                LogHelper.d(TAG, "updateUiByState : paused");
                btn_play.setImageResource(R.drawable.btn_play);
                stopSeekbar();
                seekBar.setMax((int) (mMetadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)*1000));
                int progress = (int) (SystemClock.elapsedRealtime() - mState.getLastPositionUpdateTime() + mState.getPosition());
                seekBar.setProgress(progress);
                tv_currentPos.setText(TimeUtils.turnTime(progress/1000));
                break;
            case PlaybackStateCompat.STATE_NONE:
                LogHelper.d(TAG, "updateUiByState : none");
                btn_play.setImageResource(R.drawable.btn_play);
                seekBar.setProgress(0);
                stopSeekbar();
                break;
        }
    }

    private void updatePlaymodebtn(int mode){
        mCurrentPlaytype = mode;
        if (mode == 0){
            iv_playtype.setImageResource(R.drawable.ic_random);
        }else if (mode == 1){
            iv_playtype.setImageResource(R.drawable.ic_playtype_circle);
        }else {
            iv_playtype.setImageResource(R.drawable.ic_playtype_single);
        }
    }

    private void startSeekbar(){
        handler.setDuration(mMetadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION) * 1000);
        handler.setState(mState);
        if (!handler.hasMessages(SeekbarHandler.MSG_PLAYING)){
            handler.sendEmptyMessage(SeekbarHandler.MSG_PLAYING);
        }
    }
    private void stopSeekbar(){
        handler.sendEmptyMessage(SeekbarHandler.MSG_STOP);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler.hasMessages(SeekbarHandler.MSG_PLAYING)){
            handler.sendEmptyMessage(SeekbarHandler.MSG_STOP);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_play:
                if (mState.getState() == PlaybackStateCompat.STATE_PLAYING){
                    mMediaController.getTransportControls().pause();
                }else if(mState.getState() == PlaybackStateCompat.STATE_PAUSED){
                    mMediaController.getTransportControls().play();
                }else if (mState.getState() == PlaybackStateCompat.STATE_NONE){
                    mMediaController.getTransportControls().prepare();
                }
                break;
            case R.id.btn_play_next:
                mMediaController.getTransportControls().skipToNext();
                break;
            case R.id.btn_prev:
                mMediaController.getTransportControls().skipToPrevious();
                break;
            case R.id.toolbar_playmode:
                mMediaController.getTransportControls().sendCustomAction(MediaPlayerHelper.ACTION_SWITCH_PLAYMODE,null);
                updatePlaymodebtn((mCurrentPlaytype+1)%3);
                break;
        }
    }

    private void downLoadLrc(){
        if (!URLUtil.isHttpUrl(mMetadata.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI))){
            tv_lrc.setText("暂无歌词");
            return;
        }
        OkHttpUtils.get().url(mMetadata.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI))
                .build().execute(new FileCallBack(getCacheDir().getAbsolutePath(),mMetadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)) {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "onError: lrc download error "+e.getMessage() );
                tv_lrc.setText("暂无歌词");
            }

            @Override
            public void onResponse(File response, int id) {
                lyric = LyricUtils.parseLyric(response, "UTF-8");
                handler.setLyric(lyric);
            }
        });
    }

    private void searchLrc(){
        File lrcFile = new File(getCacheDir(),mMetadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID));
        if (lrcFile.exists()){
            lyric = LyricUtils.parseLyric(lrcFile,"UTF-8");
            handler.setLyric(lyric);
        }else {
            downLoadLrc();
        }
    }


}
