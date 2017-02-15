package com.zixuan.xmusic.model;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zixuan.xmusic.api.BMA;
import com.zixuan.xmusic.bean.BillBoardBean;
import com.zixuan.xmusic.utils.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


/**
 * 获取排行榜页面数据
 */
public class BillboardModel {

    private static final String TAG = "BillboardModel";

    public void loadBillboardData(final OnLoadBillboardListener listener) {
        OkHttpUtils.get().url(BMA.Billboard.billCategory()).build().execute(new Callback<List<BillBoardBean>>() {
            @Override
            public List<BillBoardBean> parseNetworkResponse(Response response, int id) throws Exception {

                return parseJsonToBean(response.body().string());
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, "onError : " + e.getMessage());
                listener.onFail("网络异常");
            }

            @Override
            public void onResponse(List<BillBoardBean> response, int id) {
                LogHelper.d(TAG, "onResponse : ");
                if(response != null){
                    listener.onSuccess(response);

                }else{
                    listener.onFail("数据为空");
                }

            }
        });
    }

    private List<BillBoardBean> parseJsonToBean(String response){
        List<BillBoardBean> bills = new ArrayList<>();
        try {
            JSONArray content = new JSONObject(response).getJSONArray("content");

            for (int i=0;i<content.length();i++){
                BillBoardBean bill = new BillBoardBean();
                List<String> songs = new ArrayList<>();
                JSONObject jsonObject = content.getJSONObject(i);
                bill.setName( jsonObject.getString("name"));
                bill.setComment(jsonObject.getString("comment"));
                bill.setType(jsonObject.getInt("type"));
                bill.setPic_s192(jsonObject.getString("pic_s192"));

                JSONArray contents = jsonObject.getJSONArray("content");
                for (int j=0;j<contents.length();j++){
                    JSONObject song = contents.getJSONObject(j);
                    songs.add((j+1)+". "+song.getString("title")+"-"+song.getString("author"));
                }
                bill.setHotsong(songs);
                bills.add(bill);

            }

            return bills;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface OnLoadBillboardListener{
        void onSuccess(List<BillBoardBean> dataList);
        void onFail(String errMsg);
    }
}
