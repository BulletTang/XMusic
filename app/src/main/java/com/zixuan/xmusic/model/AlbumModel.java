package com.zixuan.xmusic.model;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zixuan.xmusic.api.BMA;
import com.zixuan.xmusic.bean.AlbumBean;
import com.zixuan.xmusic.global.XMusicApplication;
import com.zixuan.xmusic.utils.LogHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

//专辑信息获取
public class AlbumModel {
    private static final String TAG = "AlbumModel";

    public void getAlbumData(int offset, final int limit, final OnAlbumListener listener){
        OkHttpUtils.get().url(BMA.Album.recommendAlbum(offset,limit)).build().execute(new Callback<List<AlbumBean>>(){

            @Override
            public List<AlbumBean> parseNetworkResponse(Response response, int id) throws Exception {

                JSONArray pArray = new JSONObject(response.body().string()).getJSONObject("plaze_album_list")
                        .getJSONObject("RM").getJSONObject("album_list").getJSONArray("list");
                List<AlbumBean> list = new ArrayList<>();
                for (int i=0;i<pArray.length();i++){
                    AlbumBean bean = XMusicApplication.getGson().fromJson(pArray.getJSONObject(i).toString(),AlbumBean.class);
                    list.add(bean);
                }
                return list;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, "onError : " + e.getMessage());
                if (id == 0){
                    listener.OnNoMoreData();
                }

            }

            @Override
            public void onResponse(List<AlbumBean> response, int id) {
                LogHelper.d(TAG, "onResponse : ");
                listener.OnAlbumData(response);
            }
        });
    }


    public interface OnAlbumListener{
        void OnAlbumData(List<AlbumBean> data);
        void OnNoMoreData();
    }
}
