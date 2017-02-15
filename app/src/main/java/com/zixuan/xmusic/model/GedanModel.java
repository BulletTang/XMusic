package com.zixuan.xmusic.model;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zixuan.xmusic.api.BMA;
import com.zixuan.xmusic.bean.GedanBean;
import com.zixuan.xmusic.global.XMusicApplication;
import com.zixuan.xmusic.utils.LogHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class GedanModel {
    private final String TAG = "GedanModel";
    private boolean hasMore = true;

    public void getGedanData(int pageNo, int pageSize, final OnLoadGedanListener listener){
        LogHelper.d(TAG, "getGedanData : pageNo=" + pageNo);
        if (listener == null){
            return;
        }
        final List<GedanBean> dataList = new ArrayList<>();

        OkHttpUtils.get().url(BMA.GeDan.geDan(pageNo,pageSize)).build().execute(new Callback<List<GedanBean>>(){

            @Override
            public List<GedanBean> parseNetworkResponse(Response response, int id) throws Exception {
                JSONObject result = new JSONObject(response.body().string());
                hasMore = result.getInt("havemore") == 1;
                JSONArray contents = result.getJSONArray("content");
                for (int i=0;i<contents.length();i++){
                    GedanBean gedan = XMusicApplication.getGson().fromJson(contents.getString(i), GedanBean.class);
                    dataList.add(gedan);
                }
                return dataList;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, "onError : " + e.getMessage());
                listener.onFail("网络异常");
            }

            @Override
            public void onResponse(List<GedanBean> response, int id) {
                LogHelper.d(TAG, "onResponse : ");
                if (response != null){
                    listener.onSuccess(response , hasMore);
                }else{
                    listener.onFail("数据为空");
                }
            }
        });
    }

    public interface OnLoadGedanListener{
        void onSuccess(List<GedanBean> data, boolean hasMore);
        void onFail(String msg);
    }
}
