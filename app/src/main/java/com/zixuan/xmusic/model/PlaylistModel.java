package com.zixuan.xmusic.model;

import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zixuan.xmusic.api.BMA;
import com.zixuan.xmusic.bean.SongBean;
import com.zixuan.xmusic.constant.Constant;
import com.zixuan.xmusic.global.XMusicApplication;
import com.zixuan.xmusic.utils.LogHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

//歌单列表获取
public class PlaylistModel {
    private static final String TAG = "PlaylistModel";

    public void getGedanInfo(String  listId , final OnPlaylistInfoListener listener){
        OkHttpUtils.get().url(BMA.GeDan.geDanInfo(listId)).build().execute(new Callback<List<MediaSessionCompat.QueueItem>>() {
            @Override
            public List<MediaSessionCompat.QueueItem> parseNetworkResponse(Response response, int id) throws Exception {
                JSONObject result = new JSONObject(response.body().string());
                JSONArray pArray = result.getJSONArray("content");
                List<MediaSessionCompat.QueueItem> data = new ArrayList<>();

                for (int i = 0; i < pArray.length(); i++) {
                    SongBean bean = XMusicApplication.getGson().fromJson(pArray.getString(i),SongBean.class);
                    MediaSessionCompat.QueueItem item = buildItem(bean, i);
                    data.add(item);
                }

                return data;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "onError: "+e.getMessage());
            }

            @Override
            public void onResponse(List<MediaSessionCompat.QueueItem> response, int id) {
                listener.OnSongDetailInfo(response);
            }
        });
    }

    public void getAlbumInfo(String albumId,final OnPlaylistInfoListener listener){
        OkHttpUtils.get().url(BMA.Album.albumInfo(albumId)).build().execute(new Callback<List<MediaSessionCompat.QueueItem>>() {
            @Override
            public List<MediaSessionCompat.QueueItem> parseNetworkResponse(Response response, int id) throws Exception {
                JSONObject result = new JSONObject(response.body().string());
                JSONArray pArray = result.getJSONArray("songlist");
                List<MediaSessionCompat.QueueItem> data = new ArrayList<>();
                for (int i = 0; i < pArray.length(); i++) {
                    SongBean bean = XMusicApplication.getGson().fromJson(pArray.getString(i),SongBean.class);
                    MediaSessionCompat.QueueItem item = buildItem(bean, i);
                    data.add(item);
                }
                return data;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, "onError : " + e.getMessage());
            }

            @Override
            public void onResponse(List<MediaSessionCompat.QueueItem> response, int id) {
                LogHelper.d(TAG, "onResponse : ");
                listener.OnSongDetailInfo(response);
            }
        });
    }

    public void getBillBoardInfo(int type,int offset,int size,final OnPlaylistInfoListener listener){
        OkHttpUtils.get().url(BMA.Billboard.billSongList(type, offset, size)).build().execute(new Callback<List<MediaSessionCompat.QueueItem>>() {
            @Override
            public List<MediaSessionCompat.QueueItem> parseNetworkResponse(Response response, int id) throws Exception {
                JSONArray pArray = new JSONObject(response.body().string()).getJSONArray("song_list");
                List<MediaSessionCompat.QueueItem> data = new ArrayList<>();
                for (int i = 0; i < pArray.length(); i++) {
                    SongBean bean = XMusicApplication.getGson().fromJson(pArray.getString(i),SongBean.class);
                    MediaSessionCompat.QueueItem item = buildItem(bean, i);
                    data.add(item);
                }

                return data;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, "onError : " + e.getMessage());
            }

            @Override
            public void onResponse(List<MediaSessionCompat.QueueItem> response, int id) {
                listener.OnSongDetailInfo(response);
            }
        });
    }

    public void getRadioInfo(String channelName,int num,final OnPlaylistInfoListener listener){
        OkHttpUtils.get().url(BMA.Radio.channelSong(channelName, num)).build().execute(new Callback<List<MediaSessionCompat.QueueItem>>() {

            @Override
            public List<MediaSessionCompat.QueueItem> parseNetworkResponse(Response response, int id) throws Exception {

                JSONArray pArray = new JSONObject(response.body().string()).getJSONObject("result").getJSONArray("songlist");
                LogHelper.d(TAG, "parseNetworkResponse : " + pArray.toString());
                List<MediaSessionCompat.QueueItem> data = new ArrayList<>();
                for (int i = 0; i < pArray.length(); i++) {
                    if (i == pArray.length()-1){
                        continue;
                    }
                    SongBean bean = new SongBean();
                    bean.setSong_id(pArray.getJSONObject(i).getString("songid"));
                    bean.setTitle(pArray.getJSONObject(i).getString("title"));
                    bean.setAuthor(pArray.getJSONObject(i).getString("artist"));
                    MediaSessionCompat.QueueItem item = buildItem(bean, i);
                    data.add(item);
                }

                return data;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, "onError : " + e.getMessage());
            }

            @Override
            public void onResponse(List<MediaSessionCompat.QueueItem> response, int id) {
                listener.OnSongDetailInfo(response);
            }
        });


    }

    private MediaSessionCompat.QueueItem buildItem(SongBean song, int index) {
        long id = Long.parseLong(song.getSong_id());
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.EXTRA_INDEX, index);  //把该歌曲在播放列表中的位置也放到QueueItem中
        MediaDescriptionCompat description = new MediaDescriptionCompat.Builder()
                .setTitle(song.getTitle())
                .setSubtitle(song.getAuthor())
                .setMediaId(song.getSong_id())
                .setDescription(song.getAlbum_title())
                .setExtras(bundle)
                .build();

        return new MediaSessionCompat.QueueItem(description, id);
    }

    public interface OnPlaylistInfoListener{
        void OnSongDetailInfo(List<MediaSessionCompat.QueueItem> data);
    }
}
