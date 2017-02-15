package com.zixuan.xmusic.model;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zixuan.xmusic.api.BMA;
import com.zixuan.xmusic.bean.RadioBean;
import com.zixuan.xmusic.global.XMusicApplication;
import com.zixuan.xmusic.utils.LogHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class RadioModel {
    private final String TAG = "RadioModel";
    private int maxCount = 0;
    public void getRadioData(int pageNo, int pageSize, final OnRadioListener listener){
        LogHelper.d(TAG, "getRadioData : pageNo=" + pageNo);
        OkHttpUtils.get().url(BMA.Radio.recChannel(pageNo,pageSize)).build().execute(new Callback<List<RadioBean>>(){

            @Override
            public List<RadioBean> parseNetworkResponse(Response response, int id) throws Exception {
                JSONObject source = new JSONObject(response.body().string()).getJSONObject("result");
                maxCount = Integer.parseInt(source.getString("count"));
                JSONArray pArray =source.getJSONArray("list");
                List<RadioBean> data = new ArrayList<>();
                for (int i = 0; i < pArray.length(); i++) {
                    RadioBean bean = XMusicApplication.getGson().fromJson(pArray.getString(i),RadioBean.class);
                    data.add(bean);
                }
                return data;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, "onError : " + e.getMessage());
                listener.OnFail(e.getMessage());
            }

            @Override
            public void onResponse(List<RadioBean> response, int id) {
                LogHelper.d(TAG, "onResponse : ");
                listener.OnRadioData(response , maxCount);
            }
        });

    }





    public interface OnRadioListener{
        void OnRadioData(List<RadioBean> data, int maxCount);
        void OnFail(String msg);
    }



}
