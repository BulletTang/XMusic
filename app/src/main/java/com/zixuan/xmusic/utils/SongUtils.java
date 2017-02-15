package com.zixuan.xmusic.utils;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.zixuan.xmusic.bean.SongBean;
import com.zixuan.xmusic.constant.Constant;

/**
 * Created by zixuan on 2016/12/20.
 */

public class SongUtils {
    //将SongBean实体转化为QueueItem
    public static MediaSessionCompat.QueueItem buildQueueItemFromSong(SongBean song) {

        long id = Long.parseLong(song.getSong_id());
        Bundle bundle = new Bundle();
        bundle.putString(Constant.EXTRA_FILE_LINK, song.getFile_link());
        bundle.putString(Constant.EXTRA_LRC_LINK, song.getLrclink());
        bundle.putString(Constant.EXTRA_SONG_ART_HIGH, song.getPic_premium());
        bundle.putString(Constant.EXTRA_SONG_ART_LOW, song.getPic_small());
        bundle.putLong(Constant.EXTRA_DURATION, song.getFile_duration());

        MediaDescriptionCompat description = new MediaDescriptionCompat.Builder()
                .setTitle(song.getTitle())
                .setSubtitle(song.getAuthor())
                .setMediaId(song.getSong_id())
                .setMediaUri(Uri.parse(song.getFile_link()))
                .setIconUri(Uri.parse(song.getPic_small()))
                .setDescription(song.getAlbum_title())
                .setExtras(bundle)
                .build();

        return new MediaSessionCompat.QueueItem(description, id);

    }


}
