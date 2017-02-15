package com.zixuan.xmusic.handler;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zixuan.xmusic.utils.TimeUtils;

import java.lang.ref.WeakReference;

import cn.zhaiyifan.lyric.LyricUtils;
import cn.zhaiyifan.lyric.model.Lyric;


/**
 * 更新歌曲播放进度条和歌词
 */
public class SeekbarHandler extends Handler {

    public static final String TAG = "SeekbarHandler";
    public static final int MSG_PLAYING = 1;
    public static final int MSG_STOP = 2;

    private WeakReference<SeekBar> seekbar;
    private WeakReference<TextView> tv_currentPos;
    private WeakReference<TextView> tv_lrc;
    private Lyric lyric;
    private long mLastUpdateTime;
    private long mLastUpdatePosition;

    public SeekbarHandler(WeakReference<SeekBar> seekbar,WeakReference<TextView> tv,WeakReference<TextView> lrc) {
        this.seekbar = seekbar;
        this.tv_currentPos = tv;
        this.tv_lrc = lrc;
    }

    public void setState(PlaybackStateCompat state){
        mLastUpdateTime = state.getLastPositionUpdateTime();
        mLastUpdatePosition = state.getPosition();
    }
    public void setDuration(long duration){
        seekbar.get().setMax((int) duration);
    }
    public void setLyric(Lyric lrc){
        lyric = lrc;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case MSG_PLAYING:
                int progress = (int) (SystemClock.elapsedRealtime()-mLastUpdateTime+mLastUpdatePosition);
                tv_currentPos.get().setText(TimeUtils.turnTime(progress/1000));
                seekbar.get().setProgress(progress);
                if (lyric != null){
                    try {
                        Lyric.Sentence sentence = LyricUtils.getSentence(lyric, progress);
                        if (sentence != null){
                            tv_lrc.get().setText(sentence.content);
                        }
                    }catch (Exception e){
                        tv_lrc.get().setText("暂无歌词");
                    }

                }
                sendEmptyMessageDelayed(MSG_PLAYING,1000);
                break;
            case MSG_STOP:
                removeCallbacksAndMessages(null);
                break;
        }
    }
}
