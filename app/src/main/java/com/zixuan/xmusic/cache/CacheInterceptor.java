package com.zixuan.xmusic.cache;

import com.zixuan.xmusic.global.XMusicApplication;
import com.zixuan.xmusic.utils.NetworkUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();


        if (!NetworkUtil.isNetworkConnected(XMusicApplication.getContext())) {
            //无网络时使用缓存
            final CacheControl.Builder builder = new CacheControl.Builder();
            builder.onlyIfCached();
            CacheControl cacheControl = builder.build();
            request = request.newBuilder().cacheControl(cacheControl).build();
        }else{  //有网络时不使用缓存
            final CacheControl.Builder builder = new CacheControl.Builder();
            builder.noCache();
            CacheControl cacheControl = builder.build();
            request = request.newBuilder().cacheControl(cacheControl).build();
        }

        Response response = chain.proceed(request);
        return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "max-age=" + 3600 * 24)
                .build();
    }
}
