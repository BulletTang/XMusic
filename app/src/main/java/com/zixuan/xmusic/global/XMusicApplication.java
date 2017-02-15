package com.zixuan.xmusic.global;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zixuan.xmusic.cache.CacheInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;


public class XMusicApplication extends Application {


    private static Context sContext;

    private static Gson gson;



    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();
        gson = new Gson();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .cache(new Cache(new File(this.getCacheDir(), "okhttp_cache"), 10 * 1024 * 1024))   //若服务器支持缓存 Reponse Header中设置了Cache-Control 会自动缓存
                .addNetworkInterceptor(new CacheInterceptor())
                .build();

        OkHttpUtils.initClient(okHttpClient);



    }

    public static Context getContext(){
        return sContext;
    }

    public static Gson getGson(){
        return gson;
    }


}
